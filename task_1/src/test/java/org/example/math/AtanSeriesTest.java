package org.example.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtanSeriesTest {
    private final AtanSeries atanSeries = new AtanSeries();

    @Test
    void returnsNaNForNaNInput() {
        assertTrue(Double.isNaN(atanSeries.atan(Double.NaN)));
    }

    @Test
    void handlesInfinities() {
        assertEquals(Math.PI / 2.0, atanSeries.atan(Double.POSITIVE_INFINITY), 1e-15);
        assertEquals(-Math.PI / 2.0, atanSeries.atan(Double.NEGATIVE_INFINITY), 1e-15);
    }

    @Test
    void preservesSignedZero() {
        double plusZero = atanSeries.atan(0.0);
        double minusZero = atanSeries.atan(-0.0);

        assertEquals(Double.doubleToRawLongBits(0.0), Double.doubleToRawLongBits(plusZero));
        assertEquals(Double.doubleToRawLongBits(-0.0), Double.doubleToRawLongBits(minusZero));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -0.3, -0.7, -1.0, -3.0, 0.1, 0.3, 0.7, 1.0, 3.0})
    void matchesMathAtanOnRepresentativePoints(double x) {
        assertEquals(Math.atan(x), atanSeries.atan(x), 1e-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.05, 0.3, 0.8, 1.5, 7.0})
    void isOddFunction(double x) {
        double positive = atanSeries.atan(x);
        double negative = atanSeries.atan(-x);
        assertEquals(-positive, negative);
    }

    @Test
    void isMonotonic() {
        double previous = atanSeries.atan(-10.0);

        for (double x = -9.9; x <= 10.0; x += 0.1) {
            double current = atanSeries.atan(x);
            assertTrue(current > previous, "Function must be strictly increasing");
            previous = current;
        }
    }

    @Test
    void isMonotonicOnDenseGrid() {
        double previous = atanSeries.atan(-1000.0);

        for (double x = -999.99; x <= 1000.0; x += 0.01) {
            double current = atanSeries.atan(x);
            assertTrue(current >= previous, "Function must be strictly increasing");
            previous = current;
        }
    }

    @Test
    void validatesArguments() {
        assertThrows(IllegalArgumentException.class, () -> new AtanSeries(0.0, 10));
        assertThrows(IllegalArgumentException.class, () -> new AtanSeries(-1e-6, 10));
        assertThrows(IllegalArgumentException.class, () -> new AtanSeries(1e-6, 0));
    }

    @Test
    void throwsWhenSeriesCannotConvergeWithinGivenTerms() {
        AtanSeries strict = new AtanSeries(1e-30, 2);
        assertThrows(IllegalStateException.class, () -> strict.atan(0.5));
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.5 - 1e-15, 0.5, 0.5 + 1e-15,
            -(0.5 - 1e-15), -0.5, -(0.5 + 1e-15)
    })
    void matchesMathAtanAroundHalfBoundary(double x) {
        assertEquals(Math.atan(x), atanSeries.atan(x), 1e-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            1.0 - 1e-15, 1.0, 1.0 + 1e-15,
            -(1.0 - 1e-15), -1.0, -(1.0 + 1e-15)
    })
    void matchesMathAtanAroundOneBoundary(double x) {
        assertEquals(Math.atan(x), atanSeries.atan(x), 1e-12);
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            Double.MIN_VALUE, -Double.MIN_VALUE,
            Double.MIN_NORMAL, -Double.MIN_NORMAL,
            1e-308, -1e-308,
            1e-300, -1e-300
    })
    void handlesVerySmallValuesAndKeepsSign(double x) {
        double y = atanSeries.atan(x);

        assertEquals(x, y, 1e-20);

        assertEquals(Math.copySign(1.0, x), Math.copySign(1.0, y));
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.0, 1e-12, -1e-12,
            0.1, -0.1,
            0.7, -0.7,
            1.0, -1.0,
            10.0, -10.0,
            1e308, -1e308,
            Double.MAX_VALUE, -Double.MAX_VALUE
    })
    void outputIsInRangeUpToFloatingPointErrorForFiniteInputs(double x) {
        double y = atanSeries.atan(x);

        assertTrue(Double.isFinite(y), "atan(x) for finite x must be finite");

        double limit = Math.PI / 2.0;

        double tol = 4 * Math.ulp(limit);
        assertTrue(Math.abs(y) <= limit + tol,
                "atan(x) must be within [-pi/2, pi/2] up to fp error");
    }



    @ParameterizedTest
    @ValueSource(doubles = {
            1.0, -1.0,
            0.5773502691896257, -0.5773502691896257, // 1/sqrt(3) -> pi/6
            1.7320508075688772, -1.7320508075688772  // sqrt(3) -> pi/3
    })
    void matchesKnownTableValues(double x) {
        double expected;
        if (x == 1.0) expected = Math.PI / 4.0;
        else if (x == -1.0) expected = -Math.PI / 4.0;
        else if (x > 0 && Math.abs(x - 0.5773502691896257) < 1e-18) expected = Math.PI / 6.0;
        else if (x < 0 && Math.abs(x + 0.5773502691896257) < 1e-18) expected = -Math.PI / 6.0;
        else if (x > 0 && Math.abs(x - 1.7320508075688772) < 1e-18) expected = Math.PI / 3.0;
        else expected = -Math.PI / 3.0;

        assertEquals(expected, atanSeries.atan(x), 1e-12);
    }

    @Test
    void strictSeriesFailureOnDirectSeriesRegion() {
        AtanSeries strict = new AtanSeries(1e-40, 2);
        assertThrows(IllegalStateException.class, () -> strict.atan(0.49));
    }

}

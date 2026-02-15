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
        assertEquals(-positive, negative, 1e-12);
    }

    @Test
    void isMonotonicOnSampleGrid() {
        double previous = atanSeries.atan(-10.0);
        for (double x = -9.9; x <= 10.0; x += 0.1) {
            double current = atanSeries.atan(x);
            assertTrue(current > previous, "Function must be strictly increasing");
            previous = current;
        }
    }

    @Test
    void verySmallXIsCloseToX() {
        double x = 1e-10;
        assertEquals(x, atanSeries.atan(x), 1e-20);
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
}

package org.example.math;

/**
 * Вычисляет arctg(x) через степенной ряд с преобразованиями,
 * ускоряющими сходимость для |x|, близких к 1 и больше 1.
 */
public final class AtanSeries {
    private final double epsilon;
    private final int maxTerms;

    /**
     * Создает калькулятор с параметрами по умолчанию.
     * epsilon = 1e-12, maxTerms = 100000.
     */
    public AtanSeries() {
        this(1e-12, 100_000);
    }

    /**
     * @param epsilon порог остановки: когда |очередной член ряда| < epsilon, суммирование прекращается.
     * @param maxTerms максимум членов ряда, который разрешено использовать.
     */
    public AtanSeries(double epsilon, int maxTerms) {
        validateArguments(epsilon, maxTerms);
        this.epsilon = epsilon;
        this.maxTerms = maxTerms;
    }

    /**
     * @param x аргумент функции arctg(x).
     * @return приближенное значение arctg(x), вычисленное через ряд.
     */
    public double atan(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (x == Double.POSITIVE_INFINITY) {
            return Math.PI / 2.0;
        }
        if (x == Double.NEGATIVE_INFINITY) {
            return -Math.PI / 2.0;
        }
        if (x == 0.0) {
            return x;
        }

        return atanInternal(x);
    }

    /**
     * @return текущий порог точности (epsilon).
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * @return текущий лимит числа членов ряда.
     */
    public int getMaxTerms() {
        return maxTerms;
    }

    private void validateArguments(double epsilon, int maxTerms) {
        if (epsilon <= 0.0 || Double.isNaN(epsilon) || Double.isInfinite(epsilon)) {
            throw new IllegalArgumentException("epsilon must be a positive finite number");
        }
        if (maxTerms <= 0) {
            throw new IllegalArgumentException("maxTerms must be greater than 0");
        }
    }

    private double atanInternal(double x) {
        double absX = Math.abs(x);

        if (absX > 1.0) {
            return Math.copySign(Math.PI / 2.0, x) - atanInternal(1.0 / x);
        }

        if (absX > 0.5) {
            double transformed = x / (1.0 + Math.sqrt(1.0 + x * x));
            return 2.0 * atanInternal(transformed);
        }

        return atanSeriesDirect(x);
    }

    private double atanSeriesDirect(double x) {
        double xSquared = x * x;
        double term = x;
        double sum = term;

        if (Math.abs(term) < epsilon) {
            return sum;
        }

        for (int n = 1; n < maxTerms; n++) {
            term *= -xSquared * (2.0 * n - 1.0) / (2.0 * n + 1.0);
            sum += term;

            if (Math.abs(term) < epsilon) {
                return sum;
            }
        }

        throw new IllegalStateException(
            "Series did not converge for x=" + x + " with epsilon=" + epsilon + " and maxTerms=" + maxTerms
        );
    }
}

package hr.fer.projekt.matematika;

// Matrix.java
import java.util.Random;
import java.util.function.Function;

public class Matrix {
    public int rows, cols;
    public double[][] data;
    private static Random rand = new Random();

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    public static Matrix fromArray(double[] arr) {
        Matrix m = new Matrix(arr.length, 1);
        for (int i = 0; i < arr.length; i++) m.data[i][0] = arr[i];
        return m;
    }

    public double[] toArray() {
        double[] arr = new double[rows * cols];
        int idx = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                arr[idx++] = data[i][j];
        return arr;
    }

    public void randomize() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = rand.nextDouble(); // [0,1]
    }

    public void add(Matrix m) {
        if (rows != m.rows || cols != m.cols) throw new RuntimeException("Dimensions must match for add.");
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] += m.data[i][j];
    }

    public void add(double n) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] += n;
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        if (a.rows != b.rows || a.cols != b.cols) throw new RuntimeException("Dimensions must match for subtract.");
        Matrix result = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++)
            for (int j = 0; j < a.cols; j++)
                result.data[i][j] = a.data[i][j] - b.data[i][j];
        return result;
    }

    public static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.cols, m.rows);
        for (int i = 0; i < m.rows; i++)
            for (int j = 0; j < m.cols; j++)
                result.data[j][i] = m.data[i][j];
        return result;
    }

    // Matrix multiplication (a * b)
    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.cols != b.rows) throw new RuntimeException("Cols of A must match rows of B for multiply.");
        Matrix result = new Matrix(a.rows, b.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                double sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }


    public void multiply(double n) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] *= n;
    }

    public void map(Function<Double, Double> func) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = func.apply(data[i][j]);
    }


    public static Matrix copy(Matrix m) {
        Matrix result = new Matrix(m.rows, m.cols);
        for (int i = 0; i < m.rows; i++)
            for (int j = 0; j < m.cols; j++)
                result.data[i][j] = m.data[i][j];
        return result;
    }

    // Debug print
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                System.out.printf("% .4f ", data[i][j]);
            System.out.println();
        }
        System.out.println();
    }
}

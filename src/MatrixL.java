import java.util.Random;

public class MatrixL {
    private int m, n;
    private int[][] values;

    public MatrixL() {
        this(0, 0);
    }

    /**
     * Initializes a matrix with given m x n params
     *
     * @param m -> rows
     * @param n -> coluns
     */
    public MatrixL(int m, int n) {
        this.m = m;
        this.n = n;
        values = new int[m][n];
        init();
    }

    /**
     * Initializes a matrix with a split matrix
     *
     * @param matrices - A split matrix which consists of 4 equally sized matrices in a 2x2 order
     */
    public MatrixL(MatrixL[][] matrices) {
        if (matrices == null) {
            System.err.println("Matrices may not be null");
            return;
        }

        if (matrices.length != 2) {
            System.err.println("Matrices have to be in a 2x2 order");
            return;
        }

        if (matrices[0].length != 2 || matrices[1].length != 2) {
            System.err.println("Matrices have to be in a 2x2 order");
            return;
        }

        int splitIndex = matrices[0][0].m;
        int len = matrices.length * splitIndex;
        this.m = len;
        this.n = len;
        values = new int[m][n];

        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                int t1 = i < splitIndex ? 0 : 1;
                int t2 = j < splitIndex ? 0 : 1;
                values[i][j] = matrices[t1][t2].values[i % splitIndex][j % splitIndex];
            }
        }
    }

    /**
     * Fills the matrix with zeros
     */
    public void init() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                values[i][j] = 0;
            }
        }
    }

    /**
     * Fills the matrix with random numbers
     */
    public void randomFill() {
        Random r = new Random();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                values[i][j] = r.nextInt(10);
            }
        }
    }

    /**
     * Adds and returns the given matrix to this matrix
     * -- Does not alter this matrix --
     *
     * @param matrix - The matrix to add
     * @return - The resulting matrix
     */
    public MatrixL add(MatrixL matrix) {
        if (this.m != matrix.m || this.n != matrix.n) {
            System.err.println("Matrices have to be the same size");
            return this;
        }

        MatrixL result = new MatrixL(this.m, this.n);

        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                result.values[i][j] = this.values[i][j] + matrix.values[i][j];
            }
        }

        return result;
    }

    /**
     * Subtracts and returns the given matrix from this matrix
     * -- Does not alter this matrix --
     *
     * @param matrix - The matrix to subtract from this matrix
     * @return - The resulting matrix
     */
    public MatrixL sub(MatrixL matrix) {
        if (this.m != matrix.m || this.n != matrix.n) {
            System.err.println("Matrices have to be the same size");
            return this;
        }

        MatrixL result = new MatrixL(this.m, this.n);

        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                result.values[i][j] = this.values[i][j] - matrix.values[i][j];
            }
        }

        return result;
    }

    /**
     * Multiplies and returns the given matrix to this matrix
     * -- This is the slowest O(n^3) method --
     *
     * @param matrix - The matrix for multiplying
     * @return - The resulting matrix
     */
    public MatrixL mult_n(MatrixL matrix) {
        if (this.m != matrix.n || this.n != matrix.m) {
            System.err.println("this.m == other.n && this.n == other.m");
            return this;
        }

        MatrixL result = new MatrixL(this.m, matrix.n);

        for (int i = 0; i < this.m; i++) {
            for (int k = 0; k < result.m; k++) {
                int temp = 0;
                for (int j = 0; j < this.n; j++) {
                    temp += values[i][j] * matrix.values[j][k];
                }
                result.values[i][k] = temp;
            }
        }

        return result;
    }

    /**
     * Multiplies and returns the given matrix to this matrix
     * -- This is a faster recursive method than mult_n, but only for 2^i x 2^i matrices --
     *
     * @param matrix - The matrix for multiplying
     * @return - The resulting matrix
     */
    public MatrixL mult(MatrixL matrix) {
        if (this.m != matrix.m || this.n != matrix.n || this.n != matrix.m || this.m % 2 != 0)
            return this.mult_n(matrix);

        if (this.m < 32)
            return this.mult_n(matrix);

        MatrixL[][] M = this.split(), N = matrix.split(), O = new MatrixL[2][2];

        MatrixL[] H = new MatrixL[7];

        H[0] = M[0][0].add(M[1][1]).mult(N[0][0].add(N[1][1]));
        H[1] = M[1][0].add(M[1][1]).mult(N[0][0]);
        H[2] = M[0][0].mult(N[0][1].sub(N[1][1]));
        H[3] = M[1][1].mult(N[1][0].sub(N[0][0]));
        H[4] = M[0][0].add(M[0][1]).mult(N[1][1]);
        H[5] = M[1][0].sub(M[0][0]).mult(N[0][0].add(N[0][1]));
        H[6] = M[0][1].sub(M[1][1]).mult(N[1][0].add(N[1][1]));

        O[0][0] = H[0].add(H[3]).sub(H[4]).add(H[6]);
        O[0][1] = H[2].add(H[4]);
        O[1][0] = H[1].add(H[3]);
        O[1][1] = H[0].sub(H[1]).add(H[2]).add(H[5]);

        return new MatrixL(O);
    }

    /**
     * @return - A split matrix which consists of 4 equally sized matrices in a 2x2 order
     */
    private MatrixL[][] split() {
        MatrixL[][] splitMatrix = new MatrixL[2][2];
        int splitIndex = (this.m) / 2;

        for (int i = 0; i < splitMatrix.length; i++) {
            for (int j = 0; j < splitMatrix[i].length; j++) {
                splitMatrix[i][j] = new MatrixL(splitIndex, splitIndex);
            }
        }

        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                int t1 = i < splitIndex ? 0 : 1;
                int t2 = j < splitIndex ? 0 : 1;
                splitMatrix[t1][t2].values[i % splitIndex][j % splitIndex] = this.values[i][j];
            }
        }

        return splitMatrix;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int[][] getValues() {
        return values;
    }

    public MatrixL copy() {
        MatrixL cpy = new MatrixL(this.m, this.n);
        cpy.values = this.values.clone();
        return cpy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < m; i++) {
            sb.append("|");
            for (int j = 0; j < n; j++) {
                sb.append(" ").append(values[i][j]);
            }
            sb.append(" |\n");
        }

        return sb.toString();
    }
}

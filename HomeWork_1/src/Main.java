class Main {
    public static void main(String[] arr) {
        Complex[][] data22 = new Complex[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                data22[i][j] = new Complex(i, j);
            }
        }

        Complex[][] data34 = new Complex[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                data34[i][j] = new Complex(i, j);
            }
        }

        Complex[][] data46 = new Complex[4][6];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                data46[i][j] = new Complex(i, j);
            }
        }

        Complex[][] data64 = new Complex[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                data64[i][j] = new Complex(i, j);
            }
        }

        Matrix A22 = new Matrix(2, 2, data22);
        Matrix A34 = new Matrix(3, 4, data34);
        Matrix A46 = new Matrix(4, 6, data46);
        Matrix A64 = new Matrix(6, 4, data64);


        // Демонстрация обработки ошибок:
        // A22.setData(data34);
        // Matrix incorrectSum = A22.add(A34);
        // Matrix incorrectSub = A34.sub(A46);
        // Matrix incorrectMulti = A22.multi(A46);

        // Демонстрация корректной работы:
        // Matrix Sum = A22.add(A22); Sum.print();
        // Matrix Sub = A46.sub(A46); Sub.print();
        // Matrix Multi = A34.multi(A46); Multi.print();
        // Matrix Trans = A46.trans(); Trans.print();
    }
}
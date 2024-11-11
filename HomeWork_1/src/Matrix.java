public class Matrix {
    private final int rows; // Количество строк в матрице
    private final int cols; // Количество столбцов в матрице
    private final Complex [][] data; // Массив с матрицей

    // Конструкторы
    public Matrix(int rows, int cols, Complex[][] data){
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.data[i][j] = data[i][j];
            }
        }
    }

    // Операция сложения
    public Matrix add(Matrix b){
        if ((this.rows != b.rows) || (this.cols != b.cols)){
            System.out.println("Ошибка, попытка сложить матрицы разных размеров");
            System.exit(1);
        }
        Complex[][] newData = new Complex[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                newData[i][j] = this.data[i][j].add(b.data[i][j]);
            }
        }
        return new Matrix(rows, cols, newData);
    }

    // Операция вычитания
    public Matrix sub(Matrix b){
        if ((this.rows != b.rows) || (this.cols != b.cols)){
            System.out.println("Ошибка, попытка вычесть матрицы разных размеров");
            System.exit(1);
        }
        Complex[][] newData = new Complex[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                newData[i][j] = this.data[i][j].sub(b.data[i][j]);
            }
        }
        return new Matrix(rows, cols, newData);
    }

    // Операция умножения
    public Matrix multi(Matrix b){
        if (this.cols != b.rows){
            System.out.println("Ошибка, попытка умножить матрицы несовместимых размеров");
            System.exit(1);
        }
        Complex[][] newData = new Complex[rows][b.cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < b.cols; j++){
                Complex newCell = new Complex(0,0);
                for(int k = 0; k < cols; k++){
                    newCell = newCell.add(this.data[i][k].multi(b.data[k][j]));
                }
                newData[i][j] = newCell;
            }
        }
        return new Matrix(rows, b.cols, newData);
    }

    // Операция транспонирования
    public Matrix trans(){
        Complex[][] newData = new Complex[cols][rows];
        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                newData[i][j] = data[j][i];
            }
        }
        return new Matrix(cols, rows, newData);
    }

    // Функции set для поля data и функции get для каждого поля класса
    public void setData(Complex[][] data) {
        if ((data.length != rows) || (data[0].length != cols)){
            System.out.println("Ошибка, переданный массив с комплексными числами не соответсвует размеру матрицы");
            System.exit(1);
        }
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.data[i][j] = data[i][j];
            }
        }
    }
    public void setData(int row, int col, Complex val){
        data[row][col] = val;
    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }
    public Complex[][] getData(){
        return data;
    }
    public Complex getData(int row, int col){
        return data[row][col];
    }

    // Функция печати
    public void print(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                data[i][j].print();
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}

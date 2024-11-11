public class Complex {
    private double Re; // Действительная часть
    private double Im; // Мнимая часть

    // Конструкторы
    public Complex(){
        this(0,0);
    }
    public Complex(double Re){
        this(Re, 0);
    }
    public Complex(double Re, double Im){
        this.Re = Re;
        this.Im = Im;
    }

    // Операция сложения
    public Complex add(Complex b){
        return new Complex(this.Re + b.Re, this.Im + b.Im);
    }

    // Операция вычитания
    public Complex sub(Complex b){
        return new Complex(this.Re - b.Re, this.Im - b.Im);
    }

    // Операция умножения
    public Complex multi(Complex b){
        double resultRe = this.Re * b.Re - this.Im * b.Im;
        double resultIm = this.Re * b.Im + this.Im * b.Re;
        return new Complex(resultRe, resultIm);
    }

    // Операция деления
    public Complex division(Complex b){
        double denominator = (b.Re * b.Re) + (b.Im * b.Im);
        if (denominator == 0){
            System.out.println("Ошибка. Попытка поделить на ноль");
            System.out.print("Делимое: ");
            print();
            System.out.print("\nДелитель: ");
            b.print();
            System.exit(1);
        }
        double resultRe = (this.Re * b.Re + this.Im * b.Im) / denominator;
        double resultIm = (this.Im * b.Re - this.Re * b.Im) / denominator;
        return new Complex(resultRe, resultIm);
    }

    // Функции set и get для каждого поля класса
    public double getRe(){
        return Re;
    }
    public double getIm(){
        return Im;
    }
    public void setRe(double Re){
        this.Re = Re;
    }
    public void setIm(double Im){
        this.Im = Im;
    }

    // Функция печати
    public void print(){
        if (Im >= 0) System.out.printf("%f + %f*i\t", Re, Im);
        else System.out.printf("%f + (%f)*i\t", Re, Im);
    }
}
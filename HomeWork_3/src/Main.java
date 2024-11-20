import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Здравствуйте! Вам предлагается ввести:\n" +
                "- ФИО (используя кириллицу и соблюдая регистр)\n" +
                "- Дату рождения (в формате ДД.ММ.ГГГГ)\n" +
                "В качестве разделителей при вводе даты вы можете использовать символы: . \\ | / - ; :\n" +
                "-----------------------------------------------------------------------------------------\n" +
                "Ввод: ");
        String input = in.nextLine();
        in.close();
        Person p = new Person(input);
        System.out.println(p);
    }
}
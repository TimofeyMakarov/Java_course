import MyExceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class Person {
    private String firstName;
    private String lastName;
    private String patronymic;
    private final Date date;
    private final String gender;

    Person(String string){
        StringTokenizer st = new StringTokenizer(string, " ");

        // Временные переменные для проверки
        String inputFirstName = "";
        String inputLastName = "";
        String inputPatronymic = "";
        Date inputDate = null;

        try{
            // Ошибки будут обрабатываться так:
            // - если кол-во введённых токенов отличается от ожидаемого -> NumberOfTokensException() и соответствующее сообщение
            // - если ошибка в токенах ФИО -> IncorrectNameException() и сообщение о том, какие символы можно использовать и какой формат ввода
            // - если ошибка в дате -> IncorrectDateException() и сообщение о том, какие символы можно использовать и какой формат ввода
            // Вначале проверяется ФИО, затем дата

            int countInputTokens = st.countTokens();

            // Если токенов слишком много
            if (countInputTokens > 4) throw new NumberOfTokensException();

            // Если токенов 4 - предполагаем, что это ФИО и дата
            if (countInputTokens == 4) {
                inputLastName = st.nextToken();
                inputFirstName = st.nextToken();
                inputPatronymic = st.nextToken();
            }
            // Если токенов 3 - предполагаем, что это ФИ и дата
            if (countInputTokens == 3) {
                inputLastName = st.nextToken();
                inputFirstName = st.nextToken();
            }
            // Если токенов слишком мало
            if (countInputTokens < 3) throw new NumberOfTokensException();

            // Вводим дату
            String inputDateStr = st.nextToken();

            // Проверка ФИО
            // Не должно быть лишних символов - только буквы
            if ((incorrectSymbols(inputFirstName)) || (incorrectSymbols(inputLastName)) || (incorrectSymbols(inputPatronymic))){
                throw new IncorrectNameException();
            }
            // Первая буква должна быть заглавной, а остальные - маленькими
            if ((incorrectCase(inputFirstName)) || (incorrectCase(inputLastName)) || (incorrectCase(inputPatronymic))){
                throw new IncorrectNameException();
            }

            // Конструктор Date выбросит исключение IncorrectDateException, если формат неправильный
            inputDate = new Date(inputDateStr);
        }
        catch (IncorrectNameException e){
            System.out.println("Введено неверное ФИО. Убедитесь, что в ФИО использованы только буквы кириллицы и соблюдён регистр.");
            System.exit(0);
        }
        catch (IncorrectDateException e){
            System.out.println("Введена неверная дата. Убедитесь, что дата не содержит ничего, кроме цифр, соответствует формату ДД.ММ.ГГГГ, а также является существующей датой в Григорианском календаре.");
            System.exit(0);
        }
        catch (NumberOfTokensException e){
            System.out.println("Данные введены в неверном формате. Пожалуйста, убедитесь, что введённая строка содержит ФИО (или ФИ) и дату и не содержит ничего другого.");
            System.exit(0);
        }

        firstName = inputFirstName;
        lastName = inputLastName;
        patronymic = inputPatronymic;
        date = inputDate;
        if (patronymic.isEmpty()){
            gender = "не определён";
        }
        else {
            if (patronymic.charAt(patronymic.length() - 1) == 'а') gender = "женский";
            else gender = "мужской";
        }
    }

    private boolean incorrectSymbols(String string){
        return !string.replaceAll("[а-яА-Я]", "").isEmpty();
    }

    private boolean incorrectCase(String string){
        // На случай отсутствия отчества
        if (string.isEmpty()){
            return false;
        }
        // Первая должна быть заглавной
        if (((int)string.charAt(0) < 1040) || (1071 < (int)string.charAt(0))) {
            return true;
        }
        // Остальные маленькими
        for(int i = 1; i < string.length(); i++){
            if (((int)string.charAt(i) < 1072) || (1103 < (int)string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int yearOfLastBirthday(){
        // Зададим формат. 03-12-2005 -> 1203
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMdd");
        String monthDayNow = dtf.format(LocalDateTime.now());
        int monthDayNowInt = Integer.parseInt(monthDayNow);
        int monthDayBirthday = date.getMonth()*100 + date.getDay();
        if (monthDayNowInt < monthDayBirthday){
            return 2024 - 1;
        }
        return 2024;
    }

    public String toString(){
        StringBuilder info = new StringBuilder();
        String nameFirstAndLast = lastName + " " + firstName.charAt(0) + ".";
        info.append(nameFirstAndLast);
        if (!patronymic.isEmpty()) {
            info.append(" ");
            info.append(patronymic.charAt(0));
            info.append(".");
        }
        info.append('\n');
        info.append("Пол: ");
        info.append(gender);
        info.append('\n');
        int ages = yearOfLastBirthday() - date.getYear();
        if (ages >= 0){
            info.append("Возраст: ");
            info.append(ages);
        }
        else{
            ages = ages * (-1) - 1;
            if (ages == 0){
                ages = 1; // Чтоб вывел "год"
                info.append("Родится меньше, чем через 1");
            }
            else{
                info.append("Родится через: ");
                info.append(ages);
            }
        }
        info.append(" ");
        if ((11 <= ages % 100) && (ages % 100 <= 14)){
            info.append("лет");
        }
        else {
            if (ages % 10 == 0){
                info.append("лет");
            }
            if (ages % 10 == 1){
                info.append("год");
            }
            if ((2 <= ages % 10) && (ages % 10 <= 4)){
                info.append("года");
            }
            if (5 <= ages % 10){
                info.append("лет");
            }
        }
        info.append('\n');
        return info.toString();
    }
}

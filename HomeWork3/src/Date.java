import MyExceptions.IncorrectDateException;
import java.util.StringTokenizer;

public class Date {
    private final int day;
    private final int month;
    private final int year;

    Date(String date) throws IncorrectDateException {
        if (!isDate(date)) throw new IncorrectDateException();
        char sep = date.replaceAll("[0-9]", "").charAt(0);
        StringTokenizer st = new StringTokenizer(date, String.valueOf(sep));
        day = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        year = Integer.parseInt(st.nextToken());
    }

    private boolean isDate(String string) {
        // Проверка на посторонние символы
        // Если строка корректно задаёт дату, тогда в withoutNumbers будет лежать два символа-разделителя
        String withoutNumbers = string.replaceAll("[0-9]", "");
        if (withoutNumbers.length() != 2) return false;
        if (withoutNumbers.charAt(0) != withoutNumbers.charAt(1)) return false;
        char sep = withoutNumbers.charAt(0);
        if (!isValidSeparator(sep)) return false;

        // Поверхностная проверка дня, месяца и года
        StringTokenizer st = new StringTokenizer(string, String.valueOf(sep));
        if (st.countTokens() != 3) return false;
        int inputDay = Integer.parseInt(st.nextToken());
        int inputMonth = Integer.parseInt(st.nextToken());
        int inputYear = Integer.parseInt(st.nextToken());
        if ((inputDay < 1) || (inputDay > 31)) return false;
        if ((inputMonth < 1) || (inputMonth > 12)) return false;
        if ((inputYear < 0) || (inputYear > 10000)) return false;

        // Глубокая проверка дня, месяца и года
        // Ограничение на 30 дней для четырёх месяцев
        if (((inputMonth == 4) || (inputMonth == 6) || (inputMonth == 9) || (inputMonth == 11)) && (inputDay == 31)) return false;
        // Ограничение на 29 или 28 дней на Февраль
        byte extraDay = 0;
        if (inputYear % 4 == 0) extraDay = 1;
        if (inputYear % 100 == 0) extraDay = 0;
        if (inputYear % 400 == 0) extraDay = 1;
        if ((inputMonth == 2) && (inputDay > 28 + extraDay)) return false;

        // Иначе всё отлично
        return true;
    }

    private boolean isValidSeparator(char separator){
        // Символом-разделителем может быть: "." (точка), " " (пробел), "," (запятая), "-" (тире), "/" (слэш),
        //                                   "\" (обратный слэш), ";" (точка с запятой) и ":" (двоеточие)
        char[] validSeparators = {'.', ' ', ',', '-', '/', '\\', ';', ':'};
        for (int i = 0; i < 8; i++){
            if (separator == validSeparators[i]) return true;
        }
        return false;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}

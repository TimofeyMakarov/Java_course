import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class Main {
    public static String parse(String string){
        String[] deletingStrings = {"[", "]", "{", "}", "\""};
        string = string.trim(); // Удаление пробелов в начале и в конце строки
        for (String deletingString : deletingStrings) {
            string = string.replace(deletingString, "");
        }
        string = string.replace(" :", ":");
        StringBuilder sb = new StringBuilder(string); // Для простоты удаления последнего символа
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == ','){
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static void printContent(URL url){
        HttpURLConnection huc;
        System.out.println("Resource from " + url.toString());

        try {
            huc = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e){
            System.out.println("Не удалось установить соединение с ресурсом");
            return;
        }

        try(InputStream is = huc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr)){
            String theLine;
            while((theLine = br.readLine()) != null) {
                System.out.println(parse(theLine));
            }
        } catch (IOException e){
            System.out.println("Не удалось получить доступ к ресурсу");
        }
    }

    public static void main(String [] args) {
        URL url1;
        URL url2;
        try {
            url1 = new URL("https://fake-json-api.mock.beeceptor.com/users");
            url2 = new URL("https://fake-json-api.mock.beeceptor.com/companies");
        } catch (MalformedURLException e) {
            System.out.println("Неверно указан URL");
            return;
        }
        printContent(url1);
        printContent(url2);
    }
}
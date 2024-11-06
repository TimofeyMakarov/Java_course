import java.io.*;
import java.util.Scanner;

public class Main {
    static void FrequencyAnalysis(String path){
        // Проверка корректности введённых данных
        if (!FileIsCorrectly(path)){
            System.exit(1);
        }

        int[] result = new int[52];
        System.out.println("Начался частотный анализ!");
        counting(result, path);

        savingTheResults(result, path);
    }

    static boolean FileIsCorrectly(String path){
        File file = new File(path);
        if (!file.exists()){
            System.out.println("По этому пути ничего не существует. Пожалуйста, убедитесь, что Вы корректно ввели путь");
            return false;
        }
        if (!file.isFile()){
            System.out.println("По этому пути лежит не файл. Пожалуйста, убедитесь, что Вы корректно ввели путь");
            return false;
        }
        if(!isTxt(path)){
            System.out.println("Файл должен иметь расширение txt");
            return false;
        }
        if(!file.canRead()){
            System.out.println("Чтение этого файла невозможно. Пожалуйста, измените настройки доступа или введите путь к другому файлу");
            return false;
        }
        return true;
    }

    static boolean isTxt(String path){
        StringBuilder extension = new StringBuilder();
        char[] path_charArray = path.toCharArray();
        for(int i = path_charArray.length - 1; i >= 0; i--){
            if (path_charArray[i] == '.') break;
            extension.append(path_charArray[i]);
        }
        return extension.toString().equals("txt");
    }

    static void counting(int[] result, String path){
        try(FileReader f = new FileReader(path)){
            while(f.ready()){
                int index = from_code_to_index_Eng(f.read());
                if (index != -1) result[index] += 1;
            }
        }
        catch(FileNotFoundException obj){
            System.out.println("При попытке произвести анализ оказалось, что файла не существует. Возможно, он был удалён");
            System.exit(1);
        }
        catch(IOException obj){
            System.out.println("Произошла непредвиденная ошибка");
            System.exit(1);
        }
    }

    static int from_code_to_index_Eng(int code){
        if ((64 < code) && (code < 91)) return code - 65;
        if ((96 < code) && (code < 123)) return code - 71;
        return -1;
    }

    static int from_index_to_code_Eng(int index){
        if (index < 26) return index + 65;
        else return index + 71;
    }

    static void savingTheResults(int[] result, String path){
        System.out.print("Сохраняем результаты... ");

        // Создание файла, куда будут положены результаты
        String name_of_file_with_results = nameSelection(path);
        creating_new_file(name_of_file_with_results);

        // Записываем результаты
        try(FileWriter fw = new FileWriter(name_of_file_with_results)){
            for(int i = 0; i < 52; i++){
                int code = from_index_to_code_Eng(i);
                String string_for_writing = (char)code + " " + result[i] + "\n";
                fw.write(string_for_writing);
            }
        }
        catch(IOException obj){
            System.out.println("При попытке записать результаты в созданный файл возникла ошибка. Возможно, он был удалён в процессе записи");
        }
    }

    static String nameSelection(String path){
        String name_of_file_with_text = "RESULT_" + nameFromPath(path);
        String name_of_directory = DirectoryFromPath(path);
        String name_of_file_with_results = name_of_directory + name_of_file_with_text;
        File file_with_results = new File(name_of_file_with_results + ".txt");
        int number_of_attempt = 1;
        while(file_with_results.exists()){
            if (number_of_attempt == 1){
                file_with_results = new File(name_of_file_with_results + " (1)" + ".txt");
            }
            else {
                file_with_results = new File(name_of_file_with_results + " (" + number_of_attempt + ")" + ".txt");
            }
            number_of_attempt++;
        }
        return file_with_results.getPath();
    }

    static void creating_new_file(String path){
        try {
            File file_with_results = new File(path);
            if (file_with_results.createNewFile()){
                System.out.print("Результаты будут сохранены в файл: \"");
                System.out.print(file_with_results.getName());
                System.out.println("\", лежащий в той же папке, что и анализируемый файл");
            }
            else{
                System.out.print("Ошибка при создании файла с результатами. ");
                System.out.println("Возможно, в процессе создания файла в директорию был добавлен файл с таким же именем");
                System.exit(1);
            }
        }
        catch (IOException obj) {
            System.out.print("Возникла непредвиденная ошибка при попытке создания файла с результатами. ");
            System.out.println("Возможно, папка, в которой лежит указанный файл является закрытой, пожалуйста, переместите файл в другую директорию или откройте доступ к данной. ");
            System.exit(1);
        }
    }

    static String nameFromPath(String path){
        StringBuilder name = new StringBuilder();
        char[] path_charArray = path.toCharArray();
        int count_of_points = 0;
        for(int i = path_charArray.length - 1; i >= 0; i--){
            if (path_charArray[i] == '.'){
                count_of_points += 1;
                if (count_of_points == 1) continue;
            }
            if (count_of_points == 0) continue;
            if (path_charArray[i] == '\\') break;
            name.append(path_charArray[i]);
        }
        return name.reverse().toString();
    }

    static String DirectoryFromPath(String path){
        StringBuilder directory = new StringBuilder();
        char[] path_charArray = path.toCharArray();
        int count_of_slashes = 0;
        for(int i = path_charArray.length - 1; i >= 0; i--){
            if (path_charArray[i] == '\\') count_of_slashes += 1;
            if (count_of_slashes == 0) continue;
            directory.append(path_charArray[i]);
        }
        return directory.reverse().toString();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Пожалуйста, напишите путь, где лежит файл:");
        String path = input.nextLine();

        FrequencyAnalysis(path);
    }
}
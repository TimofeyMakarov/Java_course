package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static String getJsonString(URL url){
        HttpURLConnection huc;
        System.out.println("Extracting content from a resource at " + url.toString());

        try {
            huc = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e){
            System.out.println("Не удалось установить соединение с ресурсом");
            return "";
        }

        StringBuilder jsonString = new StringBuilder();
        try(InputStream is = huc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr)){
            String theLine;
            while((theLine = br.readLine()) != null) {
                jsonString.append(theLine);
            }
        } catch (IOException e){
            System.out.println("Не удалось получить доступ к ресурсу");
        }

        return jsonString.toString();
    }

    public static void printUsersContent(String jsonString){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<User> users = objectMapper.readValue(jsonString, new TypeReference<List<User>>(){});
            for (User user : users){
                System.out.println("ID: " + user.getId());
                System.out.println("\tName: " + user.getName());
                System.out.println("\tCompany: " + user.getCompany());
                System.out.println("\tUsername: " + user.getUsername());
                System.out.println("\tEmail: " + user.getEmail());
                System.out.println("\tAddress: " + user.getAddress());
                System.out.println("\tZip: " + user.getZip());
                System.out.println("\tState: " + user.getState());
                System.out.println("\tCountry: " + user.getCountry());
                System.out.println("\tPhone: " + user.getPhone());
                System.out.println("\tPhoto: " + user.getPhoto());
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Не удалось извлечь List<User> из переданной строки");
        }
    }

    public static void printCompaniesContent(String jsonString){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Company> companies = objectMapper.readValue(jsonString, new TypeReference<List<Company>>(){});
            for (Company company : companies){
                System.out.println("ID: " + company.getId());
                System.out.println("\tName: " + company.getName());
                System.out.println("\tAddress: " + company.getAddress());
                System.out.println("\tZip: " + company.getZip());
                System.out.println("\tCountry: " + company.getCountry());
                System.out.println("\tEmployeeCount: " + company.getEmployeeCount());
                System.out.println("\tIndustry: " + company.getIndustry());
                System.out.println("\tMarketCap: " + company.getMarketCap());
                System.out.println("\tDomain: " + company.getDomain());
                System.out.println("\tLogo: " + company.getLogo());
                System.out.println("\tCeoName: " + company.getCeoName());
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Не удалось извлечь List<User> из переданной строки");
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
        printUsersContent(getJsonString(url1));
        printCompaniesContent(getJsonString(url2));
    }
}
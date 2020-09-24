package com.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FindCapitalByCountryName implements Runnable {

    /**
     *
     * Invoke Api call for country Name and return JSON.
     *
     * @param
     * @return JSON string
     * @throws Exception
     */
    public static String invokeRestCountryApiCall(String countryName) throws Exception {
        URL urlForGetRequest = new URL(String.format("https://restcountries.eu/rest/v2/name/%s", countryName));
        String readLine;
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlForGetRequest.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new Exception("Please Enter Valid Country Name. You have entered " + countryName);
        }
    }

    /**
     *
     * Read JSON String and prints Country Name and Capital.
     *
     * @param jsonString
     * @param actualCountryName
     * @throws Exception
     */

    public static void readJson(String jsonString, String actualCountryName) throws Exception {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonString);
        for (Object object : jsonArray) {
            JSONObject jsonObject = ((JSONObject) object);
            String countryName = (String) jsonObject.get("name");
            if (countryName.toLowerCase().contains(actualCountryName.toLowerCase())) {
                System.out.println("Country Name : " + jsonObject.get("name") + " and Capital is : " + jsonObject.get("capital"));
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new FindCapitalByCountryName());
        thread.start();
    }

    public void run() {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please Enter the Country Name");
                String countryName = scanner.nextLine();
                String jsonString = invokeRestCountryApiCall(countryName);
                readJson(jsonString, countryName);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}

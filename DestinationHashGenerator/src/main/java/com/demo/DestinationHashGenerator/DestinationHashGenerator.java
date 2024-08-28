package com.demo.DestinationHashGenerator;

import org.json.JSONObject;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Random;

public class DestinationHashGenerator {

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Please provide PRN Number and JSON file path as arguments.");
                return;
            }

            String prn = args[0].toLowerCase();
            String filePath = args[1];

            // Parse JSON file
            JSONObject json = new JSONObject(new FileReader(filePath));
            String destinationValue = findDestinationValue(json);

            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                return;
            }

            // Generate random alphanumeric string
            String randomString = generateRandomString(8);

            // Concatenate and generate MD5 hash
            String toHash = prn + destinationValue + randomString;
            String hash = generateMD5Hash(toHash);

            // Output the result
            System.out.println(hash + ";" + randomString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to traverse JSON and find the first "destination" value
    private static String findDestinationValue(JSONObject json) {
        for (String key : json.keySet()) {
            if (key.equals("destination")) {
                return json.getString(key);
            }
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                String result = findDestinationValue((JSONObject) value);
                if (result != null) return result;
            }
        }
        return null;
    }

    // Function to generate an 8-character alphanumeric string
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Function to generate MD5 hash
    private static String generateMD5Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}


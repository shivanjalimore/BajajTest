package com.app;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateMD5Hash {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter PRN Number: ");
        String prnNumber = scanner.nextLine().toLowerCase().replaceAll("\\s", ""); 

        System.out.print("Enter the path to JSON file: ");
        String jsonFilePath = scanner.nextLine();
        
        String destinationValue = null;
        try {
            destinationValue = findFirstDestinationValue(jsonFilePath);
            if (destinationValue == null) {
                System.out.println("No destination key found in the JSON file.");
                return;
            }

            String randomString = generateRandomString(8);
            String combinedString = prnNumber + destinationValue + randomString;

            String hash = generateMD5Hash(combinedString);
            System.out.println(hash + ";" + randomString);

        } catch (IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating MD5 hash: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static String findFirstDestinationValue(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

        return findDestinationValue(rootNode);
    }

    private static String findDestinationValue(JsonNode node) {
        if (node.has("destination")) {
            return node.get("destination").asText();
        }
        for (JsonNode child : node) {
            String value = findDestinationValue(child);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomString.toString();
    }

    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}


package com.close_contact_monitoring.utility;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputUtility {
	private static final Scanner sc = new Scanner(System.in);	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	//Methods of InputUtility for getting user input, with try-catch blocks to handle exceptions
	
	public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public static char getCharInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().charAt(0);
    }

    public static int getIntInput(String prompt) {
        int input = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = sc.nextInt();
                sc.nextLine(); // Consume newline
                valid = true;
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Please enter a valid INTEGER.");
                sc.next(); // Clear the invalid input
            }
        }
        return input;
    }

    public static LocalDate getDateInput(String prompt) {
        LocalDate date = null;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                date = LocalDate.parse(input, formatter);
                valid = true;
            } catch (DateTimeParseException e) {
                System.err.println("ERROR. Please enter a valid date in the format YYYY-MM-DD.");
            }
        }
        return date;
    }

    public static void closeScanner() {
        sc.close();
    }
}

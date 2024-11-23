package Apps;

import java.util.Scanner;

public class Main {

    public static void dateChooser() {
        int year = -1;
        int month = -1;
        int numChoice = -1;
        Scanner sc = new Scanner(System.in);

        while (numChoice != 0) {
            Menus.healthAssessment_report();
            System.out.print("Enter selected function: ");

            if (sc.hasNextInt()) {
                numChoice = sc.nextInt();
                sc.nextLine();
            } else {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();
                continue;
            }

            if (numChoice < 1 || numChoice > 3) {
                System.out.println("Please enter a valid input between 0 and 3.");
                continue;
            }

            healthfrequencyreport frequency = new healthfrequencyreport();

            switch (numChoice) {
                case 1:
                    do {
                        System.out.println("-------------------------------------------------------");
                        System.out.println("Year: 2023 -> 2024");
                        System.out.println("-------------------------------------------------------");
                        System.out.print("Input Year: ");
                        year = sc.nextInt();

                        if (year < 2023 || year > 2024) {
                            System.out.println("Please enter a valid year!");
                        }
                    } while (year < 2023 || year > 2024);

                    int pcNum = frequency.healthAssessmentFrequencyReport();

                    if (pcNum >= 0) {
                        System.out.println("Health assessment per year: " + pcNum);
                    }

                        else {
                        System.out.println("No Health assessmets currently recorded");
                    }

                    frequency.year = year;
                    break;
                    
                case 2:
                    do {
                        Menus.month();
                        System.out.print("Month (1-12): ");
                        month = sc.nextInt();

                        if (month < 1 || month > 12) {
                            System.out.println("Please enter a valid month number!");
                        }
                    } while (month < 1 || month > 12);

                    frequency.month = month;
                    break;

                default:
                    System.out.println("\nApplication closed!\n");
                    numChoice = 0;
                    break;
            }
        }
        sc.close();
    }
}






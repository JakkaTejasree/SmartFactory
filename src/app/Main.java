
package app;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import data.DatasetReader;
import data.DatasetReader.MachineRecord;
import engine.AlgorithmSelector;
import engine.PatternSearch;
import engine.ProductionDP;
import engine.EdmondsKarp;
import engine.ApproximationScheduler;
import engine.ParallelAnalytics;

public class Main {

    private static void displayRecord(MachineRecord r) {
        if (r == null) {
            System.out.println("Record not found.");
            return;
        }

        System.out.println("\n--- Machine Record ---");
        System.out.println("UDI: " + r.udi);
        System.out.println("Product ID: " + r.productId);
        System.out.println("Type: " + r.type);
        System.out.println("Air Temperature: " + r.airTemperature + " K");
        System.out.println("Process Temperature: "
                + r.processTemperature + " K");
        System.out.println("Rotational Speed: "
                + r.rotationalSpeed + " rpm");
        System.out.println("Torque: " + r.torque + " Nm");
        System.out.println("Tool Wear: " + r.toolWear + " min");
        System.out.println("Machine Failure: " + r.machineFailure);
    }

    public static void main(String[] args) {

        try {
            List<MachineRecord> records = DatasetReader.loadData();

            System.out.println("\n==========================================");
            System.out.println(" SmartFactory Industrial Production Analytics");
            System.out.println("==========================================");
            System.out.println("Dataset records loaded: " + records.size());

            try (Scanner sc = new Scanner(System.in)) {

                while (true) {
                    System.out.println("\n========== MAIN MENU ==========");
                    System.out.println("1. CO1 - Algorithm Selection");
                    System.out.println("2. CO2 - Pattern Searching");
                    System.out.println("3. CO3 - Dynamic Programming");
                    System.out.println("4. CO4 - Maximum Flow");
                    System.out.println("5. CO5 - Approximation Scheduling");
                    System.out.println("6. CO6 - Parallel Analytics");
                    System.out.println("0. Exit");
                    System.out.print("Enter your choice: ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Enter a valid number.");
                        sc.nextLine();
                        continue;
                    }

                    int choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {

                        case 1:
                            System.out.println("\n--- CO1: Algorithm Selection ---");
                            System.out.println("1. Linear Search");
                            System.out.println("2. Binary Search");
                            System.out.print("Choose algorithm: ");

                            if (!sc.hasNextInt()) {
                                System.out.println("Invalid choice.");
                                sc.nextLine();
                                break;
                            }

                            int algorithm = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter machine UDI to search: ");

                            if (!sc.hasNextInt()) {
                                System.out.println("Enter a valid UDI.");
                                sc.nextLine();
                                break;
                            }

                            int udi = sc.nextInt();
                            sc.nextLine();

                            MachineRecord result;

                            if (algorithm == 1) {
                                result = AlgorithmSelector.linearSearch(
                                        records, udi);
                            } else if (algorithm == 2) {
                                result = AlgorithmSelector.binarySearch(
                                        records, udi);
                            } else {
                                System.out.println("Invalid algorithm choice.");
                                break;
                            }

                            displayRecord(result);
                            break;

                        case 2:
                            System.out.println("\n--- CO2: Pattern Searching ---");
                            System.out.println(
                                "Search by Product ID or Type");
                            System.out.print(
                                "Enter pattern (e.g. M14860 or L): ");

                            String pattern = sc.nextLine().trim();

                            if (pattern.isEmpty()) {
                                System.out.println("Pattern cannot be empty.");
                                break;
                            }

                            PatternSearch.searchProducts(records, pattern);
                            break;

                        case 3:
                            System.out.println(
                                "\n--- CO3: Dynamic Programming ---");
                            ProductionDP.run(records);
                            break;

                        case 4:
                            System.out.println(
                                "\n--- CO4: Maximum Flow ---");
                            EdmondsKarp.run(records);
                            break;

                        case 5:
                            System.out.println(
                                "\n--- CO5: Approximation Scheduling ---");
                            ApproximationScheduler.run(records);
                            break;

                        case 6:
                            System.out.println(
                                "\n--- CO6: Parallel Analytics ---");
                            ParallelAnalytics.run(records);
                            break;

                        case 0:
                            System.out.println("Exiting SmartFactory.");
                            return;

                        default:
                            System.out.println(
                                "Invalid choice. Select 0 to 6.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("\nCould not read dataset.");
            System.out.println("Expected file: "
                    + DatasetReader.FILE_PATH);
            System.out.println("Error: " + e.getMessage());
        }
    }
}

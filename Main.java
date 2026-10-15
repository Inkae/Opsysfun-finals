// OPERATING SYSTEM FUNDAMENTALS FINAL OUTPUT
// Caranto, Crisiane | Mangaliman, Rolaigne | Velasco, Aiken

import java.util.Scanner;

public class Main {

    static int readInt(Scanner scanner, String message, int min) {
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                if (value >= min) {
                    return value;
                }
                System.out.println("Value must be at least " + min + ". Try again.");
            } else {
                System.out.println("Invalid input. Please enter a whole number.");
                scanner.next(); 
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("Preemptive Scheduling Algorithms");
            System.out.println("  1. Round Robin");
            System.out.println("  2. Preemptive Priority");
            System.out.println("Non-Preemptive Algorithms");
            System.out.println("  3. Shortest Job Next");
            System.out.println("  4. Non-Preemptive Priority");
            System.out.println("Page Replacement Algorithms");
            System.out.println("  5. First In First Out (FIFO)");
            System.out.println("  6. Most Recently Used (MRU)");
            System.out.println("  7. Exit Application");
            
            int choice = readInt(scanner, "\nChoose an option (1-7): ", 1);

            if (choice == 7) {
                running = false;
                System.out.println("Exiting Application...");
                continue;
            }

            if (choice < 1 || choice > 7) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            // NON-PREEMPTIVE ALGORITHMS (SJN AND NPP)
            if (choice == 3 || choice == 4) {
                System.out.println("\n-Enter the following details-");
                int n = readInt(scanner, "Enter number of processes: ", 1);
                int[] proccessID = new int[n];
                int[] arrival = new int[n];
                int[] burst = new int[n];
                int[] priority = new int[n];
 
                for (int i = 0; i < n; i++) {
                    System.out.println("Process " + (i + 1) + ":");
                    boolean duplicate = true;
                    while (duplicate) {
                        duplicate = false;
                        proccessID[i] = readInt(scanner, "  Process ID: ", 0);
                        for (int j = 0; j < i; j++) {
                            if (proccessID[j] == proccessID[i]) {
                                duplicate = true;
                                System.out.println("  Process ID already used. Enter a different one.");
                            }
                        }
                    }

                    arrival[i] = readInt(scanner, "  Arrival Time: ", 0);
                    burst[i] = readInt(scanner, "  Burst Time: ", 1);

                    if (choice == 4) {
                        priority[i] = readInt(scanner, "  Priority (lower number = higher priority): ", 0);
                    }
                }
 
                if (choice == 3) {
                    SJN sjn = new SJN();
                    sjn.run(proccessID, arrival, burst);
                } else {
                    NPP npp = new NPP();
                    npp.run(proccessID, arrival, burst, priority);
                }
                continue;
            }

            // Input handling for Page Replacement Algorithms (Choices 5 and 6)
            System.out.print("Enter number of memory frames: ");
            int frames = scanner.nextInt();

            System.out.print("Enter the number of pages in your reference string: ");
            int size = scanner.nextInt();
            
            int[] referenceString = new int[size];
            System.out.print("Enter the " + size + " pages separated by spaces: ");
            for (int i = 0; i < size; i++) {
                referenceString[i] = scanner.nextInt();
            }

            // Run the chosen page replacement algorithm
            if (choice == 5) {
                FIFO fifo = new FIFO(frames);
                fifo.run(referenceString);
            } else if (choice == 6) {
                MRU mru = new MRU(frames);
                mru.run(referenceString);
            }
        }
        scanner.close();
    }
}
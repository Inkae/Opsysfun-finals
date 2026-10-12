import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("Preemptive Scheduling Algorithms - SIANE");
            System.out.println("  1. Round Robin");
            System.out.println("  2. Preemptive Priority");
            System.out.println("Non-Preemptive Algorithms - ROLAIGNE");
            System.out.println("  3. Shortest Job Next");
            System.out.println("  4. Non-Preemptive Priority");
            System.out.println("Page Replacement Algorithms - Aiken");
            System.out.println("  5. First In First Out (FIFO)");
            System.out.println("  6. Most Recently Used (MRU)");
            System.out.println("  7. Exit Application");
            System.out.print("Choose an option (1-7): ");
            
            int choice = scanner.nextInt();

            if (choice == 7) {
                running = false;
                System.out.println("Exiting Application...");
                continue;
            }

            if (choice < 1 || choice > 7) {
                System.out.println("Invalid choice. Try again.");
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
import java.util.LinkedList;
import java.util.Queue;

public class FIFO {
    private int frames;

    public FIFO(int frames) {
        this.frames = frames;
    }

    public void run(int[] referenceString) {
        System.out.println("\n--- First In First Out (FIFO) ---");
        Queue<Integer> memory = new LinkedList<>();
        int faults = 0;
        int hits = 0;
        int totalRequests = referenceString.length;

        for (int page : referenceString) {
            if (!memory.contains(page)) {
                // Page Fault
                if (memory.size() == frames) {
                    memory.poll(); 
                }
                memory.add(page); 
                faults++;
                System.out.println("Page " + page + " -> Fault: " + memory);
            } else {
                // Page Hit
                hits++;
                System.out.println("Page " + page + " -> Hit:   " + memory);
            }
        }
        
        // Calculate Rates
        double faultRate = ((double) faults / totalRequests) * 100;
        double hitRate = ((double) hits / totalRequests) * 100;

        System.out.println("\n--- FIFO Results ---");
        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Total Faults:   " + faults);
        System.out.println("Total Hits:     " + hits);
        System.out.printf("Fault Rate (Failure): %.2f%%\n", faultRate);
        System.out.printf("Hit Rate (Success):   %.2f%%\n", hitRate);
    }
}
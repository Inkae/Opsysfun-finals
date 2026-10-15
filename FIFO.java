/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    FIRST IN FIRST OUT (FIFO) PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

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
                System.out.printf("Page %-3d -> Fault: %s\n", page, memory);
            } else {
                // Page Hit
                hits++;
                System.out.printf("Page %-3d -> Hit:   %s\n", page, memory);
            }
        }
        
        // Calculate Rates
        double faultRate = ((double) faults / totalRequests) * 100;
        double hitRate = ((double) hits / totalRequests) * 100;

        System.out.println("\n--- FIFO Results ---");
        System.out.printf("Total Requests:       %d\n", totalRequests);
        System.out.printf("Total Faults:         %d\n", faults);
        System.out.printf("Total Hits:           %d\n", hits);
        System.out.printf("Fault Rate (Failure): %.2f%%\n", faultRate);
        System.out.printf("Hit Rate (Success):   %.2f%%\n", hitRate);
    }
}
/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    MOST RECENTLY USED (MRU) PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

import java.util.ArrayList;

public class MRU {
    private int frames;

    public MRU(int frames) {
        this.frames = frames;
    }

    public void run(int[] referenceString) {
        System.out.println("\n--- Most Recently Used (MRU) ---");
        ArrayList<Integer> memory = new ArrayList<>();
        int faults = 0;
        int hits = 0;
        int totalRequests = referenceString.length;

        for (int page : referenceString) {
            if (!memory.contains(page)) {
                // Page Fault
                if (memory.size() == frames) {
                    memory.remove(memory.size() - 1); 
                }
                memory.add(page); 
                faults++;
                System.out.printf("Page %-3d -> Fault: %s\n", page, memory);
            } else {
                // Page Hit
                memory.remove(Integer.valueOf(page)); 
                memory.add(page); 
                hits++;
                System.out.printf("Page %-3d -> Hit:   %s\n", page, memory);
            }
        }

        // Calculate Rates
        double faultRate = ((double) faults / totalRequests) * 100;
        double hitRate = ((double) hits / totalRequests) * 100;

        System.out.println("\n--- MRU Results ---");
        System.out.printf("Total Requests:       %d\n", totalRequests);
        System.out.printf("Total Faults:         %d\n", faults);
        System.out.printf("Total Hits:           %d\n", hits);
        System.out.printf("Fault Rate (Failure): %.2f%%\n", faultRate);
        System.out.printf("Hit Rate (Success):   %.2f%%\n", hitRate);
    }
}
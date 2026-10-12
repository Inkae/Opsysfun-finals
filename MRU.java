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
                System.out.println("Page " + page + " -> Fault: " + memory);
            } else {
                // Page Hit
                memory.remove(Integer.valueOf(page)); 
                memory.add(page); 
                hits++;
                System.out.println("Page " + page + " -> Hit:   " + memory);
            }
        }

        // Calculate Rates
        double faultRate = ((double) faults / totalRequests) * 100;
        double hitRate = ((double) hits / totalRequests) * 100;

        System.out.println("\n--- MRU Results ---");
        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Total Faults:   " + faults);
        System.out.println("Total Hits:     " + hits);
        System.out.printf("Fault Rate (Failure): %.2f%%\n", faultRate);
        System.out.printf("Hit Rate (Success):   %.2f%%\n", hitRate);
    }
}
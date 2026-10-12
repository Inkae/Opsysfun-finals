import java.util.LinkedList;
import java.util.Queue;

public class FIFO {
    private int frames;

    public FIFO(int frames) {
        this.frames = frames;
    }

    // Runs the FIFO algorithm
    public void run(int[] referenceString) {
        System.out.println("\n--- First In First Out (FIFO) ---");
        Queue<Integer> memory = new LinkedList<>();
        int faults = 0;

        for (int page : referenceString) {
            if (!memory.contains(page)) {
                // Page Fault: Memory is full, remove the oldest page
                if (memory.size() == frames) {
                    memory.poll(); 
                }
                // Add the new page
                memory.add(page); 
                faults++;
                System.out.println("Page " + page + " -> Fault: " + memory);
            } else {
                // Page Hit
                System.out.println("Page " + page + " -> Hit:   " + memory);
            }
        }
        System.out.println("Total FIFO Faults: " + faults);
    }
}
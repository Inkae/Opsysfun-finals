import java.util.ArrayList;

public class MRU {
    private int frames;

    public MRU(int frames) {
        this.frames = frames;
    }

    // Runs the MRU algorithm
    public void run(int[] referenceString) {
        System.out.println("\n--- Most Recently Used (MRU) ---");
        ArrayList<Integer> memory = new ArrayList<>();
        int faults = 0;

        for (int page : referenceString) {
            if (!memory.contains(page)) {
                // Page Fault: Memory is full, remove the most recently used (last item)
                if (memory.size() == frames) {
                    memory.remove(memory.size() - 1); 
                }
                // Add the new page to the end
                memory.add(page); 
                faults++;
                System.out.println("Page " + page + " -> Fault: " + memory);
            } else {
                // Page Hit: Move the used page to the end to mark it as most recently used
                memory.remove(Integer.valueOf(page)); 
                memory.add(page); 
                System.out.println("Page " + page + " -> Hit:   " + memory);
            }
        }
        System.out.println("Total MRU Faults: " + faults);
    }
}
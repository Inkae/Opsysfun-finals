/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    MOST RECENTLY USED (MRU) PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

public class MRU {
    private int frames;

    public MRU(int frames) {
        this.frames = frames;
    }

    public void run(int[] referenceString) {
        System.out.println("\n--- Most Recently Used (MRU) ---");

        // fixed frame slots, -1 means the slot is empty
        int[] memory = new int[frames];
        // remembers when each slot was last used
        int[] lastUsed = new int[frames];
        for (int i = 0; i < frames; i++) {
            memory[i] = -1;
            lastUsed[i] = -1;
        }

        int faults = 0;
        int hits = 0;
        int totalRequests = referenceString.length;

        for (int t = 0; t < referenceString.length; t++) {
            int page = referenceString[t];

            // look for the page in the frames
            int slot = -1;
            for (int i = 0; i < frames; i++) {
                if (memory[i] == page) {
                    slot = i;
                }
            }

            if (slot != -1) {
                // Page Hit
                // nothing moves, only update the last-used time
                lastUsed[slot] = t;
                hits++;
                System.out.printf("Page %-3d -> Hit:   %s\n", page, showFrames(memory));
            } else {
                // Page Fault
                // use an empty slot first if there is one
                int target = -1;
                for (int i = 0; i < frames; i++) {
                    if (memory[i] == -1 && target == -1) {
                        target = i;
                    }
                }

                // if all frames are full, pick the slot with the
                // highest lastUsed (the most recently used page)
                if (target == -1) {
                    target = 0;
                    for (int i = 1; i < frames; i++) {
                        if (lastUsed[i] > lastUsed[target]) {
                            target = i;
                        }
                    }
                }

                // new page takes the same slot, other frames stay in place
                memory[target] = page;
                lastUsed[target] = t;
                faults++;
                System.out.printf("Page %-3d -> Fault: %s\n", page, showFrames(memory));
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

    // builds the frame display like [7, 0, 1], empty slots show as -
    private String showFrames(int[] memory) {
        String text = "[";
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == -1) {
                text = text + "-";
            } else {
                text = text + memory[i];
            }
            if (i < memory.length - 1) {
                text = text + ", ";
            }
        }
        text = text + "]";
        return text;
    }
}
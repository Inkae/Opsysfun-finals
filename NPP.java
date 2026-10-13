// NON-PREEMPTIVE ALGORITHMS
// NON-PREEMPTIVE PRIORITY

public class NPP {

    // a lower priority number means higher priority (1 = highest)
    public void run(int[] proccessID, int[] arrival, int[] burst, int[] priority) {
        System.out.println("\n--- Non-Preemptive Priority ---");
        int n = proccessID.length;
        boolean[] done = new boolean[n];
        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int time = 0;
        int finished = 0;

        System.out.println("Ready Queue:");
            // Among the processes that have arrived pick the one with the lowest priority number
            while (finished < n) {
            int pick = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= time) {
                    if (pick == -1 || priority[i] < priority[pick]
                            || (priority[i] == priority[pick] && arrival[i] < arrival[pick])) {
                        pick = i;
                    }
                }
            }

            // no process has arrived yet, so the CPU is idle.
            if (pick == -1) {
                int next = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!done[i] && arrival[i] < next) {
                        next = arrival[i];
                    }
                }
                System.out.println("CPU idle from " + time + " to " + next);
                time = next;
                continue;
            }

            // Run the process until it finishes
            int start = time;
            time += burst[pick];
            completion[pick] = time;
            turnaround[pick] = completion[pick] - arrival[pick];
            waiting[pick] = turnaround[pick] - burst[pick];
            done[pick] = true;
            finished++;
            System.out.println("P" + proccessID[pick] + " (priority " + priority[pick] + ") runs from "
                    + start + " to " + time);
        }

        // Print results
         double totalWaiting = 0;
        double totalTurnaround = 0;
        System.out.println("\nProcess ID\tArrival\t\tBurst\t\tPriority\tComplete\tTurnaround\tWaiting");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + proccessID[i] + "\t\t" + arrival[i] + "\t\t" + burst[i] + "\t\t"
                    + priority[i] + "\t\t" + completion[i] + "\t\t" + turnaround[i] + "\t\t" + waiting[i]);
            totalWaiting += waiting[i];
            totalTurnaround += turnaround[i];
        }
 
        System.out.println("\n--- Non-Preemptive Priority Results ---");
        System.out.printf("Average Waiting Time:    %.2f\n", totalWaiting / n);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / n);
    }
}
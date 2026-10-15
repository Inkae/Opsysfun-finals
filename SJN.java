// NON-PREEMPTIVE ALGORITHMS
// SHORTEST JOB NEXT

public class SJN {
 
    public void run(int[] proccessID, int[] arrival, int[] burst) {
        System.out.println("\n--- Shortest Job Next (SJN) ---");
        int n = proccessID.length;
        boolean[] done = new boolean[n];
        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int time = 0;
        int finished = 0;
 
        System.out.println("Ready Queue:");
        while (finished < n) {
            // pick the one with the shortest burst time
            int pick = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= time) {
                    if (pick == -1 || burst[i] < burst[pick]
                            || (burst[i] == burst[pick] && arrival[i] < arrival[pick])) {
                        pick = i;
                    }
                }
            }
 
            // no process has arrived yet so the CPU is idle.
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
            System.out.println("P" + proccessID[pick] + " runs from " + start + " to " + time);
        }
 
        // Print results
        double totalWaiting = 0;
        double totalTurnaround = 0;
        System.out.println("\nProcess ID\tArrival\t\tBurst(CPU Time)\t\tEnd\tTurnaround\tWaiting");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + proccessID[i] + "\t\t" + arrival[i] + "\t\t" + burst[i] + "\t\t\t"
                    + completion[i] + "\t" + turnaround[i] + "\t\t" + waiting[i]);
            totalWaiting += waiting[i];
            totalTurnaround += turnaround[i];
        }
 
        System.out.println("\n--- SJN Results ---");
        System.out.printf("Average Waiting Time:    %.2f\n", totalWaiting / n);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / n);
    }
}
 
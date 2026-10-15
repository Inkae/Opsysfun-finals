/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    SHORTEST JOB NEXT (SJN) PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

// NON-PREEMPTIVE ALGORITHMS

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
        System.out.println();
        System.out.printf("%-12s %-14s %-12s %-18s %-16s %-14s\n", 
            "Process ID", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time", "Waiting Time");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%-11d %-14d %-12d %-18d %-16d %-14d\n",
                proccessID[i], arrival[i], burst[i], completion[i], turnaround[i], waiting[i]);
            totalWaiting += waiting[i];
            totalTurnaround += turnaround[i];
        }
 
        System.out.println("\n--- SJN Results ---");
        System.out.printf("Average Waiting Time:    %.2f\n", totalWaiting / n);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / n);
    }
}
/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    ROUND ROBIN SCHEDULING PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

/*
    CODE BASED ON:
    GeeksforGeeks. (2026, September 1). Round robin scheduling algorithm with different arrival time. https://www.geeksforgeeks.org/dsa/round-robin-scheduling-with-different-arrival-times/
    GeeksforGeeks. (2025, July 23). Program for round robin scheduling for the same arrival time. https://www.geeksforgeeks.org/operating-systems/program-for-round-robin-scheduling-for-the-same-arrival-time/
*/

import java.util.*;

public class RR{
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("\n--- Round Robin ---");
        int num_process, tq;
        System.out.print("Enter the time quantum : ");
        tq = input.nextInt();
        System.out.print("Enter the number of processes : ");
        num_process = input.nextInt();

        int id[] = new int[num_process];
        int arrival[] = new int[num_process];
        int burst[] = new int[num_process];
        int temp_burst[] = new int[num_process];

        for (int i = 0; i < num_process; i++) {
            id[i] = i + 1;
        }

        //loop input for the arrival times based on num_process
        System.out.print("\nEnter the arrival time of the processes (space-separated) : ");
        for (int i = 0; i < num_process; i++) {
            arrival[i] = input.nextInt();
        }

        //loop input for the burst times based on num_process
        System.out.print("\nEnter the burst time of the processes (space-separated) : ");
        for (int i = 0; i < num_process; i++) {
            burst[i] = input.nextInt();
            temp_burst[i] = burst[i];
        }

        // Sort processes by arrival time, and by ID if arrival times are identical
        // this is where the processes are sorted
        /*
            - compares arrival (higher arrival swaps with lower arrival)
            - (arrival[i] == arrival[j] && id[i] > id[j]) -> handles same arrival times by comparing id
        */
        for (int i = 0; i < num_process - 1; i++) {
            for (int j = i + 1; j < num_process; j++) {
                if (arrival[i] > arrival[j] || (arrival[i] == arrival[j] && id[i] > id[j])) {
                    int temp = arrival[i]; arrival[i] = arrival[j]; arrival[j] = temp;
                    temp = burst[i]; burst[i] = burst[j]; burst[j] = temp;
                    temp = temp_burst[i]; temp_burst[i] = temp_burst[j]; temp_burst[j] = temp;
                    temp = id[i]; id[i] = id[j]; id[j] = temp;
                }
            }
        }

        int wait[] = new int[num_process];
        int turn[] = new int[num_process];
        int completion[] = new int[num_process];
        boolean visited[] = new boolean[num_process];

        // initializes ready queue and status of processes
        Queue<Integer> queue = new LinkedList<>();
        int currentTime = arrival[0];
        int completed = 0; //increments when process finishes

        // Add initial arriving processes to the queue
        for (int i = 0; i < num_process; i++) {
            if (arrival[i] <= currentTime && !visited[i]) {
                queue.add(i);
                visited[i] = true;
            }
        }

        while (completed < num_process) {
            if (queue.isEmpty()) {
                // CPU is idle, jump to the arrival time of the next unvisited process
                for (int i = 0; i < num_process; i++) {
                    if (!visited[i]) {
                        currentTime = arrival[i];
                        queue.add(i);
                        visited[i] = true;
                        break;
                    }
                }
            }

            int idx = queue.poll();

            if (temp_burst[idx] > tq) {
                currentTime += tq;
                temp_burst[idx] -= tq;
            } else {
                currentTime += temp_burst[idx];
                temp_burst[idx] = 0;
                completion[idx] = currentTime;
                turn[idx] = completion[idx] - arrival[idx];
                wait[idx] = turn[idx] - burst[idx];
                completed++;
            }

            // Check for newly arrived processes up to current time
            for (int i = 0; i < num_process; i++) {
                if (arrival[i] <= currentTime && !visited[i]) {
                    queue.add(i);
                    visited[i] = true;
                }
            }

            // If the process is not finished, requeue it
            if (temp_burst[idx] > 0) {
                queue.add(idx);
            }
        }

        // Reorder results back by original process ID for output printing
        int finalArrival[] = new int[num_process];
        int finalBurst[] = new int[num_process];
        int finalWait[] = new int[num_process];
        int finalTurn[] = new int[num_process];
        int finalCompletion[] = new int[num_process];

        // adds results to the arrays for the results
        for (int i = 0; i < num_process; i++) {
            int origIdx = id[i] - 1;
            finalArrival[origIdx] = arrival[i];
            finalBurst[origIdx] = burst[i];
            finalWait[origIdx] = wait[i];
            finalTurn[origIdx] = turn[i];
            finalCompletion[origIdx] = completion[i];
        }

        //printing the output by looping the results arrays
        float avgWait = 0, avgTT = 0;
        System.out.println();
        System.out.printf("%-12s %-14s %-12s %-18s %-16s %-14s\n", 
            "Process ID", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time", "Waiting Time");

        for (int i = 0; i < num_process; i++) {
            System.out.printf("P%-11d %-14d %-12d %-18d %-16d %-14d\n", 
                (i + 1), finalArrival[i], finalBurst[i], finalCompletion[i], finalTurn[i], finalWait[i]);
            avgWait += finalWait[i];
            avgTT += finalTurn[i];
        }

        System.out.println("\n--- Round Robin Results ---");
        System.out.printf("Average Waiting Time:    %.2f\n", (avgWait / num_process));
        System.out.printf("Average Turnaround Time: %.2f\n", (avgTT / num_process));
    }
}
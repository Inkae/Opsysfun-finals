/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    PREEMPTIVE PRIORITY SCHEDULING PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

/*
    CODE BASED ON:
    GeeksforGeeks. (n.d.). Program for preemptive priority CPU scheduling. https://www.geeksforgeeks.org/dsa/program-for-preemptive-priority-cpu-scheduling/ 
*/

import java.util.Arrays;
import java.util.Comparator;

public class PP {

    // Static inner Process class representing each job/process
    static class Process {
        int processID;
        int arrivalTime;
        int priority;
        int burstTime;
        int tempBurstTime;
        int responseTime;
        int outTime;
        int inTime;

        // Constructor to initialize process attributes
        Process(int processID, int arrivalTime, int priority, int burstTime) {
            this.processID = processID;
            this.arrivalTime = arrivalTime;
            this.priority = priority;
            this.burstTime = burstTime;
            this.tempBurstTime = burstTime;
            this.responseTime = -1;
            this.outTime = 0;
            this.inTime = -1;
        }
    }

    // Helper method to compare two processes: returns true if p1 has higher priority than p2
    // Criteria: 
    // 1. Lower priority value comes first (higher priority).
    // 2. If priority is same, earlier arrival time comes first.
    // 3. If arrival time is also same, lower processID comes first (FCFS tie-breaker).
    static boolean shouldSwap(Process p1, Process p2) {
        if (p1.priority != p2.priority) {
            return p1.priority < p2.priority;
        }
        if (p1.arrivalTime != p2.arrivalTime) {
            return p1.arrivalTime < p2.arrivalTime;
        }
        return p1.processID < p2.processID;
    }

    // Function to insert a process into the min-heap based on strict ordering
    static void insert(Process[] heap, Process value, int[] heapSize, int[] currentTime) {
        int start = heapSize[0];
        heap[heapSize[0]] = value;
        if (heap[heapSize[0]].inTime == -1) {
            heap[heapSize[0]].inTime = currentTime[0];
        }
        heapSize[0]++;

        // Ordering the Heap using the strict comparator helper
        while (start != 0 && shouldSwap(heap[start], heap[(start - 1) / 2])) {
            Process temp = heap[(start - 1) / 2];
            heap[(start - 1) / 2] = heap[start];
            heap[start] = temp;
            start = (start - 1) / 2;
        }
    }

    // Function to reorder the heap based on strict priority/arrival/ID rules after extraction
    static void order(Process[] heap, int heapSize, int start) {
        int smallest = start;
        int left = 2 * start + 1;
        int right = 2 * start + 2;

        if (left < heapSize && shouldSwap(heap[left], heap[smallest])) {
            smallest = left;
        }
        if (right < heapSize && shouldSwap(heap[right], heap[smallest])) {
            smallest = right;
        }

        if (smallest != start) {
            Process temp = heap[smallest];
            heap[smallest] = heap[start];
            heap[start] = temp;
            order(heap, heapSize, smallest);
        }
    }

    // Function to extract the process with the highest priority from the heap
    static Process extractMinimum(Process[] heap, int[] heapSize, int[] currentTime) {
        Process minProcess = heap[0];
        if (minProcess.responseTime == -1) {
            minProcess.responseTime = currentTime[0] - minProcess.arrivalTime;
        }
        heapSize[0]--;
        if (heapSize[0] >= 1) {
            heap[0] = heap[heapSize[0]];
            order(heap, heapSize[0], 0);
        }
        return minProcess;
    }

    // Function responsible for executing the highest priority process for 1 time unit
    static void scheduling(Process[] heap, Process[] array, int num_process, int[] heapSize, int[] currentTime) {
        if (heapSize[0] == 0) {
            return;
        }

        Process minProcess = extractMinimum(heap, heapSize, currentTime);
        minProcess.outTime = currentTime[0] + 1;
        minProcess.burstTime--;

        // If the process is not finished, re-insert it back into the Heap
        if (minProcess.burstTime > 0) {
            insert(heap, minProcess, heapSize, currentTime);
            return;
        }

        // Update the main array once a process completes
        for (int i = 0; i < num_process; i++) {
            if (array[i].processID == minProcess.processID) {
                array[i] = minProcess;
                break;
            }
        }
    }

    // Core execution function
    public void run(int[] processID, int[] arrival, int[] burst, int[] priority) {
        System.out.println("\n--- Preemptive Priority (Strict FCFS Tie-Breaker) ---");
        int num_process = processID.length;

        Process[] array = new Process[num_process];
        for (int i = 0; i < num_process; i++) {
            array[i] = new Process(processID[i], arrival[i], priority[i], burst[i]);
        }

        // Sort processes initially by arrival time
        Arrays.sort(array, Comparator.comparingInt(o -> o.arrivalTime));

        int insertedProcess = 0;
        int[] heapSize = {0};
        int[] currentTime = {array[0].arrivalTime};

        Process[] heap = new Process[4 * num_process];

        // Core simulation loop managing preemptive scheduling over time
        do {
            if (insertedProcess != num_process) {
                for (int i = 0; i < num_process; i++) {
                    if (array[i].arrivalTime == currentTime[0]) {
                        insertedProcess++;
                        array[i].inTime = -1;
                        array[i].responseTime = -1;
                        insert(heap, array[i], heapSize, currentTime);
                    }
                }
            }
            scheduling(heap, array, num_process, heapSize, currentTime);
            currentTime[0]++;
            if (heapSize[0] == 0 && insertedProcess == num_process) {
                break;
            }
        } while (true);

        // Sort results back by original process ID for sequential output printing
        Arrays.sort(array, Comparator.comparingInt(o -> o.processID));

        float avgWait = 0, avgTT = 0;
        System.out.println();
        System.out.printf("%-12s %-14s %-12s %-10s %-18s %-16s %-14s\n", 
            "Process ID", "Arrival Time", "Burst Time", "Priority", "Completion Time", "Turnaround Time", "Waiting Time");

        for (int i = 0; i < num_process; i++) {
            int waitTime = array[i].outTime - array[i].inTime - array[i].tempBurstTime;
            int turnaroundTime = array[i].outTime - array[i].inTime;

            System.out.printf("P%-11d %-14d %-12d %-10d %-18d %-16d %-14d\n", 
                array[i].processID, array[i].arrivalTime, array[i].tempBurstTime, 
                array[i].priority, array[i].outTime, turnaroundTime, waitTime);

            avgWait += waitTime;
            avgTT += turnaroundTime;
        }

        System.out.println("\n--- Preemptive Priority Results ---");
        System.out.printf("Average Waiting Time:    %.2f\n", (avgWait / num_process));
        System.out.printf("Average Turnaround Time: %.2f\n", (avgTT / num_process));
    }
}
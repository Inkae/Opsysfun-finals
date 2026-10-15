/* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    PREEMPTIVE PRIORITY SCHEDULING PROGRAM

    Caranto, Crisiane Josef A
    Mangaliman, Rolaigne E.
    Velasco, Aiken A.

    CYB 202 - OPSYSFUN FINALS
+=+=+=+=+=+=+=+=+=+=+=+=+=+=*/

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class PP {
    private static Scanner input = new Scanner(System.in);

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

    // Function to insert a process into the min-heap based on priority
    static void insert(Process[] heap, Process value, int[] heapSize, int[] currentTime) {
        int start = heapSize[0];
        heap[heapSize[0]] = value;
        if (heap[heapSize[0]].inTime == -1) {
            heap[heapSize[0]].inTime = currentTime[0];
        }
        heapSize[0]++;

        // Ordering the Heap (Min-Heap based on priority value: lower number = higher priority)
        while (start != 0 && heap[(start - 1) / 2].priority > heap[start].priority) {
            Process temp = heap[(start - 1) / 2];
            heap[(start - 1) / 2] = heap[start];
            heap[start] = temp;
            start = (start - 1) / 2;
        }
    }

    // Function to reorder the heap based on priority after extraction
    static void order(Process[] heap, int heapSize, int start) {
        int smallest = start;
        int left = 2 * start + 1;
        int right = 2 * start + 2;
        if (left < heapSize && heap[left].priority < heap[smallest].priority) {
            smallest = left;
        }
        if (right < heapSize && heap[right].priority < heap[smallest].priority) {
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

    // Driver Code
    public static void main(String[] args) {
        System.out.println("\n+=+=+=+ PRIORITY SCHEDULING DEMONSTRATION +=+=+=+");
        int num_process;
        System.out.print("Enter the number of processes : ");
        num_process = input.nextInt();

        int arrival[] = new int[num_process];
        int priority[] = new int[num_process];
        int burst[] = new int[num_process];

        // Loop input for arrival times
        System.out.print("\nEnter the arrival time of the processes (space-separated) : ");
        for (int i = 0; i < num_process; i++) {
            arrival[i] = input.nextInt();
        }

        // Loop input for priorities
        System.out.print("\nEnter the priority of the processes (space-separated) : ");
        for (int i = 0; i < num_process; i++) {
            priority[i] = input.nextInt();
        }

        // Loop input for burst times
        System.out.print("\nEnter the burst time of the processes (space-separated) : ");
        for (int i = 0; i < num_process; i++) {
            burst[i] = input.nextInt();
        }

        Process[] array = new Process[num_process];
        for (int i = 0; i < num_process; i++) {
            array[i] = new Process(i + 1, arrival[i], priority[i], burst[i]);
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
        System.out.print("\nProgram No.\tArrival Time\tPriority\tBurst Time\tWaiting Time\tTurnaround Time\n");
        for (int i = 0; i < num_process; i++) {
            int waitTime = array[i].outTime - array[i].inTime - array[i].tempBurstTime;
            int turnaroundTime = array[i].outTime - array[i].inTime;

            System.out.printf("%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n", 
                array[i].processID, array[i].arrivalTime, array[i].priority, 
                array[i].tempBurstTime, waitTime, turnaroundTime);

            avgWait += waitTime;
            avgTT += turnaroundTime;
        }

        System.out.printf("\nAverage Waiting Time : %,.2f\nAverage Turnaround Time : %,.2f\n", (avgWait / num_process), (avgTT / num_process));
    }
}
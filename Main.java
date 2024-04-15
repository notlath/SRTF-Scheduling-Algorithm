import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Shortest remaining time (SRTF) a scheduling algorithm that selects the process with the least amount of time left until completion
// Process class to hold process details
class Process {
    int pid; // Process ID
    int arrivalTime; // Arrival time of the process
    int burstTime; // Burst time of the process
    int startTime; // Start time of the process
    int completionTime; // Completion time of the process
    int turnaroundTime; // Turnaround time of the process
    int waitingTime; // Waiting time of the process
    int responseTime; // Response time of the process
}

// GanttChartEntry class to hold Gantt chart entries
class GanttChartEntry {
    int pid; // Process ID
    int duration; // Duration of the process

    // Constructor to initialize GanttChartEntry
    GanttChartEntry(int pid, int duration) {
        this.pid = pid;
        this.duration = duration;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Scanner to read input
        int x; // Number of processes
        Process[] p = new Process[100]; // Array to hold processes
        for (int i = 0; i < 100; i++) {
            p[i] = new Process(); // Initialize each process
        }
        // Variables to hold various times and utilization
        float avgTurnaroundTime;
        float avgWaitingTime;
        float avgResponseTime;
        float cpuUtilization;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        int totalResponseTime = 0;
        int totalIdleTime = 0;
        float throughput;
        int[] burstRemaining = new int[100]; // Array to hold remaining burst time for each process
        int[] isCompleted = new int[100]; // Array to hold completion status of each process
        Arrays.fill(isCompleted, 0); // Initialize all processes as not completed
        ArrayList<GanttChartEntry> ganttChart = new ArrayList<>(); // ArrayList to hold Gantt chart entries

        System.out.print("Enter the number of processes: ");
        x = sc.nextInt(); // Read number of processes
        for (int i = 0; i < x; i++) {
            System.out.print("Enter arrival time of the process " + (i + 1) + ": ");
            p[i].arrivalTime = sc.nextInt(); // Read arrival time of process
            System.out.print("Enter burst time of the process " + (i + 1) + ": ");
            p[i].burstTime = sc.nextInt(); // Read burst time of process
            p[i].pid = i + 1; // Set process ID
            burstRemaining[i] = p[i].burstTime; // Initialize remaining burst time for process
        }
        // Variables to hold current time, number of completed processes, and previous
        // process
        int currentTime = 0;
        int completed = 0;
        int prev = 0;
        // Main loop to process all processes
        while (completed != x) {
            int idx = -1; // Index of process to be executed
            int mn = Integer.MAX_VALUE; // Minimum burst time
            // Find process with minimum burst time
            for (int i = 0; i < x; i++) {
                if (p[i].arrivalTime <= currentTime && isCompleted[i] == 0) {
                    if (burstRemaining[i] < mn) {
                        mn = burstRemaining[i];
                        idx = i;
                    }
                    if (burstRemaining[i] == mn) {
                        if (p[i].arrivalTime < p[idx].arrivalTime) {
                            mn = burstRemaining[i];
                            idx = i;
                        }
                    }
                }
            }
            // If a process is found
            if (idx != -1) {
                // If process is starting for the first time
                if (burstRemaining[idx] == p[idx].burstTime) {
                    p[idx].startTime = currentTime; // Set start time of process
                    totalIdleTime += p[idx].startTime - prev; // Update total idle time
                }
                burstRemaining[idx]--; // Decrease remaining burst time of process
                currentTime++; // Increase current time
                prev = currentTime; // Update previous process

                // If process has completed
                if (burstRemaining[idx] == 0) {
                    p[idx].completionTime = currentTime; // Set completion time of process
                    p[idx].turnaroundTime = p[idx].completionTime - p[idx].arrivalTime; // Calculate turnaround time of
                                                                                        // process
                    p[idx].waitingTime = p[idx].turnaroundTime - p[idx].burstTime; // Calculate waiting time of process
                    p[idx].responseTime = p[idx].startTime - p[idx].arrivalTime; // Calculate response time of process
                    // Update total times
                    totalTurnaroundTime += p[idx].turnaroundTime;
                    totalWaitingTime += p[idx].waitingTime;
                    totalResponseTime += p[idx].responseTime;
                    isCompleted[idx] = 1; // Mark process as completed
                    completed++; // Increase number of completed processes
                }
                // If Gantt chart is empty or last entry is not the current process
                if (ganttChart.isEmpty() || ganttChart.get(ganttChart.size() - 1).pid != p[idx].pid) {
                    ganttChart.add(new GanttChartEntry(p[idx].pid, 1)); // Add new entry to Gantt chart
                } else {
                    ganttChart.get(ganttChart.size() - 1).duration++; // Increase duration of last entry in Gantt chart
                }
            } else {
                currentTime++; // Increase current time
            }
        }
        // Variables to hold minimum arrival time and maximum completion time
        int minArrivalTime = Integer.MAX_VALUE;
        int maxCompletionTime = Integer.MIN_VALUE;
        // Find minimum arrival time and maximum completion time
        for (int i = 0; i < x; i++) {
            minArrivalTime = Math.min(minArrivalTime, p[i].arrivalTime);
            maxCompletionTime = Math.max(maxCompletionTime, p[i].completionTime);
        }
        // Calculate average times, CPU utilization, and throughput
        avgTurnaroundTime = (float) totalTurnaroundTime / x;
        avgWaitingTime = (float) totalWaitingTime / x;
        avgResponseTime = (float) totalResponseTime / x;
        cpuUtilization = ((maxCompletionTime - totalIdleTime) / (float) maxCompletionTime) * 100;
        throughput = (float) x / (maxCompletionTime - minArrivalTime);

        // Print process details
        System.out.println("\n\nProcess\tArrival Time\tBurst Time\tST\tCT\tTAT\tWT\tRT\t\n");
        for (int i = 0; i < x; i++) {
            System.out.println(p[i].pid + "\t" + p[i].arrivalTime + "\t\t" + p[i].burstTime + "\t\t" + p[i].startTime
                    + "\t" + p[i].completionTime + "\t" + p[i].turnaroundTime + "\t" + p[i].waitingTime + "\t"
                    + p[i].responseTime + "\t\n");
        }
        // Print average times, throughput, and CPU utilization
        System.out.println("Average Turnaround Time = " + avgTurnaroundTime);
        System.out.println("Average Waiting Time = " + avgWaitingTime);
        System.out.println("Average Response Time = " + avgResponseTime);
        System.out.println("Throughput = " + throughput + " process/unit time");
        System.out.println("CPU Utilization = " + cpuUtilization + "%");

        // Print Gantt Chart
        System.out.println("Gantt Chart:");
        for (GanttChartEntry entry : ganttChart) {
            System.out.print("| P" + entry.pid + "(" + entry.duration + ") ");
        }
        System.out.println("|");

        sc.close(); // Close Scanner
    }
}
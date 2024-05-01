import java.util.Scanner;

public class FCFS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] diskQueue = new int[20];
        int queueSize, initialHeadPosition, totalSeekTime = 0, maxDiskRange, seekDifference;
        float averageSeekTime;

        System.out.println("Enter the max range of disk");
        maxDiskRange = scanner.nextInt();

        System.out.println("Enter the size of queue request");
        queueSize = scanner.nextInt();

        System.out.println("Enter the queue of disk positions to be read");
        for (int i = 0; i < queueSize; i++) {
            int position = scanner.nextInt();
            if (position < 0 || position > maxDiskRange) {
                System.out.println("Invalid position entered. Please enter a position between 0 and " + maxDiskRange);
                i--; // decrement i to repeat the loop for the same position
            } else {
                diskQueue[i] = position;
            }
        }

        System.out.println("Enter the initial head position");
        initialHeadPosition = scanner.nextInt();

        // Shift all elements in the diskQueue one position to the right
        System.arraycopy(diskQueue, 0, diskQueue, 1, queueSize);

        // Add the initial head position at the start of the diskQueue
        diskQueue[0] = initialHeadPosition;
        queueSize++; // Increase queue size as we added one more element

        for (int j = 0; j < queueSize - 1; j++) {
            seekDifference = Math.abs(diskQueue[j + 1] - diskQueue[j]);
            totalSeekTime += seekDifference;
            System.out.printf("Disk head moves from %d to %d with seek %d\n", diskQueue[j], diskQueue[j + 1],
                    seekDifference);
        }

        System.out.printf("Total seek time is %d\n", totalSeekTime);
        averageSeekTime = (float) totalSeekTime / (queueSize - 1);
        System.out.printf("Average seek time is %f\n", averageSeekTime);

        scanner.close();
    }
}
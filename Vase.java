// Fiona Barry
// 3/3/2022
// COP4520
// Assignment 2 - Vase

import java.util.*;

public class Vase {

    // Random number generator
    static final Random RAND = new Random();

    // Number of guests
    static final int N = 1_000_000;

    // The "Busy" sign / is there someone there
    static boolean isBusy = false;

    // Number of guests visited
    static int visitedCount = 0;
    static boolean[] visited = new boolean[N];

    // Keep track of num visits per guest for analysis
    static int[] totalVisitsPerGuest = new int[N];

    public static void main(String[] args) {

        // Init Threads
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++)
            threads[i] = new Thread(new GuestThread(i));

        // Init analysis data
        double averageNumVisits = 0;
        long clock = System.currentTimeMillis();

        // Strategy 1: Random Visits
        while (visitedCount < N) {
            // This guest wants to visit
            int nextGuest = RAND.nextInt(N);
            // If they're already visiting then forget it
            if (!threads[nextGuest].isAlive()) {
                // They try the door
                totalVisitsPerGuest[nextGuest]++;
                // If no one is there, they can see the vase
                if (!isBusy) {
                    threads[nextGuest].run();
                }
            }
        }

        // Calculate average
        for (int i = 0; i < N; i++)
            averageNumVisits += totalVisitsPerGuest[i];

        // Print stats
        System.out.println("\n\nStrategy 1:");
        System.out.println("Execution Time: " + (System.currentTimeMillis() - clock));
        System.out.println("Average Visits: " + (averageNumVisits / N));

        // Reset data
        resetGlobal();
        averageNumVisits = 0;
        clock = System.currentTimeMillis();

        // Strategy 2: Status Sign
        while (visitedCount < N) {
            // This guest wants to visit
            int nextGuest = RAND.nextInt(N);
            // If the sign says available and they're not currently running, they go try
            if (!isBusy && !threads[nextGuest].isAlive()) {
                totalVisitsPerGuest[nextGuest]++;
                threads[nextGuest].run();
            }
        }

        // Calculate average
        for (int i = 0; i < N; i++)
            averageNumVisits += totalVisitsPerGuest[i];

        // Print stats
        System.out.println("\n\nStrategy 2:");
        System.out.println("Execution Time: " + (System.currentTimeMillis() - clock));
        System.out.println("Average Visits: " + (averageNumVisits / N));

        // Reset data
        resetGlobal();
        averageNumVisits = 0;
        clock = System.currentTimeMillis();

        // Strategy 3: Queue
        while (visitedCount < N) {
            // This guest wants to get in line
            int nextGuest = RAND.nextInt(N);
            // If they're not in line, get in line
            if (!threads[nextGuest].isAlive()) {
                totalVisitsPerGuest[nextGuest]++;
                threads[nextGuest].run();
            }
        }

        // Calculate average
        for (int i = 0; i < N; i++)
            averageNumVisits += totalVisitsPerGuest[i];

        // Print stats
        System.out.println("\n\nStrategy 3:");
        System.out.println("Execution Time: " + (System.currentTimeMillis() - clock));
        System.out.println("Average Visits: " + (averageNumVisits / N));
    }

    // The synchronized method of visiting the vase ensures only one guest can visit at once
    static synchronized void visitVase(int index) {
        // A guest is visiting
        isBusy = true;

        // If it's their first time, mark it
        if (!visited[index]) {
            visited[index] = true;
            visitedCount++;
        }

        // They're done
        isBusy = false;
    }

    // GuestThread is each guest
    static class GuestThread implements Runnable {

        // Each guest has an index
        private int index;

        // Constructor
        public GuestThread(int i) {
            this.index = i;
        }

        @Override
        public void run() {
            // Visit the vase
            visitVase(this.index);
        }
    }

    // Resets global data for next trial
    static void resetGlobal() {
        visitedCount = 0;
        isBusy = false;
        visited = new boolean[N];
        totalVisitsPerGuest = new int[N];
    }
}

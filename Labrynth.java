// Fiona Barry
// 3/3/2022
// COP4520
// Assignment 2 - Labrynth

import java.util.*;

public class Labrynth {

    // Random number generator
    static final Random RAND = new Random();

    // Number of guests
    static final int N = 1_000_000;

    // Keep track of who visited
    static int visitedCount = 0;
    static boolean[] visited = new boolean[N];

    // Keep track of who reported
    static int reportedCount = 0, totalNumNeeded = 0;
    static boolean[] reported = new boolean[N];

    public static void main(String[] args) {

        // Init Threads
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++)
            threads[i] = new Thread(new GuestThread(i));

        // Start time
        long clock = System.currentTimeMillis();

        // While not everyone has reported
        while (reportedCount < N) {
            // Minotaur chooses next guest
            int nextGuest = RAND.nextInt(N);
            // If this guest hasn't already been chosen to enter
            // (kind of like being in a line to enter but really
            // it's just the minotaur working proactively)
            // Then allow them to enter
            if (!threads[nextGuest].isAlive())
                threads[nextGuest].run();
        }

        System.out.println("Total Number of Labrynth Visits: " + totalNumNeeded +
                " for " + N + " people in " +
                (System.currentTimeMillis() - clock) + " ms.");
    }

    // This is where each guest visits the labrynth
    // It changes global data so it's synchronized... the resources are locked
    // while another thread is accessing them (i.e. one guest in the lab at once)
    static synchronized void visitLabrynth(int index) {
        // Increase total num of guest visits
        totalNumNeeded++;

        // If they haven't visited yet, they leave a wrapper behind
        if (!visited[index]) {
            visited[index] = true;
            visitedCount++;
        }

        // If this guest can see all N wrappers on the ground,
        // They report to the minotaur that all guests have visited
        if (visitedCount >= N && !reported[index]) {
            reported[index] = true;
            reportedCount++;
        }
    }

    // GuestThead object
    static class GuestThread implements Runnable {

        // Every guest has an index
        private int index;

        // Constructor
        public GuestThread(int i) {
            this.index = i;
        }

        @Override
        public void run() {
            // Visit the labrynth
            visitLabrynth(index);
        }
    }
}

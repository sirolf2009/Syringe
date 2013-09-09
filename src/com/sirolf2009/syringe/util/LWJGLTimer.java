package com.sirolf2009.syringe.util;

public class LWJGLTimer {

    private long lastTime; // nanoseconds
    private double elapsedTime;
    private boolean firstRun = true;

    /** Creates a timer. */
    public LWJGLTimer() {
    }

    /** Initializes the timer. Call this just before entering the game loop. */
    public void initialize() {
        lastTime = System.nanoTime();
        firstRun = false;
    }

    /** @return the elapsed time since the the next to last update call */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Updates the timer. Call this once every iteration of the game loop. The first time you call this method it
     * returns 0.
     *
     * @return the elapsed time in milliseconds
     */
    public double update() {
        if (firstRun) {
            firstRun = false;
            lastTime = System.nanoTime();
            return 0;
        } else {
            long elapsedTime = System.nanoTime() - lastTime;
            lastTime = System.nanoTime();
            this.elapsedTime = elapsedTime / (double) 1000000;
            return this.elapsedTime;
        }
    }
}

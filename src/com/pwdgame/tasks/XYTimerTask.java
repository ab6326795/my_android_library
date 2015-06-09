package com.pwdgame.tasks;


public abstract class XYTimerTask implements Runnable{
    /* Lock object for synchronization. It's also used by Timer class. */
    final Object lock = new Object();

    /* If timer was cancelled */
    boolean cancelled;

    /* Slots used by Timer */
    long when;

    long interval;

    /*
     * Method called from the Timer for synchronized getting of when field.
     */
    long getWhen() {
        synchronized (lock) {
            return when;
        }
    }

    /*
     * Is TimerTask scheduled into any timer?
     *
     * @return {@code true} if the timer task is scheduled, {@code false}
     * otherwise.
     */
    boolean isScheduled() {
        synchronized (lock) {
            return when > 0 || interval > 0;
        }
    }

    /**
     * Creates a new {@code TimerTask}.
     */
    protected XYTimerTask() {
    }


    /**
     * Cancels the {@code TimerTask} and removes it from the {@code Timer}'s queue. Generally, it
     * returns {@code false} if the call did not prevent a {@code TimerTask} from running at
     * least once. Subsequent calls have no effect.
     *
     * @return {@code true} if the call prevented a scheduled execution
     *         from taking place, {@code false} otherwise.
     */
    public boolean cancel() {
        synchronized (lock) {
            boolean willRun = !cancelled && when > 0;
            cancelled = true;
            return willRun;
        }
    }

    /**
     * 更新定时器执行周期
     * @param time
     */
    public void setInterval(long time) {
        synchronized (lock) {            
            interval=time;
        }
    }
    
    /**
     * 放弃已经等待的时间，重新等待
     */
    public void resetInterval(){
        synchronized (lock) {            
            when=System.currentTimeMillis()+interval;
        }
    }

    /**
     * Returns the scheduled execution time. If the task execution is in
     * progress it returns the execution time of the ongoing task. Tasks which
     * have not yet run return an undefined value.
     *
     * @return the most recent execution time.
     */
    public long getInterval() {
        synchronized (lock) {
            return interval;
        }
    }

    /**
     * The task to run should be specified in the implementation of the {@code run()}
     * method.
     */
    public abstract void run();
}
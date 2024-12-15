package edu.uprb.quizzilla.util;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Countdown implements Runnable {

    private static final Logger logger = Logger.getLogger(Countdown.class.getName());

    private final int limit;
    private final int interval;
    private final Consumer<Double> inProgressFunc;
    private final Runnable endedFunc;

    private Countdown(int limit, int interval,
                     Consumer<Double> inProgressFunc,
                     Runnable endedFunc)
    {
        this.limit = limit;
        this.interval = interval;
        this.inProgressFunc = inProgressFunc;
        this.endedFunc = endedFunc;
    }

    public static Thread start(int limit, int interval,
                             Consumer<Double> inProgressFunc, Runnable endedFunc)
    {
        return Thread.startVirtualThread(new Countdown(limit, interval, inProgressFunc, endedFunc));
    }

    public static Thread start(int limit, int interval, Runnable endedFunc) {
        return Thread.startVirtualThread(new Countdown(limit, interval, (remaining) -> {}, endedFunc));
    }

    public static Thread start(int limit, int interval) {
        return Thread.startVirtualThread(new Countdown(limit, interval, (remaining) -> {}, () -> {}));
    }

    @Override
    public void run() {
        Stopwatch timer = new Stopwatch();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                double remainingTime = Math.ceil(limit - timer.elapsedTime());
                if (remainingTime <= 0) {
                    endedFunc.run();
                    break;
                }

                inProgressFunc.accept(remainingTime);
                Thread.sleep(1000L * interval);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

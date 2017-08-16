

public class BarrierTimer implements Runnable {
  private boolean started;
  private long startTime, endTime;

  public synchronized void run() {
    long t = System.nanoTime();
    if (!started) {
      started = true;
      startTime = t;
    } else
      endTime = t;
  }

  synchronized void clear() {
    started = false;
  }

  synchronized long getTime() {
    return endTime - startTime;
  }
}
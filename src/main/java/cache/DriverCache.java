package cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DriverCache {

  private DriverCache() {

    Computable<Integer, Integer> c = arg -> {
      Integer sumFactors = 0;
      for (int i = 2; i <= arg; i++) {
        while (arg % i == 0) {
          sumFactors += i;
          arg /= i;
        }
      }
      return sumFactors;
    };

    final MemoizedComputation<Integer, Integer> memoizer =  new MemoizedComputation<>(c);
    final UncachedComputation<Integer, Integer> uncached =  new UncachedComputation<>(c);

    ExecutorService executor = Executors.newFixedThreadPool(5);
    long t1;
    long t2;
    t1 = System.nanoTime();
    for (int i = 0; i < 10000000; i++) {
      int randomNum = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
      Runnable worker = new WorkerThread<>(randomNum, uncached);
      executor.execute(worker);
    }
    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    t2 = System.nanoTime();
    System.out.println("Finished uncached");
    System.out.println((t2 - t1));


    ExecutorService executor2 = Executors.newFixedThreadPool(5);
    t1 = System.nanoTime();
    for (int i = 0; i < 10000000; i++) {
      int randomNum = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
      Runnable worker = new WorkerThread<>(randomNum, memoizer);
      executor2.execute(worker);
    }
    executor2.shutdown();

    try {
      executor2.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    t2 = System.nanoTime();
    System.out.println("Finished memoized");
    System.out.println((t2 - t1));
  }







  public static void main(String[] args) {
    new DriverCache();
  }



}



class WorkerThread<A, V> implements Runnable {

  private A input;
  private Computable<A, V> cache;

  WorkerThread(A s, Computable<A, V> cache){
    this.input=s;
    this.cache = cache;
  }

  @Override
  public void run() {
    try {
      cache.compute(input);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
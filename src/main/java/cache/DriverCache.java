package cache;

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

    final Memoizer<Integer, Integer> memoizer =  new Memoizer<>(c);

    try {
      long t1 = 0;
      long t2 = 0;

      t1 = System.nanoTime();
      for(int i = 0;i<100000;i++)
        memoizer.compute(i);
      t2 = System.nanoTime();
      System.out.println((t2 - t1));

      t1 = System.nanoTime();
      for(int i = 0;i<100000;i++)
        memoizer.compute(i);
      t2 = System.nanoTime();
      System.out.println((t2 - t1));


    } catch (InterruptedException e) {
      e.printStackTrace();
    }


  }







  public static void main(String[] args) {
    new DriverCache();
  }



}



class WorkerThread<A, V> implements Runnable {

  A input;
  Memoizer<A, V> cache;

  public WorkerThread(A s, Memoizer<A, V> cache){
    this.input=s;
    this.cache = cache;
  }

  @Override
  public void run() {
    cache.compute(input);
  }

  private void processCommand() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString(){
    return this.command;
  }
}
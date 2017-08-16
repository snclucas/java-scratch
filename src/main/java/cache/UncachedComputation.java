package cache;

import java.util.concurrent.*;

public class UncachedComputation<A, V> implements Computable<A, V> {

  private final Computable<A, V> c;

  UncachedComputation(Computable<A, V> c) {
    this.c = c;
  }

  public V compute(final A arg) throws InterruptedException {

    Callable<V> eval = () -> c.compute(arg);
    try {
      return eval.call();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

}


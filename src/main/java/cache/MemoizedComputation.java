package cache;

import java.util.concurrent.*;

public class MemoizedComputation<A, V> implements Computable<A, V> {
  private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<>();

  private final Computable<A, V> c;

  MemoizedComputation(Computable<A, V> c) {
    this.c = c;
  }

  public V compute(final A arg) throws InterruptedException {
    while (true) {
      Future<V> f = cache.get(arg);
      if (f == null) {
        Callable<V> eval = () -> c.compute(arg);

        FutureTask<V> ft = new FutureTask<>(eval);
        f = cache.putIfAbsent(arg, ft);
        if (f == null) { f = ft; ft.run(); }
      }
      try {
        return f.get();
      } catch (CancellationException e) {
        cache.remove(arg, f);
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

}


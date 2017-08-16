import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;


public class BoundedBufferTest {

  @Test
  public void testIsEmptyWhenConstructed() {
    BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
    assertTrue(bb.isEmpty());
    assertFalse(bb.isFull());
  }

  @Test
  public void testIsFullAfterPuts() throws InterruptedException {
    BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
    for (int i = 0; i < 10; i++)
      bb.put(i);
    assertTrue(bb.isFull());
    assertFalse(bb.isEmpty());
  }

  @Test
  public void testTakeBlocksWhenEmpty() {
    final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
    Thread taker = new Thread(() -> {
      try {
        int unused = bb.take();
        fail(); // if we get here, it’s an error
      } catch (InterruptedException success) { }
    });
    try {
      taker.start();
      Thread.sleep(1);
      taker.interrupt();
      taker.join(1);
      assertFalse(taker.isAlive());
    } catch (Exception unexpected) {
      fail();
    }
  }

}
/*
When should we use CountDownLatch in Java :

Use CountDownLatch when one of Thread like main thread, require to wait for one or more thread to complete,
before its start doing processing. Classical example of using CountDownLatch in Java  is any server side
core Java application which uses services architecture,  where multiple services is provided by multiple
threads and application can not start processing  until all services have started successfully as shown
in our CountDownLatch example.


CountDownLatch in Java – Things to remember
Few points about Java CountDownLatch which is worth remembering:

1) You can not reuse CountDownLatch once count is reaches to zero, this is the main difference between
CountDownLatch and CyclicBarrier, which is frequently asked in core Java interviews and multi-threading  interviews.

2) Main Thread wait on Latch by calling CountDownLatch.await() method while other thread calls
CountDownLatch.countDown() to inform that they have completed.

That’s all on What is CountDownLatch in Java, What does CountDownLatch do in Java, How CountDownLatch
works in Java along with a real life CountDownLatch example in Java. This is a very useful concurrency
utility and if you master when to use CountDownLatch and how to use CountDownLatch you will be able to
reduce good amount of complex concurrency control code written using wait and notify in Java.

* */

package concurrency;


import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountDownLatchDemo {

  public static void main(String args[]) {
    final CountDownLatch latch = new CountDownLatch(3);
    Thread cacheService = new Thread(new Service("CacheService", 1000, latch));
    Thread alertService = new Thread(new Service("AlertService", 1000, latch));
    Thread validationService = new Thread(new Service("ValidationService", 1000, latch));

    cacheService.start(); //separate thread will initialize CacheService
    alertService.start(); //another thread for AlertService initialization
    validationService.start();

    // application should not start processing any thread until all service is up
    // and ready to do there job.
    // Countdown latch is idle choice here, main thread will start with count 3
    // and wait until count reaches zero. each thread once up and read will do
    // a count down. this will ensure that main thread is not started processing
    // until all services is up.

    //count is 3 since we have 3 Threads (Services)

    try{
      latch.await();  //main thread is waiting on CountDownLatch to finish
      System.out.println("All services are up, Application is starting now");
    }catch(InterruptedException ie){
      ie.printStackTrace();
    }

  }

}

class Service implements Runnable{
  private final String name;
  private final int timeToStart;
  private final CountDownLatch latch;

  Service(String name, int timeToStart, CountDownLatch latch){
    this.name = name;
    this.timeToStart = timeToStart;
    this.latch = latch;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(timeToStart);
    } catch (InterruptedException ex) {
      Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println( name + " is Up");
    latch.countDown(); //reduce count of CountDownLatch by 1
  }

}



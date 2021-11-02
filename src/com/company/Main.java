package com.company;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

  public static void main(String[] args) {
    BlockingObjectPool pool = new BlockingObjectPool(5);

    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(() -> {
        try {
          System.out.println("thread "+ Thread.currentThread().getName() +" started");
          while (true) {
            Thread.sleep(getRandomMilliSec());
            System.out.println(" | thread "+ Thread.currentThread().getName() +" takes | ");
            pool.take(new Object());
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }, "#"+i);
      thread.start();
    }
    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(() -> {
        try {
          System.out.println("thread "+ Thread.currentThread().getName() +" started");
          while (true) {
            Thread.sleep(getRandomMilliSec());
            System.out.println(" | thread "+ Thread.currentThread().getName() +" gets | ");
            pool.get();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }, "#"+(i+10));
      thread.start();
    }
  }

  private static int getRandomMilliSec() {
    int randomInt = new Random().ints(1, 10).findFirst().getAsInt();
    return randomInt * 1000;
  }
}

class BlockingObjectPool {

  private final BlockingQueue pool;
  private int size;

  /**
   * Creates filled pool of passed size
   *
   * @param size of pool
   */
  public BlockingObjectPool(int size) {
    this.size = size;
    pool = new ArrayBlockingQueue<>(size);
  }

  /**
   * Gets object from pool or blocks if pool is empty
   *
   * @return object from pool
   */
  public Object get() {
    Object object;
    try {
      object = pool.take();
    } catch (InterruptedException e) {
      object = pool.peek();
    }
    System.out.println("Get, pool size: " + pool.size());
    return object;
  }

  /**
   * Puts object to pool or blocks if pool is full
   *
   * @param object to be taken back to pool
   */
  public void take(Object object) {
    try {
      pool.put(object);
    } catch (InterruptedException e) {
      pool.offer(object);
    }
    System.out.println("Take, pool size: " + pool.size());
  }

}



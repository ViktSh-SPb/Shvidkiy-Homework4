package org.viktsh;


import java.util.Random;

/**
 * @author Viktor Shvidkiy
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        PrintWord pword = new PrintWord();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pword.print(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pword.print(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

class PrintWord {
    private static volatile int threadNumber = 1;
    private Random rand = new Random();

    public void print(int id) throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (threadNumber != id) {
                    wait();
                }
                System.out.print(id);
                Thread.sleep(rand.nextInt(1000) + 1000);
                threadNumber = (id == 1) ? 2 : 1;
                notify();
            }
        }
    }
}

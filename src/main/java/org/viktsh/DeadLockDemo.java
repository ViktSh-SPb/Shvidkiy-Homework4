package org.viktsh;

/**
 * @author Viktor Shvidkiy
 */
public class DeadLockDemo {
    private static final Resource1 resource1 = new Resource1();
    private static final Resource2 resource2 = new Resource2();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("Забираем lock первого ресурса в первом потоке...");
            synchronized (resource1){
                resource1.doWork();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Забираем lock второго ресурса в первом потоке...");
                synchronized (resource2){
                    resource2.doWork();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Забираем lock второго ресурса во втором потоке...");
            synchronized (resource2){
                resource2.doWork();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Забираем lock первого ресурса во втором потоке...");
                synchronized (resource1) {
                    resource1.doWork();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

class Resource1 {
    public void doWork() {
        System.out.println("Ресурс 1 работает...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Resource2 {
    public void doWork() {
        System.out.println("Ресурс 2 работает...");
    }
}

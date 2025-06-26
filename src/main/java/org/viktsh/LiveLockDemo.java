package org.viktsh;

/**
 * @author Viktor Shvidkiy
 */
public class LiveLockDemo {
    static final Resource resource = new Resource();

    public static void main(String[] args) throws InterruptedException {
        Worker worker1 = new Worker("Работник 1");
        Worker worker2 = new Worker("Работник 2");
        resource.setOwner(worker1);

        Thread thread1 = new Thread(() -> worker1.doWork(resource, worker2));
        Thread thread2 = new Thread(() -> worker2.doWork(resource, worker1));

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

class Worker {
    private final String name;
    private boolean active;

    public Worker(String name) {
        this.name = name;
        active = true;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void doWork(Resource resource, Worker other) {
        while (active) {
            if (resource.getOwner() != this){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            if (other.isActive()){
                System.out.println(name + ". Даю работать другому, отдаю ресурс.");
                resource.setOwner(other);
                continue;
            }
            resource.work();
            active = false;
            System.out.println(name+" выполнил работу.");
            resource.setOwner(other);
        }
    }
}

class Resource {
    private Worker owner;

    public Worker getOwner() {
        return owner;
    }

    public void setOwner(Worker owner) {
        this.owner = owner;
    }

    public synchronized void work() {
        System.out.println(owner.getName() + " работает.");
    }
}


package reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockThread implements Runnable{
    ReentrantLock lock = new ReentrantLock();

    public void get(){
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        set();
        lock.unlock();
    }

    public void set(){
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        lock.unlock();
    }

    @Override
    public void run() {
        get();
    }

    public static void main(String[] args) {
        ReentrantLockThread r = new ReentrantLockThread();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
    }
}

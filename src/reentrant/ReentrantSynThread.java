package reentrant;

/**
 * sychronized 关键字也是重入锁
 */
public class ReentrantSynThread implements Runnable {

    public synchronized void get(){
        System.out.println(Thread.currentThread().getId());
    }

    public synchronized void set(){
        get();
        System.out.println(Thread.currentThread().getId());
    }

    @Override
    public void run() {
        set();
    }


    public static void main(String[] args) {
        ReentrantSynThread t = new ReentrantSynThread();
        new Thread(t).start();
        new Thread(t).start();
        new Thread(t).start();
    }
}

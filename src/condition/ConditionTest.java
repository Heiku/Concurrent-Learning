package condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 生成者消费者
 */
public class ConditionTest {
    private int queueSize = 10;
    private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);

    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();


    public static void main(String[] args) throws InterruptedException{
        ConditionTest test = new ConditionTest();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();

        producer.start();
        consumer.start();

        Thread.sleep(100);

        producer.interrupt();
        consumer.interrupt();
    }


    class Consumer extends Thread{
        @Override
        public void run() {
            consume();
        }

        volatile boolean flag = true;
        private void consume(){
            while (flag){
                lock.lock();
                try {
                    while (queue.isEmpty()){
                        try {
                            System.out.println("队列空，等待数据");
                            notEmpty.await();
                        }catch (InterruptedException e){
                            flag = false;
                        }
                    }

                    queue.poll();
                    notFull.signal();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size());
                }finally {
                    lock.unlock();
                }
            }
        }
    }


    class Producer extends Thread{
        @Override
        public void run() {
            produce();
        }

        volatile boolean flag = true;

        private void produce(){
            while (flag){
                lock.lock();

                try {
                    while (queue.size() == queueSize){
                        try {
                            System.out.println("队列满，等待空余空间");

                            // 当数组满的时候，生产者线程释放锁并进入等待状态，这时消费者线程进入并工作
                            notFull.await();
                        }catch (InterruptedException e){
                            flag = false;
                        }
                    }
                    queue.offer(1);
                    notEmpty.signal();
                    System.out.println("向队列插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                }finally {
                    lock.unlock();
                }

            }
        }
    }
}

package condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue {

    private Object[] items;

    private int addIndex, removeIndex, count;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();


    public BoundedQueue(int size){
        items = new Object[size];
    }


    /**
     *  添加等待
     */
    public void add(Object t) throws InterruptedException{
        lock.lock();

        try {
            while (count == items.length)
                notFull.await();

            items[addIndex] = t;
            if (++addIndex == items.length)
                addIndex = 0;

            ++count;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }


    /**
     *  删除
     */
    public Object remove() throws InterruptedException{
        lock.lock();

        try {
            while (count == 0){
                notEmpty.await();
            }
            Object x = items[removeIndex];
            if (++removeIndex == items.length)
                removeIndex = 0;

            --count;
            notFull.signal();
            return x;
        }finally {
            lock.unlock();
        }
    }
}

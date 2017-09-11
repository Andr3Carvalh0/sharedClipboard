package andre.pt.projectoeseminario.Controller.State;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Helper so we can now when we copied new content.
 * This class is thread-safe
 */
public class ClipboardController {

    private Lock nLock;
    private final List<Pair> sentRequestsQueue;
    private final List<String> ignoreQueue;
    private long orderNumber = -1;

    //Helper class
    public class Pair {
        private String value;
        private boolean wake;
        private boolean remove;
        private Condition condition;

        public Pair(String value, Condition condition) {
            this.value = value;
            this.condition = condition;
            this.wake = false;
            this.remove = false;
        }

        public String getValue() {
            return value;
        }

        public Condition getCondition() {
            return condition;
        }

        public void Wake() {
            this.wake = true;
        }

        public void Remove() {
            this.remove = true;
        }

        public boolean isWake() {
            return wake;
        }

        public boolean toRemove() {
            return remove;
        }
    }

    public ClipboardController(long order){
        this.nLock = new ReentrantLock();
        this.sentRequestsQueue = new LinkedList<>();
        this.ignoreQueue = new LinkedList<>();

    }

    /**
     * Notifies the end of the putValue request.
     */
    public void wake(){
        nLock.lock();

        try {
            if(sentRequestsQueue.size() > 0)
                sentRequestsQueue.get(0).getCondition().signal();
        }finally {
            nLock.unlock();
        }

    }

    /**
     * Changes to the new value, if it is different that the old value
     * @param value the copied text
     * @return boolean indicating weather we changed value or not.
     */
    public boolean putValue(String value, Consumer<Pair> startTimer) {
        nLock.lock();

        try {
            if (ignoreQueue.contains(value))
            {
                ignoreQueue.remove(value);
                return false;
            }

            Pair node = new Pair(value, nLock.newCondition());
            sentRequestsQueue.add(node);
            startTimer.accept(node);

            do
            {
                try
                {
                    node.getCondition().await();
                }
                catch (InterruptedException e) {}

                if (node.toRemove())
                {
                    sentRequestsQueue.remove(node);
                    return false;
                }

                if (node == sentRequestsQueue.get(0) && node.isWake() && !node.toRemove())
                {
                    sentRequestsQueue.remove(node);

                    if (ignoreQueue.contains(value))
                    {
                        ignoreQueue.remove(value);
                        return false;
                    }

                    return true;
                }

            } while (true);
        }finally {
            nLock.unlock();
        }

    }


    //return 0 -> Didnt change
    //return 1 -> Did change, we need to update history
    //return 2 -> Old request, we need to update history
    public int PutValue(String text, long order_received)
    {
        nLock.lock();

        try {
            //When we dont have any pending request
            if (sentRequestsQueue.size() == 0)
                return AvaliateAndUpdateOrder(order_received, orderNumber, text);

            //Even though we didnt finish the requests we can be sure that the request received is more recent
            // Than our sent requests.
            return AvaliateAndUpdateOrder(order_received, orderNumber + sentRequestsQueue.size(), text);

        }finally {
            nLock.unlock();
        }
    }

    private int AvaliateAndUpdateOrder(long received, long actual, String text)
    {
        if (received == actual)
            return 0;

        if (received < actual)
            return 2;

        orderNumber = received;
        ignoreQueue.add(text);
        return 1;
    }

    public void updateStateOfUpload(long order)
    {
        nLock.lock();

        try {
            if(orderNumber < order)
                orderNumber = order;

            if (sentRequestsQueue.size() > 0)
            {
                Pair node = sentRequestsQueue.get(0);
                ignoreQueue.add(node.getValue());
                node.Wake();
                node.getCondition().signal();
            }

        }finally {
            nLock.unlock();
        }
    }

    public void removeUpload(Pair node)
    {
        nLock.lock();

        try {
            ignoreQueue.add(node.getValue());
            node.Remove();
            node.condition.signal();

        }finally {
            nLock.unlock();
        }
    }

    public void addToIgnoreQueue(String text)
    {
        nLock.lock();

        try{
            ignoreQueue.add(text);
        }finally {
            nLock.unlock();
        }
    }

    public long getOrder(){
        return orderNumber;
    }
}

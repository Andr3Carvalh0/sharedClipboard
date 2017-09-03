package andre.pt.projectoeseminario.Controller.State;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Helper so we can now when we copied new content.
 * This class is thread-safe
 */
public class ClipboardController {
    private Lock nLock;
    private String clipboard_value;
    private List<Pair> queue;
    private HashMap<String, String> history;

    //Helper class
    private class Pair {
        private String value;
        private boolean finished;
        private Condition condition;

        public Pair(String value, Condition condition) {
            this.value = value;
            this.condition = condition;
            this.finished = false;
        }

        public String getValue() {
            return value;
        }

        public Condition getCondition() {
            return condition;
        }

        public boolean isFinished() {
            return finished;
        }

        public void finish() {
            this.finished = true;
        }
    }

    public ClipboardController(String initialValue){
        nLock = new ReentrantLock();
        queue = new LinkedList<>();
        this.clipboard_value = initialValue;
        history = new HashMap<>();

    }

    /**
     * Notifies the end of the putValue request.
     */
    public void wake(){
        nLock.lock();

        try {
            if(queue.size() > 0){
                queue.get(0).finish();
                queue.get(0).getCondition().signal();

            }


        }finally {
            nLock.unlock();
        }

    }

    /**
     * Changes to the new value, if it is different that the old value
     * @param value the copied text
     * @return boolean indicating weather we changed value or not.
     * @throws InterruptedException
     */
    public boolean putValue(String value) throws InterruptedException {
        nLock.lock();

        try {
            if(value.equals(clipboard_value) && queue.size() == 0)
                return false;

            //Queue is empty we can alter the value without waiting
            if(queue.size() == 0 && !value.equals(clipboard_value)){
                clipboard_value = value;
                return true;
            }

            Pair pair = new Pair(value, nLock.newCondition());
            queue.add(pair);

            do{
                try {
                    pair.condition.await();

                }catch (InterruptedException e){
                    if(pair.isFinished()){
                        clipboard_value = value;
                        queue.remove(pair);
                        return true;

                    }
                    queue.remove(pair);
                    throw e;
                }

                if(pair.isFinished()){
                    clipboard_value = value;
                    queue.remove(pair);
                    return true;

                }

            }while (true);

        }finally {
            nLock.unlock();
        }

    }
}

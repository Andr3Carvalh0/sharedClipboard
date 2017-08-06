package andre.pt.projectoeseminario.State;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClipboardController {
    private Lock nLock;
    private String clipboard_value;
    private List<Pair> queue;
    private HashMap<String, String> history;

    //Helper class
    private class Pair {
        private String value;
        private Condition condition;

        public Pair(String value, Condition condition) {
            this.value = value;
            this.condition = condition;
        }

        public String getValue() {
            return value;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    public ClipboardController(String initialValue){
        nLock = new ReentrantLock();
        queue = new LinkedList<>();
        this.clipboard_value = initialValue;
        history = new HashMap<>();

    }

    public void wake(){
        nLock.lock();

        try {
            if(queue.size() > 0)
                queue.get(0).getCondition().signal();

        }finally {
            nLock.unlock();
        }

    }

    public boolean putValue(String value, Function<String, String> addToFilteredTable, Function<String, Boolean> addToRecentTable){
        nLock.lock();

        try {
            if(value.equals(clipboard_value) && queue.size() == 0)
                return false;

            //Queue is empty we can alter the value without waiting
            if(queue.size() == 0 && !value.equals(clipboard_value)){
                clipboard_value = value;
                addToFilteredTable.apply(clipboard_value);
                addToRecentTable.apply(clipboard_value);
                return true;
            }

            Pair pair = new Pair(value, nLock.newCondition());
            queue.add(pair);

            do{
                try {
                    pair.condition.await();

                }catch (InterruptedException e){}

                if(!pair.value.equals(clipboard_value)){
                    queue.remove(pair);
                    clipboard_value = pair.getValue();
                    addToFilteredTable.apply(clipboard_value);
                    addToRecentTable.apply(clipboard_value);
                    return true;
                }

                if(pair.value.equals(clipboard_value)){
                    queue.remove(pair);
                    return false;
                }

            }while (true);

        }finally {
            nLock.unlock();
        }

    }
}

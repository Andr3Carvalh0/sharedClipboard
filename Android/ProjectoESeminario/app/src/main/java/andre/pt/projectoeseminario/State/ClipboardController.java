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
    private Condition nCondition;
    private boolean inUse;
    private String clipboard_value;
    private HashMap<String, String> history;

    public ClipboardController(String d){
        nLock = new ReentrantLock();
        inUse = false;
        this.clipboard_value = d;
        nCondition = nLock.newCondition();
        history = new HashMap<>();

    }

    public void releaseWork(){
        nLock.lock();

        try {
            if(inUse)
                inUse = false;

            nCondition.signalAll();
        }finally {
            nLock.unlock();
        }
    }

    public boolean acquireWork(){
        nLock.lock();

        try {
            if(!inUse){
                inUse = true;
                return true;
            }

            do {
                try {
                    nCondition.await();
                }catch (InterruptedException e){

                }
                if(inUse)
                    return true;

            }while (true);
        }finally {
            nLock.unlock();
        }

    }

    public Boolean switchClipboardValue(String newValue, Function<String, String> addToFilteredTable, Function<String, Boolean> addToRecentTable)
    {
        nLock.lock();

        try {
            if(newValue.equals(clipboard_value))
                return false;

            addToFilteredTable.apply(newValue);
            addToRecentTable.apply(newValue);
            clipboard_value = newValue;

            return true;

        }finally {
            nLock.unlock();
        }
    }

}

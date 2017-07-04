package andre.pt.projectoeseminario.State;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClipboardController {
    private Lock nLock;
    private Condition nCondition;
    private boolean inUse;
    private String clipboard_value;

    public ClipboardController(){
        nLock = new ReentrantLock();
        inUse = false;
        nCondition = nLock.newCondition();

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

                return inUse;
            }while (true);
        }finally {
            nLock.unlock();
        }

    }

    public Boolean switchClipboardValue(String newValue)
    {
        nLock.lock();

        try {
            if(newValue.equals(clipboard_value))
                return false;

            clipboard_value = newValue;
            return true;

        }finally {
            nLock.unlock();
        }
    }
}

package andre.pt.projectoeseminario.State;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by André Carvalho on 27/05/2017.
 */

public class ClipboardController {
    private volatile AtomicBoolean doingWork;
    private volatile AtomicReference lastClipboardContent;

    public ClipboardController(){
        doingWork = new AtomicBoolean(false);
        lastClipboardContent = new AtomicReference();
    }

    public boolean releaseWork(){
        while(doingWork.get()){
            boolean initialState = doingWork.get();

            if (doingWork.compareAndSet(initialState, !initialState)){
                return true;
            }
        }

        return true;
    }


    public boolean acquireWork(){
        while(!doingWork.get()){
            boolean initialState = doingWork.get();

            if (doingWork.compareAndSet(initialState, !initialState)){
                return true;
            }
        }

        return true;
    }


    public Boolean switchClipboardValue(String newValue)
    {
        while (!newValue.equals(lastClipboardContent.get()))
        {
            String initialValue = (String) lastClipboardContent.get();

            if(lastClipboardContent.compareAndSet(initialValue, newValue)){
                return true;
            };
        }

        return false;
    }
}

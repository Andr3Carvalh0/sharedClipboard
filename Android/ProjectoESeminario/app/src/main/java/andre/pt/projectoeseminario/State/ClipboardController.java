package andre.pt.projectoeseminario.State;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by André Carvalho on 27/05/2017.
 */

public class ClipboardController {
    private volatile AtomicInteger doingWork;

    public ClipboardController(){
        doingWork = new AtomicInteger(0);
    }

    public boolean releaseWork(){
        int v = doingWork.get();

        if(v < 0){
            doingWork.compareAndSet(v, 0);
            return false;
        }

        doingWork.compareAndSet(v, v-1);

        return doingWork.get() > 0;
    }


    public boolean acquireWork(){
        int v = doingWork.get();
        return doingWork.compareAndSet(v, v+1);
    }

}

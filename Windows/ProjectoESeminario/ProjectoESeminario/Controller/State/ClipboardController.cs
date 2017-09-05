using System;
using System.Collections.Generic;
using System.Threading;

namespace ProjectoESeminario.Controller.State
{
    public class ClipboardController
    {
        private readonly Object nLock;
        private String clipboard_value;
        private long order_number = 0;

        //Used for the sync problems discuted on the paper.Use pair object to facilitate the cancelation
        private readonly LinkedList<Pair> sentRequestsQueue;

        private readonly LinkedList<WebPair> receivedRequestsQueue;

        //We assume that we can have multiple store requests at the same time
        private readonly LinkedList<Pair> queue;

        //Helper class
        public class Pair
        {
            private String value;
            private bool finished;

            public Pair(String value)
            {
                this.value = value;
                this.finished = false;
            }

            public String getValue()
            {
                return value;
            }

            public bool isFinished()
            {
                return finished;
            }

            public void finish()
            {
                this.finished = true;
            }
        }

        private class WebPair
        {
            private String value;
            private long order;

            public WebPair(String value, long order)
            {
                this.value = value;
                this.order = order;
            }

            public String getValue()
            {
                return value;
            }

            public long getOrder()
            {
                return order;
            }
        }

        public ClipboardController(String initialValue)
        {
            nLock = new Object();
            queue = new LinkedList<Pair>();
            sentRequestsQueue = new LinkedList<Pair>();
            receivedRequestsQueue = new LinkedList<WebPair>();
            clipboard_value = initialValue;
        }

        public void Wake()
        {
            lock (nLock)
            {
                if (queue.Count > 0) {
                    MonitorEx.Pulse(nLock, queue.First);
                }
            }
        }

        public bool PutValue(String value)
        {
            lock (nLock)
            {
                if (value.Equals(clipboard_value) && queue.Count == 0 && sentRequestsQueue.Count == 0)
                    return false;
                
                //Queue is empty we can alter the value without waiting
                if (queue.Count == 0 && !value.Equals(clipboard_value) && sentRequestsQueue.Count == 0)
                {
                    clipboard_value = value;
                    return true;
                }

                var node = queue.AddLast(new Pair(value));

                do
                {
                    try
                    {
                        MonitorEx.Wait(nLock, node);
                    }
                    catch (ThreadInterruptedException) {}

                    if (node == queue.First && sentRequestsQueue.Count == 0)
                    {
                        if(!value.Equals(clipboard_value))
                            clipboard_value = value;

                        queue.Remove(node);
                        return !value.Equals(clipboard_value);
                    }

                } while (true);

            }
        }

        public int PutValue(String value, long order_received)
        {
            bool updatedOrderNumber = false;

            lock (nLock)
            {
                if (sentRequestsQueue.Count == 0)
                {
                    if(order_received > order_number) { 
                        order_number = order_received;
                        updatedOrderNumber = true;
                    }
                }
                else
                {
                    var node = receivedRequestsQueue.AddLast(new WebPair(value, order_received));
                    bool tmp = true;

                    do
                    {
                        try
                        {
                            MonitorEx.Wait(nLock, node);
                        }
                        catch (Exception)
                        {
                        }

                        if (node == receivedRequestsQueue.First)
                        {
                            if (order_received > order_number)
                            {
                                order_number = order_received;
                                updatedOrderNumber = true;
                            }
                            receivedRequestsQueue.Remove(node);
                            tmp = false;
                        }
                    } while (tmp);
                }
            }

            if(receivedRequestsQueue.Count > 0)
                MonitorEx.Pulse(nLock, receivedRequestsQueue.First);

            if (!updatedOrderNumber)
                return 2;
            
            return PutValue(value) ? 1 : 0;

        }

        public LinkedListNode<Pair> AddUpload(string value)
        {
            lock (nLock)
            {
                return sentRequestsQueue.AddLast(new Pair(value));
            }
        }

        public void ConcludeUpload(long order)
        {
            lock (nLock)
            {
                order_number = order;

                if (sentRequestsQueue.Count > 0)
                {
                    sentRequestsQueue.RemoveFirst();
                    Wake();
                }
            }
        }

        public void RemoveUpload(LinkedListNode<Pair> node)
        {
            lock (nLock)
            {
                sentRequestsQueue.Remove(node);

                if (sentRequestsQueue.Count == 0)
                    Wake();
            }
        }
    }



}

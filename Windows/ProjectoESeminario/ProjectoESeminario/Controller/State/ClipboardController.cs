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
            sentRequestsQueue = new LinkedList<Pair>();
            receivedRequestsQueue = new LinkedList<WebPair>();
            clipboard_value = initialValue;
        }

        public void Wake()
        {
            lock (nLock)
            {
                if (sentRequestsQueue.Count > 0)
                {
                    MonitorEx.Pulse(nLock, sentRequestsQueue.First);
                    return;
                }

                if(receivedRequestsQueue.Count > 0)
                    MonitorEx.Pulse(nLock, receivedRequestsQueue.First);
            }
        }

        public bool PutValue(String value, Action<LinkedListNode<Pair>> startTimer)
        {
            lock (nLock)
            {
                var node = sentRequestsQueue.AddLast(new Pair(value));

                try
                {
                    startTimer.Invoke(node);
                }
                catch (Exception e)
                {
                    
                }

                do
                {
                    try
                    {
                        MonitorEx.Wait(nLock, node);
                    }
                    catch (ThreadInterruptedException) {}

                    if (node == sentRequestsQueue.First && sentRequestsQueue.Count == 0)
                    {
                        if(!value.Equals(clipboard_value))
                            clipboard_value = value;

                        sentRequestsQueue.Remove(node);
                        return !value.Equals(clipboard_value);
                    }

                    if (!sentRequestsQueue.Contains(node.Value))
                        return false;

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
                        catch (Exception){}

                        if (node == receivedRequestsQueue.First && sentRequestsQueue.Count == 0)
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
            
            return PutValue(value, (s) => {return;}) ? 1 : 0;

        }

        public void SetOrder(long order)
        {
            lock (nLock)
            {
                order_number = order;

                if (sentRequestsQueue.Count > 0)
                {
                    var node = sentRequestsQueue.First;
                    sentRequestsQueue.Remove(node);
                    MonitorEx.Pulse(nLock, node);

                }
            }
        }

        public void RemoveUpload(LinkedListNode<Pair> node)
        {
            lock (nLock)
            {
                sentRequestsQueue.Remove(node);
                MonitorEx.Pulse(nLock, node);
            }
        }
    }
}

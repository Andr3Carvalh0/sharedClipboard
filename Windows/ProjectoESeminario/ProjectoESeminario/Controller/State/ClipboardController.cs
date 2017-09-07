using System;
using System.Collections.Generic;
using System.Threading;

namespace ProjectoESeminario.Controller.State
{
    public class ClipboardController
    {
        private readonly Object nLock;
        private long order_number;

        //Used for the sync problems discuted on the paper.Use pair object to facilitate the cancelation
        private readonly LinkedList<Pair> sentRequestsQueue;
        private readonly LinkedList<String> ignoreQueue;


        //Helper class
        public class Pair
        {
            private bool wake;
            private bool remove;
            private string value;

            public Pair(string text)
            {
                this.wake = false;
                this.remove = false;
                this.value = text;
            }

            public string GetValue()
            {
                return value;
            }

            public bool isWake()
            {
                return wake;
            }

            public void SetRemove()
            {
                this.remove = true;
            }

            public bool ToRemove()
            {
                return remove;
            }

            public void Wake()
            {
                this.wake = true;
            }
        }

        public ClipboardController()
        {
            nLock = new Object();
            sentRequestsQueue = new LinkedList<Pair>();
            ignoreQueue = new LinkedList<String>();
        }

        public void Wake()
        {
            lock (nLock)
            {

                if (sentRequestsQueue.Count > 0)
                    MonitorEx.Pulse(nLock, sentRequestsQueue.First);
                  
            }
        }

        public bool PutValue(String text, Action<LinkedListNode<Pair>> startTimer)
        {
            lock (nLock)
            {
                if (ignoreQueue.Contains(text))
                {
                    ignoreQueue.Remove(text);
                    return false;
                }

                var node = sentRequestsQueue.AddLast(new Pair(text));
                startTimer.Invoke(node);
                long order = order_number;

                do
                {
                    try
                    {
                        MonitorEx.Wait(nLock, node);
                    }
                    catch (ThreadInterruptedException) {}

                    if (node.Value.ToRemove())
                    {
                        sentRequestsQueue.Remove(node);
                        return false;
                    }

                    if (node == sentRequestsQueue.First && node.Value.isWake())
                    {
                        sentRequestsQueue.Remove(node);
                        return order_number != order;
                    }

                } while (true);

            }
        }

        //return 0 -> Didnt change
        //return 1 -> Did change, we need to update history
        //return 2 -> Old request, we need to update history
        public int PutValue(string text, long order_received)
        {
            lock (nLock)
            {
                //When we dont have any pending request
                if (sentRequestsQueue.Count == 0)
                    return AvaliateAndUpdateOrder(order_received, order_number, text);

                //Even though we didnt finish the requests we can be sure that the request received is more recent
                // Than our sent requests.
                return AvaliateAndUpdateOrder(order_received, order_number + sentRequestsQueue.Count, text);

            }
        }

        private int AvaliateAndUpdateOrder(long received, long actual, string text)
        {
            if (received == actual)
                return 0;

            if (received < actual)
                return 2;

            order_number = received;
            ignoreQueue.AddLast(text);
            return 1;
        }

        public void UpdateStateOfUpload(long order)
        {
            lock (nLock)
            {
                if(order_number < order)
                    order_number = order;

                if (sentRequestsQueue.Count > 0)
                {
                    var node = sentRequestsQueue.First;
                    ignoreQueue.AddLast(node.Value.GetValue());
                    node.Value.Wake();
                    MonitorEx.Pulse(nLock, node);

                }
            }
        }

        public void RemoveUpload(LinkedListNode<Pair> node)
        {
            lock (nLock)
            {
                ignoreQueue.AddLast(node.Value.GetValue());
                node.Value.SetRemove();
                MonitorEx.Pulse(nLock, node);
            }
        }

        public void AddToIgnoreQueue(string text)
        {
            lock (nLock)
            {
                ignoreQueue.AddLast(text);
            }
        }
        
    }
}

using System;
using System.Collections.Generic;
using System.Reflection.Emit;
using System.Threading;

namespace ProjectoESeminario.Controller.State
{
    public class ClipboardController
    {
        private readonly Object nLock;
        private long order_number = 0;
        private string last;

        //Used for the sync problems discuted on the paper.Use pair object to facilitate the cancelation
        private readonly LinkedList<Pair> sentRequestsQueue;
        private readonly LinkedList<WebPair> receivedRequestsQueue;

        //Helper class
        public class Pair
        {
            private bool wake;
            private bool remove;
            private string text;

            public Pair(string text)
            {
                this.wake = false;
                this.remove = false;
                this.text = text;
            }

            public bool isWake()
            {
                return wake;
            }

            public string GetText()
            {
                return text;
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

        private class WebPair
        {
            private long order;

            public WebPair(long order)
            {
                this.order = order;
            }

            public long getOrder()
            {
                return order;
            }
        }

        public ClipboardController()
        {
            nLock = new Object();
            sentRequestsQueue = new LinkedList<Pair>();
            receivedRequestsQueue = new LinkedList<WebPair>();
            last = "";
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

        public bool PutValue(Action<LinkedListNode<Pair>> startTimer, string text)
        {
            lock (nLock)
            {
                if (text.Equals(last))
                    return false;

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
                if (sentRequestsQueue.Count == 0)
                {
                    if (order_received == order_number)
                        return 0;

                    if(order_received < order_number)
                        return 2;

                    order_number = order_received;
                    last = text;

                    return 1;
                }

                var node = receivedRequestsQueue.AddLast(new WebPair(order_received));

                do
                {
                    try
                    {
                        MonitorEx.Wait(nLock, node);
                    }
                    catch (Exception){}

                    if (node == receivedRequestsQueue.First && sentRequestsQueue.Count == 0)
                    {
                        receivedRequestsQueue.Remove(node);

                        if (order_received == order_number)
                            return 0;

                        if (order_received < order_number)
                            return 2;

                        order_number = order_received;
                        last = text;
                        return 1;
                    }

                } while (true);
            }
        }

        public void UpdateStateOfUpload(long order)
        {
            lock (nLock)
            {
                order_number = order;

                if (sentRequestsQueue.Count > 0)
                {
                    var node = sentRequestsQueue.First;
                    last = node.Value.GetText();
                    node.Value.Wake();
                    MonitorEx.Pulse(nLock, node);

                }
            }
        }

        public void RemoveUpload(LinkedListNode<Pair> node)
        {
            lock (nLock)
            {
                last = node.Value.GetText();
                node.Value.SetRemove();
                MonitorEx.Pulse(nLock, node);
            }
        }
        
    }
}

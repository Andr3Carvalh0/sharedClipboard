﻿using ProjectoESeminario.Databases;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ProjectoESeminario.State
{
    public class ClipboardController
    {
        private Object nLock;
        private String clipboard_value;
        private LinkedList<Pair> queue;
        private DatabaseHandler db;

        //Helper class
        private class Pair
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

        public ClipboardController(String initialValue)
        {
            db = new DatabaseHandler();
            nLock = new Object();
            queue = new LinkedList<Pair>();
            this.clipboard_value = initialValue;
        }

        public void wake()
        {
            lock (nLock)
            {
                if (queue.Count > 0) {
                    queue.First.Value.finish();
                    MonitorEx.Pulse(nLock, queue.First);
                }
            }
        }

        public bool putValue(String value, Boolean addToDB)
        {
            lock (nLock)
            {
                if (value.Equals(clipboard_value) && queue.Count == 0)
                    return false;
                
                //Queue is empty we can alter the value without waiting
                if (queue.Count == 0 && !value.Equals(clipboard_value))
                {
                    clipboard_value = value;
                    if(addToDB)
                        db.store(value);
                    return true;
                }

                var node = queue.AddLast(new Pair(value));

                do
                {
                    try
                    {
                        MonitorEx.Wait(nLock, node);
                    }
                    catch (ThreadInterruptedException) {
                        if (node.Value.isFinished())
                        {
                            clipboard_value = value;
                            if (addToDB)
                                db.store(value);
                            queue.Remove(node);
                            return true;
                        }

                        queue.Remove(node);
                        throw;
                    }

                    if (node.Value.isFinished())
                    {
                        clipboard_value = value;
                        if (addToDB)
                            db.store(value);
                        queue.Remove(node);
                        return true;
                    }

                } while (true);

            }
        }
    }



}
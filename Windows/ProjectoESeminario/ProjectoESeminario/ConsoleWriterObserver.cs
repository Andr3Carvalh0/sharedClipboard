using StompNet;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario
{
    class ConsoleWriterObserver : IObserver<IStompMessage>
    {
        // Incoming messages are processed here.
        public void OnNext(IStompMessage message)
        {
            Console.WriteLine("MESSAGE: " + message.GetContentAsString());

            if (message.IsAcknowledgeable)
                message.Acknowledge();
        }

        // Any ERROR frame or stream exception comes through here.
        public void OnError(Exception error)
        {
            Console.WriteLine("ERROR: " + error.Message);
        }

        // OnCompleted is invoked when unsubscribing.
        public void OnCompleted()
        {
            Console.WriteLine("UNSUBSCRIBED!");
        }
    }
}

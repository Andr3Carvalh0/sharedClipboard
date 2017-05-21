using System;
using System.Runtime.Serialization;

namespace Projecto.UI
{
    [Serializable]
    internal class WebExceptions : Exception
    {
        public System.Net.HttpStatusCode simplerError { get; }

        public WebExceptions(System.Net.HttpStatusCode message) : base(message + "")
        {
            simplerError = message;
        }

    }
}
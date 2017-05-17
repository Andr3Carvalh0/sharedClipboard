using System;
using System.Runtime.Serialization;

namespace Projecto.UI
{
    [Serializable]
    internal class WebException : Exception
    {
        public System.Net.HttpStatusCode simplerError { get; }

        public WebException(System.Net.HttpStatusCode message) : base(message + "")
        {
            simplerError = message;
        }

    }
}
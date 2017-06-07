using System;
using System.Runtime.Serialization;

namespace Projecto.UI
{   /// <summary>
    /// This exception is launched when we have a server error(500), authentication error(403), or any other error 
    /// </summary>
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
using System;

namespace ProjectoESeminario.Exceptions
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
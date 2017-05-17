using System;
using System.Runtime.Serialization;

namespace Projecto.UI
{
    [Serializable]
    internal class UserException : Exception
    {
        public String simplerMessage { get; }
        public UserException() { }

        public UserException(bool email, bool password) : base("Fields invalid")
        {
            simplerMessage = "The following fields are invalid : \n" + (!email ? "\u2022 Email\n" : "") + (!password ? "\u2022 Password" : "");
        }
    }
}
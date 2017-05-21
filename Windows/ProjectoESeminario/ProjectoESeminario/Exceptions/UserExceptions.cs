using System;
using System.Runtime.Serialization;

namespace Projecto.UI
{
    [Serializable]
    internal class UserExceptions : Exception
    {
        public String simplerMessage { get; }
        public UserExceptions() { }

        public UserExceptions(bool email, bool password) : base("Fields invalid")
        {
            simplerMessage = "The following fields are invalid : \n" + (!email ? "\u2022 Email\n" : "") + (!password ? "\u2022 Password" : "");
        }
    }
}
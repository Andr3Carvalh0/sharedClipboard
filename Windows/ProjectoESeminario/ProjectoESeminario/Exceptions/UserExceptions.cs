using System;
using System.Runtime.Serialization;

namespace Projecto.UI
{
    /// <summary>
    /// UserException.
    /// This exception is used when we detect a local authentication error.For example we validate locally if the email is a valid email
    /// and if the password length is > 6.
    /// </summary>
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
using System;

namespace ProjectoESeminario.Controller.Communication.Utils
{
    public class RegisterJSONWrapper
    {
        public string action { get; }
        public string sub { get; }
        public string id { get; }

        public RegisterJSONWrapper(String sub, String id)
        {
            this.action = "register";
            this.sub = sub;
            this.id = id;
        }
    }

    public class UploadJSONWrapper
    {
        public string action { get; }
        public string sub { get; }
        public string data { get; }
        public string device { get; }

        public UploadJSONWrapper(String sub, String data, String device)
        {
            this.action = "push";
            this.sub = sub;
            this.data = data;
            this.device = device;
        }
    }

    public class UploadMimeJSONWrapper
    {
        public string action { get; }
        public string sub { get; }
        public string data { get; }
        public string filename { get; }
        public string device { get; }


        public UploadMimeJSONWrapper(String sub, String data, String filename, String device)
        {
            this.action = "pushMime";
            this.sub = sub;
            this.data = data;
            this.filename = filename;
            this.device = device;
        }

    }
}

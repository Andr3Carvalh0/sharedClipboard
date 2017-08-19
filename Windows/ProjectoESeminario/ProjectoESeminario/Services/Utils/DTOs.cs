using System;


namespace ProjectoESeminario.Services.Utils
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

        public UploadJSONWrapper(String sub, String data)
        {
            this.action = "push";
            this.sub = sub;
            this.data = data;
        }
    }

    public class UploadMimeJSONWrapper
    {
        public string action { get; }
        public string sub { get; }
        public string data { get; }
        public string filename { get; }

        public UploadMimeJSONWrapper(String sub, String data, String filename)
        {
            this.action = "pushMime";
            this.sub = sub;
            this.data = data;
            this.filename = filename;
        }

    }
}

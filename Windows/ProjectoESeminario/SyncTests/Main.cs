using System.Threading;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ProjectoESeminario.Controller.State;

namespace SyncTests
{
    [TestClass]
    public class Main
    {

        [TestMethod]
        public void ShouldSucced()
        {
            ClipboardController controller = new ClipboardController();

            bool res = false;
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                try
                {
                    controller.PutValue((s) =>
                    {
                        new Thread(() =>
                        {
                            Thread.Sleep(2000);
                            controller.UpdateStateOfUpload(2);
                        }).Start();
                    }, "t1");
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[1] = new Thread(() =>
            {
                try
                {
                    res = controller.PutValue(1) == 2;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[0].Start();
            arr[1].Start();

            arr[0].Join();
            arr[1].Join();

            Assert.IsTrue(res);

        }

        [TestMethod]
        public void ShouldSuccedOnTimeout()
        {
            ClipboardController controller = new ClipboardController();

            bool res = false;
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                try
                {
                    var n = controller.PutValue((s) =>
                    {
                        new Thread(() =>
                        {
                            Thread.Sleep(2000);
                            controller.RemoveUpload(s);
                        }).Start();
                    }, "T1");
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[1] = new Thread(() =>
            {
                try
                {
                    res = controller.PutValue(1) == 1;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[0].Start();
            arr[1].Start();

            arr[0].Join();
            arr[1].Join();

            Assert.IsTrue(res);

        }

        [TestMethod]
        public void ShouldFailOnTimeout()
        {
            ClipboardController controller = new ClipboardController();

            bool res = false;
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                try
                {
                    var n = controller.PutValue((s) => new Thread(() =>
                    {
                        Thread.Sleep(2000);
                        controller.RemoveUpload(s);
                    }).Start(), "t1");
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[1] = new Thread(() =>
            {
                try
                {
                    res = controller.PutValue(1) == 1;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[0].Start();
            arr[1].Start();

            arr[0].Join();
            arr[1].Join();

            Assert.IsTrue(res);

        }

        [TestMethod]
        public void ShouldSuccedOnAppLessThanServer()
        {
            ClipboardController controller = new ClipboardController();

            bool res = false;
            Thread[] arr = new Thread[1];

            arr[0] = new Thread(() =>
            {
                try
                {
                    res = controller.PutValue(2) == 1;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[0].Start();

            arr[0].Join();

            Assert.IsTrue(res);

        }

        [TestMethod]
        public void MultipleReceivedRequests()
        {
            ClipboardController controller = new ClipboardController();

            bool res1 = false;
            bool res2 = false;
            Thread[] arr = new Thread[3];
            arr[0] = new Thread(() =>
            {
                try
                {
                    var n = controller.PutValue((s) => new Thread(() =>
                    {

                        Thread.Sleep(2000);
                        controller.UpdateStateOfUpload(3);
                    }).Start(), "t1");
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[1] = new Thread(() =>
            {
                try
                {
                    res1 = controller.PutValue(1) == 2;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[2] = new Thread(() =>
            {
                try
                {
                    res2 = controller.PutValue(2) == 2;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[0].Start();
            arr[1].Start();
            arr[2].Start();

            arr[0].Join();
            arr[1].Join();
            arr[2].Join();

            Assert.IsTrue(res1);
            Assert.IsTrue(res2);
        }

        [TestMethod]
        public void MultipleReceivedRequestsOneWithHigherOrder()
        {
            ClipboardController controller = new ClipboardController();

            bool res1 = false;
            bool res2 = false;
            Thread[] arr = new Thread[3];
            arr[0] = new Thread(() =>
            {
                try
                {
                    controller.PutValue((s) => new Thread(() =>
                    {
                        Thread.Sleep(2000);
                        controller.UpdateStateOfUpload(2);
                    }).Start(), "T1");
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[1] = new Thread(() =>
            {
                try
                {
                    res1 = controller.PutValue(1) == 2;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[2] = new Thread(() =>
            {
                try
                {
                    res2 = controller.PutValue(3) == 1;
                }
                finally
                {
                    controller.Wake();
                }
            });

            arr[0].Start();
            arr[1].Start();
            arr[2].Start();

            arr[0].Join();
            arr[1].Join();
            arr[2].Join();

            Assert.IsTrue(res1);
            Assert.IsTrue(res2);
        }
    }
}

using System;
using System.Runtime.Remoting.Messaging;
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
            ClipboardController controller = new ClipboardController("Ola");

            bool res = false;
            Thread[] arr = new Thread[3];
            arr[0] = new Thread(() =>
            {
                try
                {
                    controller.PutValue("You", null);
                }
                finally
                {
                    controller.Wake();
                }


            });

            arr[1] = new Thread(() =>
            {
                Thread.Sleep(2000);
                controller.SetOrder(2);
            });

            arr[2] = new Thread(() =>
            {
                res = controller.PutValue("a", 1) == 2;
            });

            arr[0].Start();
            arr[1].Start();
            arr[2].Start();

            arr[0].Join();
            arr[1].Join();
            arr[2].Join();

            Assert.IsTrue(res);

        }

        [TestMethod]
        public void ShouldSuccedOnTimeout()
        {
            ClipboardController controller = new ClipboardController("Ola");

            bool res = false;
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                var n = controller.PutValue("You", (s) => controller.RemoveUpload(s));
                Thread.Sleep(2000);
                

            });

            arr[1] = new Thread(() =>
            {
                res = controller.PutValue("a1", 1) == 1;
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
            ClipboardController controller = new ClipboardController("Ola");

            bool res = false;
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                var n = controller.PutValue("You", (s) => controller.RemoveUpload(s));
                Thread.Sleep(2000);

            });

            arr[1] = new Thread(() =>
            {
                res = controller.PutValue("Ola", 1) == 0;
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
            ClipboardController controller = new ClipboardController("Ola");

            bool res = false;
            Thread[] arr = new Thread[1];

            arr[0] = new Thread(() =>
            {
                res = controller.PutValue("Ola1", 2) == 1;
            });

            arr[0].Start();

            arr[0].Join();

            Assert.IsTrue(res);

        }

        [TestMethod]
        public void MultipleReceivedRequests()
        {
            ClipboardController controller = new ClipboardController("Ola");

            bool res1 = false;
            bool res2 = false;
            Thread[] arr = new Thread[3];
            arr[0] = new Thread(() =>
            {
                var n = controller.PutValue("You", null);
                Thread.Sleep(2000);
                controller.SetOrder(3);

            });

            arr[1] = new Thread(() =>
            {
                res1 = controller.PutValue("a1", 1) == 2;
            });

            arr[2] = new Thread(() =>
            {
                res2 = controller.PutValue("a2", 2) == 2;
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
        public void MultipleReceivedRequests1()
        {
            ClipboardController controller = new ClipboardController("Ola");

            bool res1 = false;
            bool res2 = false;
            Thread[] arr = new Thread[3];
            arr[0] = new Thread(() =>
            {
                controller.PutValue("You", null);
                Thread.Sleep(2000);
                controller.SetOrder(2);

            });

            arr[1] = new Thread(() =>
            {
                res1 = controller.PutValue("a1", 1) == 2;
            });

            arr[2] = new Thread(() =>
            {
                res2 = controller.PutValue("a2", 3) == 1;
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
        public void Multiple()
        {
            ClipboardController controller = new ClipboardController("Ola");

            bool res1 = false;
            bool res2 = false;
            Thread[] arr = new Thread[3];
            arr[0] = new Thread(() =>
            {
                controller.PutValue("You", null);
                Thread.Sleep(2000);
                controller.SetOrder(2);

            });

            arr[1] = new Thread(() =>
            {
                try
                {

                    controller.PutValue("You", null);
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

                    controller.PutValue("You", null);
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

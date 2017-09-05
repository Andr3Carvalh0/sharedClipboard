using System;
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
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                controller.AddUpload("Yo");
                Thread.Sleep(2000);
                controller.ConcludeUpload(2);

            });

            arr[1] = new Thread(() =>
            {
                res = controller.PutValue("a", 1) == 2;
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
            ClipboardController controller = new ClipboardController("Ola");

            bool res = false;
            Thread[] arr = new Thread[2];
            arr[0] = new Thread(() =>
            {
                var n = controller.AddUpload("Yo1");
                Thread.Sleep(2000);
                controller.RemoveUpload(n);

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
                var n = controller.AddUpload("Yo1");
                Thread.Sleep(2000);
                controller.RemoveUpload(n);

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
    }
}

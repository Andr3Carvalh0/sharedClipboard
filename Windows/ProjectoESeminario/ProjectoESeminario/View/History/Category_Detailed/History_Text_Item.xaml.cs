using ProjectoESeminario.View.History.Interfaces;
using System.Threading;
using System.Windows.Controls;
using System;
using System.Windows.Input;

namespace ProjectoESeminario.View.History.Category_Detailed
{
    /// <summary>
    /// Interaction logic for History_Text_Item.xaml
    /// </summary>
    public partial class History_Text_Item : UserControl
    {
        private readonly IHistory history;
        private readonly string text;
        private volatile int hasClicked;

        public History_Text_Item(string item, IHistory history)
        {
            InitializeComponent();
            Title.Text = Title.Text.ToString().Replace("Text", item);
            this.history = history;
            this.text = item;
            Title.MouseLeftButtonUp += ClickEvent;
            Title.MouseLeftButtonDown += PrepareClickEvent;
        }

        private void PrepareClickEvent(object sender, MouseButtonEventArgs e)
        {
            Interlocked.CompareExchange(ref hasClicked, 1, 0);

            new Thread(() =>
            {
                Thread.Sleep(1000);
                Interlocked.CompareExchange(ref hasClicked, 0, 1);

            }).Start();
        }

        private void ClickEvent(object sender, MouseButtonEventArgs e)
        {
            if(hasClicked > 0)
                history.SetContent(text);
        }
    }
}

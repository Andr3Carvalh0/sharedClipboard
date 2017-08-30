﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectoESeminario.UI.History
{
    public class ItemText : History_Item
    {
        public ItemText(string name, IHistory history) : base(name, history)
        {}

        public override void handleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            history.handleTextCategory(name);
        }
    }
}
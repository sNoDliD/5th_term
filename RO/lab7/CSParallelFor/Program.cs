﻿using System;
using System.Collections.Generic;

namespace Lab7
{
    class Program
    {
        static void Main(string[] args)
        {
            var test=new Test("./resources/index.html");
            test.AddTask(2000);
            test.Run();
        }
    }
}
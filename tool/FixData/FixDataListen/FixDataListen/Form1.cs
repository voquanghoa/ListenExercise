using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace FixDataListen
{
	public partial class Form1 : Form
	{
		public Form1()
		{
			InitializeComponent();
		}

		private void button1_Click(object sender, EventArgs e)
		{
			if (textBox1.TextLength == 0)
			{
				MessageBox.Show("Chưa nhập đường dẫn !");
			}

			ProcessFolder(textBox1.Text);
        }

		private void ProcessFolder(string folder)
		{
			var files = Directory.EnumerateFiles(folder, "*.txt", SearchOption.AllDirectories);
			foreach (string file in files)
			{
				var outputFile = file.Replace(".txt", ".json");
                var obj = FileConverter.Convert(File.ReadAllText(file));
				File.WriteAllText(outputFile, JsonConvert.SerializeObject(obj));
			}
			MessageBox.Show("DONE !!!");
		}
	}
}

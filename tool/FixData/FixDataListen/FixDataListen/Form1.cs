using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
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
			FileConverter fileConverter = new FileConverter(textBox1.Text);
			progressBar1.Visible = true;

			fileConverter.OnError += FileConverter_OnError;
			fileConverter.OnFinishFeedback += FileConverter_OnFinishFeedback;
			fileConverter.OnProgressFeedback += FileConverter_OnProgressFeedback;

			new Thread(() => fileConverter.ProcessFolder()).Start();
        }

		private void FileConverter_OnProgressFeedback(int percent)
		{
			if (InvokeRequired)
			{
				Invoke(new Action(() =>
				{
					progressBar1.Value = percent;
				}));
			}
			else
			{
				progressBar1.Value = percent;
			}
		}

		private void FileConverter_OnFinishFeedback()
		{
			if (InvokeRequired)
			{
				Invoke(new Action(() =>
				{
					MessageBox.Show("Done !!!");
					progressBar1.Visible = false;
				}));
			}
			else
			{
				MessageBox.Show("Done !!!");
				progressBar1.Visible = false;
			}
		}

		private void FileConverter_OnError(string fileName)
		{
			if (InvokeRequired)
			{
				Invoke(new Action(() =>
				{
					listBox1.Items.Add(fileName);
				}));
			}
			else
			{
				listBox1.Items.Add(fileName);
			}
		}

		private void listBox1_MouseDoubleClick(object sender, MouseEventArgs e)
		{
			System.Diagnostics.Process.Start(listBox1.SelectedItem.ToString());
		}
	}
}

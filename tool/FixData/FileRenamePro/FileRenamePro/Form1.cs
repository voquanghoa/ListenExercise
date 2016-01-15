using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
namespace FileRenamePro
{
	public partial class Form1 : Form
	{
		public Form1()
		{
			InitializeComponent();
		}

		private void button1_Click(object sender, EventArgs e)
		{
			Rename(textBox1.Text);
			MessageBox.Show("DONE");
        }

		public void Rename(string folder)
		{
			var files = Directory.EnumerateFiles(folder).ToList();

			foreach (var file in files)
			{
				var fileName = Path.GetFileName(file);
				var fileWithoutExtension = Path.GetFileNameWithoutExtension(file);
				var fileExtension = Path.GetExtension(file);

				var newFileName = Convert(fileWithoutExtension) + fileExtension;
				var newFilePath = Path.Combine(Path.GetDirectoryName(file), newFileName);
				if (file != newFilePath)
				{
					File.Move(file, newFilePath);
				}
			}

			var folderParent = Path.GetDirectoryName(folder);
			var folderName = Convert(Path.GetFileName(folder));
			var newFolderPath = Path.Combine(folderParent, folderName);

			if (folder != newFolderPath)
			{
				Directory.Move(folder, newFolderPath);
            }

			var folders = Directory.EnumerateDirectories(newFolderPath);
			foreach (var childFolder in folders)
			{
				Rename(childFolder);
			}
		}

		public string Convert(string src)
		{
			return src.Replace(" ", "_");
		}
	}
}

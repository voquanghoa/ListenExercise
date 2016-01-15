using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace FixDataListen
{
	public delegate void ErrorFeedback(string fileName);
	public delegate void ProgressFeedback(int percent);
	public delegate void FinishFeedback();

	public class FileConverter
	{
		private string folder;
        public FileConverter(string folderDir)
		{
			folder = folderDir;
        }

		public event ErrorFeedback OnError;
		public event ProgressFeedback OnProgressFeedback;
		public event FinishFeedback OnFinishFeedback;

		public void ProcessFolder()
		{
			var files = Directory.EnumerateFiles(folder, "*.txt", SearchOption.AllDirectories).ToList();
			
			OnProgressFeedback(0);

			int count = files.Count;

			int lastPercent = 0;
			int finishCount = 0;
			int currentPercent;

			foreach (string file in files)
			{
				var outputFile = file.Replace(".txt", ".json");
				File.WriteAllText(outputFile, JsonConvert.SerializeObject(Convert(file)));
				finishCount++;
				currentPercent = 100 * finishCount / count;
				if (currentPercent != lastPercent)
				{
					OnProgressFeedback(currentPercent);
					lastPercent = currentPercent;
                }

            }
			OnFinishFeedback();
        }

		public ListenContent Convert(string fileName)
		{
			string fileContent = File.ReadAllText(fileName);
            string[] lines = fileContent.Split('\n')
				.Select(y=>y.Replace("\r",""))
				.Where(x => x.Trim().Length > 0).ToArray();
			var result = new ListenContent()
			{
				Script = fileContent,
				Questions = new List<Question>()
			};

			if (lines.Length > 0)
			{
				if (lines[0].EndsWith(".mp3"))
				{
					lines = lines.Skip(1).ToArray();
					fileContent = TextFormatter.JoinString(lines);
				}

				if (fileContent.Contains("Listening Comprehension Transcript"))
				{
					Question currentQuestion = null;
					for (int i = 0; i < lines.Length; i++)
					{
						var currentLine = lines[i];

						if (lines[i].Contains("Listening Comprehension Transcript"))
						{
							result.Script = TextFormatter.JoinString(lines.Skip(i+1).ToArray());
							break;
						}
						else
						{
							if (currentQuestion == null || TextFormatter.ShouldBeQuestion(currentLine))
							{
								currentQuestion = new Question();
								currentQuestion.QuestionTitle = TextFormatter.FormatQuestion(currentLine);
								currentQuestion.Answer = new List<string>();
								currentQuestion.CorrectAnswer = -1;
								result.Questions.Add(currentQuestion);
							}
							else
							{
								if (TextFormatter.isCorrectAnswer(currentLine))
								{
									currentQuestion.CorrectAnswer = currentQuestion.Answer.Count;
								}

								currentQuestion.Answer.Add(TextFormatter.FormatAnswer(currentLine));

								if (currentQuestion.Answer.Count == 4)
								{
									if (currentQuestion.CorrectAnswer == -1)
									{
										Console.WriteLine("File error " + fileContent);
										OnError(fileName);
                                    }
									currentQuestion = null;
                                }
							}
						}
					}
				}
				else
				{
					result.Script = fileContent;
                }
            }
			return result;
		}
	}
}

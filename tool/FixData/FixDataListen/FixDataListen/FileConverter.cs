using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace FixDataListen
{
	public class FileConverter
	{
		public static string JoinString(string[] lines)
		{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < lines.Length; i++)
			{
				if (i > 0)
				{
					sb.Append("\n");
				}
				sb.Append(lines[i]);
			}
			return sb.ToString();
		}



		public static ListenContent Convert(string fileContent)
		{
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
					fileContent = JoinString(lines);
				}

				if (fileContent.Contains("Listening Comprehension Transcript"))
				{
					Question currentQuestion = null;
					for (int i = 0; i < lines.Length; i++)
					{
						if (lines[i].Contains("Listening Comprehension Transcript"))
						{
							result.Script = JoinString(lines.Skip(i+1).ToArray());
							break;
						}
						else
						{
							if (currentQuestion == null)
							{
								currentQuestion = new Question();
								currentQuestion.QuestionTitle = lines[i];
								currentQuestion.Answer = new List<string>();
								currentQuestion.CorrectAnswer = -1;
							}
							else
							{
								if (lines[i].StartsWith(" "))
								{
									currentQuestion.CorrectAnswer = currentQuestion.Answer.Count;
									lines[i] = lines[i].Trim();
								}

								if (lines[i].Contains("-- CORRECT"))
								{
									currentQuestion.CorrectAnswer = currentQuestion.Answer.Count;
									lines[i] = lines[i].Replace("-- CORRECT", "").Trim();
								}

								if (lines[i].Contains("--CORRECT"))
								{
									currentQuestion.CorrectAnswer = currentQuestion.Answer.Count;
									lines[i] = lines[i].Replace("--CORRECT", "").Trim();
								}

								currentQuestion.Answer.Add(lines[i]);

								if (currentQuestion.Answer.Count == 4)
								{
									result.Questions.Add(currentQuestion);
									if (currentQuestion.CorrectAnswer == -1)
									{
										Console.WriteLine("File error " + fileContent);
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

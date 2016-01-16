using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace FixDataListen
{
	class TextFormatter
	{
		private static string[] correctAnswerMatcher =
		{
			"^[ ]",
			"--[ ]*CORRECT"
		};
        public static string FormatQuestion(string text)
		{
			return Regex.Replace(text, "^(\\d+)\\)[ ]*[.][ ]*", "").Trim();
		}

		public static bool ShouldBeQuestion(string text)
		{
			return Regex.IsMatch(text, @"^(\d+)[\)\.]");
		}

		public static bool isCorrectAnswer(string text)
		{
			return correctAnswerMatcher.Any(x => Regex.IsMatch(text, x));
		}

		public static string FormatAnswer(string text)
		{
			foreach (var regex in correctAnswerMatcher)
			{
				text = Regex.Replace(text, regex,"");
			}
			return text.Trim();
		}

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
	}
}

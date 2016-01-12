using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

namespace FixDataListen
{
	[DataContract]
	public class Question
	{
		[DataMember(Name = "questionTitle", IsRequired = true)]
		public string QuestionTitle
		{
			get; set;
		}

		[DataMember(Name = "anwers", IsRequired = true)]
		public List<string> Answer
		{
			get; set;
		}

		[DataMember(Name = "correctAnswer", IsRequired = true)]
		public int CorrectAnswer
		{
			get; set;
		}
	}
}

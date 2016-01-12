using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

namespace FixDataListen
{
	[DataContract]
	public class ListenContent
	{
		[DataMember(Name = "questions", IsRequired = true)]
		public List<Question> Questions
		{
			get; set;
		}

		[DataMember(Name = "script", IsRequired = true)]
		public string Script
		{
			get; set;
		}
	}
}

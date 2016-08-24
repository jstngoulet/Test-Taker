


public class Question 
{
	String ques, cor, ty;
	String[] ans;
	
	public Question(String type, String question, String[] answers, String correct)
	{
		ques = question;
		cor = correct;
		ans = answers;
		ty = type;
	}
	
	public String getQuestion()
	{
		return ques;
	}
	
	public String getCorrect()
	{
		return cor;
	}
	
	public String[] getAnswers()
	{
		return ans;
	}
	
	public String getType()
	{
		return ty;
	}
}
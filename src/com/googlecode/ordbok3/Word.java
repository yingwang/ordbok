package com.googlecode.ordbok3;

public class Word {

	public String getWordValue() {
		return wordValue;
	}

	public void setWordValue(String wordValue) {
		this.wordValue = "<h3><font color='red'><b>"+wordValue+"</b></font>";
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		if (lang.equals("en"))
		this.lang = "en.";
		if (lang.equals("sv"))
			this.lang = "sv.";
	}

	public String getWordClass() {
		return wordClass;
	}

	public void setWordClass(String wordClass) {
		if (wordClass.equals("nn"))
		{
			this.wordClass = "substantiv,";
			return;
		}
		else if (wordClass.equals("vb"))
		{
			this.wordClass = "verb,";
			return;
		}
		else if (wordClass.equals("jj"))
		{
			this.wordClass = "adjektiv,";
			return;
		}
		else if (wordClass.equals("ab"))
		{
			this.wordClass = "adverb,";
			return;
		}
		else if (wordClass.equals("kn"))
		{
			this.wordClass = "konjunktion,";
			return;
		}
		else if (wordClass.equals("pp"))
		{
			this.wordClass = "preposition,";
			return;
		}
		else if (wordClass.equals("in"))
		{
			this.wordClass = "interjection,";
			return;
		}
		else if (wordClass.equals("pn"))
		{
			this.wordClass = "pronomen,";
			return;
		}
		else if (wordClass.equals("abbrev"))
		{
			this.wordClass = "fšrkortning,";
			return;
		}
		this.wordClass = wordClass;
		
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getPhoneticValue() {
		return phoneticValue;
	}

	public void setPhoneticValue(String phoneticValue) {
		this.phoneticValue = "<l>["+phoneticValue+"]</l>";
	}

	public String getPhoneticSoundFile() {
		return phoneticSoundFile;
	}

	public void setPhoneticSoundFile(String phoneticSoundFile) {
		this.phoneticSoundFile = phoneticSoundFile;
	}

	private String wordValue="";
	private String lang="";
	private String wordClass="";
	private String translation="";
	private String phoneticValue="";
	private String phoneticSoundFile="";
	private String wordContent="";
	
	public String getWordContent() {
		return wordContent;
	}

	public void setWordContent(String wordContent) {
		this.wordContent += wordContent;
	}

	public Word(){
	}
	
	public Word copy()
	{
		Word copy = new Word();
		copy.lang = lang;
		copy.phoneticSoundFile = phoneticSoundFile;
		copy.phoneticValue = phoneticValue;
		copy.translation = translation;
		copy.wordClass = wordClass;
		copy.wordContent = wordContent;
		copy.wordValue = wordValue;
		return copy;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(""+getWordValue()+" " +"<font color='purple'>  "+" "+getLang()+"</font><font color='blue'>  "+getWordClass()+"</font></h3>");
		sb.append(getWordContent()+"<br />");
		return sb.toString();
	}

	public void setType(String value) {
		// TODO Auto-generated method stub
	}

}


package com.googlecode.ordbok3.feedParser;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.ordbok3.Word;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class FeedParser
{
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String ITEM = "item";
	

	public List<Word> parse()
	{
		final Word currentWord = new Word();
		RootElement root = new RootElement("rss");
		final List<Word> Words = new ArrayList<Word>();
		Element channel = root.getChild("channel");
		Element item = channel.getChild(ITEM);
		item.setEndElementListener(new EndElementListener()
		{
			public void end()
			{
				Words.add(currentWord.copy());
			}
		});
		item.getChild(TITLE).setEndTextElementListener(
		        new EndTextElementListener()
		        {
			        public void end(String body)
			        {
//				        currentWord.setTitle(body);
			        }
		        });
		item.getChild(LINK).setEndTextElementListener(
		        new EndTextElementListener()
		        {
			        public void end(String body)
			        {
//				        currentWord.setLink(body);
			        }
		        });
		item.getChild(DESCRIPTION).setEndTextElementListener(
		        new EndTextElementListener()
		        {
			        public void end(String body)
			        {
//				        currentWord.setDescription(body);
			        }
		        });
		item.getChild(PUB_DATE).setEndTextElementListener(
		        new EndTextElementListener()
		        {
			        public void end(String body)
			        {
//				        currentWord.setDate(body);
			        }
		        });
		try
		{
//			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8,
//			        root.getContentHandler());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return Words;
	}
}

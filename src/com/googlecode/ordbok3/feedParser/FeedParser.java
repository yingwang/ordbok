package com.googlecode.ordbok3.feedParser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import android.sax.*;

import com.googlecode.ordbok3.translationData.Example;
import com.googlecode.ordbok3.translationData.Word;

//import com.googlecode.ordbok3.Word;

public class FeedParser
{
	// string coped from example.
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String ITEM = "item";

	// element name in XML file
	static final String ksValue = "value";
	static final String ksWord = "word";
	static final String ksLang = "lang";
	static final String ksClass = "class";
	static final String ksTranslation = "translation";
	static final String ksPhonetic = "phonetic";
	static final String ksSoundFile = "soundFile";
	static final String ksParadigm = "paradigm";
	static final String ksInflection = "inflection";
	static final String ksSynonym = "synonym";
	static final String ksExample = "example";
	static final String ksDefinition = "definition";

	// test string, remove this after parser implement.
	static final String ksTestXml = "<node>"
	        + "<word value=\"titta\" lang=\"sv\" class=\"vb\" id=\"189390\" lexinid=\"18940\" origin=\"lexin\" comment=\"Ã¤ven &amp;quot;undersÃ¶ka nÃ¤rmare&amp;quot;\" date=\"2011-03-03\">"
	        + "<translation id=\"189390-18310\" value=\"watch\" origin=\"user\" date=\"2012-04-09T01:50:22\">"
	        + "</translation>"
	        + "<translation id=\"189390-18311\" value=\"see\" origin=\"user\" date=\"2012-04-09T01:50:22\">"
	        + "</translation>"
	        + "<phonetic value=\"Â²tIt:ar\" soundFile=\"tittar.swf\" date=\"2011-03-03\">"
	        + "</phonetic>"
	        + "<paradigm id=\"16544\" origin=\"lexin\" date=\"2011-03-03\">"
	        + "<inflection value=\"tittade\">"
	        + "</inflection>"
	        + "<inflection value=\"tittat\">"
	        + "</inflection>"
	        + "<inflection value=\"titta\">"
	        + "</inflection>"
	        + "<inflection value=\"tittar\">"
	        + "</inflection>"
	        + "</paradigm>"
	        + "<synonym value=\"beskÃ¥da\" level=\"4.2\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"betrakta\" level=\"4.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"betraktar\" level=\"5.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"blicka\" level=\"3.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"glo\" level=\"4.2\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"glutta\" level=\"3.4\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"kika\" level=\"4.1\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"plira\" level=\"4.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"se\" level=\"3.4\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"skÃ¥da\" level=\"4.6\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"spana\" level=\"4.6\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"Ã¶gna\" level=\"3.3\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<see value=\"titta||titta..1||titta..vb.1\" type=\"saldo\" origin=\"saldo\" date=\"2011-03-03\">"
	        + "</see>"
	        + "<see value=\"tittar.swf\" type=\"animation\" origin=\"lexin\" date=\"2011-03-03\">"
	        + "</see>"
	        + "<example id=\"10667\" value=\"titta pÃ¥ TV\" date=\"2011-03-03\">"
	        + "<translation value=\"watch TV\">"
	        + "</translation>"
	        + "</example>"
	        + "<example id=\"10668\" value=\"kommittÃ©n ska titta pÃ¥ reglerna fÃ¶r beskattning\" date=\"2011-03-03\">"
	        + "<translation value=\"the committee will look into the tax regulations\">"
	        + "</translation>"
	        + "</example>"
	        + "<definition id=\"18309\" value=\"se (i en viss avsikt)\" date=\"2011-03-03\">"
	        + "</definition>"
	        + "<grammar value=\"A &amp; (pÃ¥ B/x)\" origin=\"lexin\" date=\"2011-03-03\">"
	        + "</grammar>"
	        + "</word>"
	        + "<word value=\"titta test\" lang=\"sv\" class=\"vb\" id=\"189390\" lexinid=\"18940\" origin=\"lexin\" comment=\"Ã¤ven &amp;quot;undersÃ¶ka nÃ¤rmare&amp;quot;\" date=\"2011-03-03\">"
	        + "<translation id=\"189390-18310\" value=\"watch test\" origin=\"user\" date=\"2012-04-09T01:50:22\">"
	        + "</translation>"
	        + "<translation id=\"189390-18311\" value=\"see test\" origin=\"user\" date=\"2012-04-09T01:50:22\">"
	        + "</translation>"
	        + "<phonetic value=\"Â²tIt:ar\" soundFile=\"tittar.swf\" date=\"2011-03-03\">"
	        + "</phonetic>"
	        + "<paradigm id=\"16544\" origin=\"lexin\" date=\"2011-03-03\">"
	        + "<inflection value=\"tittade\">"
	        + "</inflection>"
	        + "<inflection value=\"tittat\">"
	        + "</inflection>"
	        + "<inflection value=\"titta\">"
	        + "</inflection>"
	        + "<inflection value=\"tittar\">"
	        + "</inflection>"
	        + "</paradigm>"
	        + "<synonym value=\"beskÃ¥da\" level=\"4.2\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"betrakta\" level=\"4.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"betraktar\" level=\"5.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"blicka\" level=\"3.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"glo\" level=\"4.2\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"glutta\" level=\"3.4\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"kika\" level=\"4.1\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"plira\" level=\"4.0\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"se\" level=\"3.4\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"skÃ¥da\" level=\"4.6\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"spana\" level=\"4.6\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<synonym value=\"Ã¶gna\" level=\"3.3\" origin=\"synlex\" date=\"2011-03-03\">"
	        + "</synonym>"
	        + "<see value=\"titta||titta..1||titta..vb.1\" type=\"saldo\" origin=\"saldo\" date=\"2011-03-03\">"
	        + "</see>"
	        + "<see value=\"tittar.swf\" type=\"animation\" origin=\"lexin\" date=\"2011-03-03\">"
	        + "</see>"
	        + "<example id=\"10667\" value=\"titta pÃ¥ TV\" date=\"2011-03-03\">"
	        + "<translation value=\"watch TV\">"
	        + "</translation>"
	        + "</example>"
	        + "<example id=\"10668\" value=\"kommittÃ©n ska titta pÃ¥ reglerna fÃ¶r beskattning\" date=\"2011-03-03\">"
	        + "<translation value=\"the committee will look into the tax regulations\">"
	        + "</translation>"
	        + "</example>"
	        + "<definition id=\"18309\" value=\"se (i en viss avsikt)\" date=\"2011-03-03\">"
	        + "</definition>"
	        + "<grammar value=\"A &amp; (pÃ¥ B/x)\" origin=\"lexin\" date=\"2011-03-03\">"
	        + "</grammar>" + "</word>" + "</node>";
	static final String ksNode = "node";

	public List<Word> parse(String sAXml)
	{
		final Word currentWord = new Word();
		RootElement root = new RootElement(ksNode);
		final List<Word> Words = new ArrayList<Word>();
		Element item = root.getChild(ksWord);

		// get word properties
		item.setStartElementListener(new StartElementListener()
		{

			@Override
			public void start(Attributes attributes)
			{
				// set word value
				currentWord.setWordValue(attributes.getValue(ksValue));

				// set word language
				currentWord.setLang(attributes.getValue(ksLang));

				// set word class
				currentWord.setWordClass(attributes.getValue(ksClass));
			}
		});

		// Add word into list when finish parse
		item.setEndElementListener(new EndElementListener()
		{
			public void end()
			{
				Words.add(currentWord.copy());
				currentWord.clear();
			}
		});

		// set translation property
		item.getChild(ksTranslation).setStartElementListener(
		        new StartElementListener()
		        {
			        // set translation value
			        @Override
			        public void start(Attributes attributes)
			        {
				        currentWord.addTranslation(attributes.getValue(ksValue));
			        }
		        });

		// set phonetic property
		item.getChild(ksPhonetic).setStartElementListener(
		        new StartElementListener()
		        {

			        @Override
			        public void start(Attributes attributes)
			        {
				        // set phonetic value
				        currentWord.setPhoneticValue(attributes
				                .getValue(ksValue));

				        // set phonetic file
				        currentWord.setPhoneticSoundFile(attributes
				                .getValue(ksSoundFile));

			        }
		        });
		
		// set paradigm
		item.getChild(ksParadigm).getChild(ksInflection)
		        .setStartElementListener(new StartElementListener()
		        {

			        @Override
			        public void start(Attributes attributes)
			        {
				        currentWord.addParadigms(attributes
				                .getValue(ksValue));

			        }
		        });

		// set synonym
		item.getChild(ksSynonym).setStartElementListener(
		        new StartElementListener()
		        {

			        @Override
			        public void start(Attributes attributes)
			        {
				        currentWord.setSynonym(attributes.getValue(ksValue));
			        }
		        });

		// set example original
		item.getChild(ksExample).setStartElementListener(new StartElementListener()
		{
			
			@Override
			public void start(Attributes AAttributes)
			{
				Example example = new Example(AAttributes.getValue(ksValue));
				currentWord.addExample(example);
			}
		});
		
		// set example translation
		item.getChild(ksExample).getChild(ksTranslation).setStartElementListener(new StartElementListener()
		{
			
			@Override
			public void start(Attributes AAttributes)
			{
				// get the last example in the list
				Example example = currentWord.getExampleList().get(currentWord.getExampleList().size() - 1);
				
				// update the last example translation value
				example.setTranslationExample(AAttributes.getValue(ksValue));
				
			}
		});

		try
		{
			
			Xml.parse(correctXmlFormat(sAXml), root.getContentHandler());
			// Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8,
			// root.getContentHandler());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return Words;
	}
	
	private String correctXmlFormat(String sARawXml)
    {
	    String result = "<node>" + sARawXml + "</node>";
	    result = result.replace(",", "");
	    result = result.replace("\\\"", "\"");
	    
	    return result;
    }
}

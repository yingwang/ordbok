package com.googlecode.ordbok3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.googlecode.ordbok3.feedParser.FeedParser;
import com.googlecode.ordbok3.log.OrdbokLog;
import com.googlecode.ordbok3.Word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainWindow extends Activity implements OnClickListener {

	ImageButton buttonOK;
	ImageButton buttonClear;
	//Button buttonUttal;
	EditText editTextOrd;
	TextView textViewText;
	String soundFile;
	final String LOG_TAG = this.getClass().getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// initialize ordbokLog
		OrdbokLog.initialize(this);
		
		setContentView(R.layout.main);
		editTextOrd = (EditText) findViewById(R.id.EditTextOrd);
		buttonOK = (ImageButton) findViewById(R.id.ButtonOK);
		buttonClear = (ImageButton) findViewById(R.id.ButtonClear);
		//buttonUttal = (Button) findViewById(R.id.ButtonUttal);
		
		textViewText = (TextView) findViewById(R.id.TextViewText);
		textViewText.setMovementMethod(new ScrollingMovementMethod());
		//webViewText = (WebView) findViewById(R.id.WebViewText);
		buttonOK.setOnClickListener(this);
		buttonClear.setOnClickListener(this);

		//buttonUttal.setOnClickListener(this);
		//buttonUttal.setEnabled(false);

	}
	
	@Override
	protected void onDestroy() 
	{
		OrdbokLog.uninitialize();
		super.onDestroy();
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.itemExit:
	        this.finish();
	        return true;
	    case R.id.itemAbout:
	        Intent i=new Intent(this,About.class);
	        startActivity(i);
	        return true;
	    }
		return false;
	}

	
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ButtonOK:
				editTextOrd.clearComposingText();
				buttonOK.setEnabled(false);
				LookUpWord();
				buttonOK.setEnabled(true);
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(textViewText.getWindowToken(), 0);
				break;
			case R.id.ButtonClear:
				// test code 
				editTextOrd.clearComposingText();
				editTextOrd.getText().clear();
				break;
			default:
				break;
		}

		
	}
	
	String SafeSubString(String s,int start,int end)
	{
		if (start<end)	s=s.substring(start, end);
		return s;
	}


	void LookUpWord() {
		try {
			URL url;
			URLConnection urlConn;
			DataOutputStream dos;
			DataInputStream dis;

			String ord = editTextOrd.getText().toString();
			
			url =new URL("http://folkets-lexikon.csc.kth.se/folkets/folkets/lookupword");
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);	
			urlConn.setRequestProperty("Content-Type","text/x-gwt-rpc; charset=utf-8");		
			//urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			dos = new DataOutputStream(urlConn.getOutputStream());
			
			//Log.e(LOG_TAG, ord);
			ord = ord.replace("ä","Ã¤");//�?		" +
//			char c='£'+1;
//			ord = ord.replaceAll("ä",""+c);//�?	
			ord = ord.replace("å","Ã¥");
			ord = ord.replace("ö","Ã¶");
			String message = "5|0|7|http://folkets-lexikon.csc.kth.se/folkets/folkets/|C42B706916C52B6E655EB5B8852CDE12|se.algoritmica.folkets.client.LookUpService|lookUpWord|se.algoritmica.folkets.client.LookUpRequest|se.algoritmica.folkets.client.LookUpRequest/1089007912|" +
				ord +"|1|2|3|4|1|5|6|1|0|0|7|";
			dos.writeBytes(message);
			dos.flush();
			dos.close();

			dis = new DataInputStream(urlConn.getInputStream());
			String s = "";
			String content = "";

			while ((s = dis.readLine()) != null) {
				
		        content+=s;
			}
		    //System.out.println(content);
			OrdbokLog.i(LOG_TAG, "raw content" + content);
			
			
			if ((!content.contains("<"))||(!content.contains(">"))) 
			{
				textViewText.setText("Sorry, word not found!");
				return;
			}
			content = content.substring(content.indexOf("<"),content.lastIndexOf(">")+1);
			
			OrdbokLog.i(LOG_TAG, "xml content" + content);
			
			FeedParser parser = new FeedParser();
			parser.parse(content);

			
			content = content.replace("&amp;quot;", "");
			content = content.replace("origin=lexin", "");
			content = content.replaceAll("date=(\\S{12})", "");
			
			content = content.replace("&amp", "&");
			content = content.replace("&lt", "[");
			content = content.replace("&gt", "]");
			content = content.replace("&quot", "\"");
			content = content.replace("Ã¤", "ä");//&aring;
			content = content.replace("Ã¥", "å");//�?		
			content = content.replace("Ã¶", "ö");
			//content = content.replaceAll("�?, "�?");//�?		
			content = content.replace("\\\"", "");
			content = content.replace("&;#39;", "'");
			content = content.replace(">", ">\n");
			content = content.replace("+", "_");
			content = content.replace("comment=", " -- ");
			content = content.replace("origin=lexin", "");
			
			
			//Log.e(LOG_TAG,content);
			ArrayList<Word> words = new ArrayList<Word>();
			Word word=new Word();
			
			
			String strContents[] = content.split("\n");
			
			// print the final formated content to log
			for (String line : strContents)
            {
	            OrdbokLog.i(LOG_TAG, line);
            }
			
			
			for (int i=0;i<strContents.length;i++)
			{
				//System.out.println(i+": "+strContents[i]);
				try{
				if (strContents[i].contains("<word value"))
				{
					word.setWordValue(strContents[i].substring(strContents[i].indexOf("value=")+6,strContents[i].indexOf("lang=")-1));
					word.setLang(strContents[i].substring(strContents[i].indexOf("lang=")+5,strContents[i].indexOf("class=")-1));
					word.setWordClass(strContents[i].substring(strContents[i].indexOf("class=")+6,strContents[i].indexOf("id=")-1));
				} else if (strContents[i].contains("</word"))
				{
					words.add(word);
					word = new Word();
				}
				else if (strContents[i].contains("<translation value"))
				{
					if (strContents[i-1].contains("word")){
						word.setWordContent("<h3><font color='black'><b>");
						word.setWordContent("<l>"+strContents[i].substring(strContents[i].indexOf("value=")+6,strContents[i].indexOf(">"))+"</l>");
						word.setWordContent("</b></font></h3>");
					} else {
						word.setWordContent("<l>"+strContents[i].substring(strContents[i].indexOf("value=")+6,strContents[i].indexOf(">"))+"</l>");
						word.setWordContent("<br /><br />");
					}
				} else if (strContents[i].contains("<phonetic value"))
				{
					word.setPhoneticValue(strContents[i].substring(strContents[i].indexOf("value=")+6,strContents[i].indexOf("soundFile=")-1));
					word.setPhoneticSoundFile(SafeSubString(strContents[i],strContents[i].indexOf("soundFile=")+10,strContents[i].indexOf(">")));
					if (!word.getPhoneticSoundFile().trim().equalsIgnoreCase("")) 
					{
						soundFile = word.getPhoneticSoundFile().trim();
						//buttonUttal.setEnabled(true);
					}
				} else if (strContents[i].contains("<inflection value"))
				{
					word.setWordContent(SafeSubString(strContents[i],strContents[i].indexOf("value=")+6,strContents[i].indexOf(">")));
					word.setWordContent(" ");
				} else if (strContents[i].contains("<example"))
				{
					if (!strContents[i-1].contains("example")) word.setWordContent("<font color='green'><b>Examples</b></font><br />");
					word.setWordContent("<b>"+SafeSubString(strContents[i],strContents[i].indexOf("value=")+6,strContents[i].indexOf(">"))+"</b>");
					word.setWordContent("; ");
				} else if (strContents[i].contains("<compound"))
				{
					if (!strContents[i-1].contains("compound")) word.setWordContent("<font color='yellow'><b>Compound</b></font><br />");
					word.setWordContent(SafeSubString(strContents[i],strContents[i].indexOf("value=")+6,strContents[i].indexOf(">")));
					word.setWordContent("; ");
				} else if (strContents[i].contains("<idiom"))
				{
					if (!strContents[i-1].contains("idiom")) word.setWordContent("<font color='purple'><b>Idiom</b></font><br />");
					word.setWordContent("<b>"+SafeSubString(strContents[i],strContents[i].indexOf("value=")+6,strContents[i].indexOf(">"))+"</b>");
					word.setWordContent("; ");
				}else if (strContents[i].contains("/paradigm"))
				{
					word.setWordContent("<br />");
				} else if (strContents[i].contains("</inflection>"))
				{
					word.setWordContent(" ");
				} else if (strContents[i].contains("<inflection value"))
				{
					if (!strContents[i-1].contains("inflection")) word.setWordContent("<font color='red'><b>Inflection</b></font><br />");
					word.setWordContent(SafeSubString(strContents[i],strContents[i].indexOf("value=")+6,strContents[i].indexOf(">")));
					word.setWordContent(" ");
				} else if (strContents[i].contains("<synonym value"))
				{
					if (!strContents[i-1].contains("synonym")) word.setWordContent("<font color='red'><b>Synonym</b></font><br />");
					word.setWordContent(SafeSubString(strContents[i],strContents[i].indexOf("value=")+6,strContents[i].indexOf("level=")-1));
					word.setWordContent(" ");
				} else if (strContents[i].contains("</synonym"))
				{
					if (!strContents[i+1].contains("synonym")) word.setWordContent("<br />");
				}
				}
				catch(Exception e){
					Log.e(LOG_TAG,strContents[i]+ e.getMessage());
				}
			}
			
			String allWords = "";
			for (int j=0;j<words.size();j++)
			{
				
				allWords =allWords+words.get(j).toString()+"<font color='white'> --------------------------------------- </font><br />";
			}
			allWords = allWords.replace("<br /><br />", "<br />");
			//Log.i("i ",allWords);
			//textViewText.setText(allWords);
			
			if (content.trim().equals("")) 
                textViewText.setText("Sorry, the word is not found.");
			else textViewText.setText(Html.fromHtml(allWords), TextView.BufferType.SPANNABLE);
			
		    dis.close(); 
		  } // end of "try"
		  catch (MalformedURLException mue) { 
		  } 
		  catch (IOException ioe) { 
		  }

		}  // end of postNewItem() method 
	
	}
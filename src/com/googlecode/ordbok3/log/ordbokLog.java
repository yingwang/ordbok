package com.googlecode.ordbok3.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.ordbok3.R;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class OrdbokLog
{
	// log level enum
	private static final int ERROR = 0;
	private static final int DEBUG = 1;
	private static final int INFO = 2;

	// log to device enum
	private static final int NOT_ON_DEVICE = 0;
	private static final int ON_PHONE_MEMORY = 1;
	private static final int ON_SD_CARD = 2;

	// time format
	private static final String ksTimeFormatPatternForFileName = "yyyy_MM_dd_HH_mm";
	private static final String ksTimeFormatPatternForLogContent = "HH:mm:ss.SSSZ";

	// log pattern
	private static final String ksLogFileNamePattern = "ordbok_log%s.txt";
	private static final String ksLogFileSDCardPathPattern = "%s/Android/data/com.googlecode.ordboks/files/log";
	private static final String ksLogFileMemoryPathPattern = "log";
	private static final String ksLogContentPattern = "%s-<%s>, %s\n";

	// sContext for access resource
	private static Context sContext;

	private static int snLogLevel;
	private static int snLogToDevice;

	private static FileWriter sFile = null;
	
	
	public static void initialize(Context sAContext)
	{
		sContext = sAContext;
		snLogLevel = sContext.getResources().getInteger(R.integer.logLevel);
		snLogToDevice = sContext.getResources().getInteger(
		        R.integer.logToDevice);
		createLogFile();
	}

	public static void uninitialize()
	{
		if (sFile != null)
		{
			try
			{
				sFile.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// log for debug
	public static int d(String sALogTag, String sALogMsg)
	{

		// try to write log to device
		writeLogToDevice(DEBUG, sALogTag, sALogMsg);
		// log to android log
		return Log.d(sALogTag, sALogMsg);
	}

	// log for error
	public static int e(String sALogTag, String sALogMsg)
	{
		// try to write log to device
		writeLogToDevice(ERROR, sALogTag, sALogMsg);

		// log to android log
		return Log.e(sALogTag, sALogMsg);
	}

	// log for information
	public static int i(String sALogTag, String sALogMsg)
	{
		// try to write log to device
		writeLogToDevice(INFO, sALogTag, sALogMsg);

		// log to android log
		return Log.i(sALogTag, sALogMsg);
	}

	private static boolean createLogFile()
	{
		boolean bResult = true;

		// point out the path of the folder which contains log
		File dir = null;

		// create the log file depends on the appConfig
		switch (snLogToDevice)
		{
		case NOT_ON_DEVICE:
			return false;
		case ON_PHONE_MEMORY:
			dir = sContext.getDir(ksLogFileMemoryPathPattern,
			        Context.MODE_PRIVATE);
			break;
		case ON_SD_CARD:
			dir = createSDCardFilePath();
			break;

		default:
			break;
		}

		// create log file named with time
		SimpleDateFormat s = new SimpleDateFormat(
		        ksTimeFormatPatternForFileName);
		String sTimeString = s.format(new Date());
		if (dir != null)
		{
			File file = new File(dir, String.format(ksLogFileNamePattern,
			        sTimeString));
			try
			{
				sFile = new FileWriter(file);
			} catch (IOException e)
			{
				e.printStackTrace();
				bResult = false;
			}
		}

		return bResult;
	}

	private static File createSDCardFilePath()
    {
		File dir = null;

		String state = Environment.getExternalStorageState();
		// We can read and write the media, then create log on sd card
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			File sdCard = Environment.getExternalStorageDirectory();
			dir = new File(String.format(ksLogFileSDCardPathPattern,
			        sdCard.getAbsolutePath()));

			dir.mkdirs();


		}
		else // create log in phone memory
		{
			Log.e("ordbokLog", "can not create the log on sd card, create it on phone memory instead.");
			dir = sContext.getDir(ksLogFileMemoryPathPattern,
			        Context.MODE_PRIVATE);
		}
	    return dir;
	    
    }

	private static boolean writeLogToDevice(int nALogLevel, String sALogTag,
	        String sALogMsg)
	{
		boolean bResut = false;

		// if file is created for log, and the log level is correct
		if (sFile != null && nALogLevel <= snLogLevel)
		{
			// get time
			SimpleDateFormat s = new SimpleDateFormat(
			        ksTimeFormatPatternForLogContent);
			String sTimeString = s.format(new Date());

			// format the log string
			String log = String.format(ksLogContentPattern, sTimeString,
			        sALogTag, sALogMsg);
			try
			{
				sFile.write(log);
				bResut = true;
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		return bResut;
	}

}

package mobile.teamwave.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.support.v4.app.FragmentActivity;

import mobile.teamwave.pm_activity.PM_MainActivity;

public class ConvertDatetoTimeZone {
	String resultTym;
	public ConvertDatetoTimeZone(FragmentActivity fragmentActivity) {
	}

	public String convertDateToTimeZone(String date) {
		// you should remove "Z" then it will come correct
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date value = null;
		try {
			value = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		dateFormatter.setTimeZone(TimeZone.getDefault());
		String dt = dateFormatter.format(value);
		return dt;
	}

	public String convertTimeToUtcZone(String tym) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"hh:mm a");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date value = null;
		try {
			value = formatter.parse(tym);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"hh:mm a");
		dateFormatter.setTimeZone(TimeZone.getDefault());
		 tym = dateFormatter.format(value);
		return tym;
	}
}

package it.gps.tracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class SmsReceivedActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String mittente = intent.getStringExtra(TextMessageReceiver.MITTENTE_SMS);
		String message = intent.getStringExtra(TextMessageReceiver.TESTO_SMS);

		String latitudine = null, longitudine = null;
		Pattern patternLat = Pattern.compile("lat:\\s*(\\d+\\.\\d+).*?long:\\s*(\\d+\\.\\d+)");
		Matcher matcherLat = patternLat.matcher(message);
		if (matcherLat.find()) {
			latitudine = matcherLat.group(1);
			longitudine = matcherLat.group(2);
			String urlMap = "http://maps.google.com/maps?q="+latitudine+","+longitudine+"&hl=it";
			AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
			miaAlert.setTitle(mittente);
			miaAlert.setMessage(urlMap);

			miaAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
					SmsReceivedActivity.this.finish();
				}  
			});

			/*miaAlert.setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;   
					}
				});*/
			AlertDialog alert = miaAlert.create();
			alert.show();
		}

	}

}

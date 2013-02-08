package it.gps.tracker;
import it.gps.tracker.db.DbManager;
import it.gps.tracker.db.dao.Dispositivo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class TextMessageReceiver extends BroadcastReceiver{
	public final static String TESTO_SMS = "it.gps.tracker.SMS_BODY";
	public final static String MITTENTE_SMS = "it.gps.tracker.SMS_FROM";
	private DbManager dbManager;

	public void onReceive(Context context, Intent intent)
	{
		Bundle pudsBundle = intent.getExtras();
		Object[] pdus = (Object[]) pudsBundle.get("pdus");
		SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);    
		String mittente = messages.getOriginatingAddress();
		dbManager = new DbManager(context);
		Dispositivo disp = dbManager.getDispositivoByNumTelefono(mittente);
		if (disp!=null) {
			String body = messages.getMessageBody();
			if (body.contains("lat:") && body.contains("long:")) {
				Intent intentOpenActivity = new Intent(context, SmsReceivedActivity.class);
				intentOpenActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentOpenActivity.putExtra(MITTENTE_SMS, mittente);
				intentOpenActivity.putExtra(TESTO_SMS, body);
				context.startActivity(intentOpenActivity);
			}
		}
	}
}
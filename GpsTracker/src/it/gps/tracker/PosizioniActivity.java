package it.gps.tracker;

import it.gps.tracker.db.DbManager;
import it.gps.tracker.db.PosizioneCursorAdapter;
import it.gps.tracker.db.dao.Dispositivo;
import it.gps.tracker.db.dao.Posizione;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PosizioniActivity extends ListActivity
{

	private DbManager dbManager;
	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_posizioni);
		setTitle("Posizioni GPS");
		dbManager = new DbManager(this);
		Dispositivo dispSelezionato = dbManager.getDispostivoSelezionato();
		if (dispSelezionato!=null) {
			aggiornaPosizioniDaSms();
			Cursor c = dbManager.getPosizioniByIdDispositivo(dispSelezionato.get_id());
			startManagingCursor(c);
			setListAdapter(new PosizioneCursorAdapter(this, c));
			if (c.getCount()>0) {
				Toast.makeText(this, "Trovate "+c.getCount()+" nuove posizioni", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this, "Nessuna nuova posizione rilevata", Toast.LENGTH_LONG).show();
			}

			listView = getListView();
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
						long id) {
					onLongListItemClick(listView,v,pos,id);
					//indica che l'evento è stato consumato e quindi non deve propagarsi su onListItemClick 
					return true;
				}
			});
		}else{
			Intent intent = new Intent(this, AddEditDispositivoActivity.class);
			startActivity(intent);
		}

	}

	// You then create your handler method:
	protected void onLongListItemClick(ListView l, View v, int position, long id) {
		// Log.i( TAG, "onLongListItemClick id=" + id );
		Toast.makeText(this, "hai cliccato a lungo su "+id, Toast.LENGTH_LONG).show();
	} 

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbManager.close();
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Posizione pos = dbManager.getPosizioneById((int)id);

		//https://maps.google.it/maps?q=41.082974,16.773949&hl=it&num=1&t=m&z=11
		String urlMap = "http://maps.google.com/maps?q="+pos.getLatitudine()+","+pos.getLongitudine()+"&hl=it";
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse(urlMap));
		startActivity(intent);

		//Toast.makeText(this, pos.getLatitudine()+" "+pos.getLongitudine(), Toast.LENGTH_LONG).show();

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_posizioni, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.aggiorna_posizioni:
			aggiornaPosizioniDaSms();
			return true;
		case R.id.gestisci_dispositivi:
			Intent intent = new Intent(this, ManagerDispositiviActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.aggiorna_posizioni).setEnabled(true);
		menu.findItem(R.id.gestisci_dispositivi).setEnabled(true);
		return true;
	}

	public void aggiornaPosizioniDaSms(){
		Dispositivo dispSel = dbManager.getDispostivoSelezionato();
		if (dispSel!=null) {
			String telDisp = dispSel.getNumTelefono();
			if (telDisp!=null) {
				Uri uriSMSURI = Uri.parse("content://sms/inbox");
				String columns[] = new String[] {"body", "date"}; 
				//String sortOrder = "date ASC"; 
				if (!telDisp.contains("+39")) {
					telDisp = "+39"+telDisp;
				}
				Cursor cur = getContentResolver().query(uriSMSURI, columns, "address='"+telDisp+"'", null,null);

				/*Uri uriSMSURI = Uri.parse("content://sms/inbox");
				Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,null);*/

				/*				StringBuffer info = new StringBuffer();
				for( int i = 0; i < cur.getColumnCount(); i++) {
					info.append("Column: " + cur.getColumnName(i) + "\n");
				}
				System.out.println(info.toString());*/

				while (cur.moveToNext()) {
					String testoSms = cur.getString(0);
					Posizione newPos = parseSms(testoSms);
					//System.out.println(cur.getString(1));

					if (newPos!=null) {
						Date dateFromSms = new Date(cur.getLong(1));
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss"); //16/02/2012 09:49
						String dataRil = dateFormat.format(dateFromSms);
						if(!dbManager.checkPosizioneByDataRilAndIdDisp(dataRil, dispSel.get_id())){
							newPos.setDataRilevamento(dataRil);
							newPos.setFkDispositivo(dispSel.get_id());
							dbManager.inserisciPosizione(dbManager.getWritableDatabase(), newPos);
							//getListView().invalidateViews();
						}
					}
				}
			}
		}
	}

	public Posizione parseSms(String testoSms){
		Posizione pos = null;
		if (testoSms.contains("lat:") && testoSms.contains("long:")) {
			Pattern patternLat = Pattern.compile("lat:\\s*(\\d+\\.\\d+).*?long:\\s*(\\d+\\.\\d+).*?speed:\\s*(\\d+\\.\\d+) ");
			Matcher matcherLat = patternLat.matcher(testoSms);
			if (matcherLat.find()) {
				pos = new Posizione();
				pos.setLatitudine(matcherLat.group(1));
				pos.setLongitudine(matcherLat.group(2));
				pos.setVelocita(matcherLat.group(3));
			}
		}
		return pos;

	}

}
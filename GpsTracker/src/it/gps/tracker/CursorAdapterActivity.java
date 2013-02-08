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
import android.widget.ListView;
import android.widget.Toast;

public class CursorAdapterActivity extends ListActivity
{

	private DbManager dbManager;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectable_posizioni);
		dbManager = new DbManager(this);
		Dispositivo dispSelezionato = dbManager.getDispostivoSelezionato();
		if (dispSelezionato!=null) {
			aggiornaPosizioniDaSms();
			Cursor c = dbManager.getPosizioniByIdDispositivo(dispSelezionato.get_id());
			startManagingCursor(c);
			setListAdapter(new PosizioneCursorAdapter(this, c));
			setTitle("Posizioni GPS");
			listView = getListView();
			listView.setItemsCanFocus(false);
			
			//listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}else{
			Intent intent = new Intent(this, AddEditDispositivoActivity.class);
			startActivity(intent);
		}

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbManager.close();
	}
	
	/*
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

	}*/
	

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
	
	/**
	 * Show a message giving the selected item IDs. There seems to be a bug with
	 * ListView#getCheckItemIds() on Android 1.6 at least @see
	 * http://code.google.com/p/android/issues/detail?id=6609
	 */
	private void showSelectedItemIds() {
		final StringBuffer sb = new StringBuffer("Selection: ");

		// Get an array that contains the IDs of the list items that are checked
		// --
		final long[] checkedItemIds = listView.getCheckItemIds();
		if (checkedItemIds == null) {
			Toast.makeText(this, "Nessun elemento selezionato", Toast.LENGTH_LONG).show();
			return;
		}

		// For each ID in the status array
		// --
		boolean isFirstSelected = true;
		final int checkedItemsCount = checkedItemIds.length;
		for (int i = 0; i < checkedItemsCount; ++i) {
			if (!isFirstSelected) {
				sb.append(", ");
			}
			sb.append(checkedItemIds[i]);
			isFirstSelected = false;
		}

		// Show a message with the country IDs that are selected
		// --
		Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
	}


}
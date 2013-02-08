package it.gps.tracker;

import it.gps.tracker.db.DbManager;
import it.gps.tracker.db.DispositivoCursorAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class ManagerDispositiviActivity extends ListActivity
{

	public static final String ID_DISP_TO_EDIT = "id_dispositivo_da_modificare";
	private DbManager dbManager;
	private ListView listView;
	private Cursor c;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("Scegliere il dispositivo GPS");
		dbManager = new DbManager(this);
		c = dbManager.getAllDispositivo();
		startManagingCursor(c);
		setListAdapter(new DispositivoCursorAdapter(this, c));
		listView = getListView();
		registerForContextMenu(listView);

		/*listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {
				onLongListItemClick(listView,v,pos,id);
				//indica che l'evento è stato consumato e quindi non deve propagarsi su onListItemClick 
				return true;
			}
		});*/

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
		dbManager.setDispositivoSelezionato(dbManager.getWritableDatabase(), (int)id);
		Intent intent = new Intent(this, PosizioniActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_dispositivo, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final int idDisp = (int)info.id;
		switch (item.getItemId()) {
		case R.id.elimina_dispositivo:
			AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
			miaAlert.setTitle("Elimina");
			miaAlert.setMessage("Il dispositivo verrà eliminato");
			miaAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
					if (dbManager.eliminaDispositivoPosizioni(dbManager.getWritableDatabase(), idDisp)>0) {
						//Toast.makeText(ManagerDispositiviActivity.this, "Dispositivo "+id+" eliminato", Toast.LENGTH_LONG).show();
						c.requery();
					}
					return;
				}  
			});
			miaAlert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
					return;
				}  
			});

			AlertDialog alert = miaAlert.create();
			alert.show();

			return true;
		case R.id.modifica_dispositivo:
			Toast.makeText(ManagerDispositiviActivity.this, "Modifica dispositivo", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, AddEditDispositivoActivity.class);
			intent.putExtra(ID_DISP_TO_EDIT, idDisp+"");
			startActivity(intent);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	protected void onLongListItemClick(ListView l, View v, int position, long id) {
		// Log.i( TAG, "onLongListItemClick id=" + id );
		Toast.makeText(this, "hai cliccato a lungo su "+id, Toast.LENGTH_LONG).show();
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_dispositivi, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.aggiungi_dispositivo:
			Intent intent = new Intent(this, AddEditDispositivoActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.aggiungi_dispositivo).setEnabled(true);
		return true;
	}


}
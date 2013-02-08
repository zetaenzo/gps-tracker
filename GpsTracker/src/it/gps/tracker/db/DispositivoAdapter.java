package it.gps.tracker.db;

import it.gps.tracker.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DispositivoAdapter extends CursorAdapter{

	public DispositivoAdapter(Context context, Cursor c) {
		super(context, c);
	}

	//layout di ciascuna riga
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.row_dispositivo, null);
	}

	//dati da associare alla riga prelevati dal cursor
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView) view.findViewById(R.id.nome)).setText(cursor.getString(cursor.getColumnIndex(TabellaDispositivo.NOME)));
		((TextView) view.findViewById(R.id.descrizione)).setText(cursor.getString(cursor.getColumnIndex(TabellaDispositivo.DESCRIZIONE)));
		((TextView) view.findViewById(R.id.numTelefono)).setText(cursor.getString(cursor.getColumnIndex(TabellaDispositivo.NUM_TELEFONO)));
	}


}

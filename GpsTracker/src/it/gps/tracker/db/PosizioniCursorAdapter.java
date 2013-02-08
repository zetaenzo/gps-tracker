package it.gps.tracker.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PosizioniCursorAdapter extends CursorAdapter
{
	public PosizioniCursorAdapter(Context context, Cursor c)
	{
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		String latitudine = cursor.getString(cursor.getColumnIndex(TabellaPosizione.LATITUDINE));
		String longitudine = cursor.getString(cursor.getColumnIndex(TabellaPosizione.LONGITUDINE));
		String descrizione = cursor.getString(cursor.getColumnIndex(TabellaPosizione.LUOGO));
		((TextView) view.findViewById(android.R.id.text1)).setText("lat: "+latitudine+"\nlong: "+longitudine);
		((TextView) view.findViewById(android.R.id.text2)).setText(descrizione);
	}
}
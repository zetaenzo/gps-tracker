package it.gps.tracker.db;

import it.gps.tracker.R;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class PosizioneCursorAdapter extends SimpleCursorAdapter
{
	private Context context;

	public PosizioneCursorAdapter(Context context, Cursor c)
	{
		super(context, 
				R.layout.row_posizione, 
				c, 
				new String[]{TabellaPosizione.DATA_RILEVAMENTO, TabellaPosizione.LATITUDINE, TabellaPosizione.LONGITUDINE, TabellaPosizione.LUOGO }, 
				new int[]{R.id.data, R.id.latitudine, R.id.longitudine, R.id.descrizione });
		this.context = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);
		/*ImageView logo = (ImageView) view.findViewById(R.id.logo);
		String nomeRegione = cursor.getString(cursor.getColumnIndex("nomeRegione"));
		String nomeImmagine = nomeRegione.toLowerCase().replace(' ', '_').replace('\'', '_') + ".png";
		Bitmap bitmap = readBitmap(nomeImmagine);
		logo.setImageBitmap(bitmap);*/
		
	}
	

	private Bitmap readBitmap(String nomeImmagine)
	{
		InputStream is = null;
		try
		{
			is = context.getAssets().open(nomeImmagine);
			return BitmapFactory.decodeStream(is);
		}
		catch (IOException e)
		{
			return null;
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException ignored)
				{
				}
			}
		}
	}
}
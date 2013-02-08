package it.gps.tracker.db;

import android.provider.BaseColumns;

public interface TabellaDispositivo extends BaseColumns{

		String TABLE_NAME = "dispositivo";
	 
		String NOME = "nome";
		String DESCRIZIONE = "descrizione";
		String NUM_TELEFONO = "num_telefono";
		String SELEZIONATO = "selezionato";
		
		String[] COLUMNS = new String[]	{ _ID, NOME, DESCRIZIONE, NUM_TELEFONO, SELEZIONATO};
}

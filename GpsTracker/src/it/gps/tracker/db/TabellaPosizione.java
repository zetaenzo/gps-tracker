package it.gps.tracker.db;

import android.provider.BaseColumns;

public interface TabellaPosizione extends BaseColumns{

		String TABLE_NAME = "posizione";
	 
		String DATA_RILEVAMENTO = "data_rilevamento";
		String LATITUDINE = "latitudine";
		String LONGITUDINE = "longitudine";
		
		String LUOGO = "luogo";
		
		String VELOCITA = "velocita";
		String ALTRO = "altro";
		
		String FK_DISPOSITIVO = "fk_dispositivo";
		
		String[] COLUMNS = new String[]	{ _ID, DATA_RILEVAMENTO, LATITUDINE, LONGITUDINE, LUOGO, VELOCITA, ALTRO, FK_DISPOSITIVO};
}

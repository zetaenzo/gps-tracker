package it.gps.tracker.db;

import it.gps.tracker.db.dao.Dispositivo;
import it.gps.tracker.db.dao.Posizione;

import java.text.MessageFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "it.gps.traker.db";

	private static final int SCHEMA_VERSION = 1;

	public DbManager(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//TABELLA: DISPOSITIVO
		String sqlDispositivo = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT," + 
				" {2} TEXT NOT NULL,{3} TEXT,{4} TEXT, {5} INTEGER);";
		db.execSQL(MessageFormat.format(sqlDispositivo, 
				TabellaDispositivo.TABLE_NAME, 
				TabellaDispositivo._ID,
				TabellaDispositivo.NOME,
				TabellaDispositivo.DESCRIZIONE,
				TabellaDispositivo.NUM_TELEFONO,
				TabellaDispositivo.SELEZIONATO));


		//TABELLA: POSIZIONE
		String sqlPosizione = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT," + 
				" {2} TEXT NOT NULL, {3} TEXT, {4} TEXT, {5} TEXT, {6} TEXT, {7} TEXT, {8} INTEGER," +
				"FOREIGN KEY({8}) REFERENCES "+TabellaDispositivo.TABLE_NAME+"("+TabellaDispositivo._ID+"));";
		db.execSQL(MessageFormat.format(sqlPosizione, 
				TabellaPosizione.TABLE_NAME,//0 
				TabellaPosizione._ID,//1
				TabellaPosizione.DATA_RILEVAMENTO,//2
				TabellaPosizione.ALTRO,//3
				TabellaPosizione.LATITUDINE,//4 
				TabellaPosizione.LONGITUDINE,//5
				TabellaPosizione.LUOGO,//6
				TabellaPosizione.VELOCITA,//7
				TabellaPosizione.FK_DISPOSITIVO));//8
		String indexDataRil = "CREATE UNIQUE INDEX DATA_RIL_INDEX ON "+TabellaPosizione.TABLE_NAME+
				" ("+TabellaPosizione.DATA_RILEVAMENTO+");";
		db.execSQL(indexDataRil);

		//https://maps.google.it/maps?q=41.082974,16.773949&hl=it&num=1&t=m&z=11
		/*int idDisp1 = (int)inserisciDispositivo(db, "disp1", "descrizione disp1", "3205687541", 0);
		int idDisp2 = (int)inserisciDispositivo(db, "disp2", "descrizione disp2", "3205687542", 0);
		int idDisp3 = (int)inserisciDispositivo(db, "disp3", "descrizione disp3", "3205687543", 0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //16/02/2012 09:49
		//dateFormat.format(new Date());
		inserisciPosizione(db, "16/02/2012 04:54","41.082974", "16.773949", "bari", "0", "", idDisp1);
		inserisciPosizione(db, "16/02/2012 05:51","51.6454", "64.554", "giovinazzo", "10", "", idDisp1);
		inserisciPosizione(db, "16/02/2012 06:49","13.7454", "54.654", "mola", "100", "", idDisp1);
		inserisciPosizione(db, "16/02/2012 07:50","43.8454", "34.754", "taranto", "80", "", idDisp1);
		inserisciPosizione(db, "16/02/2012 08:51","56.9454", "24.854", "vicenza", "60", "", idDisp1);
		inserisciPosizione(db, "16/02/2012 09:23","73.5454", "14.954", "milano", "50", "", idDisp1);

		inserisciPosizione(db, "16/03/2012 04:54","53.5454", "34.454", "bari", "0", "", idDisp2);
		inserisciPosizione(db, "16/04/2012 05:51","51.6454", "64.554", "giovinazzo", "10", "", idDisp2);
		inserisciPosizione(db, "16/05/2012 06:49","13.7454", "54.654", "mola", "100", "", idDisp2);
		inserisciPosizione(db, "16/06/2012 07:50","43.8454", "34.754", "taranto", "80", "", idDisp2);
		inserisciPosizione(db, "16/07/2012 08:51","56.9454", "24.854", "vicenza", "60", "", idDisp2);
		inserisciPosizione(db, "16/08/2012 09:23","73.5454", "14.954", "milano", "50", "", idDisp3);
		 */
		//inserisciCoordinata(db, "47.432", "10.2234",dateFormat.format(new Date()), "via bari","","1");

		/*inserisciCoordinate(db, "48.432", "10.2234", "molfetta","100","");
		inserisciCoordinate(db, "49.432", "10.2234", "giovinazzo","0","");
		inserisciCoordinate(db, "57.432", "10.2234", "bari","10","");*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public long inserisciDispositivo(SQLiteDatabase db, String nome, String descrizione, String numTelefono, int selezionato) {
		ContentValues v = new ContentValues();
		v.put(TabellaDispositivo.NOME, nome);
		v.put(TabellaDispositivo.DESCRIZIONE, descrizione);
		v.put(TabellaDispositivo.NUM_TELEFONO, numTelefono);
		v.put(TabellaDispositivo.SELEZIONATO, selezionato);
		return db.insert(TabellaDispositivo.TABLE_NAME, null, v);
	}
	
	public long modificaDispositivo(SQLiteDatabase db, int idDisp, String nome, String descrizione, String numTelefono, int selezionato) {
		ContentValues v = new ContentValues();
		v.put(TabellaDispositivo.NOME, nome);
		v.put(TabellaDispositivo.DESCRIZIONE, descrizione);
		v.put(TabellaDispositivo.NUM_TELEFONO, numTelefono);
		v.put(TabellaDispositivo.SELEZIONATO, selezionato);
		return db.update(TabellaDispositivo.TABLE_NAME, v, TabellaDispositivo._ID+"="+idDisp, null);
	}

	public long eliminaDispositivoPosizioni(SQLiteDatabase db, int id) {
		if(db.delete(TabellaDispositivo.TABLE_NAME, TabellaDispositivo._ID+"="+id, null)==1){
			return deletePosizioniByIdDisp(db, id);
		}
		return -1;
	}

	public long deletePosizioniByIdDisp(SQLiteDatabase db, int idDisp){
		return db.delete(TabellaPosizione.TABLE_NAME, TabellaPosizione.FK_DISPOSITIVO+"="+idDisp, null);
	}

	public long inserisciPosizione(SQLiteDatabase db, String dataRilevamento, String latitudine, 
			String longitudine, String luogo, String velocita, String altro, int fkDispositivo)
	{
		ContentValues v = new ContentValues();

		v.put(TabellaPosizione.DATA_RILEVAMENTO, dataRilevamento);
		v.put(TabellaPosizione.ALTRO, altro);
		v.put(TabellaPosizione.LATITUDINE, latitudine);
		v.put(TabellaPosizione.LONGITUDINE, longitudine);
		v.put(TabellaPosizione.LUOGO, luogo);
		v.put(TabellaPosizione.VELOCITA, velocita);
		v.put(TabellaPosizione.FK_DISPOSITIVO, fkDispositivo);
		return db.insert(TabellaPosizione.TABLE_NAME, null, v);
	}

	public long inserisciPosizione(SQLiteDatabase db, Posizione pos)
	{
		ContentValues v = new ContentValues();

		v.put(TabellaPosizione.DATA_RILEVAMENTO, pos.getDataRilevamento());
		v.put(TabellaPosizione.ALTRO, pos.getAltro());
		v.put(TabellaPosizione.LATITUDINE, pos.getLatitudine());
		v.put(TabellaPosizione.LONGITUDINE, pos.getLongitudine());
		v.put(TabellaPosizione.LUOGO, pos.getLuogo());
		v.put(TabellaPosizione.VELOCITA, pos.getVelocita());
		v.put(TabellaPosizione.FK_DISPOSITIVO, pos.getFkDispositivo());
		return db.insert(TabellaPosizione.TABLE_NAME, null, v);
	}

	public Cursor getAllDispositivo() {
		return (getReadableDatabase().query(
				TabellaDispositivo.TABLE_NAME, 
				TabellaDispositivo.COLUMNS,
				null, 
				null,
				null, 
				null, 
				null));
	}


	public Posizione getPosizioneById(int _id) {
		Cursor c = getReadableDatabase().query(
				TabellaPosizione.TABLE_NAME, 
				TabellaPosizione.COLUMNS, 
				TabellaPosizione._ID+"="+_id, 
				null,
				null, 
				null, 
				null);

		Posizione pos = new Posizione();
		if (c.moveToFirst()) {
			pos.set_id(c.getInt(c.getColumnIndex(TabellaPosizione._ID)));
			pos.setDataRilevamento(c.getString(c.getColumnIndex(TabellaPosizione.DATA_RILEVAMENTO)));
			pos.setLatitudine(c.getString(c.getColumnIndex(TabellaPosizione.LATITUDINE)));
			pos.setLongitudine(c.getString(c.getColumnIndex(TabellaPosizione.LONGITUDINE)));
			pos.setLuogo(c.getString(c.getColumnIndex(TabellaPosizione.LUOGO)));
			pos.setVelocita(c.getString(c.getColumnIndex(TabellaPosizione.VELOCITA)));
			pos.setAltro(c.getString(c.getColumnIndex(TabellaPosizione.ALTRO)));
			pos.setFkDispositivo(c.getInt(c.getColumnIndex(TabellaPosizione.FK_DISPOSITIVO)));
		}
		c.close();
		return pos;
	}

	public Cursor getPosizioniByIdDispositivo(int idDispositivo)
	{
		Cursor c = getReadableDatabase().query(
				TabellaPosizione.TABLE_NAME, 
				TabellaPosizione.COLUMNS,
				TabellaPosizione.FK_DISPOSITIVO+"="+idDispositivo, 
				null,
				null, 
				null, 
				TabellaPosizione.DATA_RILEVAMENTO + " desc");
		return c;
	}

	public void setDispositivoSelezionato(SQLiteDatabase db, int idDispositivo){
		//metto prima tutto a 0 
		ContentValues valueZero = new ContentValues();
		valueZero.put(TabellaDispositivo.SELEZIONATO, 0);
		db.update(TabellaDispositivo.TABLE_NAME, valueZero, null, null);
		//setto a 1 il dispositivo selezionato
		ContentValues newValue = new ContentValues();
		newValue.put(TabellaDispositivo.SELEZIONATO, 1);
		db.update(TabellaDispositivo.TABLE_NAME, 
				newValue, 
				TabellaDispositivo._ID+"="+idDispositivo, 
				null);
	}

	public Dispositivo getDispostivoSelezionato() {
		Cursor c = getReadableDatabase().query(
				TabellaDispositivo.TABLE_NAME, 
				TabellaDispositivo.COLUMNS, 
				TabellaDispositivo.SELEZIONATO+"=1", 
				null,
				null, 
				null, 
				null);
		Dispositivo disp = riempiBeanDispositivo(c);
		return disp;
	}

	public Dispositivo getDispositivoByNumTelefono(String mittente) {
		Cursor c = getReadableDatabase().query(
				TabellaDispositivo.TABLE_NAME, 
				TabellaDispositivo.COLUMNS, 
				TabellaDispositivo.NUM_TELEFONO+"='"+mittente+"'", 
				null,
				null, 
				null, 
				null);
		Dispositivo disp = riempiBeanDispositivo(c);
		return disp;
	}
	
	public Dispositivo getDispositivoById(String idDisp) {
		Cursor c = getReadableDatabase().query(
				TabellaDispositivo.TABLE_NAME, 
				TabellaDispositivo.COLUMNS, 
				TabellaDispositivo._ID+"="+idDisp, 
				null,
				null, 
				null, 
				null);
		Dispositivo disp = riempiBeanDispositivo(c);
		return disp;
	}

	private Dispositivo riempiBeanDispositivo(Cursor c){
		Dispositivo disp = null;
		if (c.moveToFirst()) {
			disp = new Dispositivo(); 
			disp.set_id(c.getInt(c.getColumnIndex(TabellaDispositivo._ID)));
			disp.setDescrizione(c.getString(c.getColumnIndex(TabellaDispositivo.DESCRIZIONE)));
			disp.setNome(c.getString(c.getColumnIndex(TabellaDispositivo.NOME)));
			disp.setNumTelefono(c.getString(c.getColumnIndex(TabellaDispositivo.NUM_TELEFONO)));
		}
		c.close();
		return disp;
	}

	public boolean checkPosizioneByDataRilAndIdDisp(String dataRil, int idDisp) {
		Cursor c = getReadableDatabase().query(
				TabellaPosizione.TABLE_NAME, 
				TabellaPosizione.COLUMNS, 
				TabellaPosizione._ID+"="+idDisp+" and "+TabellaPosizione.DATA_RILEVAMENTO+"='"+dataRil+"'", 
				null,
				null, 
				null, 
				null);

		if (c.moveToFirst()) {
			c.close();
			return true;
		}
		c.close();
		return false;
	}

}

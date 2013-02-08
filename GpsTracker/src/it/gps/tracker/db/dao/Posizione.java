package it.gps.tracker.db.dao;


public class Posizione {
	int _id;
	String table_name;
	String dataRilevamento;
	String latitudine;
	String longitudine;
	String luogo;
	String velocita;
	String altro;
	int fkDispositivo;
	
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getDataRilevamento() {
		return dataRilevamento;
	}
	public void setDataRilevamento(String dataRilevamento) {
		this.dataRilevamento = dataRilevamento;
	}
	public String getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}
	public String getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}
	public String getLuogo() {
		return luogo;
	}
	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}
	public String getVelocita() {
		return velocita;
	}
	public void setVelocita(String velocita) {
		this.velocita = velocita;
	}
	public String getAltro() {
		return altro;
	}
	public void setAltro(String altro) {
		this.altro = altro;
	}
	public int getFkDispositivo() {
		return fkDispositivo;
	}
	public void setFkDispositivo(int fkDispositivo) {
		this.fkDispositivo = fkDispositivo;
	}
	
}

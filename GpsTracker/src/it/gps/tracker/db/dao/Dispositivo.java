package it.gps.tracker.db.dao;

public class Dispositivo {
	private int _id;
	private String table_name;
	private String nome;
	private String descrizione;
	private String numTelefono;
	private int selezionato;
	
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getNumTelefono() {
		return numTelefono;
	}
	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}
	public int getSelezionato() {
		return selezionato;
	}
	public void setSelezionato(int selezionato) {
		this.selezionato = selezionato;
	}
	
}

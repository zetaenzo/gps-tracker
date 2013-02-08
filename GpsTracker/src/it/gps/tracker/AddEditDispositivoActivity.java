package it.gps.tracker;

import it.gps.tracker.db.DbManager;
import it.gps.tracker.db.dao.Dispositivo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditDispositivoActivity extends Activity implements OnClickListener{
	private String idDispToEdit;
	private DbManager dbManager = new DbManager(this);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_dispositivo);
		final Button button = (Button) findViewById(R.id.inserisci_dispositivo_button);
		Bundle bundle = getIntent().getExtras();
		button.setOnClickListener(this);
		//controllo se è una modifica: in tal caso saranno valorizzate le info del dispositivo in interfaccia
		this.idDispToEdit = bundle.getString(ManagerDispositiviActivity.ID_DISP_TO_EDIT);
		if (idDispToEdit!=null) {
			((TextView)findViewById(R.id.intro_add_edit_disp)).setText(R.string.intro_edit_disp);
			Dispositivo dispSelez = dbManager.getDispositivoById(idDispToEdit);
			((TextView)findViewById(R.id.edit_nome_dispositivo)).setText(dispSelez.getNome());
			((TextView)findViewById(R.id.edit_descrizione_dispositivo)).setText(dispSelez.getDescrizione());
			((TextView)findViewById(R.id.edit_numtel_dispositivo)).setText(dispSelez.getNumTelefono());
			button.setText(R.string.modifica);
		}
	}
	@Override
	public void onClick(View v) {
		switch ( v.getId() ) {
		case R.id.inserisci_dispositivo_button:
			final EditText editNomeDisp = (EditText)findViewById(R.id.edit_nome_dispositivo);
			final EditText editDescDisp = (EditText)findViewById(R.id.edit_descrizione_dispositivo);
			final EditText editNumTelDisp = (EditText)findViewById(R.id.edit_numtel_dispositivo);

			String nome = editNomeDisp.getText().toString();
			String descrizione = editDescDisp.getText().toString();
			String numTel = editNumTelDisp.getText().toString();

			if (nome!=null && numTel!=null && nome.length()>0 && numTel.length()>0) {
				if (numTel.matches("\\+?\\d+")) {
					int idDispIns = -1;
					
					String userMsgOk, userMsgKo;
					if (this.idDispToEdit!=null) {
						idDispIns = (int)dbManager.modificaDispositivo(dbManager.getWritableDatabase(),Integer.getInteger(this.idDispToEdit), nome, descrizione, numTel, 0);
						userMsgOk = "Dispositivo "+nome+" aggiornato correttamente";
						userMsgKo = "Errore durante l'aggiornamento del dispositivo "+nome;
					}else{
						idDispIns = (int)dbManager.inserisciDispositivo(dbManager.getWritableDatabase(), nome, descrizione, numTel, 0);
						userMsgOk = "Dispositivo "+nome+" inserito correttamente";
						userMsgKo = "Errore durante l'inserimento del dispositivo";
					}
					if (idDispIns!=-1) {
						Toast.makeText(this, userMsgOk, Toast.LENGTH_LONG).show();
						Intent intent = new Intent(this, ManagerDispositiviActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(this, userMsgKo, Toast.LENGTH_LONG).show();
						//creaAlertErrore("Errore nell'inserimento del nuovo dispositivo");
					}
				}else{
					Toast.makeText(this, "Numero di telefono non valido", Toast.LENGTH_LONG).show();
					//creaAlertErrore("Numero di telefono non valido");
				}
			}else{
				Toast.makeText(this, "I campi: nome e numero di telefono sono obbligatori", Toast.LENGTH_LONG).show();
				//creaAlertErrore("I campi: nome e numero di telefono sono obbligatori");
			}

			/*Bundle bundle = new Bundle();
			bundle.putString("nome_disp", );
			bundle.putString("desc_disp", editDescDisp.getText().toString());
			bundle.putString("num_tel_disp", editNumTelDisp.getText().toString());*/

			break;
		}
	}

	private void creaAlertErrore(String messaggio) {
		AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
		miaAlert.setTitle("Errore");
		miaAlert.setMessage(messaggio);

		miaAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				return;
			}  
		});
		AlertDialog alert = miaAlert.create();
		alert.show();
	}

}

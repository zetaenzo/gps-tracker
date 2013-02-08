package it.gps.tracker;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GpsTrackerActivity extends Activity {
	protected static final String ACTION_VIEW = "";
	/** Called when the activity is first created. */
	static TextView messageBox;
	static String ultimaPosizione;
	static Button button;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		messageBox=(TextView)findViewById(R.id.messageBox);

		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (messageBox!=null && !"".equals(messageBox.getText())){
					String urlMap = "http://maps.google.com/maps?q="+messageBox.getText()+"&hl=it";
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
							Uri.parse(urlMap));
					startActivity(intent);


				}
			}
		});

	}

	public static void updateMessageBox(String msg)
	{
		messageBox.append(msg);
	}
}
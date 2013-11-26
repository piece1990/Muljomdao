package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class SOSActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);
		
		SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);

	    EditText sosnumber1 = (EditText)findViewById(R.id.sosnumber1);
	    EditText sosnumber2 = (EditText)findViewById(R.id.sosnumber2);


	    sosnumber1.setText(pref.getString("sosnumber1", null));
	    sosnumber2.setText(pref.getString("sosnumber2", null));

	    
	}
	
	public void onStop(){
    	super.onStop();
    	SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();

    	EditText sosnumber1 = (EditText)findViewById(R.id.sosnumber1);
		EditText sosnumber2 = (EditText)findViewById(R.id.sosnumber2);

		
		editor.putString("sosnumber1", sosnumber1.getText().toString());
		editor.putString("sosnumber2", sosnumber2.getText().toString());

    	editor.commit();
    }
}

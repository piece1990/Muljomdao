package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);

	    CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
	    CheckBox check2 = (CheckBox)findViewById(R.id.checkBox2);
	    EditText settingnumber1 = (EditText)findViewById(R.id.settingnumber1);
	    EditText settingnumber2 = (EditText)findViewById(R.id.settingnumber2);
	    EditText settingnumber3 = (EditText)findViewById(R.id.settingnumber3);
	    
	    check1.setChecked(pref.getBoolean("Sound", false));
	    check2.setChecked(pref.getBoolean("Vibrate", false));
	    settingnumber1.setText(pref.getString("settingnumber1", ""));
	    settingnumber2.setText(pref.getString("settingnumber2", ""));
	    settingnumber3.setText(pref.getString("settingnumber3", ""));
	    
	}
	
	public void onStop(){
    	super.onStop();
    	SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();
    	
    	CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
    	CheckBox check2 = (CheckBox)findViewById(R.id.checkBox2);
    	editor.putBoolean("Sound", check1.isChecked());
    	editor.putBoolean("Vibrate", check2.isChecked());
    	
    	EditText settingnumber1 = (EditText)findViewById(R.id.settingnumber1);
		EditText settingnumber2 = (EditText)findViewById(R.id.settingnumber2);
		EditText settingnumber3 = (EditText)findViewById(R.id.settingnumber3);
		editor.putString("settingnumber1", settingnumber1.getText().toString());
		editor.putString("settingnumber2", settingnumber2.getText().toString());
		editor.putString("settingnumber3", settingnumber3.getText().toString());
    	
    	editor.commit();
    }
}

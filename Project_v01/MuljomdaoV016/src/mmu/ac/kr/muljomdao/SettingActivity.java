package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class SettingActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);

	    CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
	    CheckBox check2 = (CheckBox)findViewById(R.id.checkBox2);
	    check1.setChecked(pref.getBoolean("Sound", false));
	    check2.setChecked(pref.getBoolean("Vibrate", false));
	}
	
	public void onStop(){
    	super.onStop();
    	SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();

    	CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
    	CheckBox check2 = (CheckBox)findViewById(R.id.checkBox2);
    	
    	editor.putBoolean("Sound", check1.isChecked());
    	editor.putBoolean("Vibrate", check2.isChecked());
    	
    	editor.commit();
    }
}

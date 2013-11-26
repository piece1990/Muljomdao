package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);

	    CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
	    CheckBox check2 = (CheckBox)findViewById(R.id.checkBox2);
	    
	    check1.setChecked(pref.getBoolean("Sound", false));
	    check2.setChecked(pref.getBoolean("Vibrate", false));
	    
	    Button bt1 = (Button) findViewById(R.id.setbutton);

		// 버튼 클릭 이벤트 처리
		bt1.setOnClickListener(this);
		
		EditText settinget1 = (EditText) findViewById(R.id.seteditText1);
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
	
	
	
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		  // TODO Auto-generated method stub
			EditText settinget1 = (EditText) findViewById(R.id.seteditText1);
		  // 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		  switch (v.getId()) {
		  // 첫번쨰버튼 눌렷을 때
		  case R.id.setbutton:	// 제작자에게 문자를 전송한다.
			  SmsManager sms = SmsManager.getDefault();
			  sms.sendTextMessage("01094386235", null, settinget1.getText().toString(), null, null);
			  Toast.makeText(SettingActivity.this, "제작자에게 메시지가 전달 되었습니다.",
						Toast.LENGTH_LONG).show();
		  break;
	  }
	}
}

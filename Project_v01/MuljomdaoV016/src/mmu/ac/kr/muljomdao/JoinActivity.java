package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class JoinActivity extends Activity implements OnClickListener{

	EditText login_id;
	EditText login_pw;
	EditText login_repw;
	EditText login_phone;
	EditText login_age;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		// xml파일에 있는 버튼을 id값으로 불러와서 사용 
		ImageView bt1 = (ImageView) findViewById(R.id.b_join);
		// 버튼 클릭 이벤트 처리
		bt1.setOnClickListener(this);
		
		SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
		
		//login id input 
        login_id=new EditText(this);
        login_id.setHint("아이디");
        //login passwd input
        login_pw=new EditText(this);
        login_pw.setHint("패스워드");
        login_pw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //login re passwd input
        login_repw=new EditText(this);
        login_repw.setHint("패스워드 다시 입력");
        login_repw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //login phone input 
        login_phone=new EditText(this);
        login_phone.setHint("휴대폰 번호");
        //login age input 
        login_age=new EditText(this);
        login_age.setHint("나이");
        
        

	}
	public void onStop(){
    	super.onStop();
    	SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();
    
		EditText edit1 = (EditText)findViewById(R.id.editText2);	// id
		EditText edit2 = (EditText)findViewById(R.id.editText1);	// pw
		EditText edit3 = (EditText)findViewById(R.id.editText5);	// re pw
		EditText edit4 = (EditText)findViewById(R.id.editText3);	// PhoneNumber
		EditText edit5 = (EditText)findViewById(R.id.editText4);	// age
    	
		// 값을 저장한다. //
		editor.putString("JID", edit1.getText().toString());
		editor.putString("JPW", edit2.getText().toString());
		editor.putString("JREPW", edit3.getText().toString());
		editor.putString("JPH", edit4.getText().toString());
		editor.putString("JAGE", edit5.getText().toString());
    	
		
		
    	editor.commit();
    }
	
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		  // TODO Auto-generated method stub
	
		  // 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		  switch (v.getId()) {
		  // 첫번쨰버튼 눌렷을 때
		  case R.id.b_join:
		  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
		  Intent intetn1 = new Intent(this, LoginActivity.class);
		  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
		  startActivity(intetn1);

		  break;
		  }
	 }
}

/* StartActivity -> LoginActivity 로 Activity 이동의 예시
 * Intent intent = getIntent();
 * - 인텐트를 받음
 *  
 * intent.getExtras().getString("변수 명");
 * - 전달 받은 데이터를 받음
 */

package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity implements OnClickListener {
	
//	InputMethodManager imm;
	
	 /** Called when the activity is first created. */
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_login);
		
	//	 SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
		 // xml파일에 있는 버튼을 id값으로 불러와서 사용 
		 ImageView bt1 = (ImageView) findViewById(R.id.b_login);
		 ImageView bt2 = (ImageView) findViewById(R.id.b_loginjoin);
	/*	 
		 EditText edit1 = (EditText)findViewById(R.id.Loginid);
		 EditText edit2 = (EditText)findViewById(R.id.Loginpw);
		
		 edit1.setText(pref.getString("ID", ""));
		 edit2.setText(pref.getString("Password", ""));
		 
		 CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
		 check1.setChecked(pref.getBoolean("autologin", false));
	*/	 
		 // 버튼 클릭 이벤트 처리
		 bt1.setOnClickListener(this);
		 bt2.setOnClickListener(this);
		 
	//	 imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);	// 상태값을 가져온후
	}
	 
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		  // TODO Auto-generated method stub
	
		  // 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		  switch (v.getId()) {
		  // 첫번쨰버튼 눌렷을 때
		  case R.id.b_login:
		/*	  SharedPreferences pref = getSharedPreferences("Muljomdao", MODE_PRIVATE);
			  String Jstrid = pref.getString("JID", "");
			  EditText edit1 = (EditText)findViewById(R.id.Loginid);
			  String Lstrid = edit1.getText().toString();
			  String Jstrpassword = pref.getString("JPW", "");
			  EditText edit2 = (EditText)findViewById(R.id.Loginpw);
			  String Lstrpassword = edit2.getText().toString();
	*/	/*	  if(Jstrpassword != null && Jstrid != null)	// 저장한 값이 있을때
			  if(Lstrpassword != null && Lstrid != null)	// 입력한 값이 있을때
			  if(Jstrid.equals(Lstrid) == true){	// 로그인 아이디와 입력한 아이디가 둘다 같을때
				  if(Jstrpassword.equals(Lstrpassword) == true){	// 로그인과 비밀번호가 정답이다. 메뉴로 넘어간다.
					//페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
		*/			  Intent intetn1 = new Intent(this, MenuActivity.class);
					  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
					  startActivity(intetn1);
		/*		  }
				  else
					  Toast.makeText(LoginActivity.this, "아이디, 비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
			  }
			  else
				  Toast.makeText(LoginActivity.this, "아이디, 비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
		*/	  break;
		  case R.id.b_loginjoin:
			  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
			  Intent intetn2 = new Intent(this, JoinActivity.class);
			  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
			  startActivity(intetn2);
		  break;
		}
	}
	public void onStop(){
    	super.onStop();
    	SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();
    	
    	EditText edit1 = (EditText)findViewById(R.id.Loginid);
		EditText edit2 = (EditText)findViewById(R.id.editText2);
		editor.putString("ID", edit1.getText().toString());
		editor.putString("Password", edit2.getText().toString());

		CheckBox check1 = (CheckBox)findViewById(R.id.checkBox1);
    	editor.putBoolean("autologin", check1.isChecked());
    	
    	editor.commit();
    }
	
}


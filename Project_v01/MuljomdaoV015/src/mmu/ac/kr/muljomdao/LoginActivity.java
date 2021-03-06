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
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	
	 /** Called when the activity is first created. */
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_login);
		
		 SharedPreferences pref = getSharedPreferences("Muljomdao", Activity.MODE_PRIVATE);
		 // xml파일에 있는 버튼을 id값으로 불러와서 사용 
		 Button bt1 = (Button) findViewById(R.id.b_login);
		 Button bt2 = (Button) findViewById(R.id.b_loginjoin);
		 
		 EditText edit1 = (EditText)findViewById(R.id.editText1);
		 EditText edit2 = (EditText)findViewById(R.id.editText2);
		
		 edit1.setText(pref.getString("ID", ""));
		 edit2.setText(pref.getString("Password", ""));
		 
		 // 버튼 클릭 이벤트 처리
		 bt1.setOnClickListener(this);
		 bt2.setOnClickListener(this);
	}
	 
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		  // TODO Auto-generated method stub
	
		  // 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		  switch (v.getId()) {
		  // 첫번쨰버튼 눌렷을 때
		  case R.id.b_login:
		  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
		  Intent intetn1 = new Intent(this, MenuActivity.class);
		  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
		  startActivity(intetn1);
		  break;
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
    	
    	EditText edit1 = (EditText)findViewById(R.id.editText1);
		EditText edit2 = (EditText)findViewById(R.id.editText2);
		editor.putString("ID", edit1.getText().toString());
		editor.putString("Password", edit2.getText().toString());

    	editor.commit();
    }
	
}


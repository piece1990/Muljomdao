package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		// xml파일에 있는 버튼을 id값으로 불러와서 사용 , 버튼 텍스트 변경
		Button bt1 = (Button) findViewById(R.id.b_helpSend);

		// 버튼 클릭 이벤트 처리
		bt1.setOnClickListener(this);
	}
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		  // TODO Auto-generated method stub
	
		  // 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		  switch (v.getId()) {
		  // 첫번쨰버튼 눌렷을 때
		  case R.id.b_helpSend:
		/*  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
		  Intent intetn1 = new Intent(this, LoginActivity.class);
		  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
		  startActivity(intetn1);
		*/
		  break;
		  }
	 }
}

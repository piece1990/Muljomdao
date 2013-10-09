/* StartActivity -> LoginActivity 로 Activity 이동의 예시
 * Intent intent = new Intent(현재 Activity, 전환할 Activity);
 * 	- 객체 생성
 * 	 
 * 	intent.putExtra(변수 명, 데이터 값);
 * 	-  화면 전환 시 데이터 전달
 * 	 
 * 	startActivity(intent)
 * 	- Intent 실행
 */

package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;;


public class StartActivity extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		Button bt1 = (Button) findViewById(R.id.b_start);
		bt1.setOnClickListener(this);
	}
	

	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		  // TODO Auto-generated method stub
	
		  // 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		  switch (v.getId()) {
		  // 첫번쨰버튼 눌렷을 때
		  case R.id.b_start:
			  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
			  Intent intetn1 = new Intent(this, LoginActivity.class);
			  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
			  startActivity(intetn1);
	
			  break;
		  }
	  }
}
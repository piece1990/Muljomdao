package mmu.ac.kr.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// xml파일에 있는 ImageView을 id값으로 불러와서 사용 ,	
		ImageView bt1 = (ImageView) findViewById(R.id.b_mb1);
		ImageView bt2 = (ImageView) findViewById(R.id.b_mb2);
		ImageView bt3 = (ImageView) findViewById(R.id.b_mb3);
		ImageView bt4 = (ImageView) findViewById(R.id.b_mb4);
		ImageView bt5 = (ImageView) findViewById(R.id.b_mb5);

		// 버튼 클릭 이벤트 처리
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
	}
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		switch (v.getId()) {
		// 첫번쨰버튼 눌렷을 때
		case R.id.b_mb1:	// 추천코스
			  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
			  // Intent intetn1 = new Intent(this, MainActivity.class);
			  Intent intetn1 = new Intent(this, NMapViewer.class);
			  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
			  startActivity(intetn1);
			  break;
		case R.id.b_mb2:	// 나만의 코스
			 Intent intetn2 = new Intent(this, MyWayMenuActivity.class);
			 startActivity(intetn2);
			 break;
	    case R.id.b_mb3:	// 도전!!!
	    	 break;
	    case R.id.b_mb4:	// 설정
			 Intent intetn4 = new Intent(this, SettingActivity.class);
			 startActivity(intetn4);
			 break;
		case R.id.b_mb5:	// 도움말
			 Intent intetn5 = new Intent(this, HelpActivity.class);
			 startActivity(intetn5);
			 break; 
		}
	}
}

package mmu.ac.kr.muljomdao;

import com.nhn.android.maps.opt.N;

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
		ImageView bt1 = (ImageView) findViewById(R.id.b_mbrecomway);
		ImageView bt2 = (ImageView) findViewById(R.id.b_mbmyway);
		ImageView bt3 = (ImageView) findViewById(R.id.b_mbchallenge);
		ImageView bt4 = (ImageView) findViewById(R.id.b_mbsetting);
		ImageView bt5 = (ImageView) findViewById(R.id.b_mbhelp);
		ImageView bt6 = (ImageView) findViewById(R.id.b_mbtresurehunt);
		ImageView bt7 = (ImageView) findViewById(R.id.b_mbsos);
		ImageView bt8 = (ImageView) findViewById(R.id.b_name);
		ImageView bt9 = (ImageView) findViewById(R.id.b_restrant);
		// 버튼 클릭 이벤트 처리
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		bt6.setOnClickListener(this);
		bt7.setOnClickListener(this);
		bt8.setOnClickListener(this);
		bt9.setOnClickListener(this);
	}
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		switch (v.getId()) {
		// 첫번쨰버튼 눌렷을 때
		case R.id.b_mbrecomway:	// 추천코스
			  //페이지 이동 구문  - 명시적 인텐트 Intent(현재 엑티비티, 실행할 엑티비티)
			  // Intent intetn1 = new Intent(this, MainActivity.class);
			  Intent intetn1 = new Intent(this, RecommendwayActivity.class);
			  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
			  startActivity(intetn1);
			  break;
		case R.id.b_mbmyway:	// 나만의 코스
			 Intent intetn2 = new Intent(this, MyWayMenuActivity.class);
			 startActivity(intetn2);
			 break;
	    case R.id.b_mbchallenge:	// 도전!!!
	    	Intent intetn3 = new Intent(this, ChallengeActivity.class);
			 startActivity(intetn3);
	    	 break;
	    case R.id.b_mbsetting:	// 설정
			 Intent intetn4 = new Intent(this, SettingActivity.class);
			 startActivity(intetn4);
			 break;
		case R.id.b_mbhelp:	// 도움말
			 Intent intetn5 = new Intent(this, HelpActivity.class);
			 startActivity(intetn5);
			 break; 
		case R.id.b_mbtresurehunt:	// 보물찾기
			 Intent intetn6 = new Intent(this, TresurehuntActivity.class);
			 startActivity(intetn6);
			 break; 
		case R.id.b_mbsos:	// sos 번호 기입
			 Intent intetn7 = new Intent(this, SOSActivity.class);
			 startActivity(intetn7);
			 break;
		case R.id.b_name:	// 네이버 API
			 Intent intetn8 = new Intent(this, NMapViewer.class);
			 startActivity(intetn8);
			 break;
			 
		case R.id.b_restrant:	// 맛집 검색
			 Intent intetn9 = new Intent(this, FoodSearchActivity.class);
			 startActivity(intetn9);
			 break;
		}
	}
}

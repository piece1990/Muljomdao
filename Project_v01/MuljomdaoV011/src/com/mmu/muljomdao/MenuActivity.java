package com.mmu.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// xml파일에 있는 버튼을 id값으로 불러와서 사용 , 버튼 텍스트 변경
		Button bt1 = (Button) findViewById(R.id.b_mb1);
		Button bt2 = (Button) findViewById(R.id.b_mb2);
		Button bt3 = (Button) findViewById(R.id.b_mb3);
		Button bt4 = (Button) findViewById(R.id.b_mb4);
		Button bt5 = (Button) findViewById(R.id.b_mb5);

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
					  Intent intetn1 = new Intent(this, MainActivity.class);
					  //startActivity() 메소드가 호출하면서 인자로 전달하면 실행한다.
					  startActivity(intetn1);
					  break;
				  case R.id.b_mb2:	// 나만의 도전
					  break;
				  case R.id.b_mb3:	// 도전!!!
					  break;
				  case R.id.b_mb4:	// 설정
					  break;
				  case R.id.b_mb5:	// 도움말
					  break;
			  }
		 }
	}

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
		// xml���Ͽ� �ִ� ��ư�� id������ �ҷ��ͼ� ��� , ��ư �ؽ�Ʈ ����
		Button bt1 = (Button) findViewById(R.id.b_mb1);
		Button bt2 = (Button) findViewById(R.id.b_mb2);
		Button bt3 = (Button) findViewById(R.id.b_mb3);
		Button bt4 = (Button) findViewById(R.id.b_mb4);
		Button bt5 = (Button) findViewById(R.id.b_mb5);

		// ��ư Ŭ�� �̺�Ʈ ó��
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
	}
	// ��ư�� ������ �� ����Ǵ� �޼ҵ�(����� ����Ʈ)
		@Override
		public void onClick(View v) {
			  // TODO Auto-generated method stub
		
			  // Ŭ�� �� ��ư1���� id���� ����ġ�������� �б�
			  switch (v.getId()) {
				  // ù������ư ������ ��
				  case R.id.b_mb1:	// ��õ�ڽ�
					  //������ �̵� ����  - ����� ����Ʈ Intent(���� ��Ƽ��Ƽ, ������ ��Ƽ��Ƽ)
					  Intent intetn1 = new Intent(this, MainActivity.class);
					  //startActivity() �޼ҵ尡 ȣ���ϸ鼭 ���ڷ� �����ϸ� �����Ѵ�.
					  startActivity(intetn1);
					  break;
				  case R.id.b_mb2:	// ������ ����
					  break;
				  case R.id.b_mb3:	// ����!!!
					  break;
				  case R.id.b_mb4:	// ����
					  break;
				  case R.id.b_mb5:	// ����
					  break;
			  }
		 }
	}

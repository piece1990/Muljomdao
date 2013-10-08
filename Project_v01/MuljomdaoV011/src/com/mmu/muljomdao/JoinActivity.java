package com.mmu.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JoinActivity extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		// xml���Ͽ� �ִ� ��ư�� id������ �ҷ��ͼ� ��� , ��ư �ؽ�Ʈ ����
		Button bt1 = (Button) findViewById(R.id.b_join);
		bt1.setText("Join");

		// ��ư Ŭ�� �̺�Ʈ ó��
		bt1.setOnClickListener(this);
	}
	// ��ư�� ������ �� ����Ǵ� �޼ҵ�(����� ����Ʈ)
		@Override
		public void onClick(View v) {
			  // TODO Auto-generated method stub
		
			  // Ŭ�� �� ��ư1���� id���� ����ġ�������� �б�
			  switch (v.getId()) {
				  // ù������ư ������ ��
				  case R.id.b_join:
				  //������ �̵� ����  - ����� ����Ʈ Intent(���� ��Ƽ��Ƽ, ������ ��Ƽ��Ƽ)
				  Intent intetn1 = new Intent(this, MenuActivity.class);
				  //startActivity() �޼ҵ尡 ȣ���ϸ鼭 ���ڷ� �����ϸ� �����Ѵ�.
				  startActivity(intetn1);
		
				  break;
			  }
		 }
	}

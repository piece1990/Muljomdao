/* StartActivity -> LoginActivity �� Activity �̵��� ����
 * Intent intent = new Intent(���� Activity, ��ȯ�� Activity);
 * 	- ��ü ����
 * 	 
 * 	intent.putExtra(���� ��, ������ ��);
 * 	-  ȭ�� ��ȯ �� ������ ����
 * 	 
 * 	startActivity(intent)
 * 	- Intent ����
 */

package com.mmu.muljomdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		// xml���Ͽ� �ִ� ��ư�� id������ �ҷ��ͼ� ��� , ��ư �ؽ�Ʈ ����
		Button bt1 = (Button) findViewById(R.id.b_start);
		bt1.setText("Start");

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
		  case R.id.b_start:
			  //������ �̵� ����  - ����� ����Ʈ Intent(���� ��Ƽ��Ƽ, ������ ��Ƽ��Ƽ)
			  Intent intetn1 = new Intent(this, LoginActivity.class);
			  //startActivity() �޼ҵ尡 ȣ���ϸ鼭 ���ڷ� �����ϸ� �����Ѵ�.
			  startActivity(intetn1);
	
			  break;
		  }
	  }
}
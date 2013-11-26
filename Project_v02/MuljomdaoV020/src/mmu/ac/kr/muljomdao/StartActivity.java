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
import android.widget.ImageView;


public class StartActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView iv = new ImageView(this);
        iv.setBackgroundResource(R.drawable.startsreen);
        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        setContentView(iv);
	}
	
}
package mmu.ac.kr.muljomdao;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

public class MyWayMenuActivity extends NMapActivity {//implements OnClickListener{

	// API-KEY
	public static final String API_KEY = "58c78772317dc7e9fe20344c2a1129dc";
	// 네이버 맵 객체
	NMapView mMapView = null;
	// 맵 컨트롤러
	NMapController mMapController = null;
	// 맵을 추가할 레이아웃
	LinearLayout MyWayMenuMap;
	
	// 오버레이의 리소스를 제공하기 위한 객체
	NMapViewerResourceProvider mMapViewerResourceProvider = null;
	// 오버레이 관리자
	NMapOverlayManager mOverlayManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywaymenu);
		
		/******************* 지도 초기화 ********************/
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
		MyWayMenuMap = (LinearLayout) findViewById(R.id.MyWayMenuMap);

		// 네이버 지도 객체 생성
		mMapView = new NMapView(this);
		
		// 지도 객체로부터 컨트롤러 추출
		mMapController = mMapView.getMapController();

		// 네이버 지도 객체에 APIKEY 지정
		mMapView.setApiKey(API_KEY);

		// 생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
		MyWayMenuMap.addView(mMapView);

		// 지도를 터치할 수 있도록 옵션 활성화
		mMapView.setClickable(true);
		
		// 확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
		mMapView.setBuiltInZoomControls(true, null);
		
		
		/******************* 오버래이 초기화 ********************/
		// 오버래이 리소스 관리자 생성
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 오버래이 관리자 생성. 이 객체에게 맵뷰와 오버래이 리스소 관리자를 연결한다.
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

		/******************* 출발 위치와 도착위치 지정 ********************/
		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
		
		// addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
		poiData.addPOIitem(126.368449, 34.796053,  "출발점", NMapPOIflagType.FROM, 0);
		poiData.addPOIitem(126.372690, 34.793492,  "도착점", NMapPOIflagType.TO, 0);
		
		poiData.endPOIdata();

		// create POI data overlay
		mOverlayManager.createPOIdataOverlay(poiData, null);

		/******************* 이동 경로 지정 ********************/
		// 이동 경로를 추가하기 위한 객체
		NMapPathData pathData = new NMapPathData(9);

		// 이동 경로 표시 시작
		pathData.initPathData();
		
		/* 이동 경로 추가
		 * addPathPoint(경도, 위도, 연결선)
		 * 연결선 : NMapPathLineStyle 클래스에 상수 형태
		 * NMapPathLineStyle.TYPE_SOLID - 직선
		 * NMapPathLineStyle.TYPE_DASHED - 점선
		 * 연결선 값을 0 으로 주면 처음 지정한 종류 표시
		 */ 
		/* 어민동산에서 유달산 정상까지의 이동경로 */
		pathData.addPathPoint(126.368449, 34.796053, NMapPathLineStyle.TYPE_DASH);
		pathData.addPathPoint(126.369301, 34.796342, 0);
		pathData.addPathPoint(126.369743, 34.796328, 0);
		pathData.addPathPoint(126.370112, 34.796525, 0);
		pathData.addPathPoint(126.370977, 34.796420, 0);
		pathData.addPathPoint(126.371136, 34.795213, 0);
		pathData.addPathPoint(126.372690, 34.793492, 0);

		// 이동 경로 표시 종료
		pathData.endPathData();

		// 지도상에 이동 경로 표시
		NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
		
		// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
		pathDataOverlay.showAllPathData(0);
	}
/*		Button bt1 = (Button) findViewById(R.id.b_mws);		// 기록 시작
		Button bt2 = (Button) findViewById(R.id.b_mwe);		// 기록 종료
		Button bt3 = (Button) findViewById(R.id.b_mwrecord);// 새로 저장
		
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
	}
	
	// 버튼을 눌렀을 때 실행되는 메소드(명시적 인텐트)
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		switch (v.getId()) {
		// 첫번쨰버튼 눌렷을 때
		case R.id.b_mws:	// 시작
			 break;
		case R.id.b_mwe:	// 종료
			 break;
	    case R.id.b_mwrecord:	// 새로 기록
			 Intent intetn1 = new Intent(this, MyWayActivity.class);
			 startActivity(intetn1);
	    	 break;
	    
		}
	}*/
}
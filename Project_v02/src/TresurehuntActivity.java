package mmu.ac.kr.muljomdao;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

public class TresurehuntActivity extends NMapActivity implements OnClickListener{
	
	// API-KEY
	public static final String API_KEY = "58c78772317dc7e9fe20344c2a1129dc";
	private static final boolean DEBUG = false;
	
	// 네이버 맵 객체
	/**
	 * @uml.property  name="mMapView"
	 * @uml.associationEnd  
	 */
	NMapView mMapView = null;
	// 맵 컨트롤러
	/**
	 * @uml.property  name="mMapController"
	 * @uml.associationEnd  
	 */
	NMapController mMapController = null;
	
	// 맵을 추가할 레이아웃
	/**
	 * @uml.property  name="myWayMenuMap"
	 * @uml.associationEnd  
	 */
	
	LinearLayout MyWayMenuMap;
	
	// 오버레이의 리소스를 제공하기 위한 객체
	/**
	 * @uml.property  name="mMapViewerResourceProvider"
	 * @uml.associationEnd  
	 */
	NMapViewerResourceProvider mMapViewerResourceProvider = null;
	// 오버레이 관리자
	/**
	 * @uml.property  name="mOverlayManager"
	 * @uml.associationEnd  
	 */
	NMapOverlayManager mOverlayManager;
	// 위도 경도를 텍스트로 표시
	/**
	 * @uml.property  name="myTextView"
	 * @uml.associationEnd  
	 */
	TextView myTextView;
	
	/**
	 * @uml.property  name="mMyLocationOverlay"
	 * @uml.associationEnd  
	 */
	NMapMyLocationOverlay mMyLocationOverlay;
	/**
	 * @uml.property  name="mMapLocationManager"
	 * @uml.associationEnd  
	 */
	NMapLocationManager mMapLocationManager;
	
	// 위도의 값을 가지고있는  실수
	public static double LoLatitude;
	// 경도의 값을 가지고있는  실수
	public static double LoLongitude;
	// 나만의 코스를 표시하는지 않으는지를 보기위한 bool
	public static boolean mywayonoff= false;
	// 나만의 코스를 표시하는 갯수
	public static int limitdot=0;
	// 나만의 코스를 표시하는 라인
	public static double limitline[][]= new double[250][2];
	// 시작인지 종료인지 판단
	public static boolean boolstart=false;
	// sms 몸통을 만들기위한 변수
	public static String smsBodyaddress;
	// 보물 갯수 시작 가능
	public static int thcount = 0;
	// 보물 온 오프 탐색
	public static int thonoff[] = new int[4];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tresurehunt);
				
		/******************* 지도 초기화 ********************/
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
		MyWayMenuMap = (LinearLayout) findViewById(R.id.theMap);

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
		
		// 위성지도
		mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
		
		// 지도의 최대, 최소 축척 레벨을 설정한다.  
		mMapController.setZoomLevelConstraint(4, 14);
		
		// 지도 중심 좌표 및 축척 레벨을 설정한다. 축척 레벨을 지정하지 않으면 중심 좌표만 변경된다. 유효 축척 레벨 범위는 1~14이다.		
		mMapController.setMapCenter(new NGeoPoint(126.372690, 34.793492), 13);
		
		/******************* 버튼 설정 ********************/
		Button bt1 = (Button) findViewById(R.id.b_tresurehunt);
		Button bt2 = (Button) findViewById(R.id.b_tresurehuntend);
		Button bt3 = (Button) findViewById(R.id.b_thSendMessageButton);
		
		// 버튼 이벤트 처리 
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		
		/******************* 오버래이 초기화 ********************/
		// 오버래이 리소스 관리자 생성
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 오버래이 관리자 생성. 이 객체에게 맵뷰와 오버래이 리스소 관리자를 연결한다.
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

		
		/******************* GPS 사용 관련 함수********************/
		myTextView = (TextView)findViewById(R.id.myTextView);
        
        //LocationManager 생성
        LocationManager lc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //사용할 GPS Provider
        String provider = LocationManager.GPS_PROVIDER;

        //Latitude, Longitude 를 저장하는 Location 객체
        //최근 갱신된 GPS값을 받아옴
        Location location = lc.getLastKnownLocation(provider);
        
        //Latitude, Longitude를 TextView에 출력하는 showGPS 메소드 호출
        showGPS(location);
        
        //1초마다 또는 10미터 이상 이동시 GPS정보를 업데이트하는 리스너 등록
        lc.requestLocationUpdates(provider, 1000, 10, new LocationListener() {
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub    
            }
            
            //업데이트 조건이 발동되면 showGPS 메소드 호출
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                // location 정보가 바뀌었을때  값을 집어 넣어준다.
                LoLatitude = location.getLatitude();	// 위도
                LoLongitude = location.getLongitude();	// 경도
                 
                // 나만의 코스 표시가 ON 상태면 도트 찍기
                if(mywayonoff == true){
                	limitline[limitdot][0]=LoLatitude; // 위도
                	limitline[limitdot][1]=LoLongitude; // 경도
                	
                	showMyway();	// 현재 나의 위치
                	// 보물 찾기 이용중.
                    thSelect();
                	limitdot++;
                }
                
                //위도경도를 주소로 변환
 //               findPlacemarkAtLocation(LoLongitude, LoLatitude); 
            }
            
        });
    }
    
  //Latitude, Longitude를 TextView에 출력
    private void showGPS(Location location) {
        if(location!=null)
        	// 위도 경도를 받아서 텍스트로 출력한다.
            myTextView.setText("Latitude :"+location.getLatitude() +
                               ", Longitude : "+location.getLongitude());  
    }
    
    // 보물찾기 코스를 클릭시 발생.
    private void showthway(){
    	// set POI data
    	NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
    	poiData.beginPOIdata(1);

		// 이동 경로를 추가하기 위한 객체
    	NMapPathData pathData2 = new NMapPathData(7);
    	// 이동 경로 표시 시작
    	pathData2.initPathData();
		// addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
		poiData.addPOIitem(126.368504, 34.786539,  "출발점", NMapPOIflagType.FROM, 0);
		poiData.addPOIitem(126.372690, 34.793492,  "도착점", NMapPOIflagType.TO, 0);
		poiData.endPOIdata();

		// create POI data overlay
		mOverlayManager.createPOIdataOverlay(poiData, null);
		
		pathData2.addPathPoint(126.368504, 34.786539, NMapPathLineStyle.TYPE_DASH);
		pathData2.addPathPoint(126.368102, 34.787543, 0);
		pathData2.addPathPoint(126.369140, 34.787797, 0);
		pathData2.addPathPoint(126.369891, 34.788475, 0);
		pathData2.addPathPoint(126.370901, 34.789309, 0);
		pathData2.addPathPoint(126.371746, 34.789664, 0);
		pathData2.addPathPoint(126.372690, 34.793492, 0);

		// 경로 데이터 추가를 종료한다.
		pathData2.endPathData();

		// 지도상에 이동 경로 표시
		NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData2);
		
		// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
		pathDataOverlay.showAllPathData(0);

    }

    
    // 나만의 코스가 ON 상태일 때 나의 이동 경로를 표시한다. 
    private void showMyway(){
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
		/* 임의의 출발점부터 임의의 도착점까지의 이동경로 */
		for(int i = 0 ; i < limitdot ; i ++){
			if(i == 0)
				pathData.addPathPoint(limitline[i][1], limitline[i][0], NMapPathLineStyle.TYPE_SOLID);
			else 
				pathData.addPathPoint(limitline[i][1], limitline[i][0], 0);
		}
		// 이동 경로 표시 종료
		pathData.endPathData();

		// 지도상에 이동 경로 표시
		NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
		
		// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
		pathDataOverlay.showAllPathData(0);	
    }
    /** 
     * 지도가 초기화된 후 호출된다. 
     * 정상적으로 초기화되면 errorInfo 객체는 null이 전달되며, 
     * 초기화 실패 시 errorInfo객체에 에러 원인이 전달된다 
     */  
    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
		if (errorInfo == null) { // success
			//onLocationChanged(location);//현재위치로 이동
			//mMapController.setMapCenter(new NGeoPoint(126.368504, 34.786539), 11);
			startMyLocation();
//			showMyway();
		} else { // fail
			android.util.Log.e("NMAP","onMapInitHandler: error=" + errorInfo.toString());
		}
	}
    
    
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		switch (v.getId()) {
		// 첫번쨰버튼 눌렷을 때
		case R.id.b_tresurehunt:	// 보물찾기!
			if(boolstart == false ){
				 boolstart= true; // 시작 버튼을 눌렀다.
				 mywayonoff = true;
				 thcount=0;	// 보물의 갯수 초기화
				 Toast.makeText(this, "보물찾기 시작!", Toast.LENGTH_SHORT).show();
				 NMapPOIdata poiData1 = new NMapPOIdata(1, mMapViewerResourceProvider);
				 poiData1.beginPOIdata(1);
				 // addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
				 poiData1.addPOIitem(LoLongitude, LoLatitude,  "보물찾기시작", NMapPOIflagType.FROM, 0);
				 poiData1.endPOIdata();
	
				 // create POI data overlay
				 mOverlayManager.createPOIdataOverlay(poiData1, null); 
				}
				break;	
	
		case R.id.b_tresurehuntend:	// 보물찾기 종료
			if(boolstart == true ){	// 시작 버튼을 누른 상태에서 종료 버튼을 눌렀다.
				boolstart= false; // 종료 버튼을 눌렀다.
				mywayonoff = false;
				Toast.makeText(this, "보물찾기 종료!", Toast.LENGTH_SHORT).show();
				// set POI data
				NMapPOIdata poiData2 = new NMapPOIdata(1, mMapViewerResourceProvider);
				poiData2.beginPOIdata(1);
				// addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
				poiData2.addPOIitem(LoLongitude, LoLatitude,  "보물찾기 종료", NMapPOIflagType.TO, 0);
				poiData2.endPOIdata();
				// create POI data overlay
				mOverlayManager.createPOIdataOverlay(poiData2, null);
			}
			 break;
		case R.id.b_thSendMessageButton:
			// setting에 기록된 비상 연락망 값 가져오기
	        SharedPreferences pref = getSharedPreferences("Muljomdao", MODE_PRIVATE);
	        for(int i=1; i<4 ; i++){
		        String Phonenumber = pref.getString("settingnumber"+i, "01094386235");	// 값이 없으면 119를 가져온다. 
		        
		        sendSMS(Phonenumber, "긴급상황! 조난신고! ("+LoLatitude+", "+LoLongitude+")좌표로 빠르게 와주세요.");
		    }
	      	break;
		} 
		showthway();	// 보물찾기 코스를 클릭시 발생. 
    }
    
    private void sendSMS(String phoneNumber, String message) { // 문자전송
    	PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,TresurehuntActivity.class), 0);
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(phoneNumber, null, message, pi, null);	
    }
		
    /** 
     * 지도 레벨 변경 시 호출되며 변경된 지도 레벨이 파라미터로 전달된다. 
     */  
    public void onZoomLevelChange(NMapView mapview, int level) {}  
  
    /** 
     * 지도 중심 변경 시 호출되며 변경된 중심 좌표가 파라미터로 전달된다. 
     */    
    public void onMapCenterChange(NMapView mapview, NGeoPoint center) {
    	startMyLocation();
    }  
    
    private void startMyLocation() {

		if (mMyLocationOverlay != null) {
			// 전달된 오버레이 객체의 존재 여부를 반환한다.
			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
				mOverlayManager.addOverlay(mMyLocationOverlay);
			

	/*		if (mMapLocationManager.isMyLocationEnabled()) {

				if (!mMapView.isAutoRotateEnabled()) {
					mMyLocationOverlay.setCompassHeadingVisible(true);

					mMapCompassManager.enableCompass();

					mMapView.setAutoRotateEnabled(true, false);

					mMapContainerView.requestLayout();
				} else {
					stopMyLocation();
				}
*/
				mMapView.postInvalidate();
			} else {
				// 현재 위치 탐색을 시작한다. skipLastLocation을 true로 전달하면 마지막으로 탐색된 위치를 사용하지 않는다. 
				// 시스템 설정에 의해서 위치 탐색이 불가하다면 false를 반환한다.
				boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
				// 현재 위치 탐색 중인지 여부를 반환한다.
				if (!isMyLocationEnabled) {
					Toast.makeText(TresurehuntActivity.this, "Please enable a My Location source in system settings",
						Toast.LENGTH_LONG).show();

	/*				Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(goToSettings);
*/
					return;
				}
			}
		}
	}
    
    /**
	 * @uml.property  name="onCalloutOverlayViewListener"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

		@Override
		public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

			if (overlayItem != null) {
				// [TEST] 말풍선 오버레이를 뷰로 설정함		
				String title = overlayItem.getTitle();
		
				if (title != null && title.length() > 5) {
					return new NMapCalloutCustomOverlayView(TresurehuntActivity.this, itemOverlay, overlayItem, itemBounds);
				}
			}

			// null을 반환하면 말풍선 오버레이를 표시하지 않음
			return null;
		}

	};
	
	public void NMapPlacemarkplaceMark(NMapPlacemark placeMark){
//		smsBodyaddress = placeMark.toString();
		return;
	}
	private void thSelect(){
		// 126.368504, 34.786539,  "출발점", NMapPOIflagType.FROM, 0)
		// 126.372690, 34.793492,  "도착점", NMapPOIflagType.TO, 0);
		// LoLatitude+", "+LoLongitude
		// 기록의 시작은 출발점의 좌표를 사각형의 범위를 두어 그 안에 현 위치 좌표를 두어 참이 되면 시작이 가능하다.
		if(thonoff[0] == 0)
		if(LoLatitude >= 126.368504-50 && LoLatitude <= 126.368690+50 
				&& LoLongitude >= 34.793492-50 && LoLongitude >= 34.793492+50){
			thcount++;	// 보물 찾기
	//		Toast.makeText(this, "보물 찾을 찾았다!"+thcount, Toast.LENGTH_SHORT).show();
			thonoff[0] = 1;
		}
		if(thonoff[1] == 0)
		if(LoLatitude >= 126.372690-50 && LoLatitude <= 126.372690+50 
				&& LoLongitude >= 34.793492-50 && LoLongitude >= 34.793492+50){
			thcount++;	// 보물 찾기
//			Toast.makeText(this, "보물 찾을 찾았다!"+thcount, Toast.LENGTH_SHORT).show();
			thonoff[1] = 1;
		}
		if(thonoff[2] == 0)
		if(LoLatitude >= 126.372790-50 && LoLatitude <= 126.372790+50 
				&& LoLongitude >= 34.793592-50 && LoLongitude >= 34.793592+50){
			thcount++;	// 보물 찾기
//			Toast.makeText(this, "보물 찾을 찾았다!"+thcount, Toast.LENGTH_SHORT).show();
			thonoff[2] = 1;
		}
		if(thonoff[3] == 0)
		if(LoLatitude >= 126.36540795-50 && LoLatitude <= 126.36540795+50 		// 목포해양대
				&& LoLongitude >= 34.79153116-50 && LoLongitude >= 34.79153116+50){
			thcount++;	// 보물 찾기
//			Toast.makeText(this, "보물 찾을 찾았다!"+thcount, Toast.LENGTH_SHORT).show();
			thonoff[3] = 1;
		}
		
	}
	public void onStop(){
    	super.onStop();
    	// 위도의 값을 가지고있는  실수 -초기화
    	LoLatitude=0;
    	// 경도의 값을 가지고있는  실수-초기화
    	LoLongitude=0;
    	// 나만의 코스를 표시하는지 않으는지를 보기위한 bool-초기화
    	mywayonoff= false;
    	// 나만의 코스를 표시하는 갯수-초기화
    	limitdot=0;
    	// 나만의 코스를 표시하는 라인-초기화
    	for(int i=0; i < 250 ; i++){
    		for(int j=0; j< 2; j++){
    			limitline[i][j] =0;
    		}
    	}
    	// 시작인지 종료인지 판단-초기화
    	boolstart=false;
    	// 추천 코스를 선택하기 위한 항목-초기화
    	smsBodyaddress="";
    	// 보물 갯수 시작 가능-초기화
    	thcount = 0;
    	// 보물 온 오프 탐색-초기화
    	for(int i=0; i < 4 ; i++){
    		thonoff[i] = 0;
    	}
    }   
}
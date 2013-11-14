package mmu.ac.kr.muljomdao;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
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
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

public class MyWayMenuActivity extends NMapActivity implements OnClickListener{
	
	// API-KEY
	public static final String API_KEY = "58c78772317dc7e9fe20344c2a1129dc";
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
	public String smsBodyaddress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywaymenu);
				
		/******************* 지도 초기화 ********************/
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
		MyWayMenuMap = (LinearLayout) findViewById(R.id.CommendWayMap);

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
		
		// 지도의 최소, 최대 축척 레벨을 설정한다.  
		mMapController.setZoomLevelConstraint(4, 14);
		
		// 지도 중심 좌표 및 축척 레벨을 설정한다. 축척 레벨을 지정하지 않으면 중심 좌표만 변경된다. 유효 축척 레벨 범위는 1~14이다.		
		mMapController.setMapCenter(new NGeoPoint(126.372690, 34.793492), 13);
		
		/******************* 버튼 설정 ********************/
		Button bt1 = (Button) findViewById(R.id.b_mws);
		Button bt2 = (Button) findViewById(R.id.b_mwe);
		Button bt3 = (Button) findViewById(R.id.b_mwchange);
		Button bt4 = (Button) findViewById(R.id.b_mwrecodingend);
		Button bt5 = (Button) findViewById(R.id.b_mwSendMessageButton);
		
		// 버튼 이벤트 처리 
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		
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
            	MyLocationState();
                showGPS(location);
                // location 정보가 바뀌었을때  값을 집어 넣어준다.
                LoLatitude = location.getLatitude();	// 위도
                LoLongitude = location.getLongitude();	// 경도
                
                // 나만의 코스 표시가 ON 상태면 도트 찍기
                if(mywayonoff == true){
                	limitline[limitdot][0]=LoLatitude; // 위도
                	limitline[limitdot][1]=LoLongitude; // 경도
                	showMyway();
                	limitdot++;
                }
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
				pathData.addPathPoint(limitline[i][1], limitline[i][0], NMapPathLineStyle.TYPE_DASH);
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
    
    private void MyLocationState(){
    	NMapPOIdata poiData0 = new NMapPOIdata(1, mMapViewerResourceProvider);
		 poiData0.beginPOIdata(1);
		 // addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
		 poiData0.addPOIitem(LoLongitude, LoLatitude,  "나의 위치", NMapPOIflagType.SPOT, 0);
		 poiData0.endPOIdata();

		 // create POI data overlay
		 mOverlayManager.createPOIdataOverlay(poiData0, null); 
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
					return new NMapCalloutCustomOverlayView(MyWayMenuActivity.this, itemOverlay, overlayItem, itemBounds);
				}
			}

			// null을 반환하면 말풍선 오버레이를 표시하지 않음
			return null;
		}

	};
    
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 클릭 될 버튼1개의 id값을 스위치구문으로 분기
		switch (v.getId()) {
		// 첫번쨰버튼 눌렷을 때
		case R.id.b_mws:	// 시작
			if(boolstart == false ){
				 boolstart= true; // 시작 버튼을 눌렀다.
				 mywayonoff = true;
				 Toast.makeText(this, "이동 시작", Toast.LENGTH_SHORT).show();
		/*		 NMapPOIdata poiData1 = new NMapPOIdata(2, mMapViewerResourceProvider);
				 poiData1.beginPOIdata(2);
				 // addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
				 poiData1.addPOIitem(LoLongitude, LoLatitude,  "출발점", NMapPOIflagType.FROM, 0);
				 poiData1.endPOIdata();
	
				 // create POI data overlay
				 mOverlayManager.createPOIdataOverlay(poiData1, null); 
				 
				 */
				 
				 for(int i=0; i < 250 ; i++){
	     			SharedPreferences pref = getSharedPreferences("Muljomdao_mw", Activity.MODE_PRIVATE);
	     			pref.getString("mywayx"+i, "");
	     			pref.getString("mywayy"+i, "");
	     			String strmwx = pref.getString("mywayx"+i, "0");	// 값이 없으면 0를 가져온다.
	     			String strmwy = pref.getString("mywayx"+i, "0");	// 값이 없으면 0를 가져온다.
	     			int inmwx = Integer.parseInt(strmwx);
	     			int inmwy = Integer.parseInt(strmwy);
	     			
	     			if(inmwx != 0){
	     				limitline[i][0]	= inmwx;
	     				limitline[i][1]	= inmwy;
	     			}
	     			
			     }
			}
			 break;
		case R.id.b_mwe:	// 종료
			if(boolstart == true ){	// 시작 버튼을 누른 상태에서 종료 버튼을 눌렀다.
				boolstart= false; // 종료 버튼을 눌렀다.
				mywayonoff = false;
				Toast.makeText(this, "이동 종료", Toast.LENGTH_SHORT).show();
				// set POI data
				NMapPOIdata poiData2 = new NMapPOIdata(2, mMapViewerResourceProvider);
				poiData2.beginPOIdata(2);
				// addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
				poiData2.addPOIitem(LoLongitude, LoLatitude,  "도착점", NMapPOIflagType.TO, 0);
				poiData2.endPOIdata();
				// create POI data overlay
				mOverlayManager.createPOIdataOverlay(poiData2, null);
				

			}
			 break;
	    case R.id.b_mwchange:	// 새로 기록
	    	 Toast.makeText(this, "기록 초기화", Toast.LENGTH_SHORT).show();
	    	 for(int i=0; i < 250 ; i++){
	     		for(int j=0; j< 2; j++){
	     			limitline[i][j] = 0;
	     			SharedPreferences pref = getSharedPreferences("Muljomdao_mw", Activity.MODE_PRIVATE);
	     	    	SharedPreferences.Editor editor = pref.edit();
	     	    	editor.remove("mywayx"+i);
	     	    	editor.remove("mywayy"+i);
	     	    	editor.commit();
	     		}
	     	}
	    	 break;
	    case R.id.b_mwrecodingend:		// 기록 저장
	    	Toast.makeText(this, "기록 저장", Toast.LENGTH_SHORT).show();
	    	for(int i=0; i < 250 ; i++){
	     		for(int j=0; j< 2; j++){
	     			SharedPreferences pref = getSharedPreferences("Muljomdao_mw", Activity.MODE_PRIVATE);
	     	    	SharedPreferences.Editor editor = pref.edit();
	     	    	if(limitline[i][0] != 0){		// 값이 들어있을때까지만.
	     	    		editor.putString("mywayx"+i, String.valueOf(limitline[i][0]));
	     	    		editor.putString("mywayy"+i, String.valueOf(limitline[i][1]));
	     	    		editor.commit();
	     	    	}
	     		}
	     	}
	    	break;
	    case R.id.b_mwSendMessageButton:
			// setting에 기록된 비상 연락망 값 가져오기
	        SharedPreferences pref = getSharedPreferences("Muljomdao", MODE_PRIVATE);
	        for(int i=1; i<4 ; i++){
		        String Phonenumber = pref.getString("settingnumber"+i, "01094386235");	// 값이 없으면 119를 가져온다. 
		        sendSMS(Phonenumber, "긴급상황! 조난신고! ("+LoLatitude+", "+LoLongitude+")좌표로 빠르게 와주세요.");
		    }
	        break;
		}
    }
    private void sendSMS(String phoneNumber, String message) { // 문자전송
    	PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,MyWayMenuActivity.class), 0);
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
    public void onMapCenterChange(NMapView mapview, NGeoPoint center) {} 
    
    
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
    }
    
    public void mywaylistprint(){
    	NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
    	poiData.beginPOIdata(1); 
    	// 이동 경로를 추가하기 위한 객체
    	NMapPathData pathData2 = new NMapPathData(250);
    	// 이동 경로 표시 시작
    	pathData2.initPathData();
		// addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
		poiData.addPOIitem(limitline[0][0], limitline[0][1],  "출발점", NMapPOIflagType.FROM, 0);
	//	poiData.addPOIitem(126.372690, 34.793492,  "도착점", NMapPOIflagType.TO, 0);
		poiData.endPOIdata();

		// create POI data overlay
		mOverlayManager.createPOIdataOverlay(poiData, null);
		pathData2.addPathPoint(limitline[0][0], limitline[0][1], NMapPathLineStyle.TYPE_DASH);
		
		for(int i=0; i< 250; i++){
			pathData2.addPathPoint(limitline[i][0], limitline[i][1], 0);
		}
		
		// 경로 데이터 추가를 종료한다.
		pathData2.endPathData();

		// 지도상에 이동 경로 표시
		NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData2);
		
		// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
		pathDataOverlay.showAllPathData(0);
    }
    
}



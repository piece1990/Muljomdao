package mmu.ac.kr.muljomdao;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

public class ChallengeActivity extends NMapActivity implements OnClickListener{
	
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
	public static double limitline[][]= new double[6000][2];
	// 시작인지 종료인지 판단
	public static boolean boolstart=false;
	// 추천 코스를 선택하기 위한 항목
	public static int choiceRecomWay=0;
	// sms 몸통을 만들기위한 변수
	public static String smsBodyaddress;
	// 도전 시작 가능
	public static boolean challengestart = false; 
	// 도전 종료 판별
	public static boolean challengeend = false; 
	// 도전 범위 표시
	public static boolean challengeRange = false;
	// 도전 시작 가능 여부
	public static boolean challengeSchoolstart = false;
	// TextView - 시간 카운트
	TextView Counttext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);
				
		/******************* 지도 초기화 ********************/
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
		MyWayMenuMap = (LinearLayout) findViewById(R.id.challengeMap);

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
		ImageView bt1 = (ImageView) findViewById(R.id.b_challenge);
		ImageView bt2 = (ImageView) findViewById(R.id.b_challengeRange);
		ImageView bt3 = (ImageView) findViewById(R.id.b_challengegiveup);
		ImageView bt4 = (ImageView) findViewById(R.id.b_challengeSchool);
		// 버튼 이벤트 처리 
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		
		/******************* 텍스트 설정 ********************/
		Counttext = (TextView) findViewById(R.id.t_challengecount);
		
		
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
                // 도전 가능 여부 판별
                challengeSelect();
                showGPS(location);
                // 나만의 코스 표시가 ON 상태면 도트 찍기
                if(mywayonoff == true){
                	limitline[limitdot][0]=LoLatitude; // 위도
                	limitline[limitdot][1]=LoLongitude; // 경도
                	showMyway();
                	limitdot++;
                }
            }
        });
        
     // location manager
	mMapLocationManager = new NMapLocationManager(this);
	mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);


	// create my location overlay	- 현재 위치와 나침반 관리자를 인자로 받는데, 나침반 관리자는 사용하지않으므로 null 값을 준다.
	mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, null);
    }
	
	/* MyLocation Listener */
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

			if (mMapController != null) {
				mMapController.animateTo(myLocation);
			}

			return true;
		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

			// stop location updating
			//			Runnable runnable = new Runnable() {
			//				public void run() {										
			//					stopMyLocation();
			//				}
			//			};
			//			runnable.run();	

			Toast.makeText(ChallengeActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
			Toast.makeText(ChallengeActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
		}

	};
	
	/* 스마트폰 하단 기기의 목록을 누르면 발생하는 옵션 메뉴이다. 
	 * onCreateOptionsMenu() - 옵션 메뉴에 대한 목록을 설정
	 * onOptionsItemSelected() - 옵션 메뉴 중 메뉴를 선택할 때의 이벤트에 대한 처리 
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Java Code로 옵션메뉴 추가 하기
        menu.add(0, 0, Menu.NONE, "초기화").setIcon(android.R.drawable.ic_menu_rotate);
        menu.add(0, 1, Menu.NONE, "구조 요청").setIcon(android.R.drawable.ic_menu_add);

        // Menu에 SubMenu 추가
        SubMenu subMenu = menu.addSubMenu("지도 설정");
         
        subMenu.add(1, 2, Menu.NONE, "일반 지도");
        subMenu.add(1, 3, Menu.NONE, "위성 지도");

        return true;
    }
 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        switch (item.getItemId()) {
        case 0:
            mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
			mOverlayManager.clearOverlays();
			Toast.makeText(ChallengeActivity.this, "초기화 완료", Toast.LENGTH_SHORT).show();
            break;
        case 1:
        	SharedPreferences pref = getSharedPreferences("Muljomdao", MODE_PRIVATE);
	        for(int i=1; i<4 ; i++){
		        String Phonenumber = pref.getString("sosnumber"+i, "01094386235");	// 값이 없으면 119를 가져온다.  
		        sendSMS(Phonenumber, "긴급상황! 조난신고! ("+LoLatitude+", "+LoLongitude+")좌표로 빠르게 와주세요.");

		    }
            Toast.makeText(ChallengeActivity.this, "구조 요청 완료", Toast.LENGTH_SHORT).show();
            break;
        case 2:
        	mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
            Toast.makeText(ChallengeActivity.this, "일반 지도", Toast.LENGTH_SHORT).show();
            break;
        case 3:
        	mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
            Toast.makeText(ChallengeActivity.this, "위성 지도", Toast.LENGTH_SHORT).show();
            break;
        default:
            break;
        }
 
        return super.onOptionsItemSelected(item);
    }
	
    
  //Latitude, Longitude를 TextView에 출력
    private void showGPS(Location location) {
        if(location!=null)
        	// 위도 경도를 받아서 텍스트로 출력한다.
            myTextView.setText("Latitude :"+location.getLatitude() +
                               ", Longitude : "+location.getLongitude());  
    }
    
    // 도전코스를 클릭시 발생. 디폴틑 0
    private void showRecommandway(){
    	// set POI data
    	NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
    	poiData.beginPOIdata(2);

    	if (challengeSchoolstart == false){
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
    	else if (challengeSchoolstart == true){
			// 이동 경로를 추가하기 위한 객체
	    	NMapPathData pathData3 = new NMapPathData(3);
	    	// 이동 경로 표시 시작
	    	pathData3.initPathData();
			// addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
			poiData.addPOIitem(126.365527, 34.790750,  "출발점", NMapPOIflagType.FROM, 0);
			poiData.addPOIitem(126.365999, 34.791491,  "도착점", NMapPOIflagType.TO, 0);
			poiData.endPOIdata();

			// create POI data overlay
			mOverlayManager.createPOIdataOverlay(poiData, null);
			
			pathData3.addPathPoint(126.365527, 34.790750, NMapPathLineStyle.TYPE_DASH);
			pathData3.addPathPoint(126.366199, 34.791332, 0);
			pathData3.addPathPoint(126.365999, 34.791491, 0);

			// 경로 데이터 추가를 종료한다.
			pathData3.endPathData();

			// 지도상에 이동 경로 표시
			NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData3);
			
			// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
			pathDataOverlay.showAllPathData(0);
		}

    }
    
    private void showrange(){
    	if(challengeRange == true && challengeSchoolstart == false){
    		// set POI data
	    	NMapPOIdata poiData0 = new NMapPOIdata(1, mMapViewerResourceProvider);
	    	poiData0.beginPOIdata(1);
	    	
			NMapPathData pathData0 = new NMapPathData(5);
	    	// 이동 경로 표시 시작
	    	pathData0.initPathData();
			
	    	pathData0.addPathPoint(126.368504-0.00010, 34.786539-0.00010, NMapPathLineStyle.TYPE_DASH);
			pathData0.addPathPoint(126.368504+0.00010, 34.786539-0.00010, 0);
			pathData0.addPathPoint(126.368504+0.00010, 34.786539+0.00010, 0);
			pathData0.addPathPoint(126.368504-0.00010, 34.786539+0.00010, 0);
			pathData0.addPathPoint(126.368504-0.00010, 34.786539-0.00010, 0);

			// 경로 데이터 추가를 종료한다.
			pathData0.endPathData();

			// 지도상에 이동 경로 표시
			NMapPathDataOverlay pathDataOverlay0 = mOverlayManager.createPathDataOverlay(pathData0);
			
			// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
			pathDataOverlay0.showAllPathData(0);
			
		}
    	else if (challengeRange == true && challengeSchoolstart == true){
    		// set POI data
	    	NMapPOIdata poiData0 = new NMapPOIdata(1, mMapViewerResourceProvider);
	    	poiData0.beginPOIdata(1);
	    	
			NMapPathData pathData0 = new NMapPathData(5);
	    	// 이동 경로 표시 시작
	    	pathData0.initPathData();
			
	    	pathData0.addPathPoint(126.365527-0.00010, 34.790750-0.00010, NMapPathLineStyle.TYPE_DASH);
			pathData0.addPathPoint(126.365527+0.00010, 34.790750-0.00010, 0);
			pathData0.addPathPoint(126.365527+0.00010, 34.790750+0.00010, 0);
			pathData0.addPathPoint(126.365527-0.00010, 34.790750+0.00010, 0);
			pathData0.addPathPoint(126.365527-0.00010, 34.790750-0.00010, 0);

			// 경로 데이터 추가를 종료한다.
			pathData0.endPathData();

			// 지도상에 이동 경로 표시
			NMapPathDataOverlay pathDataOverlay0 = mOverlayManager.createPathDataOverlay(pathData0);
			
			// 지도상의 표시 위치와 축적을 이동 경로를 포함한 화면으로 초기화 
			pathDataOverlay0.showAllPathData(0);
		}
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
			showMyway();
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
		case R.id.b_challenge:	// 도전!
			if(boolstart == false && challengestart == true){
				
				 boolstart= true; // 시작 버튼을 눌렀다.
				 mywayonoff = true;
				 doit();		// 카운트 쓰레드가 시작된다.
				 NMapPOIdata poiData1 = new NMapPOIdata(2, mMapViewerResourceProvider);
				 poiData1.beginPOIdata(1);
				 // addPOIitem(경도, 위도, 표시문구, 표시할 마커 이미지의 id값, 오버래이를 관리하기위한 id값)
				 poiData1.addPOIitem(LoLongitude, LoLatitude,  "도전시작", NMapPOIflagType.FROM, 0);
				 poiData1.endPOIdata();
	
				 // create POI data overlay
				 mOverlayManager.createPOIdataOverlay(poiData1, null); 
			}
			else{
				Toast.makeText(this, "도전가능 지역이 아닙니다.", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.b_challengegiveup:	// 도전 포기
		{
			challengeSchoolstart = false;
			challengeRange =false;
			boolstart=false;
			
			Toast.makeText(this, "도전 포기!", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.b_challengeRange:	// 도전 범위 파악
		{
			challengeRange = true;
			showrange();
			Toast.makeText(this, "도전가능 지역 표시.", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.b_challengeSchool:	// 학교 테스트 버튼 
			{
				challengeSchoolstart = true;
				Toast.makeText(this, "학교 테스트 시작 버튼", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		showRecommandway();
    }
    
    private void sendSMS(String phoneNumber, String message) { // 문자전송
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(phoneNumber, null, message, null, null);	
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
			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {	//현재 오버레이 매니져가 my location 오버레이를 가지고 있지 않으면
				mOverlayManager.addOverlay(mMyLocationOverlay);	//my location 오버레이를 추가
				mMapView.postInvalidate();	//별도의 스레드에서 화면갱신을 할때 사용
			} 
			// 현재 위치 탐색을 시작한다. skipLastLocation을 true로 전달하면 마지막으로 탐색된 위치를 사용하지 않는다. 
			// 시스템 설정에 의해서 위치 탐색이 불가하다면 false를 반환한다.
			boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
			// 현재 위치 탐색 중인지 여부를 반환한다.
			if (!isMyLocationEnabled) {
				Toast.makeText(ChallengeActivity.this, "Please enable a My Location source in system settings",
					Toast.LENGTH_LONG).show();
				return;
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
					return new NMapCalloutCustomOverlayView(ChallengeActivity.this, itemOverlay, overlayItem, itemBounds);
				}
			}

			// null을 반환하면 말풍선 오버레이를 표시하지 않음
			return null;
		}

	};
	

	private void challengeSelect(){
		// 126.368504, 34.786539,  "출발점", NMapPOIflagType.FROM, 0)
		// 126.372690, 34.793492,  "도착점", NMapPOIflagType.TO, 0);
		// LoLatitude+", "+LoLongitude
		// 기록의 시작은 출발점의 좌표를 사각형의 범위를 두어 그 안에 현 위치 좌표를 두어 참이 되면 시작이 가능하다.
		if(LoLatitude >= 126.368504-0.00010 && LoLatitude <= 126.368504+0.00010 
				&& LoLongitude >= 34.786539-0.00010 && LoLongitude <= 34.786539+0.00010){
			challengestart = true;
		}
		if(LoLatitude >= 126.372690-0.00010 && LoLatitude <= 126.372690+0.00010 
				&& LoLongitude >= 34.793492-0.00010 && LoLongitude <= 34.793492+0.00010){
			challengeend = true;
			Toast.makeText(this, "도전가능 성공! 기록은..", Toast.LENGTH_SHORT).show();
		}
		// 테스트 중일때
		if(challengeSchoolstart == true){
			if(LoLatitude >= 126.365527-0.00010 && LoLatitude <= 126.365527+0.00010 
					&& LoLongitude >= 34.790750-0.00010 && LoLongitude <= 34.790750+0.00010){
				challengestart = true;
			}
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
    	for(int i=0; i < 6000 ; i++){
			if(limitline[i][0] != 0){
				limitline[i][0] =0;
				limitline[i][1] =0;
			}
			else
				break;
    	}
    	// 시작인지 종료인지 판단-초기화
    	boolstart=false;
    }

	
	private void doit(){
		new Thread(){
			@Override
			public void run(){
				loop();
			}
		}.start();
	}
	private void loop(){
		for(int i=1; i<= 60000; i++ ){
			try{
				
				handler.sendEmptyMessage(i);
				Thread.sleep(100);				
			} catch (InterruptedException e){
				
			}
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			Counttext.setText("타임어택 : "+msg.what);
		};
	};
	
	
}
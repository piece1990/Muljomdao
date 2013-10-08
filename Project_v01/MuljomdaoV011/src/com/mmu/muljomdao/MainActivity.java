package com.mmu.muljomdao;

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

public class MainActivity extends NMapActivity {

	// API-KEY
	public static final String API_KEY = "bcee0933baeff34c3bccb7f2ffba2a4a";
	// ���̹� �� ��ü
	NMapView mMapView = null;
	// �� ��Ʈ�ѷ�
	NMapController mMapController = null;
	// ���� �߰��� ���̾ƿ�
	LinearLayout MapContainer;
	
	// ���������� ���ҽ��� �����ϱ� ���� ��ü
	NMapViewerResourceProvider mMapViewerResourceProvider = null;
	// �������� ������
	NMapOverlayManager mOverlayManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/******************* ���� �ʱ�ȭ ********************/
		// ���̹� ������ �ֱ� ���� LinearLayout ������Ʈ
		MapContainer = (LinearLayout) findViewById(R.id.MapContainer);

		// ���̹� ���� ��ü ����
		mMapView = new NMapView(this);
		
		// ���� ��ü�κ��� ��Ʈ�ѷ� ����
		mMapController = mMapView.getMapController();

		// ���̹� ���� ��ü�� APIKEY ����
		mMapView.setApiKey(API_KEY);

		// ������ ���̹� ���� ��ü�� LinearLayout�� �߰���Ų��.
		MapContainer.addView(mMapView);

		// ������ ��ġ�� �� �ֵ��� �ɼ� Ȱ��ȭ
		mMapView.setClickable(true);
		
		// Ȯ��/��Ҹ� ���� �� ��Ʈ�ѷ� ǥ�� �ɼ� Ȱ��ȭ
		mMapView.setBuiltInZoomControls(true, null);
		
		
		/******************* �������� �ʱ�ȭ ********************/
		// �������� ���ҽ� ������ ����
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// �������� ������ ����. �� ��ü���� �ʺ�� �������� ������ �����ڸ� �����Ѵ�.
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

		/******************* ��� ��ġ�� ������ġ ���� ********************/
		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
		
		// addPOIitem(�浵, ����, ǥ�ù���, ǥ���� ��Ŀ �̹����� id��, �������̸� �����ϱ����� id��)
		poiData.addPOIitem(126.368449, 34.796053,  "�����", NMapPOIflagType.FROM, 0);
		poiData.addPOIitem(126.372690, 34.793492,  "������", NMapPOIflagType.TO, 0);
		
		poiData.endPOIdata();

		// create POI data overlay
		mOverlayManager.createPOIdataOverlay(poiData, null);

		/******************* �̵� ��� ���� ********************/
		// �̵� ��θ� �߰��ϱ� ���� ��ü
		NMapPathData pathData = new NMapPathData(9);

		// �̵� ��� ǥ�� ����
		pathData.initPathData();
		
		/* �̵� ��� �߰�
		 * addPathPoint(�浵, ����, ���ἱ)
		 * ���ἱ : NMapPathLineStyle Ŭ������ ��� ����
		 * NMapPathLineStyle.TYPE_SOLID - ����
		 * NMapPathLineStyle.TYPE_DASHED - ����
		 * ���ἱ ���� 0 ���� �ָ� ó�� ������ ���� ǥ��
		 */ 
		/* ��ε��꿡�� ���޻� ��������� �̵���� */
		pathData.addPathPoint(126.368449, 34.796053, NMapPathLineStyle.TYPE_DASH);
		pathData.addPathPoint(126.369301, 34.796342,  0);
		pathData.addPathPoint(126.369743, 34.796328,  0);
		pathData.addPathPoint(126.370112, 34.796525,  0);
		pathData.addPathPoint(126.370977, 34.796420,  0);
		pathData.addPathPoint(126.371136, 34.795213, 0);
		pathData.addPathPoint(126.372690, 34.793492,  0);

		// �̵� ��� ǥ�� ����
		pathData.endPathData();

		// ������ �̵� ��� ǥ��
		NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
		
		// �������� ǥ�� ��ġ�� ������ �̵� ��θ� ������ ȭ������ �ʱ�ȭ 
		pathDataOverlay.showAllPathData(0);
	}
}
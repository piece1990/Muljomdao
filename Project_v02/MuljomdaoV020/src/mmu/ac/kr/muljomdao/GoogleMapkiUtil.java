package mmu.ac.kr.muljomdao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GoogleMapkiUtil {
	/**
	 * 결과값 전송을 위한 Intent Extra 키
	 */
	final static public String RESULT = "result";
	/**
	 * 맵 검색이 실패했을 경우 Intent Extra 값
	 */
	final static public String FAIL_MAP_RESULT = "fail_map_result";
	/**
	 * 에러 Intent Extra 값
	 */
	final static public String ERROR_RESULT = "error_result";
	/**
	 * 성공 Intent Extra 값
	 */
	final static public String SUCCESS_RESULT = "success_result";
	/**
	 * 시간 지연 Intent Extra 값
	 */
	final static public String TIMEOUT_RESULT = "timeout_result";
	/**
	 * Log Client
	 */
	final static public String TAG_CLIENT = "client";
	/**
	 * Log Server
	 */
	final static public String TAG_SERVER = "server";

	/**
	 * 결과 String 값
	 */
	public String stringData;

	// 지도 검색 비동기 스레드
	private SearchThread searchThread;
	// 결과 전달을 위한 핸들러
	private Handler resultHandler;
	// HttpClient
	private HttpClient httpclient;
	// 메뉴 선택
	private int Foodchoice ;

	/**
	 * HttpClientUtil 객체 생성자
	 */
	public GoogleMapkiUtil() {
	}

	/**
	 * 지도 검색 요청 (Google Mapki 사용)
	 * 
	 * @param _resultHandler
	 *            결과를 받을 핸들러
	 * @param searchingName
	 *            검색어
	 * @param nearAddress
	 *            기준 주소
	 */
	public void foodrequestMapsearch(Handler _resultHandler, int fatherFoodchoice){
		resultHandler = _resultHandler;
		Foodchoice = fatherFoodchoice;
		searchThread = new SearchThread();
		searchThread.start();
	}

	// 지도 검색 스레드
	private class SearchThread extends Thread {
		private String parameters;

		public SearchThread(NameValuePair[] _nameValues) {
			parameters = encodeParams(_nameValues);
		}
		
		public SearchThread(){
			
		}

		public void run() {
			httpclient = new DefaultHttpClient();
			try {
				HttpGet get = new HttpGet();
				if(Foodchoice == 1)
					get.setURI(new URI("https://maps.google.co.kr/?q=횟집&near=전라남도+목포시&output=json&mrt=yp&radius=10&num=20"));
				else if (Foodchoice == 2)
					get.setURI(new URI("https://maps.google.co.kr/?q=낙지&near=전라남도+목포시&output=json&mrt=yp&radius=10&num=20"));
				else if (Foodchoice == 3)
					get.setURI(new URI("https://maps.google.co.kr/?q=바지락&near=전라남도+목포시&output=json&mrt=yp&radius=10&num=20"));
				else if (Foodchoice == 4)
					get.setURI(new URI("https://maps.google.co.kr/?q=커피&near=전라남도+목포시&output=json&mrt=yp&radius=10&num=20"));
				else
					get.setURI(new URI("https://maps.google.co.kr/?q=맛집&near=전라남도+목포시&output=json&mrt=yp&radius=10&num=20"));
				// 5초 응답시간 타임아웃
				HttpParams params = httpclient.getParams();
				params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				HttpConnectionParams.setSoTimeout(params, 10000);
				httpclient.execute(get, responseSearchHandler);

			} catch (ConnectTimeoutException e) {
				Message message = resultHandler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString(RESULT, TIMEOUT_RESULT);
				message.setData(bundle);
				resultHandler.sendMessage(message);
				stringData = e.toString();

			} catch (Exception e) {

				Message message = resultHandler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString(RESULT, ERROR_RESULT);
				message.setData(bundle);
				resultHandler.sendMessage(message);
				stringData = e.toString();
			} finally {
				httpclient.getConnectionManager().shutdown();
			}
		}
	}

	// 파라메터들을 GET 형식으로 묶어줌
	private String encodeParams(NameValuePair[] parameters) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parameters.length; i++) {
			sb.append(parameters[i].getName());
			sb.append('=');
			sb.append(parameters[i].getValue().replace(" ", "+"));
			if (i + 1 < parameters.length)
				sb.append('&');
		}

		return sb.toString();
	}

	// 지도검색 결과 핸들러
	private ResponseHandler<String> responseSearchHandler = new ResponseHandler<String>() {

		private String jsonString;

		@Override
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			StringBuilder sb = new StringBuilder();
			try {
				InputStreamReader isr = new InputStreamReader(response
						.getEntity().getContent(), "EUC-KR");
				BufferedReader br = new BufferedReader(isr);
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					sb.append(line + '\n');
				}
				br.close();

				String jsonString = sb.toString().substring(9);// while(1); 문자
																// 지우기
				Log.d(TAG_SERVER, jsonString);
				JSONObject jj = new JSONObject(jsonString);
				JSONObject overlays = jj.getJSONObject("overlays");
				JSONArray markers = overlays.getJSONArray("markers");
				if (markers != null) {

					ArrayList<String> searchList = new ArrayList<String>();
					String lat, lon;
					String address;
					for (int i = 0; i < markers.length(); i++) {
						address = markers.getJSONObject(i).getString("laddr");
						lat = markers.getJSONObject(i).getJSONObject("latlng")
								.getString("lat");
						lon = markers.getJSONObject(i).getJSONObject("latlng")
								.getString("lng");
						// 주소, 위도, 경도 순으로 저장함.
						searchList.add(address);
						searchList.add(lat);
						searchList.add(lon);
					}

					Message message = resultHandler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putString(RESULT, SUCCESS_RESULT);
					bundle.putStringArrayList("searchList", searchList);
					message.setData(bundle);
					resultHandler.sendMessage(message);
				} else {
					Message message = resultHandler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putString(RESULT, FAIL_MAP_RESULT);
					message.setData(bundle);
					resultHandler.sendMessage(message);

					stringData = "JSon >> \n" + sb.toString();
					return stringData;
				}

			} catch (Exception e) {

				Message message = resultHandler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString(RESULT, ERROR_RESULT);
				message.setData(bundle);
				resultHandler.sendMessage(message);

				stringData = "JSon >> \n" + e.toString();
				return stringData;
			}

			stringData = jsonString;
			return stringData;
		}
	};
}

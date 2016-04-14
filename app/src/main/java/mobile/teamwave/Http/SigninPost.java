package mobile.teamwave.Http;

import java.net.URI;

import mobile.teamwave.application.SimpleSSLSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SigninPost {
	public static JSONObject makeRequestLogin(JSONObject jsonSignin,
			String hostname) throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);
		String hostnamee = hostname + "api-token-auth/";
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonSignin.toString());

		HttpResponse response = null;
		try {
			httpost.setEntity(se);
			httpost.setHeader("Content-type", "application/json");
			httpost.setHeader("Accept", "*/*");
			httpost.setHeader("Accept-Encoding", "gzip, deflate");
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
			httpost.setHeader("User-Agent", "nnst");
			httpost.setHeader("Accept-Charset", "utf-8");
			response = client.execute(httpost);

		} catch (Exception e) {
			System.out.println("::my Exception ::" + e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());

		JSONObject jobj = new JSONObject(output);

		return jobj;
	}
	public static JSONObject makeRequestCurrentUser(String token,
			String hostname) throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);

		HttpGet httget = new HttpGet();
		httget.setURI(new URI(hostname + "users/currentuser"));
		System.out.println(hostname);
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}
		System.out.println(httget + "URI");
		int responseCode = 0;
		HttpResponse response = null;
		try {
			response = client.execute(httget);
			 responseCode=response.getStatusLine().getStatusCode();
			System.out.println(responseCode + "----- STATUS CODE");
			//it should get 401 as a response
		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());
		JSONObject jObj = new JSONObject(output.toString());
		
		
		if(String.valueOf(responseCode)=="401"||String.valueOf(responseCode).equals("401")){
			jObj.put("status_code", responseCode);
		}
		
		return jObj;
	}

	public static JSONObject makeRequestRefreshToken(JSONObject jsonToken,String token,
			String hostname) throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);
		String hostnamee = hostname + "api-token-refresh/";
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonToken.toString());
		int responseCode = 0;
		HttpResponse response = null;
		try {
			httpost.setEntity(se);
			httpost.setHeader("Content-type", "application/json");
			httpost.setHeader("Accept", "*/*");
			httpost.setHeader("Accept-Encoding", "gzip, deflate");
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
			httpost.setHeader("User-Agent", "nnst");
			httpost.setHeader("Accept-Charset", "utf-8");
			if (token == "NV") {
			} else {
				httpost.setHeader("Authorization", "Token" + " " + token);
			}
			response = client.execute(httpost);
			 responseCode=response.getStatusLine().getStatusCode();
				System.out.println(responseCode + "----- STATUS CODE");
		} catch (Exception e) {
			System.out.println("::my Exception ::" + e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());

		JSONObject jobj = new JSONObject(output);
		if(String.valueOf(responseCode)=="401"||String.valueOf(responseCode).equals("401")){
			jobj.put("status_code", responseCode);
		}
		return jobj;
	}
	public static JSONObject makeRequestCheck(String hostname, String token,String proId,String taskGrpId, String taskId,String status) 
			throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);
		String hostnamee = hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"/tasks/"+taskId+"/"+status;
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
	//	StringEntity se = new StringEntity(jsonSignin.toString());

		HttpResponse response = null;
		try {
		//	httpost.setEntity(se);
			httpost.setHeader("X-HTTP-Method-Override", "PATCH");
			httpost.setHeader("Content-type", "application/json");
			httpost.setHeader("Accept", "*/*");
			httpost.setHeader("Accept-Encoding", "gzip, deflate");
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
			httpost.setHeader("User-Agent", "nnst");
			httpost.setHeader("Accept-Charset", "utf-8");
			if (token == "NV") {
			} else {
				httpost.setHeader("Authorization", "Token" + " " + token);
			}
			response = client.execute(httpost);

		} catch (Exception e) {
			System.out.println("::my Exception ::" + e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());

		JSONObject jobj = new JSONObject(output);

		return jobj;
	}
	public static JSONObject makeRequestChecked(String hostname, String token,String proId,int i, int j,String status) 
			throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);
		String hostnamee = hostname+"projects/"+proId+"/taskgroups/"+i+"/tasks/"+j+"/"+status;
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
	//	StringEntity se = new StringEntity(jsonSignin.toString());

		HttpResponse response = null;
		try {
		//	httpost.setEntity(se);
			httpost.setHeader("X-HTTP-Method-Override", "PATCH");
			httpost.setHeader("Content-type", "application/json");
			httpost.setHeader("Accept", "*/*");
			httpost.setHeader("Accept-Encoding", "gzip, deflate");
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
			httpost.setHeader("User-Agent", "nnst");
			httpost.setHeader("Accept-Charset", "utf-8");
			if (token == "NV") {
			} else {
				httpost.setHeader("Authorization", "Token" + " " + token);
			}
			response = client.execute(httpost);

		} catch (Exception e) {
			System.out.println("::my Exception ::" + e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());

		JSONObject jobj = new JSONObject(output);

		return jobj;
	}
	public static JSONObject makeRequestForgot(JSONObject jsonForgot,
			String hostname) throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);
		String hostnamee = hostname + "forget-password/";
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonForgot.toString());
		HttpResponse response = null;
		try {
			httpost.setEntity(se);
			httpost.setHeader("Content-type", "application/json");
			httpost.setHeader("Accept", "*/*");
			httpost.setHeader("Accept-Encoding", "gzip, deflate");
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
			httpost.setHeader("User-Agent", "nnst");
			httpost.setHeader("Accept-Charset", "utf-8");
			response = client.execute(httpost);

		} catch (Exception e) {
			System.out.println("::my Exception ::" + e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());

		JSONObject jobj = new JSONObject(output);

		return jobj;
	}
	public static JSONArray makeRequestUsers(String hostname,String token,
			String proId) throws Exception {

		SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
		sslFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sslFactory, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		HttpClient client = new DefaultHttpClient(ccm, params);

		HttpGet httget = new HttpGet();
		httget.setURI(new URI(hostname +"projects/"+proId +"/users"));
		System.out.println(hostname +"projects/"+proId +"/users");
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}
		System.out.println(httget + "URI");
		int responseCode = 0;
		HttpResponse response = null;
		try {
			response = client.execute(httget);
			 responseCode=response.getStatusLine().getStatusCode();
			System.out.println(responseCode + "----- STATUS CODE");
			//it should get 401 as a response
		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}
		String output = EntityUtils.toString(response.getEntity());
		JSONArray jObj = new JSONArray(output.toString());
		
		
		return jObj;
	}
}

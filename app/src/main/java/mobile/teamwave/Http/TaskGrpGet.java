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

public class TaskGrpGet {
	public static JSONObject callTaskGrpList(String hostname, String token,String proId, String nextCall)
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

		HttpGet httget = new HttpGet();
		System.out.println(nextCall);
		if(nextCall==""||nextCall.equals("")){
			httget.setURI(new URI(hostname+"projects/"+proId+"/taskgroups"));
		}else{
			httget.setURI(new URI(nextCall));
		}
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname);
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");

		System.out.println(httget + "URI");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}

		HttpResponse response = null;
		try {
			response = client.execute(httget);

		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}

		String output = EntityUtils.toString(response.getEntity());
		JSONObject jobj = new JSONObject(output.toString());
		return jobj;

	}
	public static JSONObject callTasksList(String hostname, String token,String proId,String taskGrpId)
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

		HttpGet httget = new HttpGet();
		
			httget.setURI(new URI(hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"?is_completed=all"));
		
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"?is_completed=all");
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");

		System.out.println(httget + "URI");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}

		HttpResponse response = null;
		try {
			response = client.execute(httget);

		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}

		String output = EntityUtils.toString(response.getEntity());
		JSONObject jobj = new JSONObject(output.toString());
		return jobj;

	}
	public static JSONObject callTasksDetail(String hostname, String token,String proId,String taskGrpId,String taskId)
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

		HttpGet httget = new HttpGet();
		
			httget.setURI(new URI(hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"/tasks/"+taskId));
		
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"/tasks/"+taskId);
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");

		System.out.println(httget + "URI");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}

		HttpResponse response = null;
		try {
			response = client.execute(httget);

		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}

		String output = EntityUtils.toString(response.getEntity());
		JSONObject jobj = new JSONObject(output.toString());
		return jobj;

	}
	public static JSONArray callTaskComments(String hostname, String token,String proId,String taskId)
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

		HttpGet httget = new HttpGet();
		
			httget.setURI(new URI(hostname+"projects/"+proId+"/tasks/"+taskId+"/comments"));
		
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname+"projects/"+proId+"/tasks/"+taskId+"/comments");
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");

		System.out.println(httget + "URI");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}

		HttpResponse response = null;
		try {
			response = client.execute(httget);

		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}

		String output = EntityUtils.toString(response.getEntity());
		JSONArray jobj = new JSONArray(output.toString());
		return jobj;
	}
	public static JSONArray callTaskGrpComments(String hostname, String token,String proId,String taskGtpId)
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

		HttpGet httget = new HttpGet();
		
			httget.setURI(new URI(hostname+"projects/"+proId+"/taskgroups/"+taskGtpId+"/comments"));
		
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname+"projects/"+proId+"/taskgroups/"+taskGtpId+"/comments");
		httget.setHeader("Accept", "*/*");
		httget.setHeader("Accept-Encoding", "identity");
		httget.addHeader("Connection", "keep-alive");
		httget.setHeader("User-Agent", "nnst");
		httget.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		httget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httget.setHeader("Accept-Charset", "utf-8");

		System.out.println(httget + "URI");
		if (token == "NV") {
		} else {
			httget.setHeader("Authorization", "Token" + " " + token);
		}

		HttpResponse response = null;
		try {
			response = client.execute(httget);

		} catch (Exception e) {
			System.out.println(e);
		}
		if (response == null) {
			System.out.println("no data");
		}

		String output = EntityUtils.toString(response.getEntity());
		JSONArray jobj = new JSONArray(output.toString());
		return jobj;
	}
	
	//Creating task group
	public static JSONObject createTaskGroup(JSONObject json,String token,
			String hostname,String proId) throws Exception {

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
		String hostnamee = hostname + "projects/"+proId+"/taskgroups";
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(json.toString());
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
	//Creating task 
	public static JSONObject createTask(JSONObject json,String token,
			String hostname,String proId,int addTaskGrpId) throws Exception {

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
		String hostnamee = hostname + "projects/"+proId+"/taskgroups/"+addTaskGrpId+"/tasks";
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(json.toString());
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
}

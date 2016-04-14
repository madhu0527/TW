package mobile.teamwave.Http;

import mobile.teamwave.application.SimpleSSLSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
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
import org.json.JSONObject;
import teamwave.android.mobile.teamwave.R;
public class CommentsPost {
	public static JSONObject PostTaskComment(JSONObject jsonComment,
			String hostname, String token, String proId, String taskId) throws Exception {

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
		
		String hostnamee = hostname+"projects/"+proId+"/tasks/"+taskId+"/comments";
		
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonComment.toString());

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
	public static JSONObject PostTaskGrpComment(JSONObject jsonComment,
			String hostname, String token, String proId, String taskGrpId) throws Exception {

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
		
		String hostnamee = hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"/comments";
		
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonComment.toString());

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
	public static JSONObject PostFileComment(JSONObject jsonComment,
			String hostname, String token, String proId, String fileId) throws Exception {

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
		
		String hostnamee = hostname+"projects/"+proId+"/files/"+fileId+"/comments";
		
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonComment.toString());

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
	public static JSONObject PostNoteComment(JSONObject jsonComment,
			String hostname, String token, String proId, String noteId) throws Exception {

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
		
		String hostnamee = hostname+"projects/"+proId+"/notes/"+noteId+"/comments";
		
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonComment.toString());

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
	public static JSONObject PostEventsComment(JSONObject jsonComment,
			String hostname, String token, String proId, String eventId) throws Exception {

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
		
		String hostnamee = hostname+"projects/"+proId+"/events/"+eventId+"/comments";
		
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonComment.toString());

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
	public static JSONObject PostMesgsComment(JSONObject jsonComment,
			String hostname, String token, String proId, String eventId) throws Exception {

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
		
		String hostnamee = hostname+"projects/"+proId+"/messages/"+eventId+"/comments";
		
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(jsonComment.toString());

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

}

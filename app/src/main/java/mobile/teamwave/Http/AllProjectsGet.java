package mobile.teamwave.Http;

import java.net.URI;

import mobile.teamwave.application.SimpleSSLSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class AllProjectsGet {
	public static JSONArray callProsList(String hostname, String token)
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
		httget.setURI(new URI(hostname + "projects"));

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
		JSONArray jobj = new JSONArray(output.toString());
		return jobj;

	}
}

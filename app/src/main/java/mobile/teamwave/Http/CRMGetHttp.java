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

public class CRMGetHttp {
	public static JSONObject callDefaultPipelines(String hostname, String token,String userId,String pipeLineType,String userType,String filterType)
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
		if(userType=="ALLUSERDEALS"||userType.equals("ALLUSERDEALS")){
			httget.setURI(new URI(hostname + "crm/pipelines/"+pipeLineType+"?deal="+filterType));
		}else{
			httget.setURI(new URI(hostname + "crm/pipelines/"+pipeLineType+"?deal="+filterType+"&owner="+userId));
		}
	

		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname + "crm/pipelines/default?deal=open&owner="+userId + "!!!!!");
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
	public static JSONArray callPipelines(String hostname, String token)
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
		httget.setURI(new URI(hostname + "crm/pipelines"));

		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
		System.out.println(hostname + "crm/pipelines");
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
	public static JSONObject callPeopleList(String hostname, String token,String nextPeople)
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
		
		if(nextPeople==null||nextPeople=="null"||nextPeople==""||nextPeople.equals("")){
			httget.setURI(new URI(hostname + "contacts/people?sort=name"));
		}
		else{
			httget.setURI(new URI(nextPeople));
		}

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
	public static JSONArray callOrgaList(String hostname, String token)
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
		
			httget.setURI(new URI(hostname + "contacts/companies"));

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
	public static JSONObject callPeopleGenDetail(String hostname, String token,String peoOrOrg, String personId)
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
		
		httget.setURI(new URI(hostname + "contacts/"+peoOrOrg+"/"+personId));
		System.out.println(hostname + "contacts/" + peoOrOrg + "/" + personId + " !!!!!!!!!!!!!!!!!!!!!");
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
	public static JSONArray callPersonDeals(String hostname, String token,String contactType,String userId)
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
		httget.setURI(new URI(hostname +"crm/deals/filter?"+contactType+"="+userId+"&status=open"));
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
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
	public static JSONArray callPersonFlow(String hostname, String token,String peoOrOrg,String userId)
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
		httget.setURI(new URI(hostname +"crm/"+peoOrOrg+"/"+userId+"/activities"));
	//	https://app.teamwave.com/api/crm/people/446/activities
		// httget.setURI(new
		// URI("http://192.168.1.78:8010/restapi/articles/list/"));
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
	public static JSONArray callOrgPeopleList(String hostname, String token,String orgId)
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
		
			httget.setURI(new URI(hostname + "contacts/companies/"+orgId+"/people"));

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
	public static JSONObject callDealDetail(String hostname, String token,String dealId)
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
		httget.setURI(new URI(hostname + "crm/deals/"+dealId));
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
	public static JSONObject changeDealStage(String hostname, String token,String dealId,JSONObject json) 
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
		String hostnamee = hostname+"crm/deals/"+dealId+"/reorder";
		HttpPost httpost = new HttpPost(hostnamee);
		System.out.println(hostnamee);
		StringEntity se = new StringEntity(json.toString());

		HttpResponse response = null;
		try {
			httpost.setEntity(se);
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
	public static JSONArray callLinkAPerson(String hostname, String token,String searchStr)
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
		httget.setURI(new URI(hostname + "contacts/people/search?q="+searchStr));
		System.out.println(hostname + "contacts/people/search?q="+searchStr);
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
    public static JSONArray callLinkAOrg(String hostname, String token,String searchStr)
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
        httget.setURI(new URI(hostname + "contacts/companies?q="+searchStr));
        System.out.println(hostname + "contacts/companies?q="+searchStr);
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
    public static JSONObject makeRequestAddDeal(String hostname,String token,JSONObject jsonAddDeal) throws Exception {

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
        String hostnamee = hostname + "crm/deals";
        HttpPost httpost = new HttpPost(hostnamee);
        System.out.println(hostnamee);
        StringEntity se = new StringEntity(jsonAddDeal.toString());

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
		System.out.println("OUTPUT  "+output);
        JSONObject jobj = new JSONObject(output);

        return jobj;
    }
    public static JSONArray getCurrencies(String hostname, String token)
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
        //https://app.goteamwave.com/api/settings/currencies
        httget.setURI(new URI(hostname + "settings/currencies"));
        System.out.println(hostname + "settings/currencies");
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

package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import model.SettingsSingleton;

public class APIController
{
	private final static String BASE_URL_IR = "https://api.irail.be/connections/";
	private final static String BASE_URL_TT = "https://traintracks.online/api/";
	private final static String BASE_URL_G3 = "http://nmbs-team.tk/api/";

	private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36";

	private String url;
	private HashMap<String, String> params;
	private RequestType requestType;
	private String base_url;
	private SettingsSingleton settings;

	public enum RequestType
	{
		GET, POST, PUT, DELETE, MASSPUT, GETFILE
	}

	public enum APIUrl
	{
		TRAINTRACKS, IRAILS, G3
	}

	public APIController(APIUrl apiUrl, String url, RequestType requestType, HashMap<String, String> params)
	{
		this.url = url;
		this.params = params;
		this.requestType = requestType;
		this.settings = SettingsSingleton.getSettings();

		switch (apiUrl) {
		case TRAINTRACKS:
			this.base_url = BASE_URL_TT;
			break;
		case IRAILS:
			this.base_url = BASE_URL_IR;
			break;
		default:
			this.base_url = BASE_URL_G3;
			break;
		}
	}
	
	public JSONArray getJsonResult() throws UnsupportedOperationException, IOException
	{
		HttpResponse response = null;

		switch (this.requestType) {
		case GET:
			response = this.getRequest();
			break;

		case POST:
			response = this.postRequest();
			break;

		case PUT:
			response = this.putRequest();
			break;
			
		case MASSPUT:
			response = this.massPutRequest();
			break;
			
		case GETFILE:
			response = this.getFileRequest();
			return this.httpToFile(response);

		case DELETE:
			break;
		}

		return this.httpToJson(response);
	}


	private JSONArray httpToJson(HttpResponse response) throws UnsupportedOperationException, IOException
	{

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		String resultStr = "";
		
		if (result.toString().equals(""))
			return null;
			
		if (!result.substring(0, 1).equals("["))
			resultStr = "[" + result.toString() + "]";
		else
			resultStr = result.toString();
	

		System.out.println(url);
		System.out.println(resultStr);
		System.out.println("   -apicontroller:21");
		System.out.println("  ");
		
		return new JSONArray(resultStr);
	}
	
	private JSONArray httpToFile(HttpResponse response) throws UnsupportedOperationException, IOException
	{
		JSONObject result = new JSONObject();
		
		InputStream initialStream = response.getEntity().getContent();
		File targetFile = new File(this.params.get("TargetFilePath"));
		
		FileUtils.copyInputStreamToFile(initialStream, targetFile);
		result.append("FileSaved", true);

		return new JSONArray(result);
	}

	private HttpResponse getRequest() throws ClientProtocolException, IOException
	{
		if (this.base_url.equals(BASE_URL_IR)) {
			this.params.putIfAbsent("format", "json");
			this.params.putIfAbsent("timeSel", "depart");
			this.params.putIfAbsent("lang", "NL");
		}

		boolean firstItt = true;

		for (String key : params.keySet())
		{
			if (firstItt) {
				url += "?" + key + "=" + params.get(key);
				firstItt = false;
			}
			else {
				url += "&" + key + "=" + params.get(key);
			}
		}
		
		String finalUrl = this.base_url + url;
		if (this.base_url.equals(BASE_URL_TT))
		{
			finalUrl = finalUrl.replace(" ", "_");
			finalUrl = finalUrl.replace("-", "_");
		}
		else if (this.base_url.equals(BASE_URL_IR))
		{
			finalUrl = finalUrl.replace(" ", "+");
		}
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(finalUrl);

		// add header
		get.setHeader("User-Agent", USER_AGENT);
		get.setHeader("Authorization", "Bearer " + this.settings.getApiToken());

		return client.execute(get);
	}
	
	private HttpResponse getFileRequest() throws ClientProtocolException, IOException
	{
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(this.base_url + url);

		// add header
		get.setHeader("User-Agent", USER_AGENT);
		get.setHeader("Authorization", "Bearer " + this.settings.getApiToken());

		return client.execute(get);
	}

	private HttpResponse postRequest() throws ClientProtocolException, IOException
	{
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(this.base_url + url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Authorization", "Bearer " + this.settings.getApiToken());

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		for (Map.Entry<String, String> entry : this.params.entrySet()) {
			urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		return client.execute(post);
	}

	private HttpResponse putRequest() throws ClientProtocolException, IOException
	{
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(this.base_url + url);

		// add header
		put.setHeader("User-Agent", USER_AGENT);
		put.setHeader("Authorization", "Bearer " + this.settings.getApiToken());

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		for (Map.Entry<String, String> entry : this.params.entrySet()) {
			urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		 put.setEntity(new UrlEncodedFormEntity(urlParameters));

		 return client.execute(put);
	}
	
	private HttpResponse massPutRequest() throws ClientProtocolException, IOException
	{
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(this.base_url + url);

		// add header
		put.setHeader("User-Agent", USER_AGENT);
		put.setHeader("Authorization", "Bearer " + this.settings.getApiToken());
		put.setHeader("Content-Type", "application/json");
		
		JSONArray list = new JSONArray();
		JSONObject urlParameters = new JSONObject();
		
		for (String key : params.keySet())
		{
			list = new JSONArray(params.get(key));
			
			urlParameters = new JSONObject();
			urlParameters.put(key, list);
		}
		
		String jsonString = urlParameters.toString();
		
		jsonString = jsonString.replace(":false", ":0");
		jsonString = jsonString.replace(":true", ":1");
		
		System.out.println(jsonString);
		
		put.setEntity(new StringEntity(jsonString));

		return client.execute(put);
	}
	
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public HashMap<String, String> getParams()
	{
		return params;
	}

	public void setParams(HashMap<String, String> params)
	{
		this.params = params;
	}

	public RequestType getRequestType()
	{
		return requestType;
	}

	public void setRequestType(RequestType requestType)
	{
		this.requestType = requestType;
	}
}

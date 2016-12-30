package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import model.SettingsSingleton;

public class URLCon {

	private static BufferedReader reader;
	private static URL url;
	
	private static final HostnameVerifier defaultHV = HttpsURLConnection.getDefaultHostnameVerifier();
	private static final SSLSocketFactory defaultSF = HttpsURLConnection.getDefaultSSLSocketFactory();
	private static SettingsSingleton settings;

	public static String readUrl(String urlString, String rqMethod) throws IOException {
		try {
			
			settings = SettingsSingleton.getSettings();
			
			enableCertificateValidation();
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(rqMethod);
			connection.setRequestProperty("Authorization", "Bearer " + settings.getApiToken());
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	public static String readUnsecureUrl(String urlString) throws IOException {
		try {
			disableCertificateValidation();
			url = new URL(urlString);
			HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	@Deprecated
	public static void disableCertificateValidation() {
		// trust all certificates - solution found on stackoverflow
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void enableCertificateValidation() {
		try {
			HttpsURLConnection.setDefaultSSLSocketFactory(defaultSF);
			HttpsURLConnection.setDefaultHostnameVerifier(defaultHV);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

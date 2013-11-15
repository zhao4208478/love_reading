package com.example.love_reading.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DataUtil {

	private static String id;

	private static String url = "https://api.douban.com/v2/book/isbn/:";

	private String bookUrl = ""; 
	/**
	 * set id
	 */
	public void setId(String content) {
		DataUtil.id = content;
	}

	/**
	 * 
	 * @return ɨ���ά�������id
	 */
	private String getId() {
		return id;
	}
	private String getUrl(){
		return url;
	}
	private String getBookUrl(){
		return getUrl() + getId();
	}

	/**
	 * ����ҳ��ȡ����
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 */
	public JSONObject getDataFromPHP() throws ClientProtocolException,
			IOException, JSONException {
		
		JSONObject jsonObject = null;
		// ��������
		HttpPost request = new HttpPost(getBookUrl());

		// ��������
		if (request != null) {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String retSrc = EntityUtils.toString(httpResponse.getEntity());
				// ���� JSON ����
				jsonObject = new JSONObject(retSrc);
			} else {
				Log.i("HttpPost", "����ʧ��");
			}
		}
		return jsonObject;
	}

}

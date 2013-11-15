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
	 * @return 扫描二维码产生的id
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
	 * 从网页获取数据
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 */
	public JSONObject getDataFromPHP() throws ClientProtocolException,
			IOException, JSONException {
		
		JSONObject jsonObject = null;
		// 建立连接
		HttpPost request = new HttpPost(getBookUrl());

		// 发送请求
		if (request != null) {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String retSrc = EntityUtils.toString(httpResponse.getEntity());
				// 生成 JSON 对象
				jsonObject = new JSONObject(retSrc);
			} else {
				Log.i("HttpPost", "请求失败");
			}
		}
		return jsonObject;
	}

}

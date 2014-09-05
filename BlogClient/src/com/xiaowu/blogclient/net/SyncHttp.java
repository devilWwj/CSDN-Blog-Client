package com.xiaowu.blogclient.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class SyncHttp {
	/**
	 * 通过Get方式发送请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String httpGet(String url, String params) throws Exception {
		String response = null;	//返回信息
		//拼接请求URl
		if(null != params && !params.equals("")) {
			url += "?" + params;
		}
		
		int timeOutConnection = 3000;
		int timeOutSocket = 5000; 
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeOutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeOutSocket);
		
		//构造HttpClient实例
		HttpClient httpClient = new DefaultHttpClient();
		//创建GET方法实例
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK) {
				//获得返回结果
				response = EntityUtils.toString(httpResponse.getEntity());
			}
			else{
				response = "返回码:" + statusCode;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}
	
	/**
	 * 通过post方式发送请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String httpPost(String url, List<Parameter> params) throws Exception {
		String response  = null;
		int timeOutConnection = 3000;
		int timeOutSocket = 5000; 
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeOutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeOutSocket);
		
		
		//构造HttpClient实例
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if(params.size() > 0) {
			//设置post请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(buildNameValuePair(params), HTTP.UTF_8));
		}
		
		//使用execute方法发送Http Post 请求，并返回HttpResponse对象
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if(statusCode == HttpStatus.SC_OK) {
			//获得返回结果
			response = EntityUtils.toString(httpResponse.getEntity());
		}
		else {
			response = "返回码:" + statusCode;
		}
		return response;
	}
		
	/**
	 * 把Paramster类型集合转换为NameValuePair类型集合
	 * @param params
	 * @return
	 */
	private List<BasicNameValuePair> buildNameValuePair (List<Parameter> params) {
		List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		for(Parameter param : params) {
			BasicNameValuePair pair = new BasicNameValuePair(param.getName(), param.getValue());
			result.add(pair);
		}
		return result;
	}
}

package com.cookienats.common.papimq.common.utils;

import java.io.IOException;

import com.google.protobuf.GeneratedMessageV3;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom2.internal.ArrayCopy;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;

public class HttpProtoUtil {

	public static <T extends GeneratedMessageV3> T post(int i, GeneratedMessageV3 request, Parser<T> parser) throws ClientProtocolException, IOException {
		byte[] arr = request.toByteArray();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String packageName = request.getClass().getPackage().toString();
		HttpPost httpPost = new HttpPost("http://localhost:8080/PapiMQ/"+ packageName.substring(packageName.lastIndexOf(".")+1)+"/msg"+ i);
		//httpPost.addHeader("Content-Type","application/x-protobuf");
		//httpPost.addHeader("Accept", "application/x-protobuf");

		ByteArrayEntity entity = new ByteArrayEntity(arr);
		httpPost.setEntity(entity);
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		try {
		    System.out.println(response2.getStatusLine());
		    HttpEntity entity2 = response2.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    byte[] b = EntityUtils.toByteArray(entity2);
		    EntityUtils.consume(entity2);
		    return (T) parser.parseFrom(b);
		} finally {
		    response2.close();
		}
	}

}

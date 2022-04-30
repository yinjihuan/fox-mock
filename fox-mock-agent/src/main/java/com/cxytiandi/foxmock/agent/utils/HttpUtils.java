package com.cxytiandi.foxmock.agent.utils;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class HttpUtils {

	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

	public static String get(String url) {
		HttpURLConnection connection = null;
		try {
			URL getUrl = new URL(url);
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; CIBA)");
			connection.setRequestProperty("Accept-Language", "zh-cn");
			connection.connect();
			return IOUtils.toString(connection.getInputStream(), "UTF-8");
		} catch (Exception e) {
			LOG.error("http request exception, url is {}", url, e);
		} finally {
			if (Objects.nonNull(connection)) {
				connection.disconnect();
			}
		}

		return null;
	}
	
}

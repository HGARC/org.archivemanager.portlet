package org.archivemanager.portal.listener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexerPostProcessor;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portlet.journal.model.JournalArticle;


public class JounalArticleIndexerPostProcessor extends BaseIndexerPostProcessor {
	private static Log log = LogFactoryUtil.getLog(JounalArticleIndexerPostProcessor.class);
	
	public void postProcessDocument(Document document, Object object) throws Exception {
		if(object instanceof JournalArticle) {
			JournalArticle article = (JournalArticle)object;
			String json = JSONFactoryUtil.looseSerialize(article);
			String url = PropsUtil.get("openapps.service.url") + "/service/import/entity.json";
			String collectionId = PropsUtil.get("archivemanager.portal.content.collection.id");
			if(collectionId != null && collectionId.length() > 0) {
				//httpPost(url + "?collectionId=" + collectionId, json);
				System.out.println("Journal article:"+article.getArticleId()+" updated");
			}
		}
	}
	/*
	public String httpPost(String url, String body) throws ClientProtocolException, IOException {		
		String result = "";	
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);		
		try {	
			httppost.setEntity(new StringEntity(body, "UTF-8"));
			final HttpParams httpParameters = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			HttpConnectionParams.setSoTimeout        (httpParameters, 10000);
			httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = httpclient.execute(httppost);
			result = convertStreamToString(response.getEntity().getContent());			
		} catch(Exception e) {
			//int status = e.getStatusCode();
			//String msg = e.getMessage();
			//log.debug("Status Code:"+status+" Message:"+msg);		
			log.error(e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	*/
	/**
	 * Converts the specified <code>InputStream</code> into a string.
	 * 
	 * @param is the <code>InputStream</code> to read from
	 * 
	 * @return the resulting string that was read from the stream
	 * 
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
}

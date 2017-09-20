/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package jazz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.xerces.parsers.DOMParser;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestClass {
	public static void main(String[] args) {
		BufferedReader input = null;
		PrintWriter output = null;
		try {
			TrustManager[] trustAllCerts = { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
			} };
			SSLContext mySSLContext = SSLContext.getInstance("SSL");
			mySSLContext.init(null, trustAllCerts, new SecureRandom());

			SSLSocketFactory sf = new SSLSocketFactory(mySSLContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 9443, sf));
			ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

			DefaultHttpClient httpclient = new DefaultHttpClient(ccm);
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();

			localContext.setAttribute("http.cookie-store", cookieStore);
			HttpGet httpGetID = new HttpGet("https://eecsaruh6how077.prod.mobily.lan:9443/ccm/authenticated/identity");
			httpclient.execute(httpGetID, localContext);
			httpGetID.abort();

			List cookies1 = cookieStore.getCookies();
		//	for (Cookie cookie : cookies1) {
		//		System.out.println("\t" + cookie.getName() + " : " + cookie.getValue());
		//	}

			List authFormParams = new ArrayList();
			authFormParams.add(new BasicNameValuePair("j_username", "a.baig.dar"));
			authFormParams.add(new BasicNameValuePair("j_password", "a.baig.dar"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(authFormParams, "UTF-8");
			HttpPost httpPostAuth = new HttpPost(
					"https://eecsaruh6how077.prod.mobily.lan:9443/jazz/authenticated/j_security_check");
			httpPostAuth.setEntity(entity);

			httpclient.execute(httpPostAuth, localContext);

			List cookies2 = cookieStore.getCookies();
		//	for (Cookie cookie : cookies2) {
			//	System.out.println("\t" + cookie.getName() + " : " + cookie.getValue());
			//}

			input = new BufferedReader(new FileReader("C:/id.txt"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss-SS a");
			output = new PrintWriter(sdf.format(new Date()) + ".csv");
			output.write(
					"Id,Category,Creator,Creation Date,Resolution Date,Internal State,Sr Number,Modified Date,Phase, Phase Status, Modified By,Defect Type,Status,Filed Against,Owned By\n");
				//	"Id, Phase, Phase Status, SubPhase \n");
			HttpEntity httpEntity = null;
			String responseString = null;
			HttpResponse resp = null;
			Node rootNode = null;
			org.w3c.dom.Document doc = null;
			org.jsoup.nodes.Document docTable = null;
			NodeList nl = null;
			String htmlContent = "";
			String id = "";
			String srNumber = "";
			int count = 0;

			while ((id = input.readLine()) != null)
				if ((id != null) && (!(id.trim().equals("")))) {
					httpGetID = new HttpGet(
							"https://eecsaruh6how077.prod.mobily.lan:9443/ccm/service/com.ibm.team.workitem.common.internal.rest.IWorkItemRestService/workItemDTO2?id="
									+ id
									+ "&includeAttributes=true&includeLinks=false&includeApprovals=false&includeHistory=true&includeLinkHistory=false");
					resp = httpclient.execute(httpGetID, localContext);

					httpEntity = resp.getEntity();
					responseString = EntityUtils.toString(httpEntity, "UTF-8");
					
					System.out.println(responseString);

					doc = parseXMLString(responseString, null, null);

					String key = "";
					String category = "";
					String creator = "";
					String creationDate = "";
					String resolutionDate = "";
					String internalState = "";
					String Modified_Date = "";
					String Modified_By = "";
					String Defect_Type = "";
					String Status = "";
					String Filed_Against = "";
					String Owned_By = "";
					String Phase = "";
					//String Phase Status = "";
					String SubPhase = "";
					String TextExecCycles = "";

					
					int i = 0;
					for (; i < doc.getElementsByTagName("attributes").getLength(); ++i) {
						
						rootNode = doc.getElementsByTagName("attributes").item(i);
						nl = rootNode.getChildNodes();
						key = getValueFromNode(rootNode.getChildNodes(), "key", "key");
						if ((key != null) && (key.trim().equalsIgnoreCase("category"))) {
							System.out.println("11111111");
							category = getValueFromNode(rootNode.getChildNodes(), "value/path", "path");
						} else if ((key != null) && (key.trim().equalsIgnoreCase("creator"))) {
							System.out.println("2222222222");
							creator = getValueFromNode(rootNode.getChildNodes(), "value/userId", "userId");
						} else if ((key != null) && (key.trim().equalsIgnoreCase("creationDate"))) {
							System.out.println("3333333333");
							creationDate = getValueFromNode(rootNode.getChildNodes(), "value/id", "id");
						} else if ((key != null) && (key.trim().equalsIgnoreCase("resolutionDate"))) {
							System.out.println("4444444444444");
							resolutionDate = getValueFromNode(rootNode.getChildNodes(), "value/id", "id");
						} else {
							System.out.println("5555555555555");
							if ((key == null) || (!(key.trim().equalsIgnoreCase("internalState"))))
								continue;
							internalState = getValueFromNode(rootNode.getChildNodes(), "value/label", "label");
						}

					}

				//	int i = 0;
					for (; i < doc.getElementsByTagName("changes").getLength(); ++i) {
						
						//System.out.println("11111111111");
						rootNode = doc.getElementsByTagName("changes").item(i);
						nl = rootNode.getChildNodes();

						Modified_By = getValueFromNode(rootNode.getChildNodes(), "modifiedBy/userId", "userId");
						Modified_Date = getValueFromNode(rootNode.getChildNodes(), "modifiedDate", "modifiedDate");

						htmlContent = getValueFromNode(rootNode.getChildNodes(), "content", "content");

						srNumber = null;
						Defect_Type = null;
						Status = null;
						Filed_Against = null;
						Owned_By = null;

						docTable = Jsoup.parse(htmlContent);

						Iterator localIterator3 = docTable.select("table").iterator();

						while (localIterator3.hasNext()) {
							org.jsoup.nodes.Element table = (org.jsoup.nodes.Element) localIterator3.next();

							Iterator localIterator4 = table.select("tr").iterator();

							while (localIterator4.hasNext()) {
								org.jsoup.nodes.Element row = (org.jsoup.nodes.Element) localIterator4.next();
								Elements tds = row.select("td");
								if ((tds.size() > 0) && (((org.jsoup.nodes.Element) tds.get(0)).text() != null)
										&& (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
												.indexOf("SR Number") != -1)) {
									if (tds.size() > 1) {
										System.out.println(((org.jsoup.nodes.Element) tds.get(1)).text());
										srNumber = ((org.jsoup.nodes.Element) tds.get(1)).text();
									}
								} else if ((tds.size() > 0) && (((org.jsoup.nodes.Element) tds.get(0)).text() != null)
										&& (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
												.indexOf("Modified Date") != -1)) {
									if (tds.size() > 1)
										Modified_Date = ((org.jsoup.nodes.Element) tds.get(1)).text();
								} else if ((tds.size() > 0) && (((org.jsoup.nodes.Element) tds.get(0)).text() != null)
										&& (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
												.indexOf("Modified By") != -1)) {
									if (tds.size() > 1)
										Modified_By = ((org.jsoup.nodes.Element) tds.get(1)).text();
								} else if ((tds.size() > 0) && (((org.jsoup.nodes.Element) tds.get(0)).text() != null)
										&& (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
												.indexOf("Defect Type") != -1)) {
									if (tds.size() > 1)
										Defect_Type = ((org.jsoup.nodes.Element) tds.get(1)).text();
								} else if ((tds.size() > 0) && (((org.jsoup.nodes.Element) tds.get(0)).text() != null)
										&& (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
												.indexOf("Status") != -1)) {
									if (tds.size() > 1)
										Status = ((org.jsoup.nodes.Element) tds.get(1)).text();
								} else if ((tds.size() > 0) && (((org.jsoup.nodes.Element) tds.get(0)).text() != null)
										&& (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
												.indexOf("Filed Against") != -1)) {
									if (tds.size() > 1)
										Filed_Against = ((org.jsoup.nodes.Element) tds.get(1)).text();
								} else {
									if ((tds.size() <= 0) || (((org.jsoup.nodes.Element) tds.get(0)).text() == null)
											|| (((org.jsoup.nodes.Element) tds.get(0)).text().trim()
													.indexOf("Owned By") == -1)
											|| (tds.size() <= 1))
										continue;
									Owned_By = ((org.jsoup.nodes.Element) tds.get(1)).text();
								}

							}
							System.out.println(id + "," + category + "," + creator + "," + creationDate + "," + resolutionDate
									+ "," + internalState + "," + srNumber + "," + Modified_Date + "," + Modified_By + ","
									+ Defect_Type + "," + Status + "," + Filed_Against + "," + Owned_By + "\n");
						}

						if ((Modified_Date == null) && (Modified_By == null) && (Defect_Type == null)
								&& (Status == null) && (Filed_Against == null) && (Owned_By == null))
							continue;
						output.write(id + "," + category + "," + creator + "," + creationDate + "," + resolutionDate
								+ "," + internalState + "," + srNumber + "," + Modified_Date + "," + Modified_By + ","
								+ Defect_Type + "," + Status + "," + Filed_Against + "," + Owned_By + "\n");
						
						

						++count;
						if (count % 1000 == 0) {
							System.out.println("flushing...");
							try {
								output.flush();
							} catch (Exception localException1) {
							}
						}
					}
				}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception localException4) {
			}
			try {
				if (output != null) {
					output.flush();
					output.close();
				}
			} catch (Exception localException5) {
			}
		}
	}

	public static org.w3c.dom.Document parseXMLString(String aXMLStr, String msisdn, String requestId)
			throws Exception {
		DOMParser parser = new DOMParser();
		try {
			StringReader strReader = new StringReader(aXMLStr);
			InputSource inputSource = new InputSource(strReader);
			parser.parse(inputSource);

			strReader.close();
		} catch (IOException ex) {
			System.out.println("parseXMLString - MSISDN: " + msisdn + ", RequestId: " + requestId);
			throw ex;
		} catch (SAXException saxEX) {
			System.out.println("parseXMLString - MSISDN: " + msisdn + ", RequestId: " + requestId);
			throw saxEX;
		} catch (Exception exp) {
			System.out.println("parseXMLString - MSISDN: " + msisdn + ", RequestId: " + requestId);
			throw exp;
		}
		return parser.getDocument();
	}

	private static String getValueFromNode(NodeList nl, String xpath, String element) {
		String[] nodeTitles = xpath.split("/");
		String value = null;
		for (int i = 0; i < nodeTitles.length; ++i) {
			if ((nodeTitles[i] != null) && (!(nodeTitles[i].trim().equals("")))) {
				for (int j = 0; j < nl.getLength(); ++j) {
					if ((nl.item(j).getLocalName() == null)
							|| (!(nl.item(j).getLocalName().equalsIgnoreCase(nodeTitles[i]))))
						continue;
					if (nl.item(j).getLocalName().equalsIgnoreCase(element)) {
						org.w3c.dom.Element l_Node = (org.w3c.dom.Element) nl.item(j);
						if (l_Node.getFirstChild() == null)
							break;
						value = l_Node.getFirstChild().getNodeValue();
						break;
					}
					nl = nl.item(j).getChildNodes();
					j = -1;
				}
			}
		}

		return value;
	}
}
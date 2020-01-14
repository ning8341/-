package spiderImage;

/**
 * @author dongwn
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupUtil {
	/**
	 * Jspup工具类 url:采集的URL domian: 采集的域名
	 */
	public static Document getDocument(String url, String domain) {
		int error_count = 0;
		Document doc = null;
		while (true) {
			if (error_count > 10) {
				break;
			}
			try {
				doc = Jsoup.connect(url).timeout(6000)
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
						.header("Accept-Encoding", "gzip,deflate,sdch").header("Connection", "keep-alive")
						.header("referer", domain).header("cookie", "data").followRedirects(true)
						.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").get();
			} catch (Exception e) {
				error_count++;
			}
			if (doc != null) {
				break;
			}
		}
		return doc;
	}

	public static Document parseHtml(String html) {
		return Jsoup.parse(html);
	}

}

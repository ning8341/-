package spiderImage;
/**
* @author dongwn
*/

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TestSImage {

	public static final String PRE_URL = "https://www.ivsky.com/bizhi/nvxing/";// 目标网址

	public static final String SOURCE_HTML = "https://www.ivsky.com/";// 目标首页地址

	public static final String GENERATE_PATH = "E:\\2\\";// 本地保存的路径

	/**
	 * 
	 * @param index
	 *            组装下一页的计数变量，如index_1 index_2
	 * @throws Exception
	 *             使用递归的方式爬取图片
	 */
	public static void getGirlImage(String detailHtml, String sourceHtml, String generatePath, int index)
			throws Exception {
		String url = detailHtml;
		Document doc = JsoupUtil.getDocument(url, sourceHtml);
		Element element = doc.getElementsByClass("ali").first();
		// 3.提取图片
		Document imgDoc = JsoupUtil.parseHtml(element.toString());
		Elements elements = imgDoc.select("img[src]");
		String picFile = generatePath;

		for (int i = 0; i < elements.size(); i++) {
			Element ele = elements.get(i);
			String src = ele.attr("src");// 获取到src的值
			src = "http:" + src;
			String name = src.substring(src.lastIndexOf("/") + 1, src.length());
			FileUtils.copyURLToFile(new URL(src), new File(picFile + "\\" + Math.random() * 10000 + name));
			if (i == elements.size() - 1) {// 如果为本页最后一张则组装目标页面地址
				index++;
				detailHtml = PRE_URL + "index_" + index + ".html";// 拼装下一页访问地址
				System.out.println(detailHtml);
				getGirlImage(detailHtml, sourceHtml, generatePath, index);// 继续调用获取资源
			}
		}
	}

	public static void main(String[] args) throws MalformedURLException, IOException, Exception {
		getGirlImage(PRE_URL, SOURCE_HTML, GENERATE_PATH, 0);
	}
	
	

}

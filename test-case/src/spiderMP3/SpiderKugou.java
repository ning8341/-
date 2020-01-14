package spiderMP3;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import spiderUtils.HtmlManage;
import spiderUtils.HttpGetConnect;

/**
 * @author dongwn
 */
public class SpiderKugou {

	private static String filePath;// 文件地址

	private static String LINK;// 酷狗地址

	private static String mp3;// mp3地址

	static {
		filePath = "E:\\3\\";
		LINK = "https://www.kugou.com/yy/rank/home/PAGE-8888.html?from=rank";
		mp3 = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&callback=jQuery19103632090130122796_1558800325111&"
				+ "hash=HASH&_=TIME";
	}

	public static void main(String[] args) throws Exception {
		for (int i = 7; i < 23; i++) {

			String url = LINK.replace("PAGE", i + "");
			downSong(url);
		}
	}

	/**
	 * 下载歌曲
	 * 
	 * @param url
	 * @throws Exception
	 */
	private static void downSong(String url) throws Exception {

		HttpGetConnect connect = new HttpGetConnect();
		String content = connect.connect(url, "utf-8");
		HtmlManage html = new HtmlManage();
		Document doc = html.manage(content);
		Element ele = doc.getElementsByClass("pc_temp_songlist").get(0);
		Elements elements = ele.getElementsByTag("li");
		for (int i = 0; i < elements.size(); i++) {

			Element item = elements.get(i);
			String title = item.attr("title").trim();
			String link = item.getElementsByTag("a").first().attr("href");
			downLoad(link, title);
			Thread.sleep(1000);
		}
	}

	/**
	 * 下载
	 * 
	 * @param url
	 * @param name
	 * @throws IOException
	 */
	private static void downLoad(String url, String name) throws IOException {

		String hash = "";
		HttpGetConnect connect = new HttpGetConnect();
		String content = connect.connect(url, "utf-8");

		String regEx = "\"hash\":\"[0-9A-Z]+\"";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			hash = matcher.group();
			hash = hash.replace("\"hash\":\"", "");
			hash = hash.replace("\"", "");
		}

		String item = mp3.replace("HASH", hash);
		item = item.replace("TIME", System.currentTimeMillis() + "");
		System.out.println("item:" + item);

		String mp = connect.connect(item, "utf-8");
		mp = mp.substring(mp.indexOf("(") + 1, mp.length() - 3);

		JSONObject json = JSON.parseObject(mp);
		if (Integer.parseInt(json.get("status") + "") != 0) {

			String playUrl = json.getJSONObject("data").getString("play_url");
			FileDownload down = new FileDownload();
			down.download(playUrl, filePath + name + ".mp3");
		}

	}

}

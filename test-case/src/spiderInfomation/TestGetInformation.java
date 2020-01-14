package spiderInfomation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import spiderUtils.HtmlManage;
import spiderUtils.HttpGetConnect;

/**
 * @author dongwn 拟抓取页面的资讯数据
 */
public class TestGetInformation {

	static String url = "http://news.chemnet.com/list-11-11-PAGE.html";// 列表页

	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		String newUrl = "";// 动态url
		for (int i = 1; i < 50; i++) {// 只抓50页吧
			newUrl = url.replace("PAGE", i + "");
			getConnection(newUrl);
			Thread.sleep(1000);
		}
	}

	// 获取链接获取元素
	@SuppressWarnings("static-access")
	public static void getConnection(String url) throws IOException, ParseException {
		StringBuffer sb = new StringBuffer();
		HttpGetConnect connect = new HttpGetConnect();
		String content = connect.connect(url, "utf-8");
		HtmlManage html = new HtmlManage();
		Document doc = html.manage(content);// 转 Document
		Elements elements = doc.select(".content-list>ul>li");
		sb.append(System.getProperty("user.name") + "----->>>"
				+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		sb.append("\r\n");// 换行
		sb.append("\r\n");// 换行
		for (Element ele : elements) {
			sb.append("标题---->>>");
			sb.append(ele.select("a").text().trim());// 获取标题
			sb.append("\r\n");// 换行
			String detailUrl = "http://news.chemnet.com" + ele.select("a").attr("href");
			String detailcontent = connect.connect(detailUrl, "utf-8");
			HtmlManage detailhtml = new HtmlManage();
			Document detailDoc = detailhtml.manage(detailcontent);// 转 Document
			String detailContent = detailDoc.select(".detail-text>div").get(0).text();
			sb.append("内容---->>>");
			sb.append(detailContent);
			sb.append("\r\n");// 换行
			sb.append("\r\n");// 换行
			sb.append("\r\n");// 换行
		}
		writeToTxt(sb);
	}

	public static void writeToTxt(StringBuffer sb) {
		FileWriter fw = null;
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		try {
			File f = new File(desktopPath + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "今日资讯.txt");
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(sb);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package com.prudential.webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawlerApp {
	public static Queue<String> queue = new LinkedList<>();
	public static Set<String> marked = new HashSet<>();
	public static String regex = "http[s]*://(\\w+\\.*(prudential.co.uk|pru.co.uk|prudentialcorporation.co.uk))";      

	public static void searchAlgorithm(String root) throws IOException {

		queue.add(root);
		URL url = null;
		BufferedReader br = null;
		while (!queue.isEmpty()) {
			String crawledUrl = queue.poll();
			System.out.println("\n === Site Crowled ===> " + crawledUrl );
			boolean ok = false;

			while (!ok) {
				try {
					url = new URL(crawledUrl);
					br = new BufferedReader(new InputStreamReader(url.openStream()));
					ok = true;
				} catch (MalformedURLException e) {
					System.out.println("=== MalformedURL == " + crawledUrl);
					crawledUrl = queue.poll();
					ok = false;
				} catch (IOException ioe) {
					System.out.println("=== IOException for URL == " + crawledUrl);
					crawledUrl = queue.poll();
					ok = false;
				}
			}

			StringBuffer sb = new StringBuffer();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			temp = sb.toString();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(temp);

			while (matcher.find()) {
				String w = matcher.group();

				if (!marked.contains(w)) {
					marked.add(w);
					System.out.println("URL added for crawling : " + w);
					queue.add(w);
				}
			}

		}

		if (br != null) {
			br.close();
		}

	}

	public static void showResult() {
		System.out.println("\n Results : ");
		System.out.println("Web sites crawled : " + marked.size() + "\n");
		for (String s : marked) {
			System.out.println("*" + s);
		}
	}

	public static void main(String[] args) {
		try {
			searchAlgorithm("https://www.prudential.co.uk/");
			showResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package com.example.vimitest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class BlogActivity extends Activity {
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog);

		// Receive the URL from the upstream activity
		Intent intent = getIntent();
		String url = intent.getStringExtra("BLOG_URL");

		WebView webView = (WebView) findViewById(R.id.web);

		// Enable JavaScript
		webView.getSettings().setJavaScriptEnabled(true);

		// Load the web page of the URL
		webView.loadUrl(url);
	}
}

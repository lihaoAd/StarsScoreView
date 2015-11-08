package com.h.stars;

import com.cs.stars.R;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	StarsScoreView stars1;
	StarsScoreView stars2;
	StarsScoreView stars3;
	StarsScoreView stars4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		stars1 = (StarsScoreView) findViewById(R.id.stars1);
		stars2 = (StarsScoreView) findViewById(R.id.stars2);
		stars3 = (StarsScoreView) findViewById(R.id.stars3);
		stars4 = (StarsScoreView) findViewById(R.id.stars4);
		stars1.setProgress(3);
		stars2.setProgress(6);
		stars3.setProgress(6);
		stars4.setProgress(7);
	}

}

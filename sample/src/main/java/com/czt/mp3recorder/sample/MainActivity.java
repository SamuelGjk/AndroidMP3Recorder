
package com.czt.mp3recorder.sample;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;

public class MainActivity extends Activity {

	private static final int ONE_SECOND = 1000;

	private TextView mTextStatus;

	private MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));

	private long mRecordingDuration;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mRecordingDuration += ONE_SECOND;
			mTextStatus.setText(mDateFormat.format(mRecordingDuration));
			mHandler.postDelayed(this, ONE_SECOND);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTextStatus = findViewById(R.id.text_status);

		Button startButton = findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (!mRecorder.isRunning()) {
						mRecorder.start();
						mHandler.postDelayed(mRunnable, ONE_SECOND);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		Button stopButton = findViewById(R.id.stop_button);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mRecorder.isRunning()) {
					mRecorder.stop();
					mHandler.removeCallbacks(mRunnable);
					mRecordingDuration = 0;
				}
			}
		});

		Button resumeButton = findViewById(R.id.resume_button);
		resumeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mRecorder.isRunning() && !mRecorder.isRecording()) {
					mRecorder.resume();
					mHandler.postDelayed(mRunnable, ONE_SECOND);
				}
			}
		});

		Button pauseButton = findViewById(R.id.pause_button);
		pauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mRecorder.isRecording()) {
					mRecorder.pause();
					mHandler.removeCallbacks(mRunnable);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRecorder.stop();
	}
}

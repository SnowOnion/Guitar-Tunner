package com.houxiyang.guitar;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.houxiyang.guitar.Utils.KeepScreenon;
import com.houxiyang.guitar.Utils.TunnerThread;

public class MainActivity extends Activity {

	private boolean startRecording = true;

	private TunnerThread tunner;

	private Button tunning_button = null;
	private TextView frequencyView = null;
	private TextView nameView = null;
	private EditText logView = null;
	

	private Handler handler = new Handler();
	private Runnable callback = new Runnable() {

		public void run() {
			updateText(tunner.getCurrentFrequency());
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KeepScreenon.keepScreenOn(this, true);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		tunning_button = (Button) findViewById(R.id.tunning_button);
		frequencyView = (TextView) findViewById(R.id.frequency);
		nameView = (TextView) findViewById(R.id.name);
		logView = (EditText) findViewById(R.id.log);
		tunning_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onRecord(startRecording);
				if (startRecording) {
					tunning_button.setText(R.string.stop_tunning);
				} else {
					tunning_button.setText(R.string.start_tunning);
				}
				startRecording = !startRecording;
			}

		});
	}

	private void onRecord(boolean startRecording) {
		if (startRecording) {
			startTunning();
		} else {
			stopTunning();
		}
	}

	private void startTunning() {
		tunner = new TunnerThread(handler, callback);
		tunner.start();
	}

	private void stopTunning() {
		//tunner.stop();///////好像不太科学
		tunner.close();
	}
/* http://en.wikipedia.org/wiki/Piano_key_frequencies
 * 
E3			164.814
D♯3/E♭3		155.563
D3			146.832
C♯3/D♭3		138.591
C3 Low C	130.813
B2			123.471
A♯2/B♭2		116.541
A2			110
G♯2/A♭2		103.826
G2			97.9989
F♯2/G♭2		92.4986
F2			87.3071
E2			82.4069

 * */
	private String freq2name(double freq) {
		if (freq >= 82.41 && freq < 89) {
			return "F";
		} else if (freq >= 89 && freq < 95) {
			return "F#";
		} else if (freq >= 95 && freq < 100) {
			return "G";
		} else if (freq >= 100 && freq < 105) {
			return "G#";
		} else if (freq >= 105 && freq < 111) {
			return "A";
		} else if (freq >= 111 && freq < 118) {
			return "A#";
		} else if (freq >= 118 && freq < 125) {
			return "B";
		} else if (freq >= 125 && freq < 134) {
			return "C";
		} else if (freq >= 134 && freq < 140) {
			return "C#";
		} else if (freq >= 140 && freq < 149) {
			return "D";
		} else if (freq >= 149 && freq < 158) {
			return "D#";
		} else {//if (freq >= 158 && freq <= 164.81)
			return "E";
		}
	}

	private void updateText(double currentFrequency) {
		while (currentFrequency < 82.41) {
			currentFrequency = currentFrequency * 2;
		}
		while (currentFrequency > 164.81) {
			currentFrequency = currentFrequency * 0.5;
		}
		BigDecimal a = new BigDecimal(currentFrequency);
		BigDecimal result = a.setScale(2, RoundingMode.DOWN);
		
		frequencyView.setText(String.valueOf(result));
		nameView.setText(freq2name(currentFrequency));
		logView.append(String.valueOf(result)+" "+freq2name(currentFrequency)+"\n");
	}
}

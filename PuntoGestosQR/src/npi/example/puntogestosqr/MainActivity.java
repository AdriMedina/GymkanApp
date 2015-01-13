package npi.example.puntogestosqr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// Librerias para realizar los gestos
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void pasarActividadDibuja(){
		Intent act = new Intent(this, ActividadDibuja.class);
		startActivity(act);
	}
	
	
	
}

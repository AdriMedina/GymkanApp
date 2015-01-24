package npi.example.puntogestosqr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;


// Librerias para realizar los gestos
/*import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;*/


public class MainActivity extends Activity{
	
	//Etiquetas para la salida de datos.
	private static final String DEBUG_TAG = "SalidaEvento";
	private static final String COORD_TAG = "Coordenadas";
	
	//Variables necesarias para comprobar gestos.
	float x_inicial, y_inicial, x_actual, y_actual, y_final;
	boolean correcto = true;
	
	//Objeto utilizado para dibujar y detectar el gesto.
	Gesto gestoUsuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gestoUsuario = new Gesto(this);
		setContentView(gestoUsuario);
		gestoUsuario.requestFocus();		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evento)
	{				
		//Referencio el fondo de la aplicación en un objeto tipo View.
		//View vista_actual = findViewById(R.id.fondo);
		//vista_actual.setBackgroundColor(getResources().getColor(R.color.pintura_roja));
		
		
		
		
		return true;
	}
	
/*	public void Dibuja(View view){
		Intent intent = new Intent(MainActivity.this, ActividadDibuja.class);
		startActivity(intent);
	}
*/

}





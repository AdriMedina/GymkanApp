package npi.example.puntogestosqr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;


// Librerias para realizar los gestos
/*import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;*/


public class MainActivity extends Activity{
	
	//Etiquetas para la salida de datos.
	private static final String DEBUG_TAG = "Fase";
	@SuppressWarnings("unused")
	private static final String COORD_TAG = "Coordenadas";
	
	String posicion;
	
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
	
	/*
	 * Funci�n que se activa cada vez que hay un evento t�ctil.
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evento)
	{
		/*
		 * El objeto "gestoUsuario" declarado en "onCreate()" contendr� un valor 
		 * llamado "fase" que puede ser "-1" o "3". Si el valor es -1 significar�
		 * que el gesto "L" no ha sido realizado correctamente.
		 * Por el contrario, si devuelve un 3 significar� que el gesto se ha
		 * realizado de forma correcta. (Ver "src/Gesto.java")
		 */
		Log.d(DEBUG_TAG, "Fase:" + gestoUsuario.fase);
		/*
		 * @see: http://stackoverflow.com/questions/8831050/android-how-to-read-qr-code-in-my-application
		 */
		/*
		 * Si el gesto del usuario es correcto (fase = 3)
		 */
		if(gestoUsuario.fase == 3)
		{
			/*
			 * La actividad intentar� abrir la aplicaci�n "Barcode Scanner" que es open source para el escaneo
			 * del c�digo QR. (https://play.google.com/store/apps/details?id=com.google.zxing.client.android).
			 */
			try
			{
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				
				startActivityForResult(intent, 0);
			}
			/*
			 * Si no puede abirla, crear� una excepci�n en la que se abrir� Google Play para descargar la
			 * aplicaci�n.
			 */
			catch(Exception e)
			{
				Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
			    startActivity(marketIntent);
			}
		}		
		
		return true;
	}
	
	/*
	 * Esta funci�n se llamar� para recoger los resultados del intent en el que llamamos
	 * a Barcode Scanner.
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 0)
		{
			/*
			 * Si el resultado es correcto, almacenamos en el string 'posicion' el contenido
			 * del c�digo QR analizado.
			 */
			if(resultCode == RESULT_OK)
			{
				/*
				 * Capturamos el string y lo almacenamos en posici�n.
				 */
				posicion = data.getStringExtra("SCAN_RESULT");
				/*
				 * Llamamos a la funci�n GPS con los datos recogidos.
				 */
				GPS(posicion);
	
			}
			/*if(resultCode == RESULT_CANCELED)
			{
				
			}*/
		}
	}
	
	/*
	 * Funci�n que recibe los datos del c�digo QR e invoca a Google Maps con la
	 * posicion correspondiente.
	 */
	public void GPS(String posicion)
	{
		/*
		 * Dividimos el string a partir del caracter "_", por ejemplo:
		 * "String posicion" contiene : LATITUD_37.19678168548899_LONGITUD_-3.62465459523194
		 * Tras el 'split' division contendr�:
		 * "String division = {LATITUD, 37.19678168548899, LONGITUD, -3.62465459523194)"
		 * Seleccionamos las posiciones 1 y 3 del vector que contienen los valores reales.
		 * (http://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java)
		 */
		if(posicion.contains("LATITUD") && posicion.contains("LONGITUD"))
		{
			String[] division = posicion.split("_");
			String latitud = division[1];
			String longitud = division[3];
			Log.d(DEBUG_TAG, latitud + longitud);
			
			/*
			 * Declaramos un Uri con el que llamaremos a Google Maps mediante un Intent, que se compondr�
			 * de latitud y longitud del punto leido.
			 */
			/*
			 * Ver:
			 * https://developer.android.com/guide/components/intents-common.html#Maps
			 */
			
			Uri coordenadas = Uri.parse("geo:0,0?q="+latitud+","+longitud+"?z="+1+"(Punto)");
			
			/*
			 * Declaramos un intent del tipo ACTION_VIEW.
			 */
			Intent intent = new Intent(Intent.ACTION_VIEW);
			/*
			 * Le pasamos como datos el Uri declarado antes. Al tener la "etiqueta" "geo:" lanzar�
			 * Google Maps autimaticamente.
			 */
			intent.setData(coordenadas);
		
			if(intent.resolveActivity(getPackageManager())!=null)
				startActivity(intent);
		}
		else
		{
			throw new IllegalArgumentException("La cadena " + posicion + " no es un punto GPS v�lido.");
		}
	}
}





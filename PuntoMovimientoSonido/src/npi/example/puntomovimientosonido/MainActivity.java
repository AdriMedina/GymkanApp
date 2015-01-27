package npi.example.puntomovimientosonido;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

	// variables para el giroscopio
	private float actX = 0, actY = 0, actZ = 0;
	private boolean primeraVez = false;
	
	// variables para los sonidos
	private Context pContext;
	private SoundPool sndPool;
	private int explo, guita, mune, pist, timb, vient;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // creamos el objeto SoundPool
        sndPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
   	  	pContext = getApplicationContext();
        
   	  	// cargamos los distintos clips de los sonidos
   	  	explo = sndPool.load(pContext, R.raw.explosion, 1);
   	  	guita = sndPool.load(pContext, R.raw.guitarra, 1);
   	  	mune = sndPool.load(pContext, R.raw.muneco, 1);
   	  	pist = sndPool.load(pContext, R.raw.pistola, 1);
   	  	timb = sndPool.load(pContext, R.raw.timbre, 1);
   	  	vient = sndPool.load(pContext, R.raw.viento, 1);
    }
    

    @Override
	protected void onResume() {  // registro del listener
		super.onResume(); 
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		// se utilizará el acelerómetro como sensor
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER); 
		if (sensors.size() > 0) {
			
			// indicar tasa de lectura de datos: “SensorManager.SENSOR_DELAY_GAME” 
			// que es la velocidad mínima para que el acelerómetro pueda usarse
			sm.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	
	@Override
	protected void onStop() { // anular el registro del listener
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(this);
		sndPool.release();
		super.onStop();
	}

	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// es llamado cuando la precisión del sensor ha cambiado
	} 

	
	@Override
	public void onSensorChanged(SensorEvent event) {  // es llamado cuando los valores del sensor han cambiado
		synchronized (this) { // sincronizar, para evitar problemas de concurrencia

			// Obtener los valores de los ejes del acelerometro
			actX = event.values[0];
			actY = event.values[1];
			actZ = event.values[2];

			// Si están dentro del rango asignado los movimentos
			int rango = 2;
			boolean rangoX = actX > rango*(-1) && actX < rango;
			boolean rangoY = actY > rango*(-1) && actY < rango;
			boolean rangoZ = actZ > rango*(-1) && actZ < rango;
			
			// VERTICAL (ARRIBA)
			if ( (actY > actX && actY > actZ) && ( rangoX && rangoZ ) ){
				((TextView) findViewById(R.id.tv_resultado)).setText("Una explosion");
				if(!primeraVez){
					sndPool.play(explo, 1.0f, 1.0f, 1, 0, 1.0f);
					primeraVez = true;
				}
				
			// VERTICAL (ABAJO)
			}else if( (actY < actX && actY < actZ) && ( rangoX && rangoZ ) ){
				((TextView) findViewById(R.id.tv_resultado)).setText("Una guitarra");
				if(!primeraVez){
					sndPool.play(guita, 1.0f, 1.0f, 1, 0, 1.0f);
					primeraVez = true;
				}
				
			// HORIZONTAL (IZQUIERDA)
			}else if( (actX > actY && actX > actZ) && ( rangoY && rangoZ ) ){
				((TextView) findViewById(R.id.tv_resultado)).setText("Un muñeco");
				if(!primeraVez){
					sndPool.play(mune, 1.0f, 1.0f, 1, 0, 1.0f);
					primeraVez = true;
				}
				
			// HORIZONTAL (DERECHA)
			}else if( (actX < actY && actX < actZ) && ( rangoY && rangoZ ) ){
				((TextView) findViewById(R.id.tv_resultado)).setText("Una pistola");
				if(!primeraVez){
					sndPool.play(pist, 1.0f, 1.0f, 1, 0, 1.0f);
					primeraVez = true;
				}
				
			// BOCA ARRIBA
			}else if( (actZ > actY && actZ > actX) && ( rangoY && rangoX ) ){
				((TextView) findViewById(R.id.tv_resultado)).setText("Un timbre");
				if(!primeraVez){
					sndPool.play(timb, 1.0f, 1.0f, 1, 0, 1.0f);
					primeraVez = true;
				}
				
			// BOCA ABAJO
			}else if( (actZ < actY && actZ < actX) && ( rangoY && rangoX ) ){
				((TextView) findViewById(R.id.tv_resultado)).setText("El viento");
				if(!primeraVez){
					sndPool.play(vient, 1.0f, 1.0f, 1, 0, 1.0f);
					primeraVez = true;
				}
			
			// Mientras no esté en alguna de las posiciones
			}else{
				((TextView) findViewById(R.id.tv_resultado)).setText("No hay sonido");
				primeraVez = false;
			}

			
			// mostrar por pantalla los valores del acelerómetro
			((TextView) findViewById(R.id.txtAccX)).setText(getResources()
					.getString(R.string.valX) + actX);
			((TextView) findViewById(R.id.txtAccY)).setText(getResources()
					.getString(R.string.valY) + actY);
			((TextView) findViewById(R.id.txtAccZ)).setText(getResources()
					.getString(R.string.valZ) + actZ);

		}
	}
}

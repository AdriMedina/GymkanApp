package npi.example.puntofotobrujula;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ActividadBrujula extends Activity implements SensorEventListener {

	// TextViews
    private TextView direccion;
    private TextView infoSeleccionado;

    // Initialize ImageView
    private ImageView image;

    // Degrees
    private float currentDegree = 0.0f;
    private float lastDegree = 0.0f;
    private float gradoMin;
    private float gradoMax;

    
    private int posicionSeleccionado;
    
    
    // Sensors and location
    private SensorManager mSensorManager;
	private String cardinalSeleccionado;
	
	
	// Variables de la camara
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private Uri fileUri;
    private ImageView imgPreview;
    private int nVeces = 0;
    private TextView infoFotografia;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_brujula);
		
		 // Fill the image with the compass
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // Set other texts
        direccion = (TextView) findViewById(R.id.tv_direccion);
        infoSeleccionado = (TextView) findViewById(R.id.tv_info_seleccionado);
        infoFotografia = (TextView) findViewById(R.id.tv_info_fotografia);
        
        // Initialize sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        cardinalSeleccionado = getIntent().getExtras().getString("seleccion");
        posicionSeleccionado = getIntent().getExtras().getInt("posicion");
        
        buscaCardinalSeleccionado();
        
        
	}

	@Override
    protected void onResume() {
        super.onResume();

        // Continue listening the orientation sensor
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Method called when the app pauses his activity
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Stop listening the sensor
        mSensorManager.unregisterListener(this);
    }
	
    @Override
    public void onSensorChanged(SensorEvent event) {
    	
        // Get the degree
        lastDegree = Math.round(event.values[0]);

        // Say if we must turn left or right to go north
        
        if(posicionSeleccionado != 0){
	        if (lastDegree > gradoMin && lastDegree < gradoMax) {
	        	direccion.setText("Flecha verde");
	        	
	        	// Controlamos que solo entre la primera vez para tirar la foto
	        	nVeces++;
	        	if(nVeces == 1){
	        		
	        		infoFotografia.setText("Tomando fotografia en 3 ... 2 ... 1 ...");
	        		
	        		captureImage();
	        	}
	        } else {
	        	direccion.setText("Flecha roja");
	        	infoFotografia.setText("Esperando");
	        	nVeces = 0;
	        }
        }else{
        	if ((lastDegree > gradoMin && lastDegree <= 359) || (lastDegree >= 0 && lastDegree < gradoMax)) {
        		direccion.setText("Flecha verde");
	        	
	        	// Controlamos que solo entre la primera vez para tirar la foto
	        	nVeces++;
	        	if(nVeces == 1){
	        		
	        		infoFotografia.setText("Tomando fotografia en 3 ... 2 ... 1 ...");
	        		
	        		captureImage();
	        	}
	        } else {
	        	direccion.setText("Flecha roja");
	        	infoFotografia.setText("Esperando");
	        	nVeces = 0;
	        }
        }
        // Create and exetute rotation animation
        RotateAnimation rotation;
        rotation = new RotateAnimation(
                currentDegree,
                -lastDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotation.setDuration(300);
        rotation.setFillAfter(true);
        image.startAnimation(rotation);

        // Update current degree
        currentDegree = -lastDegree;


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    
    public void buscaCardinalSeleccionado(){

    	infoSeleccionado.setText("Estoy buscando el " + cardinalSeleccionado);
    	
    	switch (posicionSeleccionado) {
	    	// Norte
			case 0:
				gradoMin = 345; gradoMax = 15;
				break;
				
			// Sur
			case 1:
				gradoMin = 165; gradoMax = 195;
				break;
				
			// Oeste
			case 2:
				gradoMin = 255; gradoMax = 285;
				break;
				
			// Este
			case 3:
				gradoMin = 75; gradoMax = 105;
				break;
				
			default:
				break;
		}
    }
    
    
    private void captureImage() {
    	// Dar un nombre a la fotografía para almacenarla en la carpeta por defecto del movil
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	
    	ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");
 
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     
        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
     
        // start el intent" de captura
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        
        
         
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprobar la solicitud del usuario de guardar la fotografía
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
                
                onStop();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    
    
    private void previewCapturedImage() {
        try {
 
            imgPreview.setVisibility(View.VISIBLE);
 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    
    
}

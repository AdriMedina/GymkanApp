package npi.example.puntogpsvoz;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.speech.RecognizerIntent;


public class MainActivity extends Activity {
	
	private String languageModel = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
	private int numeroResultados = 5;
	
	private static int codigo_solicitud_hablar = 123;
	private static final String DEBUGTAG = "cadena";
	
	private boolean estado = false;
	private String datos_latitud = new String();
	private String datos_longitud = new String();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setSpeakButton();
    }
    
    private void escuchar()
    {
    	estado = !estado;
    	
    	Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    	
    	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);
    	
    	intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, numeroResultados);
    	
    	startActivityForResult(intent, codigo_solicitud_hablar);
    }
    
    @SuppressLint("DefaultLocale")
	private void setSpeakButton() {
    	
    	Button boton_hablar = (Button) findViewById(R.id.talk_button);
    	
    	boton_hablar.setOnClickListener(new View.OnClickListener() {
    		@Override
			public void onClick(View v) {
    			escuchar();
    		}
    	});
    }
    
    @SuppressLint("InlinedApi")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == codigo_solicitud_hablar)
    	{
    		if(resultCode == RESULT_OK)
    		{
    			if(data != null)
    			{    				
    				//ArrayList<String> nBestList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    				String datos = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
    				analizarDatos(datos);
    			}
    		}
    	}
    }
    
    private void GPS()
    {
    	Uri coordenadas = Uri.parse("geo:0,0?q="+datos_latitud+","+datos_longitud);
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(coordenadas);
    	
    	if(intent.resolveActivity(getPackageManager())!=null)
			startActivity(intent);
    }
    
    private void analizarDatos(String datos)
    {
    	Log.d(DEBUGTAG, datos);
    	
    	String salida = "";
    	List<String> elementos = new ArrayList<String>(Arrays.asList(datos.split(" ")));

    	if(datos_longitud.length() > 0 && datos_latitud.length() > 0 && datos.contains("correcto"))
    		GPS();
    	else
    	{

    		if(elementos.contains("latitud")){
    			elementos.remove(elementos.indexOf("latitud"));
    			estado = true;
    		}
    		else if(elementos.contains("longitud")){
    			elementos.remove(elementos.indexOf("longitud"));
    			estado = false;
    		}
    	
    		if(elementos.contains("menos"))
    			elementos.set(elementos.indexOf("menos"), "-");
    		
    		if(elementos.contains("coma"))
    			elementos.set(elementos.indexOf("coma"), ".");
    		else if(elementos.contains("punto"))
    			elementos.set(elementos.indexOf("punto"), ".");
    		else if(elementos.contains("como"))
    			elementos.set(elementos.indexOf("como"), ".");    	
    		
    		for(int i=0; i<elementos.size(); i++)
    			salida = salida + elementos.get(i);
    	
    		Log.d(DEBUGTAG, salida);
    	
    		if(estado)
    		{
    			TextView latitud = (TextView) findViewById(R.id.displayLatitud);
    			latitud.setText(salida);
    			datos_latitud = salida;
    		}
    		else
    		{
    			TextView longitud = (TextView) findViewById(R.id.displayLongitud);
    			longitud.setText(salida);
    			datos_longitud = salida;
    		}
    	}
    }
    
    public void ayuda(View v)
    {
    	Intent intent = new Intent(this, Ayuda.class);
    	this.startActivity(intent);
    }
 }

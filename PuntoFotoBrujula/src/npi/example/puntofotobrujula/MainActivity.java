package npi.example.puntofotobrujula;

import npi.example.puntofotobrujula.ActividadBrujula;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnItemSelectedListener{

	private Spinner spPuntosCardinales;
	
	private String cardSeleccionado;
	private int cardPosicion;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.spPuntosCardinales = (Spinner) findViewById(R.id.sp_puntos_cardinales);
        
        loadSpinnerPuntosCardinales();
        
    }
    
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		cardSeleccionado = parent.getItemAtPosition(position).toString();
		cardPosicion = position;
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
    
    
    private void loadSpinnerPuntosCardinales(){
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.puntosCardinales, android.R.layout.simple_spinner_item);
    	
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	this.spPuntosCardinales.setAdapter(adapter);
    	
    	spPuntosCardinales.setOnItemSelectedListener(this);
    	
    }

    
    public void botonBuscarActividadBrujula(View view){
		Intent act = new Intent(this, ActividadBrujula.class);
		act.putExtra("seleccion", cardSeleccionado);
		act.putExtra("posicion", cardPosicion);
		startActivity(act);
		
		
	}


}

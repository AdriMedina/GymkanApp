/*
 * Fuente:
 * https://thenewcircle.com/s/post/1036/android_2d_graphics_example
 */

package npi.example.puntogestosqr;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Gesto extends View implements OnTouchListener{
	
/*
 * 	Declaración de variables necesarias.
 */
	/*
	 * Conjunto de etiquetas para diferenciar las salidas por pantalla.
	 */
	private static final String TAG = "DrawView";
	private static final String TAG1 = "Comprobación";
	
	/*
	 *  Lista de puntos que almacenará los puntos que van a ser pintados
	 *  por pantalla.
	 */
    List<Point> points = new ArrayList<Point>();
    
    /*
     *  Objeto de tipo Paint que sirve para seleccionar el color en el que
     *  queremos pintar los puntos de la lista anterior.
     */
    Paint paint = new Paint();
    
    /*
     *  Conjunto de variables usadas para hacer un tracking correcto del
     *  gesto.
     */
    float x_inicial, y_inicial, x_actual, y_actual, check_pointX, check_pointY;
    boolean fin_dibujo = false;
    boolean gesto_correcto = true;

    /*
     * Constructor de la clase en el que indicamos que puede ser foco
     * de eventos táctiles.
     */
    public Gesto(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint.setColor(getResources().getColor(R.color.pintura_azul));
        paint.setAntiAlias(true);
    }

    /*
     * Función de dibujado. Cada vez que se lanza dibuja el conjunto
     * de puntos de la lista de puntos recogidos
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
    		for (Point point : points)
    			canvas.drawCircle(point.x, point.y, 5, paint);
    }
    
    /*
     * Función que se activa siempre que hay un evento táctil.
     * Recibe una vista y el tipo de evento que ha recogido. 
     * 
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    public boolean onTouch(View view, MotionEvent evento) {    		
   
    	/*
    	 * En función del tipo de evento recogido, se actuará de
    	 * una forma distinta.
    	 */
       	switch(evento.getActionMasked()){
    		/*
    		 * Caso en el que se acaba de situar el dedo sobre la pantalla.
    		 */
    		case(MotionEvent.ACTION_DOWN):
    			
    			/*
    			 * Limpio la lista de puntos que se hubiesen recogido en
    			 * detecciones de gestos anteriores.
    			 */
    			points.clear();
    			/*
    			 * La llamada a la función invalidate() nos asegura que
    			 * la función de dibujado será llamada en un futuro.
    			 */
    			invalidate();
    			/*
    			 * Estas dos acciónes combinadas nos permiten limpiar la
    			 * pantalla de puntos cada vez que comencemos a realizar
    			 * un nuevo gesto.
    			 */
    			
    			/*
    			 *	Selecciono el color de la pintura por defecto con la que se
    			 * 	pintarán los puntos y el color de fondo.
    			 */    			
	        	paint.setColor(getResources().getColor(R.color.pintura_azul));
    			view.setBackgroundColor(getResources().getColor(R.color.fondo_gris_coloreando));
	        	/*
	        	 *	Recojo el punto inicial en el que empieza el gesto y lo selecciono
	        	 *	como punto actual.
	        	 */
    			x_inicial = check_pointX = evento.getX(evento.getPointerId(0));
    			y_inicial = check_pointY = evento.getY(evento.getPointerId(0));
    			/*
    			 *	Como se trata de un nuevo gesto, lo consideramos correcto de inicio.
    			 */
    			gesto_correcto = true;
    			/*
    			 * Salida por pantalla de las coordenadas.
    			 */
    			Log.d(TAG1,"Punto inicial: " + x_inicial + " - " + y_inicial);
        		
    			break;
        		
    		/*
    		 * Caso en el que un dedo se está moviendo por la pantalla
    		 */
    		case(MotionEvent.ACTION_MOVE):
    			/*
    			 * Capuramos el punto del dedo en todo momento.
    			 */
    			x_actual = evento.getX(evento.getPointerId(0));
    			y_actual = evento.getY(evento.getPointerId(0));
    			/*
    			 *	Por motivos de eficiencia se dibuja solo cuando el dedo se ha
    			 *	movido por lo menos 20 pixels en cualquier dirección para no
    			 *	saturar dibujando cada vez que se avanza un pixel.
    			 */
    			if( check_pointX < x_actual-20 || check_pointX > x_actual+20 ||
    				check_pointY < y_actual-20 || check_pointY > y_actual+20)
    			{
    				/*
    				 * Capturamos el punto que será dibujado.
    				 */
    				Point point = new Point();
    				point.x = check_pointX = x_actual;
    				point.y = check_pointY = y_actual;
    				/*
    				 * Y lo añadimos a la lista.
    				 */
    				points.add(point);
    				/*
    				 * Por último dibujamos los puntos que tengamos hasta el momento.
    				 */
	    			invalidate();
	    			/*
	    			 * Salida por pantalla del punto recogido.
	    			 */
	    			Log.d(TAG, "point: " + point);
    			}
    		
    			break;
    		
    		/*
    		 *	Caso en el que se detecta que se deja de tocar la pantalla
    		 *	con el dedo:
    		 */
    		case(MotionEvent.ACTION_UP):
    			//view.setBackgroundColor(getResources().getColor(R.color.fondo_gris));
    			/*
    			 * Si lo recopilado por el caso "MotionEvent.ACTION_MOVE" determina que
    			 * el gesto no ha sido correcto:
    			 */
    			if(!gesto_correcto)
    			{
    				/*
    				 * Pintamos los puntos con la pintura roja.
    				 */
    		        paint.setColor(getResources().getColor(R.color.pintura_roja));
    		        invalidate();
    			}
    			break;
    	}
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        //return super.onTouchEvent(evento);
        return true;
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
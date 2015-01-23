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
 * 	Declaraci�n de variables necesarias.
 */
	/*
	 * Conjunto de etiquetas para diferenciar las salidas por pantalla.
	 */
	private static final String TAG = "DrawView";
	private static final String TAG1 = "Comprobaci�n";
	
	/*
	 *  Lista de puntos que almacenar� los puntos que van a ser pintados
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
     *	Fase de detecci�n del gesto. Al detectar el gesto " L " hay dos fases claramente
     *	diferenciadas. La fase en la que realizamos un scroll vertical y la fase en la
     *	que realizamos el scroll horizontal para dibujar la " L ".
     *	Definimos la fase 0 como la fase en la que se realiza el scroll vertical y la
     *	fase 1 como en la que se realiza el scroll horizontal.
     */
    int fase = 0;

    /*
     * Constructor de la clase en el que indicamos que puede ser foco
     * de eventos t�ctiles.
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
     * Funci�n de dibujado. Cada vez que se lanza dibuja el conjunto
     * de puntos de la lista de puntos recogidos
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
    		for (Point point : points)
    			canvas.drawCircle(point.x, point.y, 5, paint);
    }
    
    /*
     * Funci�n que se activa siempre que hay un evento t�ctil.
     * Recibe una vista y el tipo de evento que ha recogido. 
     * 
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    public boolean onTouch(View view, MotionEvent evento) {    		
   
    	/*
    	 * En funci�n del tipo de evento recogido, se actuar� de
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
    			 * La llamada a la funci�n invalidate() nos asegura que
    			 * la funci�n de dibujado ser� llamada en un futuro.
    			 */
    			invalidate();
    			/*
    			 * Estas dos acci�nes combinadas nos permiten limpiar la
    			 * pantalla de puntos cada vez que comencemos a realizar
    			 * un nuevo gesto.
    			 */
    			
    			/*
    			 *	Selecciono el color de la pintura por defecto con la que se
    			 * 	pintar�n los puntos y el color de fondo.
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
    			 * 	Indicamos la fase de detecci�n actual.
    			 */
    			fase=0;
    			/*
    			 * Salida por pantalla de las coordenadas.
    			 */
    			Log.d(TAG1,"Punto inicial: " + x_inicial + " - " + y_inicial);
        		
    			break;
        		
    		/*
    		 * Caso en el que un dedo se est� moviendo por la pantalla
    		 */
    		case(MotionEvent.ACTION_MOVE):
    			/*
    			 * Capuramos el punto del dedo en todo momento.
    			 */
    			x_actual = evento.getX(evento.getPointerId(0));
    			y_actual = evento.getY(evento.getPointerId(0));
    			
    			if(fase == 2 && gesto_correcto && (x_actual + 25 < x_inicial ||
       				y_actual + 50 < y_inicial || y_actual - 50 > y_inicial ) )
    				gesto_correcto = false;
    			
    			//*
    			if(fase == 1 && gesto_correcto && y_actual > y_inicial &&  x_actual + 50 < x_inicial)
    				gesto_correcto = false;
    			
    			if(fase == 1 && gesto_correcto  && x_actual - 50 > x_inicial)
    			{
    				fase = 2;
    				y_inicial = y_actual;
    				x_inicial = x_actual;
    			}
    			
    				
    			if(fase == 0 && gesto_correcto && (y_actual + 25 < y_inicial || 
    				x_actual + 50 < x_inicial || x_actual - 50 > x_inicial))
    				gesto_correcto = false;
    			
    			if(fase == 0 && gesto_correcto && y_actual > y_inicial+250)
    				fase = 1;
    			
    			Log.d(TAG1, "Fase " + fase + " gesto: " + gesto_correcto);
    			
    			/*
    			 *	Por motivos de eficiencia se dibuja solo cuando el dedo se ha
    			 *	movido por lo menos 20 pixels en cualquier direcci�n para no
    			 *	saturar dibujando cada vez que se avanza un pixel.
    			 */
    			if( check_pointX < x_actual-20 || check_pointX > x_actual+20 ||
    				check_pointY < y_actual-20 || check_pointY > y_actual+20)
    			{
    				/*
    				 * Capturamos el punto que ser� dibujado.
    				 */
    				Point point = new Point();
    				point.x = check_pointX = x_actual;
    				point.y = check_pointY = y_actual;
    				/*
    				 * Y lo a�adimos a la lista.
    				 */
    				points.add(point);
    				/*
    				 * Por �ltimo dibujamos los puntos que tengamos hasta el momento.
    				 */
	    			invalidate();
	    			/*
	    			 * Salida por pantalla del punto recogido.
	    			 */
	    			Log.d(TAG, "point: " + point );
    			}
    		
    			break;
    		
    		/*
    		 *	Caso en el que se detecta que se deja de tocar la pantalla
    		 *	con el dedo:
    		 */
    		case(MotionEvent.ACTION_UP):
    			if(fase == 2 && gesto_correcto && x_actual > x_inicial + 100 && x_actual < x_inicial + 250 )
    				fase = 3;

    			//view.setBackgroundColor(getResources().getColor(R.color.fondo_gris));
    			/*
    			 * Si lo recopilado por el caso "MotionEvent.ACTION_MOVE" determina que
    			 * el gesto no ha sido correcto:
    			 */
    			if(!gesto_correcto || fase != 3)
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
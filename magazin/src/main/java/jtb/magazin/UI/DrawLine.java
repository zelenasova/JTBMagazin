package jtb.magazin.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import jtb.magazin.MainActivity;
import jtb.magazin.model.WOTModel;

//predstavuje ciaru medzi textView v krystali suvislosti
public class DrawLine extends View {
	private Path path = new Path();
    Paint paint = new Paint();
  
    Context context;
    WOTModel model1;
    WOTModel model2;

    public DrawLine(Context context, WOTModel model1, WOTModel model2) {
        super(context);
        paint.setColor(Color.BLACK);
        this.model1=model1;
        this.model2=model2;
        this.context=context;

    }
    
    public void setLine(int x, int y) {
    		
		invalidate();
	}
    
    @Override
    public void onDraw(Canvas canvas) {     
    	super.onDraw(canvas);
    	paint.setStyle(Paint.Style.STROKE);
        if (((MainActivity) context).articleWhiteBackground){
            paint.setColor(Color.BLACK);
        } else {
            paint.setColor(Color.WHITE);
        }

    	paint.setStrokeWidth(2);
            path.reset();
          //  System.out.println(model1.getActualX());
           // kreslime ciaru
            path.moveTo(model1.actualX, model1.actualY+(model1.height*model1.actualKoefHeight)/2);
            path.lineTo(model2.actualX, model2.actualY+(model2.height*model2.actualKoefHeight)/2);
            //nastavujeme priehladnost ciary
            if (model1.actualKoef>model2.actualKoef){
            setAlpha(model2.actualKoef);
            } else  setAlpha(model1.actualKoef);
            canvas.drawPath(path, paint);
        path.close();
    }
    
    

}
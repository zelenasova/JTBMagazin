package jtb.magazin.model;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WOTModel {

    public double x;
    public double y;
    public double z;
    public int actualX=0;
    public int actualY=0;
    public float actualKoef=(float) 0.0;
    public float actualKoefHeight=(float) 0.0;
    public String keyword;
    public String keywordID;
    public TextView text;
	public RelativeLayout.LayoutParams params;
    public int width;
    public int height;

	
	public WOTModel(Context context){
		text = new TextView(context);
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		text.setLayoutParams(params);
	}

}

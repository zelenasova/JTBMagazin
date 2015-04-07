package jtb.magazin.UI;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;

//zobrazuje vrstvu s moznostou pridat clanok medzi ulozene
public class ArticleControlsLayer extends FrameLayout {
	
	private Path triangle = new Path();
	private Rect rect = new Rect();
	TextView text;
	private float screenHeight;
	float screenWidth;
	Context context;
	LinearLayout lin;
	SharedPreferences prefs;
	ArticleModel model;
	String id;
	public ArticleControlsLayer(Context context,final ArticleModel model) {
		super(context);
		this.context=context;
		this.id=model.getId();
		this.model=model;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		

	//	addView(text, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		 LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    li.inflate(R.layout.frame_detail, this, true);
		lin = (LinearLayout) findViewById(R.id.read);
		LinearLayout.LayoutParams params1 = (android.widget.LinearLayout.LayoutParams) lin.getLayoutParams();
		DisplayMetrics metrics = getResources().getDisplayMetrics();
    	screenHeight = metrics.heightPixels;
    	screenWidth = metrics.widthPixels;
    	List<String> list = getReadLater();
    	int index = ((MainActivity)context).isIdinList(list, id,model.getSource());
    	if (index<0){
    		lin.setBackgroundColor(color.transparent);
    	} else {
    		lin.setBackgroundColor(Color.parseColor("#495a62"));
    	} 
    	
    	
    	float m=screenWidth/Constants.MARGIN_WIDTH_KOEF;
    	
    	 ImageView read_img = (ImageView) findViewById(R.id.read_later_img);
		 LinearLayout.LayoutParams read_img_Params = (android.widget.LinearLayout.LayoutParams) read_img.getLayoutParams();
		 read_img_Params.width= (int) (23*m/6);
		 read_img_Params.height= (int) (30*m/6);
		 read_img_Params.setMargins(0, 0, (int) (3*m), 0);
		 
		 TextView text = (TextView) findViewById(R.id.read_later_text);
		 text.setText(Html.fromHtml(((MainActivity)context).getString(R.string.read2)));

			text.setTypeface(((MainActivity) context).fedraSansAltProBook);
			text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size13));
    	
    	params1.height=(int) (screenWidth/25);
		params1.width=(int) (screenWidth/6);
		//System.out.println(params1.width);
		//po kliku na precitat neskor sa najprv pomocou isIdinList skontroluje ci sa nachadza medzi ulozenymi a podla toho ulozi alebo odoberie z tohto 
				//zoznamu
		lin.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            { 
         //   ArticleControlsLayer.this.prefs.edit().putString("read_later",  ArticleControlsLayer.this.i).commit();
           List<String> list = ((MainActivity)ArticleControlsLayer.this.context).getReadLater();
           
           int index = ((MainActivity)ArticleControlsLayer.this.context).isIdinList(list, ArticleControlsLayer.this.id,model.getSource());
       	if (index<0){
       		list.add(model.getSource()+"-"+ArticleControlsLayer.this.id);
       		((MainActivity)ArticleControlsLayer.this.context).saveReadLaterList(list);
            ((MainActivity)ArticleControlsLayer.this.context).checkIfArticleDownloaded(model);
       		lin.setBackgroundColor(Color.parseColor("#495a62"));
       	} else {
       	  for (Iterator<String> iter = list.listIterator(); iter.hasNext(); ) {
  		    String a = iter.next();
  		    if (a.equals(model.getSource()+"-"+ArticleControlsLayer.this.id)) {
  		        iter.remove();
  		    }
       	  	}
       		lin.setBackgroundColor(color.transparent);
       		((MainActivity)ArticleControlsLayer.this.context).saveReadLaterList(list);
       	} 
           
     
           }
            
        });
		
	//	addView(lin);
//		setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
	}
	
	
	//ulozi zoznam oblubenych, zoznam predstavuje prefs do ktorych sa ukladaju retazce s predponou valReadLater+poradove cislo + hodnota(source-id)
	public void saveList(List<String> list){
		SharedPreferences.Editor sEdit=prefs.edit();
		for(int i=0;i<list.size();i++)
		{
		         sEdit.putString("valReadLater"+i,list.get(i));
		}
		 sEdit.putInt("sizeReadLater",list.size());
		 sEdit.commit();
	}
	
	public List<String> getReadLater(){
		 ArrayList<String> list=new ArrayList<String>();
		 int size=prefs.getInt("sizeReadLater",0);

		 for(int j=0;j<size;j++)
		 {			
		           list.add(prefs.getString("valReadLater"+j,""));
		 }
		 		
		/*for (String string : list) {
			System.out.println(string);
		}*/
		return list;
	}

	public void setTriangleSize(int triangleSize) {
		triangle.reset();
		triangle.moveTo(0, 0);
		triangle.lineTo(getWidth(), 0);
		triangle.lineTo(getWidth(), triangleSize);

		triangle.lineTo(0, triangleSize);
      
//		triangle.lineTo(a.x, a.y);
		triangle.close();
		
		invalidate();
	}
	
	

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.clipPath(triangle);
		super.dispatchDraw(canvas);
	}

}

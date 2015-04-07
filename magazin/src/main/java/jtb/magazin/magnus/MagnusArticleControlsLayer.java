package jtb.magazin.magnus;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;

//vrstva s moznostou pridat clanok medzi ulozene
public class MagnusArticleControlsLayer extends FrameLayout {
	
	private Path triangle = new Path();
	TextView text;
	float screenWidth;
	final Context context;
	LinearLayout lin;
	SharedPreferences prefs;
	ArticleModel model;
	String id;
	
	public MagnusArticleControlsLayer(final Context context,final ArticleModel model) {
		super(context);
		this.context=context;
		this.id=model.getId();
		this.model=model;		
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.frame_magnus_detail, this, true);
		lin = (LinearLayout) findViewById(R.id.read_magnus);
		LinearLayout.LayoutParams params1 = (android.widget.LinearLayout.LayoutParams) lin.getLayoutParams();
		
		TextView tv = (TextView) findViewById(R.id.read_magnus_text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size28));
		tv.setText(Html.fromHtml(((MainActivity)context).getString(R.string.read)));

		tv.setTypeface(((MainActivity) context).giorgioSansWebRegular);
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
    	screenWidth = metrics.widthPixels;
    	List<String> list = getReadLater();
    	//kontrulujeme ci sa clanok uz nachadza medzi ulozenymi a podla toho zvolime farba ramceka okolo textu precitat si neskor
    	int index = ((MainActivity)context).isIdinList(list, id,model.getSource());
    	if (index<0){
    		lin.setBackgroundColor(color.transparent);
    	} else {
    		lin.setBackgroundColor(Color.parseColor("#495a62"));
    	} 
    	
    	
    	int m=(int) (screenWidth/Constants.MARGIN_WIDTH_KOEF);
    	
    	
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
           List<String> list = ((MainActivity)MagnusArticleControlsLayer.this.context).getReadLater();
           
           int index = ((MainActivity)MagnusArticleControlsLayer.this.context).isIdinList(list, MagnusArticleControlsLayer.this.id,model.getSource());
       	if (index<0){
       		list.add(model.getSource()+"-"+MagnusArticleControlsLayer.this.id);
       		((MainActivity)MagnusArticleControlsLayer.this.context).saveReadLaterList(list);
            ((MainActivity)MagnusArticleControlsLayer.this.context).checkIfArticleDownloaded(model);
       		lin.setBackgroundColor(Color.parseColor("#495a62"));
            Gson gson = new Gson();
            String jsonItem = gson.toJson(model);
            //ak sme ziskali data pre dany clanok ulozime si do suboru pre pripad vypadnutia internetu
            File cacheFile = new File(context.getExternalCacheDir ().toString() +"/cache/articles/"+model.getSource()+
                    "-"+model.getId()+"-"+((MainActivity)(context)).lang+"-"+model.getLastChange()+".txt");
            ((MainActivity)(context)).writeToFile(cacheFile,jsonItem);
       	} else {
       	  for (Iterator<String> iter = list.listIterator(); iter.hasNext(); ) {
  		    String a = iter.next();
  		    if (a.equals(model.getSource()+"-"+MagnusArticleControlsLayer.this.id)) {
  		        iter.remove();
  		    }
       	  	}
       		lin.setBackgroundColor(color.transparent);
       		((MainActivity)MagnusArticleControlsLayer.this.context).saveReadLaterList(list);
       	} 
                
           }           
        });

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
		return list;
	}

	public void setTriangleSize(int triangleSize) {
		triangle.reset();
		triangle.moveTo(0, 0);
		triangle.lineTo(getWidth(), 0);
		triangle.lineTo(getWidth(), triangleSize);
		triangle.lineTo(0, triangleSize);
		triangle.close();		
		invalidate();
	}
	
	

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.clipPath(triangle);
		super.dispatchDraw(canvas);
	}

}

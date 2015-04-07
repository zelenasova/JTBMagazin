package jtb.magazin.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.UI.ArticleThumbnailFrame;
import jtb.magazin.UI.FavoritesFrame;
import jtb.magazin.UI.ReadFrame;
import jtb.magazin.store.ReadLaterArticleStore;
// adapter spolocny pre ulozene a oblubene clanky, o ktory zdroj ide je ulozene v premennej isFavorites
public class ReadLaterPagerAdapter extends PagerAdapter {
	
	DisplayImageOptions options;
	private Context context;
	ImageLoader imageLoader;
	ReadLaterArticleStore store;
	int rowHeight;
	int rowWidth;
	int rowLastWidth;
	int rowLastHeight;
	int m;
	int column = 5;
    int row = 4;
	boolean isFavorites;
	ArrayList<GridLayout.LayoutParams> params;

	public ReadLaterPagerAdapter(Context context,DisplayImageOptions options,ImageLoader imageLoader, List<String> listId, 
			boolean isFavorites) {
		this.options=options;
		this.context = context;
		this.imageLoader=imageLoader;
		this.isFavorites=isFavorites;
		//store je sa vytvara rovnaky pre oba typy, dolezity je zoznam id-ciek clankov ktore su ulozene v jednom alebo druhom zozname,
		//zvysok podobny ako pri ostatnych adapteroch
		 store = new ReadLaterArticleStore(listId,context);

		  	
			float mf= ((MainActivity)this.context).screenWidth;
			m=(int) (mf/Constants.MARGIN_WIDTH_KOEF);
	    	float width = (float)(mf-6*m);
	    	float height = ((MainActivity)this.context).screenHeight-5*m; 
	    	rowHeight = (int)(height/4);   	
	    	rowWidth = (int) (width/5);
	    	rowLastWidth = (int) (mf-6*m-4*rowWidth);
	    	rowLastHeight = (int) (height-3*rowHeight);
	    	
	    	 GridLayout.LayoutParams param1 =new GridLayout.LayoutParams();
             param1.setMargins(m, m, m, 0);            
             param1.width = rowWidth;
             param1.height=rowHeight;    
            
             GridLayout.LayoutParams param2 =new GridLayout.LayoutParams();     
             param2.setMargins(0, m, m, 0);           
             param2.width = rowWidth;
             param2.height=rowHeight;
          
             GridLayout.LayoutParams param3 =new GridLayout.LayoutParams();
             param3.setMargins(0, m, m, 0);  
             param3.width = rowWidth;
             param3.height=rowHeight;       
            
             GridLayout.LayoutParams param4 =new GridLayout.LayoutParams();
             param4.setMargins(0, m, m, 0); 
             param4.width = rowWidth;
             param4.height=rowHeight;
            
             GridLayout.LayoutParams param5 =new GridLayout.LayoutParams();
             param5.setMargins(0, m, m, 0); 
             param5.width = rowLastWidth;
             param5.height=rowHeight;
             
             GridLayout.LayoutParams param6 =new GridLayout.LayoutParams();
             param6.setMargins(m, m, m, 0);  
             param6.width = rowWidth;
             param6.height=rowHeight;
             
             GridLayout.LayoutParams param7 =new GridLayout.LayoutParams();
             param7.setMargins(0, m, m, 0);  
             param7.width = rowWidth;
             param7.height=rowHeight;
             
             GridLayout.LayoutParams param8 =new GridLayout.LayoutParams();
             param8.setMargins(0, m, m, 0);  
             param8.width = rowWidth;
             param8.height=rowHeight;

             
             GridLayout.LayoutParams param9 =new GridLayout.LayoutParams();
             param9.setMargins(0, m, m, 0);  
             param9.width = rowWidth;
             param9.height=rowHeight;
             
             GridLayout.LayoutParams param10 =new GridLayout.LayoutParams();
             param10.setMargins(0, m, m, 0);  
             param10.width =  rowLastWidth;
             param10.height=rowHeight;
             
             GridLayout.LayoutParams param11 =new GridLayout.LayoutParams();
             param11.setMargins(m, m, m, 0);            
             param11.width = rowWidth;
             param11.height=rowHeight;    
            
             GridLayout.LayoutParams param12 =new GridLayout.LayoutParams();     
             param12.setMargins(0, m, m, 0);           
             param12.width = rowWidth;
             param12.height=rowHeight;
          
             GridLayout.LayoutParams param13 =new GridLayout.LayoutParams();
             param13.setMargins(0, m, m, 0);  
             param13.width = rowWidth;
             param13.height=rowHeight;       
            
             GridLayout.LayoutParams param14 =new GridLayout.LayoutParams();
             param14.setMargins(0, m, m, 0); 
             param14.width = rowWidth;
             param14.height=rowHeight;
            
             GridLayout.LayoutParams param15 =new GridLayout.LayoutParams();
             param15.setMargins(0, m, m, 0); 
             param15.width =  rowLastWidth;
             param15.height=rowHeight;
             
             GridLayout.LayoutParams param16 =new GridLayout.LayoutParams();
             param16.setMargins(m, m, m, m);  
             param16.width = rowWidth;
             param16.height=rowLastHeight;
             
             GridLayout.LayoutParams param17 =new GridLayout.LayoutParams();
             param17.setMargins(0, m, m, m);  
             param17.width = rowWidth;
             param17.height=rowLastHeight;
             
             GridLayout.LayoutParams param18 =new GridLayout.LayoutParams();
             param18.setMargins(0, m, m, m);  
             param18.width = rowWidth;
             param18.height=rowLastHeight;

             
             GridLayout.LayoutParams param19 =new GridLayout.LayoutParams();
             param19.setMargins(0, m, m, m);  
             param19.width = rowWidth;
             param19.height=rowLastHeight;
             
             GridLayout.LayoutParams param20 =new GridLayout.LayoutParams();
             param20.setMargins(0, m, m, m);  
             param20.width =  rowLastWidth;
             param20.height=rowLastHeight;
             
             params = new ArrayList<GridLayout.LayoutParams>();
             params.add(param1);params.add(param2);params.add(param3);params.add(param4);params.add(param5);
             params.add(param6);params.add(param7);params.add(param8);params.add(param9);params.add(param10);
             params.add(param11);params.add(param12);params.add(param13);params.add(param14);params.add(param15);
             params.add(param16);params.add(param17);params.add(param18);params.add(param19);params.add(param20);
		 
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return (store.getCount() +1+ 19) / 20;
	}	

	@Override
	public Object instantiateItem(ViewGroup view, int pos) {
		
		GridLayout g = new GridLayout(context);
		
         g.setColumnCount(column);
         g.setRowCount(row);    
        
            
             
             int k =0;
             if (pos==0) {
            	 FrameLayout f1;
            	 if (isFavorites) f1 = new FavoritesFrame(context, store.getCount()); else f1 = new ReadFrame(context, store.getCount());
            	 f1.setLayoutParams (params.get(0));
            	 g.addView(f1); 
            	 k=1;
             }
             for(int i=k;i<20;i++){  
            	 FrameLayout f1=null;
            	
            	 
            	 if ((pos*20+i) < (store.getCount()+1)){
            	 f1 = new ArticleThumbnailFrame(context, store.getItem(pos*20+i-1),params.get(i).height);
            	 
            	 f1.setLayoutParams (params.get(i));           	 
                 g.addView(f1);
            	 } else  {
            		 f1 = new ArticleThumbnailFrame(context);
            	 f1.setLayoutParams (params.get(i));
            	 g.addView(f1);}
            	 
             }
             
             view.addView(g);   
        return g;
		
		/**
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

		imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
				}
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				spinner.setVisibility(View.GONE);
			}
		});
**/
		
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
} 
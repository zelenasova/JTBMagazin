package jtb.magazin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.TwitterModel;
import jtb.magazin.store.TwitterStore;

//Tento adapter je iny ako ostatne, pretoze sa nenaplna ViewPager ale horizontalne listView - two way view
public class TwitterAdapter extends BaseAdapter {
    TwitterStore store;
	private Context mContext;
	DisplayMetrics metrics;
	float width;
	float height;
	int m;
	float itemWidth; 
	float itemHeight;
	 float lin1Height;
     float lin2Height;
     float lin22Height;
     float lin3Height;
     float padding;
     float retweetedHeight;
     float retweetedWidth;
     float dy;


    public TwitterAdapter(Context context,TwitterStore twitterStore) {
    	mContext = context;
    	metrics = mContext.getResources().getDisplayMetrics();
    	width = metrics.widthPixels;
    	height = metrics.heightPixels;
    	float mFloat = width/Constants.MARGIN_WIDTH_KOEF;
    	m=(int) (mFloat);  
    	// kedze 3/4 vysky tvori hlavny grid, jedna stvrtina je pre twitter pasik
    	itemHeight = ((MainActivity)context).screenHeight/4-m; 
    	//pomer stran je z grafiky 50 ku 38
    	itemWidth = itemHeight*50/38;
    	this.store=twitterStore;
    	dy = height/Constants.HEIGHT_KOEF;  
    	//vyska obsahu jedneho ramceka zhora je odsadenie 6/100 a zdola 4/100, lin2 je LinearLayout do ktoreho sa umiestnuje obsah ramceka
    	//lin22 je hlavicka clanku ktora obsahuje fotku + autor a datum
    	 lin2Height= itemHeight*9/10;
         lin22Height = itemHeight/5; 
         padding = itemHeight/10;
         retweetedHeight = itemHeight/12;
         retweetedWidth = itemHeight/8;
    	
    }
    public int getCount() {
    	return (store.getCount());
    	}
    
    public TwitterModel getItem(int pos) { return store.getItem(pos); }
  

    
    @Override
    public View getView(int pos, View v, ViewGroup p) {
    	   
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.twitter_item, null);
       
            TextView text1 = (TextView) v.findViewById(R.id.text1);
            TextView text2 = (TextView) v.findViewById(R.id.text2);
            TextView text3 = (TextView) v.findViewById(R.id.text3);
            TextView text4 = (TextView) v.findViewById(R.id.text_retweeted);
            text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)mContext).getResources().getDimension(R.dimen.size11));
            text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)mContext).getResources().getDimension(R.dimen.size10));
            text3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)mContext).getResources().getDimension(R.dimen.size10));
            text4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)mContext).getResources().getDimension(R.dimen.size8));
            final String autor = store.getItem(pos).getAutor();
            final String id = store.getItem(pos).getId();
            String date = store.getItem(pos).getDatePublish();
            text1.setText(Html.fromHtml(autor));

    		text1.setTypeface(((MainActivity) mContext).fedraSansAltProBook);
            text1.setMaxLines(1);
    		text2.setTypeface(((MainActivity) mContext).openSansRegular);
    		text3.setTypeface(((MainActivity) mContext).openSansRegular);
    		text4.setTypeface(((MainActivity) mContext).fedraSansAltProBook);
            text2.setMaxLines(1);
            text2.setText(Html.fromHtml(date));

            FrameLayout frame_wrapper = new FrameLayout(mContext);
            LinearLayout lin = (LinearLayout) v.findViewById(R.id.lin); //cely wrapper
            LinearLayout link = (LinearLayout) v.findViewById(R.id.link); //link - vrstva ponad
            LinearLayout lin2 = (LinearLayout) v.findViewById(R.id.lin2); //obsah
            LinearLayout lin22 = (LinearLayout) v.findViewById(R.id.lin22); //hlavicka
            LinearLayout lin221 = (LinearLayout) v.findViewById(R.id.lin221);//autor+datum
            LinearLayout.LayoutParams lin22Params = (LayoutParams) lin22.getLayoutParams();
            lin2.setPadding((int)padding, 0, (int)padding, 0);
            lin221.setPadding((int)padding, 0, 0, 0);          
            lin22Params.height= (int)lin22Height;
            
            //obrazok autora zaplna vysku hlavicky
            ImageView img = (ImageView) v.findViewById(R.id.twitter_foto);
            LinearLayout.LayoutParams imgParams = (LayoutParams) img.getLayoutParams();
            imgParams.width=(int)lin22Height;
            imgParams.height=(int)lin22Height;
            
            ImageView img2 = (ImageView) v.findViewById(R.id.retweeted_foto);
            LinearLayout.LayoutParams img2Params = (LayoutParams) img2.getLayoutParams();
            img2Params.width=(int)retweetedWidth;
            img2Params.height=(int)retweetedHeight;
            img2Params.setMargins(0, 0, (int)(retweetedWidth/3), 0);

            // pre efektivnejsie nacitanie obrazka sa pouziva imageLoader
            ((MainActivity)mContext).imageLoader.displayImage(store.getItem(pos).getImageUrl(), img, ((MainActivity)mContext).options, new SimpleImageLoadingListener() {
     			@Override
     			public void onLoadingStarted(String imageUri, View view) {  				
     			}  
     			@Override
     			public  void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {			
     			}
     		});
            text4.setText("retweeted by "+store.getItem(pos).getRetweetedBy());
            if (store.getItem(pos).getRetweetedBy().equals("")){
            	text4.setText("");
            	img2.setVisibility(View.INVISIBLE);
            }



        FrameLayout.LayoutParams linParams = new FrameLayout.LayoutParams((int)itemWidth, (int)itemHeight);
           
            lin.setLayoutParams(linParams);
            link.setLayoutParams(linParams);
            //text3 je hlavny text
            text3.setPadding(0, 0, 0, (int) (5*dy));
            String text = store.getItem(pos).getTitle();
           text3.setText(Html.fromHtml(text));

           RelativeLayout.LayoutParams text3Params = (android.widget.RelativeLayout.LayoutParams) text3.getLayoutParams();
           text3Params.setMargins(0, (int)(lin22Height/4), 0, 0);
           //musime vypocitat kolko riadkov sa zmesti
           int text3Height=(int)lin2Height-(int)lin22Height-(int)(retweetedWidth)-(int)(10*dy);//10*dy je 4*dy padding + rezerva 6dy
          
              int lineHeight = text3.getLineHeight();
              int maxLines = (int) (text3Height / lineHeight);
             // int maxLines = 5;
              text3.setMaxLines(maxLines);
           
            Linkify.addLinks(text3, Linkify.WEB_URLS);
            
          
           frame_wrapper.addView(v);


        link.setOnClickListener(new View.OnClickListener() //slide panel
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = null;
                PackageManager pkManager = mContext.getPackageManager();
                try {
                    PackageInfo pkgInfo = pkManager.getPackageInfo("com.twitter.android", 0);
                    String getPkgInfo = pkgInfo.toString();
                    System.out.println(getPkgInfo);

                    //if (getPkgInfo.equals("com.twitter.android"))   {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://status?id="+id));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                    //}
                } catch (PackageManager.NameNotFoundException e) {


                    String url = "http://www.twitter.com/"+autor+"/status/"+id;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(browserIntent);

                }


            }
        });



        return frame_wrapper;
    	
  
    	
    	
   
    }
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
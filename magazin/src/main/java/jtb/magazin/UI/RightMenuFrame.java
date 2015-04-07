package jtb.magazin.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;
import jtb.magazin.model.MagnusListModel;

//predtavuje jednu polozku odporucanych clankov v pravom menu
public class RightMenuFrame extends LinearLayout {
	
	TextView text3;
	TextView text1;
	ImageView img;
	LinearLayout.LayoutParams img_params;
    TextView text2;
    RelativeLayout rel;
    LinearLayout rel2;
    LayoutParams params;
    RelativeLayout.LayoutParams params2;
	RelativeLayout.LayoutParams params1;
	RelativeLayout.LayoutParams params3;
	LinearLayout.LayoutParams magnus_params2;
	LinearLayout.LayoutParams magnus_params1;
	LinearLayout.LayoutParams magnus_params3;
	LinearLayout.LayoutParams divider_params;
	Context context;
	ArticleModel model;
	public int i;
	
	//rozoznavame 2 typy na zaklade premennej isMagnusFrame ci ide o obycajny clanok alebo odkaz na aktualny magnus magazin
	public RightMenuFrame(final Context context, final ArticleModel model,int i, final boolean isMagnusFrame) {
		super(context);
		this.model=model;
		this.i=i;
		this.context=context;
		// TODO Auto-generated constructor stub
		
		 LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		 View convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		 img = (ImageView) convertView.findViewById(R.id.icon);
		 img_params = (LayoutParams) img.getLayoutParams();
		 
		        DisplayMetrics metrics = null;
		   		double height;
		   		 int item = 0;
		   		 metrics = context.getResources().getDisplayMetrics();
		   		 height = ((MainActivity)context).screenHeight;
		   		int dividerHeight = (int) (height/40);
		   		//ak je magnus tak frame je vacsi a zaberie miesto namiesto dcoch
		   		if (isMagnusFrame){
		   			item =(int) (2*(((height*89/100-5*dividerHeight)/5))+dividerHeight);
		   		} else item =(int) ((height*89/100-5*dividerHeight)/5);
				 
		   		LinearLayout lin = (LinearLayout) convertView.findViewById(R.id.related_articles); 
		   		
		   		text1 = new TextView(context);		
				text2 = new TextView(context);
				text3 = new TextView(context);
		           params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, item); 
		          //magnus frame je linearLayout zarovnany vertikalne na stres, obycajny frame je relative kde perex je dole a nazov a datum hore
		       /*  if(!isMagnusFrame){
		        rel = (RelativeLayout) convertView.findViewById(R.id.relRelated); 
			     params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			     params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			     params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			     text1.setId(500);
					text2.setId(501);
					text3.setId(502);
					params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					params2.addRule(RelativeLayout.BELOW, text1.getId());
					rel.addView(text1,params1);
					rel.addView(text2,params2);			
					rel.addView(text3,params3);
					rel.setLayoutParams(params);
					rel.setPadding(0, 0, metrics.widthPixels/14*13/5/20, 0);
					//text3.setMaxLines(4);
					
		         } else {*/
		        	 rel2 = (LinearLayout) convertView.findViewById(R.id.relRelated2);
		        	 magnus_params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				     magnus_params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				     magnus_params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				     rel2.addView(text1,magnus_params1);
					rel2.addView(text2,magnus_params2);			
					rel2.addView(text3,magnus_params3);
					rel2.setLayoutParams(params);
					rel2.setPadding(0, 0, metrics.widthPixels/14*13/5/20, 0);
		        // }

				ViewTreeObserver vto = text3.getViewTreeObserver();
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				    private int maxLines = -1;
				    
				    //pred vykreslovanim sa zisti vyska perexu(text3) a vpripade ze ma nad 3 riadky oreze sa a na konci textu sa daju 3 bodky
				    @Override
				    public void onGlobalLayout() {
				        if (maxLines < 0 && text3.getHeight() > 0 && text3.getLineHeight() > 0) {
				        	if(!isMagnusFrame){
				            double height = text3.getHeight();
				            double  lineHeight = text3.getLineHeight();
				            maxLines = (int) (height / lineHeight);
				            if (maxLines>3){
				            int start =text3.getLayout().getLineStart(0);
				            int end = text3.getLayout().getLineEnd(2);
				            //System.out.println(text3.getHeight());			           
				             text3.setText(model.getPerex().substring(start, end)+"...");
				            }
				             text3.setMaxLines(3);
				            
				        	} else {
				        		text3.setMaxLines(10);
				        	}
				            
	            
				        }
				    }
				});
				
				
			    params.setMargins(0, 0, metrics.widthPixels/20, 0); 
				text2.setTextColor(Color.parseColor("#5b5b5b"));
				

				text2.setTypeface(((MainActivity)context).fedraSansAltProBold);
		
				img_params.height=item;
				img.setScaleType(ScaleType.CENTER_CROP);
				
				//pri magnuse sa berie aktualny magazin, pri kliku na neho staci zatvorit aktulany clanok v magazine a dostavame sa na obsah magazinu
				if (isMagnusFrame){
					img_params.setMargins(metrics.widthPixels/35, +dividerHeight, metrics.widthPixels/50, 0);
					String magazinID=model.getMagazinID();
					final MagnusListModel magnusListModel = findMagnusItem(magazinID);
					text1.setText("MAGNUS "+magnusListModel.getTitle());
					text2.setText(magnusListModel.getSubTitle());
					text3.setText(magnusListModel.getPerex());
					text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size16));
					img_params.width=item*27/34;
					((MainActivity)context).imageLoader.displayImage(magnusListModel.getCover1(), img, ((MainActivity)context).options, new SimpleImageLoadingListener() {
		    			@Override
		    			public void onLoadingStarted(String imageUri, View view) {
		    			}
		    		});   
					setOnClickListener(new View.OnClickListener() 
		             {
		                 @Override
		                 public void onClick(View v)
		                 { 	
		                	 if (((MainActivity)context).actualView==1){
		                		 ((MainActivity)context).displayView(9, "", null);
		                	 } else {
		                		 ((MainActivity)context).createMagnusFragment(magnusListModel);
		                	 }
		                 }
		             });			        
		           
				} else {
					img_params.setMargins(metrics.widthPixels/35,0, metrics.widthPixels/50, 0);
					text1.setText(model.getDatePublish());
					text2.setText(model.getTitle());
					text3.setText(model.getPerex());
					text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
					img_params.width=item;
					((MainActivity)context).imageLoader.displayImage(model.getImageUrl(), img, ((MainActivity)context).options, new SimpleImageLoadingListener() {
		    			@Override
		    			public void onLoadingStarted(String imageUri, View view) {
		    			}
		    		});   
		           
				}
				
				
				text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size10));
				text1.setTextColor(Color.parseColor("#b2b2b2"));
				text1.setIncludeFontPadding(false);
				

				text3.setTextColor(Color.parseColor("#1a1a1a"));
				text3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size10));
				text3.setEllipsize(TruncateAt.MARQUEE);
				
				LinearLayout divider = new LinearLayout(context);
				 divider_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  dividerHeight);
				 divider.setLayoutParams(divider_params);
				
				 this.setOrientation(LinearLayout.VERTICAL);
				this.addView(lin);
				this.addView(divider);
	}

	private MagnusListModel findMagnusItem(String magazinID) {
		System.out.println(magazinID);
		for (MagnusListModel item:((MainActivity)context).magnusList.getItems()){
			if (item.getId().equals(magazinID)) return item;
		}
		return ((MainActivity)context).magnusList.getItem(0);
	}

	public int getI() {
		return i;
	}
	
	


}

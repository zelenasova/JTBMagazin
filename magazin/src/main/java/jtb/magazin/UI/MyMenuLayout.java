package jtb.magazin.UI;

import java.util.ArrayList;

import android.content.Intent;
import jtb.magazin.Constants;
import jtb.magazin.CustomHttpClient;
import jtb.magazin.Functions;
import jtb.magazin.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import jtb.magazin.PdfActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

//cele lave vysuvacie menu sa nastavuje tu. Zakladna struktura menu je v activity_main.xml 
public class MyMenuLayout  {

	Context context;
	ImageView searchArticles;
	public TextView small;
	public TextView medium;
	public TextView big;
	public boolean magazin;
	public boolean klub;
	public boolean color;
	public boolean language;
	public ImageView slide_magazin_img;
	public ImageView slide_klub_img;
	public ImageView slide_color_img;
	public ImageView slide_language_img;
	EditText edit_text;
	EditText edit_login;
	CustomWebView myWebView;
	SharedPreferences prefs;
	LinearLayout lin_tut;
	LinearLayout lin_login;
	int m;
	int slide_height;
	float dx;
	float dy;
	final public RelativeLayout slide_settings;
	float screenHeight;
	float screenWidth;
	LinearLayout loginPopup;
	TextView t1Count;
	TextView t2Count;
	TextView t3Count;
    TextView t4Count;
	TextView t1;
	TextView t2;
	TextView t3;
	TextView t4;
	TextView t5;
	TextView t6;
	TextView tutorial_text;
	TextView login_text;
	RelativeLayout rel1;
	RelativeLayout rel2;
	RelativeLayout rel3;
    RelativeLayout rel4;
	LinearLayout row1;
	LinearLayout row2;
	LinearLayout row3;
	LinearLayout row4;
	public LinearLayout lin8;
	public LinearLayout lin_left;
	public LinearLayout lin_right;
	public ImageView imgCenter;
	TextView set;
	TextView velkost;
	TextView magazin_notifikacie;
	TextView magnus_notifikacie;
	TextView farba_pozadia;
	TextView vyber_jazyka;
	RelativeLayout.LayoutParams slide;
	LinearLayout topSlide;
	LinearLayout centerSlide;
	LinearLayout bottomSlide;
	RelativeLayout.LayoutParams topSlidePar;


	
	public MyMenuLayout(final Context context,LinearLayout settings,int menuContentWidth) {

		this.context=context;
    	screenHeight= ((MainActivity)context).screenHeight;
    	screenWidth= ((MainActivity)context).screenWidth;
		m=(int) (screenWidth/Constants.MARGIN_WIDTH_KOEF*2);
		prefs= PreferenceManager.getDefaultSharedPreferences(context);
    	dx =  (screenWidth/Constants.WIDTH_KOEF);
    	dy =  (screenHeight/Constants.HEIGHT_KOEF);
    	
    	//menu je rozdelene do piatich hlavnych LinearLayotov
    	row1= (LinearLayout) ((Activity) context).findViewById(R.id.menuRow1); //svet ocami banky +Magazin magnus
    	row2= (LinearLayout) ((Activity) context).findViewById(R.id.menuRow2); //informacny servis + klub magnus
    	row3= (LinearLayout) ((Activity) context).findViewById(R.id.read_later); //ulozene a oblubene clanky
    	row4= (LinearLayout) ((Activity) context).findViewById(R.id.settings); //logo JTB + nastavenia
    	edit_text= (EditText) ((Activity) context).findViewById(R.id.search_edit_text); // vyhladavanie
    	
    	//sirka a vyska polozky menu(4 hlavne)
    	int img_width = (menuContentWidth - 4*m)/2;
    	int img_height = img_width*8/9;
    	
    	//vyhladavanie
    	LayoutParams edit_text_params = (LayoutParams) edit_text.getLayoutParams();
    	edit_text_params.setMargins(2*m, 0, 0, 0);
    	searchArticles= (ImageView) ((Activity) context).findViewById(R.id.searchArticles);
    	
    	//vysuvacie podmenu, implicitne neviditelne
    	slide_settings= (RelativeLayout) ((Activity) context).findViewById(R.id.slide_settings);
    	
    	//odsadenie hlavnych poloziek menu
    	LinearLayout.LayoutParams rowParams1 = (LayoutParams) row1.getLayoutParams();
    	rowParams1.setMargins(3*m/2, m, 3*m/2, 0);
    	rowParams1.height=img_height;
    	LinearLayout.LayoutParams rowParams2 = (LayoutParams) row2.getLayoutParams();
    	rowParams2.setMargins(3*m/2, m, 3*m/2, 0);
    	rowParams2.height=img_height;
    	LinearLayout.LayoutParams rowParams3 = (LayoutParams) row3.getLayoutParams();
    	rowParams3.setMargins(3*m/2, 0, 3*m/2, 0);
     
    	 
    	LinearLayout lin1 = new LinearLayout(context); // svet ocami
    	LinearLayout lin2 = new LinearLayout(context); // magazin magnus
    	LinearLayout lin3 = new LinearLayout(context); // informacny servis
    	LinearLayout lin4 = new LinearLayout(context); // klub magnus
    	LinearLayout lin5 = new LinearLayout(context); // ulozene clanky
    	LinearLayout lin6 = new LinearLayout(context); // oblubene clanky
    	LinearLayout lin7 = new LinearLayout(context); //logo JTB
    	lin8 = new LinearLayout(context); //nastavenia + login
    	

    	RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	lp.setMargins(0, (int)(15*dy), 0, 0);
    	RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	lp2.setMargins(0, (int)(15*dy), 0, 0);
    	RelativeLayout.LayoutParams lp3=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	lp3.setMargins(0, (int)(15*dy), 0, 0);
    	
    	//svet ocami banky
    	rel1 = new RelativeLayout(context);
    	t1 = new TextView(context);
    	t1Count = new TextView(context);
    	t1Count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	t1Count.setLayoutParams(lp);
    	t1Count.setBackgroundColor(Color.parseColor("#90ffffff"));
    	t1Count.setPadding((int)(15*dx), 0, (int)(15*dx), 0);
    	t1Count.setTextColor(Color.RED);
    	t1.setText(Html.fromHtml(((MainActivity)context).getString(R.string.svet_ocami)));
    	t1.setTextColor(Color.WHITE);
    	t1.setShadowLayer(dx*2, dx, dx, Color.BLACK);
    	t1.setPadding(2*m, m, 0, 0);
    	t1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size14));
    	rel1.addView(t1);
    	rel1.addView(t1Count); 	
    	
    	//magazin magnus
    	rel2 = new RelativeLayout(context);
    	t2 = new TextView(context);
    	t2Count = new TextView(context);
    	t2Count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	t2Count.setLayoutParams(lp2);
    	t2Count.setGravity(Gravity.CENTER);
    	t2Count.setBackgroundColor(Color.parseColor("#90ffffff"));
    	t2Count.setPadding((int)(15*dx), 0, (int)(15*dx+m), 0);
    	t2Count.setTextColor(Color.RED);
    	t2.setText(Html.fromHtml(((MainActivity)context).getString(R.string.magazin_magnus)));
    	t2.setTextColor(Color.WHITE);
    	t2.setShadowLayer(dx*2, dx, dx, Color.BLACK);
    	t2.setPadding(2*m, m, 0, 0);
    	t2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size14));
    	rel2.addView(t2);
    	rel2.addView(t2Count);
    	
    	//informacny servis
    	rel3 = new RelativeLayout(context);
    	t3 = new TextView(context);
    	t3Count = new TextView(context);
    	t3Count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	t3Count.setLayoutParams(lp3);
    	t3Count.setBackgroundColor(Color.parseColor("#90ffffff"));
    	t3Count.setPadding((int)(15*dx), 0, (int)(15*dx), 0);
    	t3Count.setTextColor(Color.RED);
    	t3.setText(Html.fromHtml(((MainActivity)context).getString(R.string.informacny_servis)));
    	t3.setTextColor(Color.WHITE);
    	t3.setShadowLayer(dx*2, dx, dx, Color.BLACK);
    	t3.setPadding(2*m, m, 0, 0);
    	t3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size14));
    	rel3.addView(t3);
    	rel3.addView(t3Count);
    	
    	//klub magnus
        rel4 = new RelativeLayout(context);
    	t4 = new TextView(context);
        t4Count = new TextView(context);
        t4Count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
        t4Count.setLayoutParams(lp3);
        t4Count.setBackgroundColor(Color.parseColor("#90ffffff"));
        t4Count.setPadding((int)(15*dx), 0, (int)(15*dx), 0);
        t4Count.setTextColor(Color.RED);
    	t4.setText(Html.fromHtml(((MainActivity)context).getString(R.string.klub_magnus)));
    	t4.setTextColor(Color.WHITE);
    	t4.setShadowLayer(dx*2, dx, dx, Color.BLACK);
    	t4.setPadding(2*m, m, 0, 0);
    	t4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size14));
        rel4.addView(t4);
    	
    	//updateMenuItems();
    	Typeface tf1 = ((MainActivity) context).fedraSansAltProBold;
		t1.setTypeface(tf1);
		t2.setTypeface(tf1);
		t3.setTypeface(tf1);
		t4.setTypeface(tf1);
    	
    	
    	
    	RelativeLayout.LayoutParams relParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
    	RelativeLayout.LayoutParams relParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
    	RelativeLayout.LayoutParams relParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
    	LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(img_width,LayoutParams.MATCH_PARENT);
    	LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(img_width+m,LayoutParams.MATCH_PARENT);
    	LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(menuContentWidth/2,LayoutParams.MATCH_PARENT);
    	LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(menuContentWidth/2,LayoutParams.MATCH_PARENT);
    	params2.setMargins(m,0,0,0);
    	rel1.setLayoutParams(relParams1);
    	rel2.setLayoutParams(relParams2);
    	rel3.setLayoutParams(relParams3);
        rel4.setLayoutParams(relParams3);
    	lin1.setLayoutParams(params1);
    	lin2.setLayoutParams(params2);
    	lin3.setLayoutParams(params1);
    	lin4.setLayoutParams(params2);
    	lin5.setLayoutParams(params1);
    	lin6.setLayoutParams(params2);
    	lin7.setLayoutParams(params3);
    	lin8.setLayoutParams(params4);
    	row1.addView(lin1);
    	row1.addView(lin2);
    	row2.addView(lin3);
    	row2.addView(lin4);
    	row3.addView(lin5);
    	row3.addView(lin6);
    	row4.addView(lin7);
    	row4.addView(lin8);
    	lin1.addView(rel1);
    	lin2.addView(rel2);
    	lin3.addView(rel3);
        lin4.addView(rel4);
        //lin4.setVisibility(View.INVISIBLE);
    	lin5.setBackgroundColor(Color.parseColor("#e2e2e2"));
    	lin6.setBackgroundColor(Color.parseColor("#e2e2e2"));
    	lin7.setBackgroundResource(R.drawable.logo);
    	lin8.setBackgroundColor(Color.parseColor("#e2e2e2"));
    	
    	//******************************************read later a ublubene******************************************************
    	//ulozene
    	LinearLayout lin_r1 = new LinearLayout(context);
    	LinearLayout.LayoutParams params_r1 = new LinearLayout.LayoutParams(18*m,3*m);
    	lin_r1.setLayoutParams(params_r1);
    	lin5.addView(lin_r1);
    	lin5.setGravity(Gravity.CENTER);
    	ImageView img3 = new ImageView(context);
    	img3.setBackgroundResource(R.drawable.flag);
    	LinearLayout.LayoutParams params_img3 = new LinearLayout.LayoutParams(3*m*5/6,3*m);
    	t5 = new TextView(context);
    	t5.setText(((MainActivity)context).getString(R.string.citat_neskor));
    	t5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size11));
    	t5.setTextColor(Color.parseColor("#5b5b5b"));
    	LinearLayout lin_r1_pom = new LinearLayout(context);
    	LinearLayout.LayoutParams params_r1_pom = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    	lin_r1_pom.setLayoutParams(params_r1_pom);
    	lin_r1_pom.setGravity(Gravity.CENTER | Gravity.RIGHT);
    	lin_r1.addView(img3,params_img3);
    	lin_r1.addView(lin_r1_pom,params_r1_pom);
    	lin_r1_pom.addView(t5);
    	
    	//oblubene
    	lin6.setGravity(Gravity.CENTER);
    	ImageView img4 = new ImageView(context);
    	img4.setBackgroundResource(R.drawable.star);
    	LinearLayout.LayoutParams params_img4 = new LinearLayout.LayoutParams(3*m,3*m);
    	params_img4.setMargins(0, 0, 2*m, 0);
    	img4.setLayoutParams(params_img4);	
    	t6 = new TextView(context);
    	t6.setGravity(Gravity.CENTER);
    	t6.setText(((MainActivity)context).getString(R.string.oblubene));
    	t6.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size11));
    	t6.setTextColor(Color.parseColor("#5b5b5b"));	
    	lin6.addView(img4);
    	lin6.addView(t6);
    	
    	Typeface tf2 = ((MainActivity)context).fedraSansAltProMedium;
    	t5.setTypeface(tf2);
    	t6.setTypeface(tf2);
    	//******************************************end read later******************************************************
    	
    	//******************************************settings******************************************************
    	lin8.setGravity(Gravity.CENTER);	
    	lin8.setOrientation(LinearLayout.HORIZONTAL);	
    	lin_left = new LinearLayout(context);
    	LinearLayout.LayoutParams params_left = new LinearLayout.LayoutParams(menuContentWidth/4,LinearLayout.LayoutParams.MATCH_PARENT);
    	lin_left.setLayoutParams(params_left);
    	lin_left.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
    	lin_right = new LinearLayout(context);
    	LinearLayout.LayoutParams params_right = new LinearLayout.LayoutParams(menuContentWidth/4,LinearLayout.LayoutParams.MATCH_PARENT);
    	lin_right.setLayoutParams(params_right);
    	lin_right.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
    	ImageView img1 = new ImageView(context);
    	ImageView img2 = new ImageView(context);
    	img1.setBackgroundResource(R.drawable.zone_icon);
    	img2.setBackgroundResource(R.drawable.settings_icon);
    	LinearLayout.LayoutParams params_img1 = new LinearLayout.LayoutParams(3*m*5/6,3*m);
    	params_img1.setMargins(0, 0, menuContentWidth/16, 0);
    	LinearLayout.LayoutParams params_img2 = new LinearLayout.LayoutParams(3*m,3*m);
    	params_img2.setMargins(menuContentWidth/16, 0, 0, 0);
    	//ak som prihlaseny
    	imgCenter = new ImageView(context);
    	imgCenter.setBackgroundResource(R.drawable.settings_icon);
    	LinearLayout.LayoutParams params_img2Logged = new LinearLayout.LayoutParams(3*m,3*m);
    	imgCenter.setLayoutParams(params_img2Logged);
    	
    	//*********************koniec ak som prihlaseny
    	//if(((MainActivity)context).logged){
    	if(((MainActivity)context).logged){
			lin8.addView(imgCenter);
		} else {
			lin8.addView(lin_left);
			lin8.addView(lin_right);
		}
    	lin_left.addView(img1,params_img1);
    	lin_right.addView(img2,params_img2);
    	
    	
    
    	//******************************************end settings******************************************************	
    	
    	//******************************************create slide settings************************************************************
    	slide= (android.widget.RelativeLayout.LayoutParams) slide_settings.getLayoutParams();
    	slide_height = (int) (screenHeight*70/100);
    	//slide.width=menuContentWidth;
    	slide.height=(int) (slide_height+140*dy);
    	
    	// slide settings prihlasenie a zvysok
    	topSlide = new LinearLayout(context);
    	centerSlide = new LinearLayout(context);
    	bottomSlide = new LinearLayout(context);
    	centerSlide.setId(2001);
    	bottomSlide.setId(2000);
    	topSlidePar= new RelativeLayout.LayoutParams(menuContentWidth,slide_height);
    	RelativeLayout.LayoutParams bottomSlidePar= new RelativeLayout.LayoutParams(menuContentWidth,(int) (80*dy));
    	RelativeLayout.LayoutParams centerSlidePar= new RelativeLayout.LayoutParams(menuContentWidth,(int) (60*dy));
    	bottomSlidePar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	topSlidePar.addRule(RelativeLayout.ABOVE, centerSlide.getId());
    	centerSlidePar.addRule(RelativeLayout.ABOVE, bottomSlide.getId());
    	topSlide.setLayoutParams(topSlidePar);
    	centerSlide.setLayoutParams(centerSlidePar);
    	//centerSlide.setBackgroundColor(Color.BLUE);
    	bottomSlide.setLayoutParams(bottomSlidePar);
    	
    	slide_settings.addView(topSlide);
    	slide_settings.addView(centerSlide);
    	slide_settings.addView(bottomSlide);
    	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	inflater.inflate(R.layout.slide_menu_layout, topSlide,true);
    	//ako pouzivat tutorial*********************************************
    	lin_tut = new LinearLayout(context);
    	LinearLayout.LayoutParams lin_tut_params = new LinearLayout.LayoutParams(menuContentWidth*5/10,(int) (35*dy));
    	lin_tut.setLayoutParams(lin_tut_params);
    	lin_tut.setBackgroundColor(Color.parseColor("#cccccc"));
    	bottomSlide.addView(lin_tut);
    	bottomSlide.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
    	lin_tut.setGravity(Gravity.CENTER);
    	ImageView img_info = new ImageView(context);
    	img_info.setBackgroundResource(R.drawable.slide_info);
    	LinearLayout.LayoutParams params_img_info = new LinearLayout.LayoutParams((int)(18*dy),(int) (18*dy));
    	params_img_info.setMargins(0, 0, m, 0);
    	img_info.setLayoutParams(params_img_info);
    	tutorial_text = new TextView(context);
    	tutorial_text.setText(((MainActivity)context).getString(R.string.help));
    	tutorial_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	tutorial_text.setTextColor(Color.parseColor("#393939"));
    	lin_tut.addView(img_info);
    	lin_tut.addView(tutorial_text);
    	//*******koniec ako pouzivat tutorial************************************
    	//Prihlasenie*********************************************
    	lin_login = new LinearLayout(context);
    	LinearLayout.LayoutParams lin_login_params = new LinearLayout.LayoutParams(menuContentWidth*5/10,(int) (35*dy));
    	lin_login.setLayoutParams(lin_tut_params);
    	lin_login.setBackgroundColor(Color.parseColor("#cccccc"));
    	centerSlide.addView(lin_login);
    	centerSlide.setGravity(Gravity.CENTER);
    	lin_login.setGravity(Gravity.CENTER);
    	ImageView img_login = new ImageView(context);
    	img_login.setBackgroundResource(R.drawable.slide_lock);
    	LinearLayout.LayoutParams params_img_login = new LinearLayout.LayoutParams((int)(15*dy),(int) (18*dy));
    	params_img_login.setMargins(0, 0, m, 0);
    	img_login.setLayoutParams(params_img_login);
    	login_text = new TextView(context);
    	login_text.setText(((MainActivity)context).getString(R.string.prihlasit));
    	login_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	login_text.setTextColor(Color.parseColor("#393939"));
    	lin_login.addView(img_login);
    	lin_login.addView(login_text);
    
    	//*******koniec ako pouzivat tutorial************************************
    	LinearLayout slide_layout= (LinearLayout) ((Activity) context).findViewById(R.id.slide_layout);
    	LinearLayout.LayoutParams slide_layout_params = (LinearLayout.LayoutParams) slide_layout.getLayoutParams();
    	slide_layout_params.width=menuContentWidth-16*m;
    	slide_layout_params.setMargins(8*m, 0, 8*m, 0);
    	
    	
    	
    	ImageView settings_img= (ImageView) ((Activity) context).findViewById(R.id.settings_img);
    	LinearLayout.LayoutParams settings_params= (LinearLayout.LayoutParams) settings_img.getLayoutParams();	
    	settings_params.width=8*m/3;
    	settings_params.height=8*m/3;
    	settings_params.rightMargin=m;
    	
    	
    	//vypocet sirky obrazkov na zaklade layoutu(320 weight)
    	int notWidth=(menuContentWidth-16*m)*320/1000;
    	int notHeight=notWidth*77/174;
    	magazin=prefs.getBoolean("magazin",true);
    	slide_magazin_img= (ImageView) ((Activity) context).findViewById(R.id.slide_magazin);
    	RelativeLayout.LayoutParams slide_magazin_params= (RelativeLayout.LayoutParams) slide_magazin_img.getLayoutParams();
    	slide_magazin_params.width=notWidth;
    	slide_magazin_params.height=notHeight;
    	if (magazin){slide_magazin_img.setImageResource(R.drawable.settings_switch_1);
    	} else {slide_magazin_img.setImageResource(R.drawable.settings_switch_2);}
    	
    	klub=prefs.getBoolean("klub",true);
    	slide_klub_img= (ImageView) ((Activity) context).findViewById(R.id.slide_klub);
    	RelativeLayout.LayoutParams slide_klub_params= (RelativeLayout.LayoutParams) slide_klub_img.getLayoutParams();
    	slide_klub_params.height=notHeight;
    	if (klub){slide_klub_img.setImageResource(R.drawable.settings_switch_1);
    	} else {slide_klub_img.setImageResource(R.drawable.settings_switch_2);}

        color=((MainActivity) context).articleWhiteBackground;
    	//System.out.println(color);
    	slide_color_img= (ImageView) ((Activity) context).findViewById(R.id.slide_color);
    	RelativeLayout.LayoutParams slide_color_params= (RelativeLayout.LayoutParams) slide_color_img.getLayoutParams();
    	slide_color_params.width=notWidth;
    	slide_color_params.height=notHeight;
    	if (color){slide_color_img.setImageResource(R.drawable.bg_color_switch_2);
    	} else {slide_color_img.setImageResource(R.drawable.bg_color_switch_1);}
    	
    	language=prefs.getBoolean("language",true);
    	slide_language_img= (ImageView) ((Activity) context).findViewById(R.id.slide_language);
    	RelativeLayout.LayoutParams slide_language_params= (RelativeLayout.LayoutParams) slide_language_img.getLayoutParams();
    	slide_language_params.height=notHeight;
    	if (((MainActivity)context).lang.equals("cs")){
    		slide_language_img.setImageResource(R.drawable.language_switch_2);
    	} else {	
    		slide_language_img.setImageResource(R.drawable.language_switch_1);}
    	
    	small= (TextView) ((Activity) context).findViewById(R.id.small);
    	medium= (TextView) ((Activity) context).findViewById(R.id.medium);
    	big= (TextView) ((Activity) context).findViewById(R.id.big);
    	small.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size13));
    	medium.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size17));
    	big.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size21));
    	String font=prefs.getString("font","2");
    //	System.out.println(font);
    	if (font.equals("1")) small.setTextColor(Color.parseColor("#3a3a3a"));
    	if (font.equals("2")) medium.setTextColor(Color.parseColor("#3a3a3a"));
    	if (font.equals("3")) big.setTextColor(Color.parseColor("#3a3a3a"));
    	
    	set =(TextView) ((Activity) context).findViewById(R.id.slide_t_settings);
    	velkost =(TextView) ((Activity) context).findViewById(R.id.slide_t_velkost);
    	magazin_notifikacie =(TextView) ((Activity) context).findViewById(R.id.magazin_notifikacie);
    	magnus_notifikacie =(TextView) ((Activity) context).findViewById(R.id.magnus_notifikacie);
    	farba_pozadia =(TextView) ((Activity) context).findViewById(R.id.farba_pozadia);
    	vyber_jazyka =(TextView) ((Activity) context).findViewById(R.id.vyber_jazyka);
    	Typeface tf3 = ((MainActivity)context).fedraSansAltProBook;
    	Typeface tf4 = ((MainActivity)context).robotoSlabRegular;
    	set.setTypeface(tf3);tutorial_text.setTypeface(tf2);login_text.setTypeface(tf2);
    	velkost.setTypeface(tf4);small.setTypeface(tf4);medium.setTypeface(tf4);big.setTypeface(tf4);
    	magazin_notifikacie.setTypeface(tf3);magnus_notifikacie.setTypeface(tf3);farba_pozadia.setTypeface(tf3);vyber_jazyka.setTypeface(tf3);
    	edit_text.setTypeface(tf2);
    	edit_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size15));
    	set.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size15));
    	velkost.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size11));
    	magazin_notifikacie.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	magnus_notifikacie.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	farba_pozadia.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	vyber_jazyka.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size10));
    	magazin_notifikacie.setText(Html.fromHtml(((MainActivity)context).getString(R.string.magazin_notifikacie)));
		magnus_notifikacie.setText(Html.fromHtml(((MainActivity)context).getString(R.string.magnus_notifikacie)));
		

    	lin_login.setOnClickListener(new View.OnClickListener() // prihlasenie z vysuvacieho menu
        {
           @Override
           public void onClick(View v)
           {
        	   ((MainActivity)context).getmDrawerLayout().closeDrawer(((MainActivity)context).mDrawerLinear);
        	   ((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ((MainActivity)context).mDrawerLinear);
        	   displayLogin(false,0,0);
           }
       }); 
		
    	small.setOnClickListener(new View.OnClickListener()  //velkost pisma male
        {
            @Override
            public void onClick(View v)
            {        	   
            	((MainActivity)context).font_clicked(1);
            	
            }
        });
    	medium.setOnClickListener(new View.OnClickListener() //velkost pisma normalne
        {
            @Override
            public void onClick(View v)
            {        	            
            	((MainActivity)context).font_clicked(2);
            }
        });
    	big.setOnClickListener(new View.OnClickListener() //velkost pisma velke
        {
            @Override
            public void onClick(View v)
            {        	   
            	((MainActivity)context).font_clicked(3);
            }
        });
    	
    	
    	slide_magazin_img.setOnClickListener(new View.OnClickListener() //zatial nic
        {
            @Override
            public void onClick(View v)
            {        	   
            	((MainActivity)context).magazin_clicked(magazin);
            	magazin=!magazin;
            	
            }
        });
    	slide_klub_img.setOnClickListener(new View.OnClickListener() //zatial nic
        {
            @Override
            public void onClick(View v)
            {        	   
            	((MainActivity)context).klub_clicked(klub);
            	klub=!klub;
            	
            }
        });
    	slide_color_img.setOnClickListener(new View.OnClickListener() //farba pozadia v clankoch
        {
            @Override
            public void onClick(View v)
            {    color=!color;    	   
            	((MainActivity)context).color_clicked(color);
            	
            }
        });
    	slide_language_img.setOnClickListener(new View.OnClickListener() //vyber jazyka
        {
            @Override
            public void onClick(View v)
            {       	   
            	((MainActivity)context).language_clicked();
            	
            }
        });
    	lin_tut.setOnClickListener(new View.OnClickListener() //tutorial
        {
            @Override
            public void onClick(View v)
            {        	   
            	((MainActivity)context).createTutorialFragment();
            	
            }
        });
    	
    	

    	
    	
    	//******************************************end create slide settings************************************************************
    
    	
    	rel2.setOnClickListener(new View.OnClickListener() //magnus
        {
           @Override
           public void onClick(View v)
           {
          	((MainActivity)context).displayMagnus();     
           }
       }); 
    	
    	rel1.setOnClickListener(new View.OnClickListener() //svet ocami banky
        {
           @Override
           public void onClick(View v)
           {
           	((MainActivity)context).displayView(1,"",null);

           }
       });
    	rel3.setOnClickListener(new View.OnClickListener()  //informacny
        {
           @Override
           public void onClick(View v)
           {
           	((MainActivity)context).displayView(0,"",null);     
           }
       });
        rel4.setOnClickListener(new View.OnClickListener()  //informacny
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)context).displayView(2,"",null);
            }
        });

    	//ulozene
    	lin5.setOnClickListener(new View.OnClickListener() //citaj neskor
        {
           @Override
           public void onClick(View v)
           {
          	 ((MainActivity)context).displayView(3, "", null);
              // ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
               ////int memoryClass = am.getMemoryClass();
              // int largeMemoryClass = am.getLargeMemoryClass();
        /*Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        System.out.println(memoryClass);
        System.out.println(maxMemory);*/
               //Toast.makeText(context, "Max memory: "+memoryClass+"MB", Toast.LENGTH_LONG).show();

           }
       });
    	
    	lin6.setOnClickListener(new View.OnClickListener() //oblubene
        {
           @Override
           public void onClick(View v)
           {
          	 ((MainActivity)context).displayView(4, "", null);
           }
       });

        lin7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent pdf = new Intent(((MainActivity)context), PdfActivity.class);
                context.startActivity(pdf);
            }
        });
    	
    	  	
    	lin_left.setOnClickListener(new View.OnClickListener() // prihlasenie 
        {
           @Override
           public void onClick(View v)
           {
        	   ((MainActivity)context).getmDrawerLayout().closeDrawer(((MainActivity)context).mDrawerLinear);
        	   ((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ((MainActivity)context).mDrawerLinear);
        	   displayLogin(false,0,0);
           }
       }); 
    	
    		
    	lin_right.setOnClickListener(new View.OnClickListener() //slide panel
        {
           @Override
           public void onClick(View v)
           {
        	   slide_settings.setVisibility(View.VISIBLE);   
        	   slideAnimation();
           }
       }); 
    	lin8.setOnClickListener(new View.OnClickListener() 
        {
           @Override
           public void onClick(View v)
           {
        	   slide_settings.setVisibility(View.VISIBLE);   
        	   slideAnimation();
           }
       });
    	
    	//pri swipe prstom sa zatvori vysuvaci panel
    	slide_settings.setOnTouchListener(new View.OnTouchListener() {
			float downX;
			float downY;
			float upX;
			float upY;
			int MIN_DISTANCE=(int) (screenHeight/20);
    		@Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){              	
                    downX = event.getX();
                    downY = event.getY();
                    return true; // allow other events like Click to be processed
                }
                else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
                     upX = event.getX();
                     upY = event.getY();
                     float deltaX = downX - upX;
                     float deltaY = downY - upY;
                   
                    if (Math.abs(deltaY) > MIN_DISTANCE) { // vertical swipe detection
                        // top or down
                        if (deltaY < 0) {
                        	slideAnimationOff();
                      	   slide_settings.setVisibility(View.GONE);
                            return false;
                        }                      
                    }
                    return false;
                }
                else if(action == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }
		});   	
    	//osetrenie ak sa klikne do vyhladavacieho okienka
    	edit_text.setOnEditorActionListener(new OnEditorActionListener() {
    	    @Override
    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    	        boolean handled = false;
    	        if (actionId == EditorInfo.IME_ACTION_SEND) {
    	        	InputMethodManager imm = (InputMethodManager)((MainActivity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
    	        	imm.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
    	        	String searchText= edit_text.getText().toString();       	
    	           	((MainActivity)context).displaySearchView(searchText);
    	            handled = true;
    	        }
    	        return handled;
    	    }			
    	});
    	edit_text.setOnTouchListener( new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {            	
                if (edit_text.getText().toString().equals(((MainActivity)context).getString(R.string.vyhladat))){
                	edit_text.setText("");
                	edit_text.setTextColor(Color.BLACK);
                }
                return false;
            }
			
        });
    	edit_text.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {               
               // if(!hasFocus && TextUtils.isEmpty(search.getText().toString())){
            	
            	if(!hasFocus){
            		InputMethodManager imm = (InputMethodManager) ((MainActivity)context).getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
            		edit_text.setText(((MainActivity)context).getString(R.string.vyhladat));
            		edit_text.setTextColor(Color.WHITE);
                } else if (hasFocus && edit_text.getText().toString().equals(((MainActivity)context).getString(R.string.vyhladat))){
                	edit_text.setText("");              	                	
                }
            }
        });    
    	
    	   
}
	
	public void updateMenuItems(){
		if (((MainActivity)context).menuModel.jetCount.equals("0")) {
			t1Count.setVisibility(View.INVISIBLE);
		} else {
			t1Count.setText(((MainActivity)context).menuModel.jetCount);
			t1Count.setVisibility(View.VISIBLE);
		}
		if (((MainActivity)context).menuModel.magnusCount.equals("0")) {
			t2Count.setVisibility(View.INVISIBLE);
		} else {
			t2Count.setText(((MainActivity)context).menuModel.magnusCount);
			t2Count.setVisibility(View.VISIBLE);
		}
		if (((MainActivity)context).menuModel.servisCount.equals("0")) {
			t3Count.setVisibility(View.INVISIBLE);
		} else {
			t3Count.setText(((MainActivity)context).menuModel.servisCount);
			t3Count.setVisibility(View.VISIBLE);
		}
        if (((MainActivity)context).menuModel.magnusKlubCount.equals("0")) {
            t4Count.setVisibility(View.INVISIBLE);
        } else {
            t4Count.setText(((MainActivity)context).menuModel.magnusKlubCount);
            t4Count.setVisibility(View.VISIBLE);
        }
		ImageView img = new ImageView(context);
		((MainActivity)context).imageLoader.displayImage(((MainActivity)context).menuModel.jetUrl, img, ((MainActivity)context).options);
		img.setScaleType(ScaleType.CENTER_CROP);
		rel1.addView(img, 0);
		ImageView img2 = new ImageView(context);
		((MainActivity)context).imageLoader.displayImage(((MainActivity)context).menuModel.magnusUrl, img2, ((MainActivity)context).options);
		img2.setScaleType(ScaleType.CENTER_CROP);
		rel2.addView(img2, 0);
		ImageView img3 = new ImageView(context);
		((MainActivity)context).imageLoader.displayImage(((MainActivity)context).menuModel.servisUrl, img3, ((MainActivity)context).options);
		img3.setScaleType(ScaleType.CENTER_CROP);
		rel3.addView(img3, 0);
        ImageView img4 = new ImageView(context);
        ((MainActivity)context).imageLoader.displayImage(((MainActivity)context).menuModel.magnusKlubUrl, img4, ((MainActivity)context).options);
        img4.setScaleType(ScaleType.CENTER_CROP);
        rel4.addView(img4, 0);
		
		updateSettings();
		
	}
	
	//ak je uzivatel prihlaseny tak sa nezobrazuje moznost prihlasit ani vo vysuvacom paneli ani v casti nastavenia(lin8)
	public void updateSettings(){
		lin8.removeAllViews();
		slide_settings.removeAllViews();
		if(((MainActivity)context).logged){
			lin8.addView(imgCenter);
			slide_settings.addView(bottomSlide);
			slide_settings.addView(topSlide);
			slide.height=(int) (slide_height+80*dy);
			topSlidePar.addRule(RelativeLayout.ABOVE, bottomSlide.getId());
			bottomSlide.setGravity(Gravity.CENTER);
			
		} else {
			lin8.addView(lin_left);
			lin8.addView(lin_right);
			slide_settings.addView(bottomSlide);
			slide_settings.addView(centerSlide);
			slide_settings.addView(topSlide);
			slide.height=(int) (slide_height+120*dy);
			topSlidePar.addRule(RelativeLayout.ABOVE, centerSlide.getId());
			bottomSlide.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
			
		}
	}
	
	//vysunutie a zasunutie vysuvacieho panela
	public void slideAnimation() {
		  TranslateAnimation animation = new TranslateAnimation(
				  Animation.RELATIVE_TO_PARENT, 0f,
				  Animation.RELATIVE_TO_PARENT, 0f,
				  Animation.RELATIVE_TO_SELF, 1f,
				  Animation.RELATIVE_TO_SELF, 0);
		  animation.setDuration(300);
		  slide_settings.startAnimation(animation);
		}
	
	public void slideAnimationOff() {
		  TranslateAnimation animation = new TranslateAnimation(
				  Animation.RELATIVE_TO_PARENT, 0f,
				  Animation.RELATIVE_TO_PARENT, 0f,
				  Animation.RELATIVE_TO_SELF, 0f,
				  Animation.RELATIVE_TO_SELF, 1);
		  animation.setDuration(300);
		  slide_settings.startAnimation(animation);
		}
	
	public void set()
	  {
	
		AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,
			    R.animator.set);
			set.setTarget(slide_settings);
			set.setDuration(200);
			set.start();
	  }

public void move()
{

  Animator anim = AnimatorInflater.loadAnimator(context, R.animator.translate_x);
  anim.setTarget(slide_settings);
  anim.setDuration(3000);
  anim.start();
}



public void flipOnVertical()
{

  Animator anim = AnimatorInflater.loadAnimator(context, R.animator.flip_on_vertical);
  anim.setTarget(slide_settings);
  anim.setDuration(3000);
  anim.start();
}

//zobrazenie login screenu
public void displayLogin(final boolean locked,final int current, final int next){
	LayoutInflater inflater = (LayoutInflater)((MainActivity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	loginPopup = (LinearLayout) ((MainActivity)context).findViewById(R.id.loginPopup);
	View v = inflater.inflate(R.layout.login, loginPopup,false);
	if(loginPopup.getChildCount()>0) return;
	loginPopup.setClickable(true);
	loginPopup.addView(v);
	loginPopup.setBackgroundColor(Color.parseColor("#CC6A6A6A"));
	LinearLayout loginWrapper = (LinearLayout) ((MainActivity)context).findViewById(R.id.login);
	int w = (int) (screenWidth*2/5);
	int h = (int) (screenHeight/2);
	loginWrapper.getLayoutParams().width= w;
	loginWrapper.getLayoutParams().height=h ;
	
	LinearLayout login_close = (LinearLayout) ((MainActivity)context).findViewById(R.id.login_close);
	login_close.setPadding(0, 0, h*4/100, 0);
	ImageView img_close = (ImageView) ((MainActivity)context).findViewById(R.id.img_close);
	img_close.getLayoutParams().width=h*7/100;
	img_close.getLayoutParams().height=h*7/100;

	ImageView img = (ImageView) ((MainActivity)context).findViewById(R.id.login_img);
	LinearLayout login_text= (LinearLayout) ((MainActivity)context).findViewById(R.id.login_text);
	LinearLayout login_input= (LinearLayout) ((MainActivity)context).findViewById(R.id.login_input);
	RelativeLayout login_bottom= (RelativeLayout) ((MainActivity)context).findViewById(R.id.login_bottom);
	
	TextView text = (TextView) ((MainActivity)context).findViewById(R.id.text);
	text.setText(Html.fromHtml(((MainActivity)context).getString(R.string.login_text)));
	TextView login_nadpis = (TextView) ((MainActivity)context).findViewById(R.id.login_nadpis);


//	TextView forgot = (TextView) ((MainActivity)context).findViewById(R.id.login_forgot_text);
	edit_login= (EditText)  ((MainActivity)context).findViewById(R.id.login_edit_text);
	
	login_input.getLayoutParams().width=w*2/3;
	login_bottom.getLayoutParams().width=w*2/3;

	ImageView arrow = (ImageView) ((MainActivity)context).findViewById(R.id.login_arrow);
	//ImageView img_forgot = (ImageView) ((MainActivity)context).findViewById(R.id.login_forgot);
	arrow.getLayoutParams().height=(int)(20*dx);
	//img_forgot.getLayoutParams().height=20*dx;
	

	text.setTypeface(((MainActivity) context).fedraSansAltProLight);
	login_nadpis.setTypeface(((MainActivity) context).fedraSansAltProLight);
    edit_login.setTypeface(((MainActivity) context).fedraSansAltProLight);
    edit_login.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size17));
	text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size13));
	login_nadpis.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((MainActivity)context).getResources().getDimension(R.dimen.size22));
	//forgot.setTypeface(tf1);
	
	((MainActivity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	
	login_close.setOnClickListener(new View.OnClickListener() 
    {
        @Override
        public void onClick(View v)
        {     
        	loginPopup.removeAllViews();
        	loginPopup.setBackgroundColor(((MainActivity)context).getResources().getColor(android.R.color.transparent));
        	loginPopup.setClickable(false);
        	 ((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ((MainActivity)context).mDrawerLinear);
        	 //prihlasenie vybehne aj ked uzivatel prejde na lock clanok a nie je prihlaseny, po uzavreti sa najde najblizsi nezamknuty clanok
        	 if(locked){
        		 int newPos =  ((MainActivity)context).getUnlockedPos(current, next);
        		 System.out.println(newPos);
        		 ((MainActivity)context).displayDetail( ((MainActivity)context).currentItems.get(newPos));
        		 }
        }
    });
	
	login_nadpis.setOnClickListener(new View.OnClickListener() 
    {
        @Override
        public void onClick(View v)
        {            	
        	handleLogin();
        }
    });
	
	edit_login.setOnEditorActionListener(new OnEditorActionListener() {
	    @Override
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	        boolean handled = false;
	        if (actionId == EditorInfo.IME_ACTION_SEND) {
	        	InputMethodManager imm = (InputMethodManager)((MainActivity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(edit_login.getWindowToken(), 0);
	            handleLogin();
	            handled = true;
	        }
	        return handled;
	    }		
	});
	edit_login.setOnTouchListener( new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {            	
            if (edit_login.getText().toString().equals(((MainActivity)context).getString(R.string.login_edit))){
            	edit_login.setText("");
            }
            return false;
        }			
    });
	edit_login.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {                          	
        	if(!hasFocus){
        		InputMethodManager imm = (InputMethodManager) ((MainActivity)context).getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_login.getWindowToken(), 0);
        		//edit_login.setText(((MainActivity)context).getString(R.string.login_edit));
            } else if (hasFocus && edit_login.getText().toString().equals(((MainActivity)context).getString(R.string.login_edit))){
            	edit_login.setText("");            	
            }
        }
    }); 
}

	

	public void handleLogin(){
		((MainActivity)context).spinner.setVisibility(View.VISIBLE);
		loginPopup.removeAllViews();
     	loginPopup.setBackgroundColor(((MainActivity)context).getResources().getColor(android.R.color.transparent));
     	loginPopup.setClickable(false);
     	 ((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ((MainActivity)context).mDrawerLinear);
     	ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
        String response="";
     	 try {
			String url = "http://194.50.215.137/api/auth/login_request";
			//String forHash = Functions.sha1Hash(url+((MainActivity)context).authHash);
			JSONObject json;
			JSONObject json2;
			if (!((MainActivity)(context)).isNetworkAvailable()){
				((MainActivity)context).spinner.setVisibility(View.INVISIBLE);
			((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error8));
			

			} else {
			response = CustomHttpClient.executeHttpGet(url,((MainActivity)context).authHash);
			json = new JSONObject(response);
			//System.out.println(json);
	   		//JSONArray jsonIds = json.getJSONArray("result");
			String url2 = "http://194.50.215.137/api/auth/login";
	   		 		
	   		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>(); 
	   		String heslo =edit_login.getText().toString();
	   	//	System.out.println(heslo);
	   		postParameters.add(new BasicNameValuePair("psw",Functions.sha1Hash(heslo+Functions.sha1Hash(heslo))));  
	   	//	postParameters.add(new BasicNameValuePair("psw",heslo));
			
			response = CustomHttpClient.executeHttpPost(url2,((MainActivity)context).authHash,postParameters);
			json2 = new JSONObject(response);
		//	System.out.println(json2);
				if (json2.has("result")) {
					String newAuth = json2.getString("result");
					System.out.println("mam hodbnotu");
					((MainActivity)context).authHash=newAuth;
					((MainActivity)context).logged=true;
					((MainActivity)context).updateView();
				} else {
					((MainActivity)context).spinner.setVisibility(View.INVISIBLE);
			((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error15));
				}
			}
	   	} catch (Exception e) {
	   		System.out.println("from login: "+e);
	   //	((MainActivity)context).errorMessage("Pripojenie so serverom", "Nepodarilo sa nastavi� s�visiace �l�nky");
	   	}
	}
	
	//poziva sa pri zmene jazyka
	public void updateWholeMenu() {
		t1.setText(Html.fromHtml(((MainActivity)context).getString(R.string.svet_ocami)));
		t2.setText(Html.fromHtml(((MainActivity)context).getString(R.string.magazin_magnus)));
		t3.setText(Html.fromHtml(((MainActivity)context).getString(R.string.informacny_servis)));
		t4.setText(Html.fromHtml(((MainActivity)context).getString(R.string.klub_magnus)));
		t5.setText(((MainActivity)context).getString(R.string.citat_neskor));
		t6.setText(((MainActivity)context).getString(R.string.oblubene));
		set.setText(Html.fromHtml(((MainActivity)context).getString(R.string.nastavenia)));
		velkost.setText(Html.fromHtml(((MainActivity)context).getString(R.string.velkost_pisma)));
		magazin_notifikacie.setText(Html.fromHtml(((MainActivity)context).getString(R.string.magazin_notifikacie)));
		magnus_notifikacie.setText(Html.fromHtml(((MainActivity)context).getString(R.string.magnus_notifikacie)));
		farba_pozadia.setText(Html.fromHtml(((MainActivity)context).getString(R.string.farba_pozadia)));
		vyber_jazyka.setText(Html.fromHtml(((MainActivity)context).getString(R.string.vyber_jazyka)));
		tutorial_text.setText(Html.fromHtml(((MainActivity)context).getString(R.string.help)));
		login_text.setText(Html.fromHtml(((MainActivity)context).getString(R.string.login)));
		small.setText(Html.fromHtml(((MainActivity)context).getString(R.string.small)));
		medium.setText(Html.fromHtml(((MainActivity)context).getString(R.string.normal)));
		big.setText(Html.fromHtml(((MainActivity)context).getString(R.string.big)));
		edit_text.setText(Html.fromHtml(((MainActivity)context).getString(R.string.vyhladat)));
		
	}
	
	public void showLogin(){
		
	}

}

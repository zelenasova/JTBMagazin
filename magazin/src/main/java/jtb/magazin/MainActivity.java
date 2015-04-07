package jtb.magazin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import jtb.magazin.UI.ArticleThumbnailFrame;
import jtb.magazin.UI.CustomDrawerLayout;
import jtb.magazin.UI.MyMenuLayout;
import jtb.magazin.adapter.JetPagerAdapter;
import jtb.magazin.adapter.TwitterAdapter;
import jtb.magazin.asyncTasks.LoadArticleOnBackground;
import jtb.magazin.asyncTasks.LoadSearchStore;
import jtb.magazin.asyncTasks.LoadStores;
import jtb.magazin.asyncTasks.LoadSystem;
import jtb.magazin.asyncTasks.LoadTutorial;
import jtb.magazin.magnus.MagnusArticleFrame;
import jtb.magazin.magnus.MagnusListFrame;
import jtb.magazin.model.ArticleModel;
import jtb.magazin.model.MagnusListModel;
import jtb.magazin.model.MenuModel;
import jtb.magazin.store.ArticleStore;
import jtb.magazin.store.MagnusListStore;
import jtb.magazin.store.MarketStore;
import jtb.magazin.store.SearchArticleStore;
import jtb.magazin.store.TwitterStore;

//hlavna trieda, uchovava vsetky potrebne premenne a fuknkcie ktore su spristupnene pre vsetky triedy
//uchovavaju sa aktualne stavy aby pri refreshi alebo pri prechode aplikacie do pozadia sa vedelo kde sa appka nachadza(v akej polozke menu...)
public class MainActivity extends FragmentActivity {
    public boolean articleWhiteBackground = true;

    private CustomDrawerLayout mDrawerLayout; // vysuvacie menu
    public boolean logged = false; // ci je uzivatel aktualne prihlaseny
    public List<String> jetListId; // zoznam id clankov svet ocami banky
    public List<String> servisListId; // zoznam id clankov informacny servis
    public List<String> magnusArticleIds; // zoznam id clankov magazinu
    public List<ArticleModel> magnusArticleItems;
    public ArticleModel magnusJetArticle;
    public String authHash;
    public ArticleStore storeJet; // store clankov svet ocami banky, v nom sa
    // nachadza zoznam objektov jednotlivych
    // clankov(jeden clanok - ArticleModel)
    public ArticleStore storeServis;// to iste ale pre informacny servis
    public ArticleStore storeKlub;// to iste ale pre magnus klub
    public MagnusListStore magnusList; // store pre magnus
    public MarketStore marketStore; // store pre MarketFrame - prve okienko v
    // informacnom servise
    public TwitterStore twitterStore; // store pre twitter
    public FrameLayout marketFrame = null; // ak sa raz vygeneruje marketFrame
    // tak sa ulozi sem aby sa nemuselo
    // znova generovat
    public ArrayList<ArticleModel> currentItems; // aktualny zoznam clankov,
    // zavisi kde sa v aplikacii
    // nachadzame
    public ArrayList<ArticleModel> gridCurrentItems; // aktualny zoznam danej mriezky(napr. ked sme v related, tak current items = related
    // ale po stlaceni back musime nastavit currentItems na tieto polozky)
    public LinearLayout mDrawerLinear; // lave menu
    public LinearLayout mDrawerRight; // prave menu
    DetailFragment detail; // vrstva detailu, ktora sa pridava navrch
    public Fragment obsah;
    public ArticleThumbnailFrame openFrame; // v hlavnom gride okineko clanku
    // ktore je otvorene(vysunuty perex)
    public ArticleThumbnailFrame openRoller;
    public MagnusArticleFrame openMagnusFrame; // v magnuse okienko clanku ktore
    // je otvorene(aktivovalo sa
    // klikom na polozku)
    public MagnusListFrame openMagnusListFrame; // v magnuse okienko magazinu
    // ktore je otvorene(aktivovalo
    // sa klikom na polozku)
    public MagnusArticleFrame openMagnusRoller;
    private LinearLayout error_button; // button zavriet pri chybovych hlaskach
    private LinearLayout errorPopup; // vrstva chybovych hlasok
    public MyMenuLayout menuLayout; // objekt triedy MyMenuLayout, kde sa
    // optimalizuje lave menu
    public HomeFragment homeFragment = null; // zakladny fragment
    public Fragment fragment = null;
    public ImageLoader imageLoader = ImageLoader.getInstance(); // nacitava
    // obrazky
    public DisplayImageOptions options; // nastavenia imageLoadera
    public float  screenWidth; // sirka obrazovky
    public float screenHeight; // vyska bez status baru
    public boolean isGallery = false; // indikuje ci v detaile clanku je
    // spustena galeria
    public MenuModel menuModel; // v lavom menu su 4 polozky hlavne. Uchovava
    // hodnoty tychto poloziek
    public ProgressBar dialog; // ciara ktora ukazuje priebeh pri nacitavani,
    // stahovani...
    public LinearLayout dialog_lin; // hlavny loader
    LinearLayout settings;
    TextView related_text; // text v pravom menu odporucane clanky
    private SimpleDrawerListener mDrawerToggle; // listener pre vytahovanie menu
    ImageView logo;
    ImageView logo_background;
    public LinearLayout spinner;
    TwitterAdapter twitterAdapter = null;
    public int menuWidth; // sirka laveho menu
    LinearLayout right_frames;
    LinearLayout right_frames_magnus;
    public SharedPreferences prefs; // zapisuje data do pamati
    public boolean changeFont = false;
    RelativeLayout logo_wrapper;
    LinearLayout language; // vyber jazyka ak systemovy jazyk nie je slovensky
    // ani cesky
    FragmentManager fragmentManager = getSupportFragmentManager();
    public String lang = "sk";
    public Gson gson = new Gson(); // pomaha pri konvertovani Jsona na retazec
    public HashMap<String, String> urlsMap; // pouziva sa pri imageLoaderi
    // kontrolovat ci cachovane obrazky
    // nie su zastarale
    public Editor prefsEditor;
    String savedLang = "";
    MyTimerTask myTask;
    Timer myTimer; // casovac
    Handler handler;
    public boolean visited = false;
    // *************************************stavove
    // premenne***********************************************
    int display = -1;
    private boolean changedLanguage = false;
    public int visibleType = 1; // 1-jet, 2-servis, 3-klub magnus
    boolean isFirstTimeWithoutInternet = false;
    boolean isLaunching = true; // prvy krat sa spusta
    public int actualView = 0; // 1-magnus magazins, 2-magnus articles
    // 3-readLater 4-favorites 5-search 6-keywords
    public String actualSearchText = "";
    String actualMagazinID = "";
    String actualMagazinLastChange = "";
    String actualMagazinTitle = "";
    String actualMagazinSubTitle = "";
    public int actualPos = 0;
    public boolean fromResume = false; // ci sa aplikacia prebrala z pozadia
    public boolean currentError = false;
    public boolean updateFavorites = false;
    public boolean completLoadStore = false; // ak sa nacitaju vsetky stores, tak sa
    // nastavi na true
    ConnectivityManager connectivityManager;
    AsyncTask<String, String, String> system_task;
    AsyncTask<String, String, String> tutorial_task;
    AsyncTask<String, String, String> store_task;
    AsyncTask<String, String, String> article_task;
    public RelativeLayout world;
    public RelativeLayout world_wrapper;
    public Typeface openSansRegular;
    public Typeface fedraSansAltProBold;
    public Typeface openSansaLight;
    public Typeface giorgioSansWebBold;
    public Typeface giorgioSansWebRegular;
    public Typeface robotoSlabRegular;
    public Typeface robotoSlabLight;
    public Typeface openSansCondLight;
    public Typeface fedraSansAltProLight;
    public Typeface fedraSansAltProBook;
    public Typeface fedraSansAltProMedium;

    @Override
    public void onBackPressed() {
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        if (detail != null) {
            if (world_wrapper.getVisibility() == View.VISIBLE) {
                removeWOT();
                return;
            }
            displayView(9, "", null);
            return;
        }
        if (obsah != null) {
            if (fragment instanceof MagnusFragment) {
                ((MagnusFragment) fragment).removeMagazinObsah();
                return;
            }
        }

        if (fragment != null) {
            removeFragment();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (system_task != null) {
            if (!system_task.isCancelled()) {
                System.out.println("idem zatvorit task");
                system_task.cancel(true);
            }
        }
        if (tutorial_task != null) {
            if (!tutorial_task.isCancelled()) {
                System.out.println("idem zatvorit tutorial");
                tutorial_task.cancel(true);
            }
        }
        if (store_task != null) {
            if (!store_task.isCancelled()) {
                System.out.println("idem zatvorit storeLoad");
                store_task.cancel(true);
            }
        }
        if (article_task != null) {
            if (!article_task.isCancelled()) {
                System.out.println("idem zatvorit articleLoad");
                article_task.cancel(true);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        marketFrame = null;
        if (!isLaunching) {
            // ked prejde z pozadia appka do popredia
            myResume("");
        }
        isLaunching = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        //System.out.println("onPause");
        if (!completLoadStore) {
            //System.out.println("!completLoadStore");
            completLoadStore = true;
            finish();
        }
        //myTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("som v onCreate");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefsEditor = prefs.edit();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels - getStatusBarHeight();
        logo = (ImageView) findViewById(R.id.launch_logo);
        logo_background = (ImageView) findViewById(R.id.logo_background);
        logo_wrapper = (RelativeLayout) findViewById(R.id.logo_wrapper);
        world = (RelativeLayout) findViewById(R.id.world_of_tags);
        world_wrapper = (RelativeLayout) findViewById(R.id.world_of_tags_wrapper);
        findViewById(R.id.rooot).setBackgroundColor(Color.WHITE);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // kontroluje ci existuje potrebne priecinky v pamati
        File magnusDir = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE);
        if (!magnusDir.isDirectory())
            magnusDir.mkdirs();
        File articlesDir = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles");
        if (!articlesDir.isDirectory())
            articlesDir.mkdirs();
        File cacheDir = new File(getExternalCacheDir().toString() + "/cache/articles");
        if (!cacheDir.isDirectory())
            cacheDir.mkdirs();
        // sirka a vyska obrazovky

        logo.getLayoutParams().width = (int) (screenWidth / 5);
        fadeIN();
        // pre zapis do internej pamati


        // ************************************************************lang********************************************************************
        // String currentLang = Locale.getDefault().getLanguage();
        String savedLang = prefs.getString("savedLang", "");
        lang = savedLang;
        Locale.setDefault(new Locale(savedLang));
        Configuration config = new android.content.res.Configuration();
        config.locale = new Locale(savedLang);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        /*
		 * if (savedLang!=null) { lang=savedLang; } else lang=currentLang;
		 */
        // System.out.println(lang);
        // ************************************************************endlang********************************************************************
        // inicializacia imageLoadera
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(10 * 1024 * 1024).build());
        options = new DisplayImageOptions.Builder()
                // .showImageForEmptyUri(R.drawable.ic_empty)//.showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /********************************************************* menu ********************************************************/
        menuWidth = (int) (screenWidth / Constants.MENU_WIDTH_KOEF);
        menuModel = new MenuModel();
        menuLayout = new MyMenuLayout(MainActivity.this, settings, menuWidth);
        // *********************************************** end menu
        // ********************************************************/
        // loaders
        spinner = (LinearLayout) findViewById(R.id.main_spinner);
        dialog_lin = (LinearLayout) findViewById(R.id.main_progress_dialog);
        dialog = (ProgressBar) findViewById(R.id.progressDialog);
        articleWhiteBackground = prefs.getBoolean("bg_white", true);
        if (!articleWhiteBackground) {
            dialog_lin.setBackgroundColor(Color.BLACK);
            dialog.setVisibility(View.GONE);
            dialog = (ProgressBar) findViewById(R.id.progressDialogBlack);
            dialog.setVisibility(View.VISIBLE);
        }


        openSansRegular = Typeface.createFromAsset(getAssets(), "Fonts/OpenSans-Regular.ttf");
        fedraSansAltProBold = Typeface.createFromAsset(getAssets(), "Fonts/FedraSansAltPro-Bold.ttf");
        openSansaLight = Typeface.createFromAsset(getAssets(), "Fonts/OpenSans-Light.ttf");
        giorgioSansWebBold = Typeface.createFromAsset(getAssets(), "Fonts/GiorgioSansWeb-Bold.ttf");
        giorgioSansWebRegular = Typeface.createFromAsset(getAssets(), "Fonts/GiorgioSansWeb-Regular.ttf");
        robotoSlabRegular = Typeface.createFromAsset(getAssets(), "Fonts/RobotoSlab-Regular.ttf");
        openSansCondLight = Typeface.createFromAsset(getAssets(), "Fonts/OpenSans-CondLight.ttf");
        fedraSansAltProLight = Typeface.createFromAsset(getAssets(), "Fonts/FedraSansAltPro-Light.otf");
        fedraSansAltProBook = Typeface.createFromAsset(getAssets(), "Fonts/FedraSansAltPro-Book.otf");
        fedraSansAltProMedium = Typeface.createFromAsset(getAssets(), "Fonts/FedraSansAltPro-Medium.otf");
        robotoSlabLight = Typeface.createFromAsset(getAssets(), "Fonts/RobotoSlab-Light.ttf");

        if (isNetworkAvailable()) {
            store_task = new LoadStores(this).execute(new String[]{"", "", ""});
        } else {
            // ak nemame pripojenie pozerame do cache pre nacitanie jednotlivych
            // storov
            String json = prefs.getString("savedStoreJet", null);
            if (json != null) {
                updateAuth();
                updateAppSettings();
                updateLatest();
                storeJet = gson.fromJson(json, ArticleStore.class);
                String json2 = prefs.getString("savedStoreServis", "");
                storeServis = gson.fromJson(json2, ArticleStore.class);
                String json4 = prefs.getString("savedStoreKlub", "");
                storeKlub = gson.fromJson(json4, ArticleStore.class);
                String json3 = prefs.getString("savedTwitterStore", "");
                magnusList = new MagnusListStore(this);
                twitterStore = gson.fromJson(json3, TwitterStore.class);
                marketStore = new MarketStore(MainActivity.this);
                fadeOUT();
            } else {
                fadeOUT();
                errorMessage(getString(R.string.error3),
                        getString(R.string.error6));
                setErrorOnClickFinish();
                isFirstTimeWithoutInternet = true;
            }
            // aktualizuje sa lave menu
            menuLayout.updateMenuItems();
        }
        urlsMap = getImagesLastChange();
		/*
		 * Iterator iterator = urlsMap.keySet().iterator();
		 * 
		 * while (iterator.hasNext()) { String key = iterator.next().toString();
		 * String value = urlsMap.get(key).toString();
		 * System.out.println(key+":"+value); }
		 */

        mDrawerLayout = (CustomDrawerLayout) findViewById(R.id.drawer_layout);
        // mDrawerLayout.setScrimColor(Color.parseColor("#90FFFFFF"));
        // mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerLinear.getLayoutParams().width = menuWidth;
        mDrawerRight = (LinearLayout) findViewById(R.id.right_drawer_wrapper);
        mDrawerRight.getLayoutParams().width = (int) (screenWidth / 2);
        mDrawerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mDrawerToggle = new SimpleDrawerListener() {
            // pri vybere z menu, sa najprv zatvori lave menu a potom na zaklade
            // premennej display sa zobrazuje polozka menu
            public void onDrawerClosed(View view) {

                if (view.equals(mDrawerLinear))
                    menuLayout.slide_settings.setVisibility(View.GONE);
                // if(view.equals(mDrawerLinear))
                // mDrawerLayout.setScrimColor(Color.parseColor("#50FFFFFF"));
                if (display == 0) {
                    updateHomeFragment(true, "servis");
                    currentItems = storeServis.getItems();
                    gridCurrentItems = storeServis.getItems();
                    // detail = null;
                    display = -1;
                }
                if (display == 1) {
                    updateHomeFragment(true, "jet");
                    currentItems = storeJet.getItems();
                    gridCurrentItems = storeJet.getItems();
                    // detail = null;
                    display = -1;
                }
                if (display == 2) {
                    new LoadSearchStore(MainActivity.this).execute();
                    // detail = null;
                    display = -1;
                }
                if (display == 3) {
                    createReadFragment();
                    // detail = null;
                    display = -1;
                }
                if (display == 4) {
                    createFavoritesFragment();
                    // detail = null;
                    display = -1;
                }
                if (display == 5) {
                    createMagnusFragment();
                    // detail = null;
                    display = -1;
                }
                if (display == 6) {
                    updateHomeFragment(true, "klubMagnus");
                    currentItems = storeKlub.getItems();
                    gridCurrentItems = storeKlub.getItems();
                    display = -1;
                }
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                // if(drawerView.equals(mDrawerLinear))
                // mDrawerLayout.setScrimColor(Color.parseColor("#70FFFFFF"));mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        visited = prefs.getBoolean("visited", false);
        related_text = (TextView) findViewById(R.id.related_text);

        related_text.setTypeface(robotoSlabLight);
        related_text.setText(getString(R.string.suvisiace_clanky));
        RelativeLayout.LayoutParams related_text_params = (android.widget.RelativeLayout.LayoutParams) related_text.getLayoutParams();
        related_text_params.setMargins((int) (screenWidth / 35), 0, 0, 0);
        right_frames = (LinearLayout) findViewById(R.id.right_frames);
        right_frames_magnus = (LinearLayout) findViewById(R.id.right_frames_magnus);

        // vytvara sa zakladny homeFragment a aktualny zoznam clankov je
        // storeJet(svet ocami banky)
        if ((savedInstanceState == null) && !isNetworkAvailable()) {
            if (!isFirstTimeWithoutInternet) {
                createHomeFragment(storeJet, false);
                currentItems = storeJet.getItems();
            }
        }

        // zapne sa casovac a po 5 minutach sa aktualizuje obsah(znovu sa
        // kontaktuje server)
        //myTask = new MyTimerTask();
        //myTimer = new Timer();
        //handler = new Handler();
        //myTimer.schedule(myTask, 300000, 300000);

        if (!isFirstTimeWithoutInternet && !isNetworkAvailable()) {
            spinner.setVisibility(View.INVISIBLE);
        }


    }

    // pre vyhladavanie
    public void displaySearchView(String searchText) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
        spinner.setVisibility(View.VISIBLE);
        actualSearchText = searchText;
        fragment = null;
        // detail = null;
        display = 2;
        mDrawerLayout.closeDrawer(mDrawerLinear);
    }

    // zobrazi sa vysledky pre keywords z krystalu suvislosti
    public void displayKeywords(String keyword) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
        actualView = 6;
        // detail = null;
        actualSearchText = keyword;
        spinner.setVisibility(View.VISIBLE);
        fragment = new SearchFragment(this, keyword, true);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    // zobrazi magnus
    public void displayMagnus() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
        spinner.setVisibility(View.VISIBLE);
        // detail = null;
        display = 5;
        mDrawerLayout.closeDrawer(mDrawerLinear);
    }

    // zobrazi detail clanku
    public void displayDetail(ArticleModel model) {
        if (detail != null) {
            //detail.myWebView.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.animator.translate_x)
                    .remove(detail).commit();
            detail = null;
        }
        spinner.setVisibility(View.VISIBLE);
        detail = new DetailFragment(this, model, spinner);
        fragmentManager.beginTransaction().setCustomAnimations(0, R.animator.translate_x).add(R.id.frame_container, detail).commit();
        mDrawerLayout.closeDrawer(mDrawerLinear);
        mDrawerLayout.closeDrawer(mDrawerRight);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                mDrawerRight);
    }

    public void removeFragment() {
        fragmentManager.beginTransaction().setCustomAnimations(0, R.animator.translate_x).remove(fragment).commit();
        fragment = null;
    }

    public void displayView(int position, String id, int[] related) {

        switch (position) {
            case 0: // informacny servis
                visibleType = 2;
                spinner.setVisibility(View.VISIBLE);
                currentItems = storeServis.getItems();
                gridCurrentItems = storeJet.getItems();
                display = 0;
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
                mDrawerLayout.closeDrawer(mDrawerLinear);
                break;

            case 1: // svet ocami banky
                visibleType = 1;
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
                spinner.setVisibility(View.VISIBLE);
                currentItems = storeJet.getItems();
                gridCurrentItems = storeServis.getItems();
                display = 1;
                mDrawerLayout.closeDrawer(mDrawerLinear);
                break;

            case 2: // klub magnus
                visibleType = 3;
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
                spinner.setVisibility(View.VISIBLE);
                currentItems = storeJet.getItems();
                gridCurrentItems = storeServis.getItems();
                display = 6;
                mDrawerLayout.closeDrawer(mDrawerLinear);
                break;

            case 3: // read later
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
                spinner.setVisibility(View.VISIBLE);
                fragment = null;
                // detail = null;
                display = 3;
                mDrawerLayout.closeDrawer(mDrawerLinear);
                break;

            case 4: // favourites
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
                spinner.setVisibility(View.VISIBLE);
                fragment = null;
                // detail = null;
                display = 4;
                mDrawerLayout.closeDrawer(mDrawerLinear);
                break;

            case 9:// odstranenie detailu
                if (detail==null) return;
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(0, R.animator.translate_x);
                transaction.remove(detail).commit();
                detail = null;
                currentItems = gridCurrentItems;
                if (updateFavorites) {
                    spinner.setVisibility(View.VISIBLE);
                    createFavoritesFragment();
                    updateFavorites = false;
                }
                break;
            default:
                break;
        }
    }

    // zistuje ci sa nachadza status bar
    private boolean hasOnScreenSystemBar() {
        Display display = getWindowManager().getDefaultDisplay();
        int rawDisplayHeight = 0;
        try {
            Method getRawHeight = Display.class.getMethod("getRawHeight");
            rawDisplayHeight = (Integer) getRawHeight.invoke(display);
        } catch (Exception ex) {
            //System.out.println(ex);
            //System.out.println(rawDisplayHeight);
        }
        int UIRequestedHeight = display.getHeight();
        return rawDisplayHeight - UIRequestedHeight > 0;
    }

    // zistuje vysku status baru
    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        if (!hasOnScreenSystemBar()) {
            int resourceId = getResources().getIdentifier("status_bar_height",
                    "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(
                        resourceId);
            }
        }

        return statusBarHeight;
    }

    // vytvaranie jednotlivych fragmentov
    public void createHomeFragment(ArticleStore store, boolean isServis) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
        homeFragment = new HomeFragment(store, twitterStore, this, isServis);
        fragmentManager.beginTransaction().setCustomAnimations(0, R.animator.translate_x).replace(R.id.home_container, homeFragment).commit();
    }

    public void updateHomeFragment(boolean fromMenu, String zdroj) {

        if ((detail != null) && (fromMenu)) {
            actualView = 0;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.animator.translate_x)
                    .remove(detail).commit();
            detail = null;
        }
        if ((fragment != null) && (fromMenu)) {
            actualView = 0;
            fragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.animator.translate_x)
                    .remove(fragment).commit();
            fragment = null;
        }

        if (zdroj.equals("servis")) {
            //System.out.println(homeFragment.getServisAdapter());
            homeFragment.twitter_wrapper.setVisibility(View.GONE);
            if (homeFragment.getServisAdapter() == null) {
                homeFragment.setServisAdaper(storeServis);
                homeFragment.pager.setAdapter(homeFragment.getServisAdapter());
            } else {
                homeFragment.getServisAdapter().setData(storeServis);
                homeFragment.getServisAdapter().notifyDataSetChanged();
                homeFragment.pager.setAdapter(homeFragment.getServisAdapter());

            }

        } else if (zdroj.equals("klubMagnus")) {
            homeFragment.twitter_wrapper.setVisibility(View.GONE);
            if (homeFragment.getMagnusKlubPagerAdapter() == null) {
                homeFragment.setMagnusKlubAdaper(storeKlub);
                homeFragment.pager.setAdapter(homeFragment.getMagnusKlubPagerAdapter());
            } else {
                homeFragment.getMagnusKlubPagerAdapter().setData(storeKlub);
                homeFragment.getMagnusKlubPagerAdapter().notifyDataSetChanged();
                homeFragment.pager.setAdapter(homeFragment.getMagnusKlubPagerAdapter());

            }
        } else {
            homeFragment.twitter_wrapper.setVisibility(View.VISIBLE);
            JetPagerAdapter adapter = homeFragment.getJetAdapter();
            //System.out.println(adapter);
            homeFragment.pager.setAdapter(adapter);
            //ak sa prebudila zo spanku tak sa musi nastavit store
            if (!fromMenu) {
                adapter.setData(storeJet);
                adapter.notifyDataSetChanged();
            }
            //adapter.setData(store);
            //adapter.notifyDataSetChanged();
        }

        if (fromMenu) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    homeFragment.pager.setCurrentItem(0); // Where "2" is the
                    // position you want
                    // to go
                }
            });
        }
        spinner.setVisibility(View.INVISIBLE);
    }

    public void createMagnusFragment() {
        actualView = 1;
        fragment = new MagnusFragment(prefs, this);
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

    }

    public void createMagnusFragment(MagnusListModel model) {
        actualView = 1;
        fragment = new MagnusFragment(prefs, this, model, true);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    public void createReadFragment() {
        actualView = 3;
        List<String> list = getReadLater();
        for (String item:list){
            System.out.println(item.toString());
        }
        System.out.println(list.size());
        fragment = new ReadLaterFragment(this, list);
        fragmentManager.beginTransaction().setCustomAnimations(0, R.animator.translate_x).replace(R.id.frame_container, fragment).commit();
    }

    public void createSearchFragment(SearchArticleStore store) {
        actualView = 5;

        fragment = new SearchFragment(this, store);
        fragmentManager.beginTransaction()
                .setCustomAnimations(0, R.animator.translate_x)
                .replace(R.id.frame_container, fragment).commit();
    }

    public void createFavoritesFragment() {
        actualView = 4;
        List<String> list_fav = getFavourites();
        fragment = new FavouritesFragment(this, list_fav);
        fragmentManager.beginTransaction()
                .setCustomAnimations(0, R.animator.translate_x)
                .replace(R.id.frame_container, fragment).commit();
    }

    public void createTutorialFragment() {
        mDrawerLayout.closeDrawer(mDrawerLinear);
        fragment = new TutorialFragment(this);
        fragmentManager.beginTransaction().add(R.id.frame_container, fragment)
                .commit();
    }

    // zistuje ci sa nachadza clanok v ozname
    public int isIdinList(List<String> list, String id, String source) {
        int i = 0;
        for (String list2 : list) {
            if (list2.equals(source + "-" + id))
                return i;
            i++;
        }
        return -1;
    }

    // uklada zoznam oblubenych do internej pamati
    public void saveFavouritesList(List<String> list) {
        SharedPreferences.Editor sEdit = prefs.edit();
        for (int i = 0; i < list.size(); i++) {
            sEdit.putString("valFavourites" + i, list.get(i));
        }
        sEdit.putInt("sizeFavourites", list.size());
        sEdit.commit();
    }

    // uklada zoznam ulozenych clankov do internej pamati
    public void saveReadLaterList(List<String> list) {
        SharedPreferences.Editor sEdit = prefs.edit();
        for (int i = 0; i < list.size(); i++) {
            sEdit.putString("valReadLater" + i, list.get(i));
            // sEdit.putString("valReadLater"+i,"");

        }
        sEdit.putInt("sizeReadLater", list.size());
        // sEdit.putInt("sizeReadLater",0);
        sEdit.commit();
    }

    // ziska z internej pameti zoznam oblubenych
    public List<String> getFavourites() {
        ArrayList<String> list = new ArrayList<String>();
        int size = prefs.getInt("sizeFavourites", 0);

        for (int j = 0; j < size; j++) {
            list.add(prefs.getString("valFavourites" + j, ""));
        }
		/*
		 * for (String string : list) { System.out.println(string); }
		 */
        return list;

    }

    // ziska z internej pameti zoznam ulozenych clankov
    public List<String> getReadLater() {
        ArrayList<String> list = new ArrayList<String>();
        int size = prefs.getInt("sizeReadLater", 0);
        for (int j = 0; j < size; j++) {
            list.add(prefs.getString("valReadLater" + j, ""));
        }
        return list;
    }

    // z pamati ziska hashMap obrazkov (obrazokId + lastChange)
    public HashMap<String, String> getImagesLastChange() {

        HashMap<String, String> urlsMap;
        String string = prefs.getString("urlsMap", "");
        // System.out.println(string);
        // System.out.println("halloooo");
        if (string.equals("")) {
            return new HashMap<String, String>();
        } else {
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            urlsMap = gson.fromJson(string, type);
            return urlsMap;
        }
    }

    public void setImagesLastChange(HashMap<String, String> urlsMap) {
        SharedPreferences.Editor sEdit = prefs.edit();
        String jsonItems = gson.toJson(urlsMap);
        sEdit.putString("urlsMap", jsonItems);
        sEdit.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        // mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public ArticleStore getStoreJet() {
        return storeJet;
    }

    // nasledujuce funkcie robia obsluhu nastaveni vo vysuvacom settings paneli
    // v lavom menu
    public void font_clicked(int font) {
        // ak je detail clanku spusteny tak hned sa aktualizuje, inak len sa
        // upravi subor settingd.js pomocou triedy EditSettingsFile
        if (detail != null) {
            detail.getMyWebView().loadUrl(
                    "javascript:System.set_font_size(" + font + ")");
            detail.getMyWebView().clearCache(false);
        } else {
            prefs.edit().putString("changedFont", "changed").commit();
        }
        new EditSettingsFile(2, font + "", this);
        prefs.edit().putString("font", font + "").commit();
        // meni sa farba
        switch (font) {
            case 1:
                menuLayout.small.setTextColor(Color.parseColor("#3a3a3a"));
                menuLayout.medium.setTextColor(Color.parseColor("#9c9c9c"));
                menuLayout.big.setTextColor(Color.parseColor("#9c9c9c"));
                break;
            case 2:
                menuLayout.small.setTextColor(Color.parseColor("#9c9c9c"));
                menuLayout.medium.setTextColor(Color.parseColor("#3a3a3a"));
                menuLayout.big.setTextColor(Color.parseColor("#9c9c9c"));
                break;
            case 3:
                menuLayout.small.setTextColor(Color.parseColor("#9c9c9c"));
                menuLayout.medium.setTextColor(Color.parseColor("#9c9c9c"));
                menuLayout.big.setTextColor(Color.parseColor("#3a3a3a"));
                break;
        }
    }

    // to iste ako pri fonte
    public void magazin_clicked(boolean magazin) {

        prefs.edit().putBoolean("magazin", !magazin).commit();
        if (!magazin) {
            menuLayout.slide_magazin_img.setImageResource(R.drawable.settings_switch_1);
        } else {
            menuLayout.slide_magazin_img.setImageResource(R.drawable.settings_switch_2);
        }

        send_magazin_notifikations(!magazin);
    }

    public void send_magazin_notifikations(boolean magazin) {

        try {
            String str = "";
            if (magazin) {
                str = "1";
            } else {
                str = "0";
            }
            String response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/user/set_notifications/" + str + "/0", authHash);
            JSONObject json = new JSONObject(response);
            //System.out.println(json);
            //System.out.println(str);

        } catch (Exception e) {
            //error.setText(R.string.connection);
            System.out.println("from magazin notifikacie" + e);
        }
    }

    // to iste ako pri fonte
    public void klub_clicked(boolean klub) {

        prefs.edit().putBoolean("klub", !klub).commit();
        if (!klub) {
            menuLayout.slide_klub_img
                    .setImageResource(R.drawable.settings_switch_1);
        } else {
            menuLayout.slide_klub_img
                    .setImageResource(R.drawable.settings_switch_2);
        }
    }

    // to iste ako pri fonte
    public void color_clicked(boolean color) {
        if (color) {
            detail.getMyWebView().setBackgroundColor(Color.WHITE);
            detail.pozadie.setBackgroundColor(Color.WHITE);
            menuLayout.slide_color_img.setImageResource(R.drawable.bg_color_switch_2);
            dialog.setVisibility(View.GONE);
            dialog = (ProgressBar) findViewById(R.id.progressDialog);
            dialog.setVisibility(View.VISIBLE);
            dialog_lin.setBackgroundColor(Color.WHITE);
        } else {
            detail.getMyWebView().setBackgroundColor(Color.BLACK);
            detail.pozadie.setBackgroundColor(Color.BLACK);
            menuLayout.slide_color_img.setImageResource(R.drawable.bg_color_switch_1);
            dialog.setVisibility(View.GONE);
            dialog = (ProgressBar) findViewById(R.id.progressDialogBlack);
            dialog.setVisibility(View.VISIBLE);
            dialog_lin.setBackgroundColor(Color.BLACK);
        }
        // System.out.println(color);
        if (detail != null) {
            if (color) {
                detail.getMyWebView().loadUrl(
                        "javascript:System.set_background_color(1)");
            } else {
                detail.getMyWebView().loadUrl(
                        "javascript:System.set_background_color(0)");
            }
            detail.getMyWebView().clearCache(false);
        } else {
            prefs.edit().putString("changedFont", "changed").commit();
        }
        prefs.edit().putBoolean("bg_white", color).commit();
        articleWhiteBackground = color;
        //1-biela


        if (color) {
            new EditSettingsFile(3, "1", this);
        } else {
            new EditSettingsFile(3, "0", this);
        }
        if (detail != null) {
            if (color) {
                detail.pozadie.setBackgroundColor(Color.WHITE);
            } else {
                detail.pozadie.setBackgroundColor(Color.BLACK);
            }
        }

    }

    // pri zmene jazyka sa musia vsetky texty v aplikacii aktualizovat
    public void language_clicked() {
        //dialog_lin.setVisibility(View.VISIBLE);
        dialog.setVisibility(View.GONE);
        changedLanguage = true;
        if (lang.equals("cs")) {
            prefs.edit().putString("savedLang", "sk").commit();
            lang = "sk";
            Locale.setDefault(new Locale("sk"));
            Configuration config = new android.content.res.Configuration();
            config.locale = new Locale("sk");
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //System.out.println("sk");
        } else {
            prefs.edit().putString("savedLang", "cs").commit();
            lang = "cs";
            Locale.setDefault(new Locale("cs"));
            Configuration config = new android.content.res.Configuration();
            config.locale = new Locale("cs");
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //System.out.println("cz");
        }

        related_text.setText(getString(R.string.suvisiace_clanky));
        menuLayout.updateWholeMenu();
        myResume("language");

        if (lang.equals("cs")) {
            menuLayout.slide_language_img.setImageResource(R.drawable.language_switch_2);
        } else {
            menuLayout.slide_language_img.setImageResource(R.drawable.language_switch_1);
        }
        if (lang.equals("cs")) {
            new EditSettingsFile(1, "cs", this);
        } else {
            new EditSettingsFile(1, "sk", this);
        }

    }

    // ziskanie jedinecneho ID pre zariadenie
    public String getDeviceID() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        //androidId = ""+ android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
        //UUID deviceUuid = new UUID(androidId.hashCode(),((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        //String deviceId = deviceUuid.toString() + lang;
        //String deviceId = deviceUuid.toString();
        return tmDevice + tmSerial;
    }

    public LinearLayout getmDrawerRight() {
        return mDrawerRight;
    }

    public void setmDrawerRight(LinearLayout mDrawerRight) {
        this.mDrawerRight = mDrawerRight;
    }

    // zistuje pritomnost internetu
    public boolean isNetworkAvailable() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public CustomDrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    // inicializacia progress dialogu
    public void createDialog(ProgressDialog pDialog, String title) {
        pDialog.setMessage(title);
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    // pri prechode clankov v detaile na susedny v informacnom servise, mozu byt
    // clanky uzamknute. Tato funkcia zistuje najblizsi nezamknuty
    // clanok vo zvolenom smere
    public int getUnlockedPos(int current, int next) {
        int count = currentItems.size();
        if (next > current) {
            for (int i = next; i < count; i++) {
                if (currentItems.get(i).getLocked().equals("0"))
                    return i;
            }
            for (int i = current - 1; i >= 0; i--) {
                if (currentItems.get(i).getLocked().equals("0"))
                    return i;
            }
        } else {
            for (int i = next; i >= 0; i--) {
                if (currentItems.get(i).getLocked().equals("0"))
                    return i;
            }
            for (int i = next + 1; i < count; i++) {
                if (currentItems.get(i).getLocked().equals("0"))
                    return i;
            }
        }
        return current;
    }

    // vytvara error panel
    public void errorMessage(String name, String text) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        errorPopup = (LinearLayout) findViewById(R.id.errorPopup);

        View v = inflater.inflate(R.layout.error_popup, errorPopup, false);
        if (errorPopup.getChildCount() > 0)
            return;
        errorPopup.addView(v);

        errorPopup.setBackgroundColor(Color.parseColor("#CC6A6A6A"));
        LinearLayout errorWrapper = (LinearLayout) findViewById(R.id.error);
        // errorPopup.setOnClickListener(null);
        errorPopup.setClickable(true);
        LinearLayout.LayoutParams errorWrapperParams = (LayoutParams) errorWrapper
                .getLayoutParams();
        errorWrapperParams.width = (int) (screenWidth / 2);
        errorWrapperParams.height = (int) (screenHeight / 3);
        LinearLayout errorText = (LinearLayout) findViewById(R.id.error_text);
        errorText.setPadding((int) (screenWidth / 40),
                (int) (screenWidth / 100), (int) (screenWidth / 30), 0);
        ImageView img = (ImageView) findViewById(R.id.img_error);
        LinearLayout.LayoutParams imgParams = (LayoutParams) img
                .getLayoutParams();
        imgParams.width = (int) (screenWidth / 30);
        error_button = (LinearLayout) findViewById(R.id.error_button);
        LinearLayout.LayoutParams error_buttonParams = (LayoutParams) error_button
                .getLayoutParams();
        error_buttonParams.width = (int) (screenWidth / 9);
        error_buttonParams.height = (int) (screenHeight / 18);
        TextView title = (TextView) findViewById(R.id.error_title);
        TextView content = (TextView) findViewById(R.id.error_content);
        TextView close = (TextView) findViewById(R.id.error_close);
        close.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources()
                .getDimension(R.dimen.size14));
        title.setText(name);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources()
                .getDimension(R.dimen.size17));
        content.setText(text);
        content.setMaxLines(3);
        content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources()
                .getDimension(R.dimen.size13));
        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorPopup.removeAllViews();
                errorPopup.setBackgroundColor(getResources().getColor(
                        android.R.color.transparent));
                errorPopup.setClickable(false);
                if (detail != null) {
                    displayView(9, "", null);
                }

            }
        });
    }

    public void setErrorOnClickFinish() {
        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // zapisuje data do suboru
    public void writeToFile(File file, String data) {
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println("from writeToFile:"+e);
        }

    }

    // nacitava data zo suboru
    public String readFromFile(File file) {
        String aBuffer = "";
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(
                    fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            myReader.close();

        } catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println("from readFromFile:"+e);
        }
        return aBuffer;
    }

    // aktualizuje sa sucasny pohlad
    public void updateView() {
        if (detail != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                    mDrawerRight);
        }
        switch (actualView) {
            // magnus, zoznam magazinov
            case 1:
                createMagnusFragment();
                break;
            // magnus magazin
            case 2:
                if (changedLanguage) {
                    changedLanguage = false;
                    createMagnusFragment();

                } else {
                    ((MagnusFragment) fragment).removeMagazinObsah();
                    ((MagnusFragment) fragment).displayMagazinObsah(
                            actualMagazinID, actualMagazinLastChange,
                            actualMagazinTitle, actualMagazinSubTitle);
                }

                break;
            case 3:
                createReadFragment();
                break;
            case 4:
                createFavoritesFragment();
                break;
            case 5:
                new LoadSearchStore(this).execute();
                break;
            case 6:
                new LoadSearchStore(this).execute();
                break;

            default:
                break;
        }
        if (visibleType == 1) {
            // storeJet = new ArticleStore(this,"0");
            updateHomeFragment(false, "jet");
            // storeServis = new ArticleStore(this,"1");

        } else if (visibleType == 2) {

            // storeServis = new ArticleStore(this,"1");
            updateHomeFragment(false, "servis");
            // storeJet = new ArticleStore(this,"0");
        } else if (visibleType == 3) {

            // storeServis = new ArticleStore(this,"1");
            updateHomeFragment(false, "klubMagnus");
            // storeJet = new ArticleStore(this,"0");
        }
        menuLayout.updateSettings();
    }

    // ************************************************Auth****************************************************************************
    // na zaciatku sa stale vola server pre overenie
    public void updateAuth() {
        System.out.println("som v auth");
        /*try {
            //json test
            String url2 = "https://www.fcbapps.sk/test.php";
            String response2 = CustomHttpClient.executeHttpGet(url2, authHash);
            JSONObject jsonLatest2 = new JSONObject(response2);
            System.out.println(jsonLatest2);
        } catch(Exception e)  {
            System.out.println(e);
        }*/
        JSONObject jsonAuth;
        try {
            if (!isNetworkAvailable()) {
                String fromFile = readFromFile(new File(getExternalCacheDir().toString() + "/cache/auth.txt"));
                jsonAuth = new JSONObject(fromFile);
                // System.out.println(jsonAuth);
            } else {
                String getPar = "http://194.50.215.137/api/auth/get/" + Functions.sha1Hash(getDeviceID());
                //responseGetAuth = CustomHttpClient.executeHttpGet(getPar, "");
                System.out.println(Functions.sha1Hash(getDeviceID()));
                String response = CustomHttpClient.executeHttpGet(getPar, "");
                //System.out.println("prva response"+response );
                jsonAuth = new JSONObject(response);
                //System.out.println(responseGetAuth.get(0));
                String jsonString = jsonAuth.toString();
                // System.out.println(jsonString);
                writeToFile(new File(getExternalCacheDir().toString() + "/cache/auth.txt"), jsonString);
            }

            JSONObject jsonResult = jsonAuth.getJSONObject("result");
            authHash = jsonResult.getString("sessionID");
            System.out.println(authHash);
            logged = jsonResult.getString("logged").equals("1");
            // logged= false;
            // System.out.println("logged: "+jsonResult.getString("logged"));
        } catch (Exception e) {
            currentError = true;
            System.out.println(e);
        }
    }

    // ************************************************ end
    // Auth*****************************************************

    // ************************************************AppSetings****************************************************************************
    // kontroluju sa nastavenia appky, ci nie je zastarala, ci netreba stiahnut
    // novy system.zip
    public void updateAppSettings() {
        System.out.println("som v updateSettings");
        JSONObject jsonSettings = null;
        try {
            if (!isNetworkAvailable()) {
                return;
				/*
				 * String fromFile = readFromFile(new File(getExternalCacheDir
				 * ().toString()+"/cache/auth.txt")); jsonLatest = new
				 * JSONObject(fromFile);
				 */
            } else {
                String url = "http://194.50.215.137/api/common/get_app_settings/android";
                String response = CustomHttpClient.executeHttpGet(url, authHash);
                jsonSettings = new JSONObject(response);
				/*
				 * String jsonString = jsonAuth.toString(); writeToFile(new
				 * File(getExternalCacheDir ().toString()
				 * +"/cache/auth.txt"),jsonString);
				 */
            }
            JSONObject jsonResult = jsonSettings.getJSONObject("result");
            //System.out.println(jsonResult);
            Float lastSuportedVersion = Float.parseFloat(jsonResult.getString("last_supported_app_version"));
            Float currentVersion = Float.parseFloat(jsonResult.getString("current_app_version"));
            Float versionName = Float.parseFloat(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            if (versionName < lastSuportedVersion)
                errorMessage(getString(R.string.error3), getString(R.string.error12));

            JSONObject jsonSystem = jsonResult.getJSONObject("system");
            String systemUrl = jsonSystem.getString("zip");
            String tutorialUrl = jsonSystem.getString("tutorial");
            String versionLastChange = jsonSystem.getString("version");
            String actualVersionLastChange = prefs.getString("versionSystem", "");
            String tutorialVersionLastChange = jsonSystem.getString("tutorial_version");
            String actualTutorialVersionLastChange = prefs.getString("versionTutorial", "");
            String actual_purge_request = prefs.getString("purge_request", "");
            String purge_request = jsonSystem.getString("purge_request");
            //System.out.println("system actual: "+actualVersionLastChange);
            //System.out.println("system versionLastChange: "+versionLastChange);
            File systemDir = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/system");
            File tutorialDir = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/tutorial");
            File js1 = new File(systemDir + "/settings.js");
            File js2 = new File(systemDir + "/regions.js");
            File js3 = new File(systemDir + "/system.js");
            File js4 = new File(systemDir + "/dictionary.js");
            File js5 = new File(systemDir + "/jquery-2.1.0.min.js");

            //system
            if ((!versionLastChange.equals(actualVersionLastChange)) || (!systemDir.isDirectory()) || !systemDir.isDirectory() || !js1.isFile()) {
                if (!systemDir.isDirectory()) {
                    // systemDir.mkdirs();
                } else {
                    File directoryForDelete = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/system");
                    Functions.deleteDirectory(directoryForDelete);
                }

                system_task = new LoadSystem(this, versionLastChange).execute(new String[]{systemUrl, "system", ""});

            }
            //tutorial
            if ((!tutorialVersionLastChange.equals(actualTutorialVersionLastChange)) || (!tutorialDir.isDirectory())) {
                if (!tutorialDir.isDirectory()) {
                    // systemDir.mkdirs();
                } else {
                    File directoryForDelete = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/tutorial");
                    Functions.deleteDirectory(directoryForDelete);
                }

                tutorial_task = new LoadTutorial(this).execute(new String[]{tutorialUrl, "tutorial", ""});
                Editor prefsEditor = prefs.edit();
                prefsEditor.putString("versionTutorial", tutorialVersionLastChange).commit();
            }

            //cache obrazkov
            if (!(actual_purge_request.equals(purge_request))) {
                prefs.edit().putString("purge_request", purge_request).commit();
                imageLoader.clearDiscCache();
            }


        } catch (Exception e) {
            errorMessage(getString(R.string.error3), getString(R.string.error6));
            System.out.println(e);
        }
    }

    // ************************************************ end
    // AppSetings****************************************************

    // ************************************************Latest****************************************************************************
    // hlavne polozky v lavom menu sa nastavuju tu. Obrazky a aj pocty
    // neprecitanych
    public void updateLatest() {
        System.out.println("som v updateLatest");
        JSONObject jsonLatest = null;
        try {
            if (!isNetworkAvailable()) {
                String fromFile = readFromFile(new File(getExternalCacheDir().toString() + "/cache/latest.txt"));
                jsonLatest = new JSONObject(fromFile);
            } else {

                send_magazin_notifikations(menuLayout.magazin);
                String url = "http://194.50.215.137/api/common/get_latests/" + lang;
                String response = CustomHttpClient.executeHttpGet(url, authHash);
                jsonLatest = new JSONObject(response);
                System.out.println(response);
                String jsonString = jsonLatest.toString();
                writeToFile(new File(getExternalCacheDir().toString() + "/cache/latest.txt"), jsonString);

            }
            JSONObject jsonResult = jsonLatest.getJSONObject("result");
            JSONObject magazines = jsonResult.getJSONObject("magazines");
            JSONObject club = jsonResult.getJSONObject("club");
            if (magazines.has("thumb")) {
                menuModel.magnusUrl = magazines.getString("thumb");
            }
            if (magazines.has("badgeCount")) {
                menuModel.magnusCount = magazines.getString("badgeCount");
            }
            if (club.has("thumb")) {
                menuModel.magnusKlubUrl = club.getString("thumb");
            }
            if (club.has("badgeCount")) {
                menuModel.magnusKlubCount = club.getString("badgeCount");
            }
            if (jsonResult.has("cat0")) {
                JSONObject cat0 = jsonResult.getJSONObject("cat0");
                menuModel.jetUrl = cat0.getString("thumb");
                menuModel.jetCount = cat0.getString("badgeCount");
            }
            if (jsonResult.has("cat1")) {
                JSONObject cat1 = jsonResult.getJSONObject("cat1");
                menuModel.servisUrl = cat1.getString("thumb");
                menuModel.servisCount = cat1.getString("badgeCount");
            }


        } catch (Exception e) {
            currentError = true;
            System.out.println("from update latest" + e);
        }
    }


    // ************************************************ end
    // Latest****************************************************
    // pri resume alebo ked vyprsi cas 5min sa aktivuje tato funkcia kde sa opat
    // nacitaju stores zo servera
    public void myResume(String language_changed) {
        if (isNetworkAvailable()) {
            fromResume = true;
            store_task = new LoadStores(this).execute(new String[]{"", "", ""});
            //myTimer.cancel();
            //myTimer = new Timer();
            //myTimer.schedule(new MyTimerTask(), 300000, 300000);
        } else
            menuLayout.updateSettings();
    }


    class MyTimerTask extends TimerTask {
        public void run() {
            handler.post(new Runnable() {
                @SuppressWarnings("unchecked")
                public void run() {
                    try {
                        myResume("");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }
                }
            });

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //System.out.println("onSaveInstanceState");
    }

    public void fadeIN() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setStartOffset(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
        logo.startAnimation(fadeIn);
    }

    public void fadeOUT() {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(2000);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {

                hideStartScreen();
            }
        });

        logo.startAnimation(fadeOut);

    }

    public void hideStartScreen() {
        logo_wrapper.setVisibility(View.INVISIBLE);
        logo_wrapper.removeAllViews();
        logo_background.setImageDrawable(null);
        logo.setImageDrawable(null);
    }

    public void removeWOT() {
        world.setVisibility(View.INVISIBLE);
        world_wrapper.setVisibility(View.INVISIBLE);
        world.removeAllViews();
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerLinear);
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerRight);
    }

    //stiahne clanok
    private void downloadArticleOnBackground(String zipUrl, String newDir) {
        new LoadArticleOnBackground(this).execute(new String[]{zipUrl, newDir});
    }

    public void checkIfArticleDownloaded(final ArticleModel model) {
        String newDir = "";
        if (!(model.getSource().equals("3"))) {
            try {
                String lastChange = model.getLastChange();
                newDir = "/" + Constants.STORAGE_FILE + "/articles/1-" + model.getId() + "-" + lang + "-" + lastChange;
                File currentArticle = new File(getExternalCacheDir().toString() + newDir);
                File currentArticleIndex = new File(getExternalCacheDir().toString() + newDir + "/index.html");
                //zistujeme ci sa clanok s danym lastChange nachadza v pamati tabletu
                if (!(currentArticle.isDirectory() && currentArticleIndex.isFile())) {
                    if (isNetworkAvailable()) {
                        //ak sa nenachadza vymazeme stare s neaktualnym lastChange a stiahneme clanok do pamate
                        //System.out.println(newDir);
                        Functions.deleteOldDirectory("1-" + model.getId(), this);
                        //System.out.println("http://194.50.215.137/api/news/get_detail/" + id+"/"+((MainActivity)context).lang);
                        String response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/news/get_detail/" + model.getId() + "/" + lang, authHash);
                        JSONObject json = new JSONObject(response);
                        JSONObject jsonDetail = json.getJSONObject("result");
                        String zipUrl = jsonDetail.getString("file");
                        downloadArticleOnBackground(zipUrl, newDir);
                    }
                    // zistovanie ci existuje stary priecinok
                }

            } catch (Exception e) {
                System.out.println("from detail jet: " + e);
                File forDelete = new File(getExternalCacheDir().toString() + newDir + "/index.html");
                forDelete.delete();
            } //**************************************************************magnus**************************************************************************
        } else {
            try {
                //kontrola ci existuje magazin priecinok
                File magazinDir = new File(getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/3-" + model.getMagazinID() +
                        "-" + lang + "-" + model.getLastChange());
                if (!magazinDir.isDirectory()) {
                    Functions.deleteOldDirectory("3-" + model.getMagazinID(), this);
                    magazinDir.mkdirs();
                }
                //koniec kontroly ci existuje magazin priecinok
                String zipUrl = model.getZipUrl();

                newDir = "/" + Constants.STORAGE_FILE + "/articles/3-" + model.getMagazinID() + "-" + lang + "-" + model.getLastChange() + "/" +model.getMagnusArticleId();
                File currentArticle = new File(getExternalCacheDir().toString() + newDir);
                File currentArticleIndex = new File(getExternalCacheDir().toString() + newDir + "/index.html");

                if (!(currentArticle.isDirectory() && currentArticleIndex.isFile())) {
                    if (isNetworkAvailable()) {
                        if (zipUrl.equals("")) {
                            String response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/archive/get_article/" + model.getId(), authHash);
                            JSONObject json = new JSONObject(response);
                            JSONObject jsonDetail = json.getJSONObject("result");
                            //System.out.println(json);
                            zipUrl = jsonDetail.getString("zip");
                        }
                        downloadArticleOnBackground(zipUrl, newDir);
                    }
                }

            } catch (Exception e) {
                System.out.println("from get_article" + e);
                File forDelete = new File(getExternalCacheDir().toString() + newDir + "/index.html");
                forDelete.delete();
            }


        }
        //*****************************************************koniec magnusu******************************************************************************
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Functions.getRelated(model, MainActivity.this);
                } catch (Exception e) {
                    System.out.println("checkIfArticleDownloaded getRelated:" + e);
                }
            }
        };
        thread.start();


    }


}

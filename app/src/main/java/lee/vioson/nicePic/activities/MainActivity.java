package lee.vioson.nicePic.activities;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lee.vioson.nicePic.R;
import lee.vioson.nicePic.adapter.MenuItemAdapter;
import lee.vioson.nicePic.fragments.ListFragment;
import lee.vioson.nicePic.models.ClassifyBean;
import lee.vioson.nicePic.utils.ActivitiSwtcher;
import lee.vioson.nicePic.utils.DataServie;
import lee.vioson.nicePic.views.LvMenuItem;
import lee.vioson.utils.DebugLog;
import lee.vioson.utils.ShareUtil;
import lee.vioson.xiumm.models.Type;
import lee.vioson.xiumm.utils.DataHandler;

public class MainActivity extends AppCompatActivity implements MenuItemAdapter.OnItemSelectListener
        , ListFragment.OnListScrollListener {

    private static final String FRAGMENT_TAG = "list_fragment";
    public static String types = "types";
    //    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.fab)
    FloatingActionButton fab;
    //    @BindView(R.id.id_lv_left_menu)
    ListView mLvLeftMenu;
    //    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    //    private ListView mLvLeftMenu;
    private MenuItemAdapter menuItemAdapter;
    private ArrayList<LvMenuItem> items = new ArrayList<>();

    private ListFragment listFragment;
    private List<ClassifyBean.TngouEntity> tngou = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mLvLeftMenu = (ListView) findViewById(R.id.id_lv_left_menu);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        DebugLog.setDebug(true);
        //常规view的初始化
        initNormalView();
        //初始化侧滑菜单
        setUpDrawer();
        //加载类型
        initFragment(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mTypes.size() <= 0)
            loadType();
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        else {
            listFragment = ListFragment.newInstance(0);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, listFragment, FRAGMENT_TAG)
                    .show(listFragment).commit();
        }
    }

    private void initNormalView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listFragment != null)
                    listFragment.upToFirst();
                fab.hide();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //        navigationView.setNavigationItemSelectedListener(this);
    }

    long toggleClickTime = 0;

    private void setUpDrawer() {
//        items = new ArrayList<>();
//        LvMenuItem item = new LvMenuItem(R.drawable.heart, "最新");
//        items.add(item);

        menuItemAdapter = new MenuItemAdapter(this, items);
        mLvLeftMenu = (ListView) findViewById(R.id.id_lv_left_menu);
        View inflate = LayoutInflater.from(this).inflate(R.layout.nav_header_main, mLvLeftMenu, false);
        mLvLeftMenu.addHeaderView(inflate);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - toggleClickTime <= 500) {
                    DataServie.isLimit = !DataServie.isLimit;
                    loadType();
                    drawer.closeDrawer(Gravity.LEFT);
                } else toggleClickTime = System.currentTimeMillis();
            }
        });

        mLvLeftMenu.setAdapter(menuItemAdapter);
        menuItemAdapter.setOnItemSelectListener(this);
    }

    ArrayList<Type> mTypes = new ArrayList<>();

    private void loadType() {
//        DataHelper.loadTypes(new DataHelper.DataHandler<ArrayList<Type>>() {
//            @Override
//            public void onDataBack(boolean isEmpty, ArrayList<Type> types) {
//                if (!isEmpty) {
//                    upMenu(types);
//                    mTypes.clear();
//                    mTypes.addAll(types);
//                }
//            }
//
//            @Override
//            public void onDocumentNull() {
//
//            }
//        });
        DataServie.loadTypes(new DataHandler<ArrayList<Type>>() {
            @Override
            public void onDataBack(boolean isEmpty, ArrayList<Type> types) {
                if (!isEmpty) {
                    upMenu(types);
                    mTypes.clear();
                    mTypes.addAll(types);
                }
            }

            @Override
            public void onDocumentNull() {

            }
        });
    }

    private void upMenu(ArrayList<Type> types) {
        items.clear();
        for (Type type : types) {
            LvMenuItem item = new LvMenuItem();
            item.icon = R.drawable.heart;
            item.type = LvMenuItem.TYPE_NORMAL;
            item.name = type.title;
            items.add(item);
        }
        LvMenuItem item = new LvMenuItem();
        item.type = LvMenuItem.TYPE_SEPARATOR;
        items.add(item);//横线
        addAppMenus();
        menuItemAdapter.notifyDataSetChanged();

        if (listFragment != null & types.size() > 0) {
            listFragment.swtihType(types.get(0));
        }
    }


    private void addAppMenus() {
//        items.add(new LvMenuItem(R.drawable.ic_setting, getString(R.string.action_settings)));
        items.add(new LvMenuItem(R.drawable.ic_about, getString(R.string.about)));
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            String content = "看图神器！";
            ShareUtil.share(this, content);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelect(int position, View v) {

//        if (position == 0) {
//            if (listFragment != null)
//                listFragment.swtihType(0, true);
//        } else
        if (position < items.size() - 2) {
//            if (listFragment != null)
//                listFragment.swtihType(tngou.get(position - 1).getId(), false);
//            setTitle(items.get(position).name);

            if (listFragment != null)
                listFragment.swtihType(mTypes.get(position));
            setTitle(items.get(position).name);
        }
//        else if (position == items.size() - 2) {
//            //设置
//
//        }
        else if (position == items.size() - 1) {
            //关于
            ActivitiSwtcher.toWeb(this, getString(R.string.about), "file:///android_asset/about.html");
        }


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onListScroll(int firstVisiblePosition, int endVisiblePosition, int totalCount) {
        Log.e("onListScroll", firstVisiblePosition + "\n" + endVisiblePosition + "\n" + totalCount);
        if (firstVisiblePosition == 0) {
            fab.hide();
        } else {
            if (!fab.isShown())
                fab.show();
        }
    }

}

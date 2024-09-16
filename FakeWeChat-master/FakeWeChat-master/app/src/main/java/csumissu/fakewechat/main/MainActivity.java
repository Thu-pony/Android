package csumissu.fakewechat.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.AppManager;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.source.EntityRepository;
import csumissu.fakewechat.data.source.local.LocalDataSource;
import csumissu.fakewechat.data.source.remote.RemoteDataSource;
import csumissu.fakewechat.util.DoubleClickExitHelper;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private static final String KEY_SELECTED_POSITION = "key_selected_position";
    private ArrayList<Fragment> mFragment;
    private DoubleClickExitHelper mDoubleClickExitHelper;
    public static final int TAB_INDEX_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        int selectedPosition = savedInstanceState != null ? savedInstanceState.getInt(
                KEY_SELECTED_POSITION, 0) : 0;
        initViews(selectedPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTED_POSITION, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initViews(int selectedPosition) {
        mBottomNavBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavBar.addItem(new BottomNavigationItem(R.drawable.ic_donut_large, R.string.tab_chat)
                .setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle, R.string.tab_contacts)
                        .setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.drawable.ic_explore, R.string.tab_explore)
                        .setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.drawable.ic_face, R.string.tab_me)
                        .setActiveColorResource(R.color.colorAccent))
                .setFirstSelectedPosition(selectedPosition)
                .initialise();
        mBottomNavBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position, false);
            }
        });

        if (mFragment == null) {
            mFragment = new ArrayList<>();
            mFragment.add(new ChatFragment());
            ContactsFragment contactsFragment = new ContactsFragment();
            ContactsPresenter contactsPresenter = new ContactsPresenter(contactsFragment,
                    EntityRepository.getInstance(LocalDataSource.getInstance(this),
                            RemoteDataSource.getInstance()));
            contactsFragment.setPresenter(contactsPresenter);
            mFragment.add(contactsFragment);
            mFragment.add(new ExploreFragment());
            mFragment.add(new MeFragment());
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        });
        mViewPager.setCurrentItem(selectedPosition);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mBottomNavBar.selectTab(position, false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        ((ContactsFragment) mFragment.get(TAB_INDEX_CONTACTS))
                .setLetterViewVisibility(state == ViewPager.SCROLL_STATE_IDLE);
    }

}

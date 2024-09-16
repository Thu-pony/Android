package csumissu.fakewechat.v2.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.base.BaseActivity;
import csumissu.fakewechat.v2.msg.ScrollStateEvent;
import csumissu.fakewechat.v2.ui.contact.ContactFragment;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String KEY_SELECTED_POSITION = "key_selected_position";
    private static final String[] TAB_FRAGMENT_TAG = {"tag_home", "tag_contact", "tag_explore", "tag_me"};
    private static final int TAB_HOME = 0;
    private static final int TAB_CONTACT = 1;
    private static final int TAB_EXPLORE = 2;
    private static final int TAB_ME = 3;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavBar;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTED_POSITION, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private void initViews(Bundle savedInstanceState) {
        int selectedPosition = savedInstanceState == null ? 0 :
                savedInstanceState.getInt(KEY_SELECTED_POSITION, 0);

        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(findFragment(TAB_HOME));
            mFragments.add(findFragment(TAB_CONTACT));
            mFragments.add(findFragment(TAB_EXPLORE));
            mFragments.add(findFragment(TAB_ME));
        }

        initBottomNavBar(selectedPosition);
        initViewPager(selectedPosition);
    }

    private void initBottomNavBar(int selectedPosition) {
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
    }

    private void initViewPager(int selectedPosition) {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(selectedPosition);
    }

    private Fragment findFragment(int tabIndex) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAB_FRAGMENT_TAG[tabIndex]);
        if (fragment != null) {
            return fragment;
        }

        switch (tabIndex) {
            case TAB_HOME:
                return new HomeFragment();
            case TAB_CONTACT:
                return new ContactFragment();
            case TAB_EXPLORE:
                return new ExploreFragment();
            case TAB_ME:
                return new MeFragment();
            default:
                throw new IllegalArgumentException("invalid tab index " + tabIndex);
        }
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
        EventBus.getDefault().post(new ScrollStateEvent(state));
    }
}

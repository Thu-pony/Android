package csumissu.fakewechat.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.AppContext;
import csumissu.fakewechat.R;
import csumissu.fakewechat.base.BaseChildActivity;
import csumissu.fakewechat.data.User;
import csumissu.fakewechat.data.source.EntityRepository;
import csumissu.fakewechat.data.source.local.LocalDataSource;
import csumissu.fakewechat.data.source.remote.RemoteDataSource;
import csumissu.fakewechat.util.ActivityUtils;

/**
 * @author sunyaxi
 * @date 2016/5/23
 */
public class CommentsActivity extends BaseChildActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = CommentsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_image)
    ImageView mAppBarImage;
    @BindView(R.id.owner_photo)
    ImageView mOwnerPhoto;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        // actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Glide.with(this).load(R.drawable.photo).centerCrop().into(mAppBarImage);
        showOwnerInternal(AppContext.getInstance().getLoginUser());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment == null) {
            fragment = new CommentsFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.content_frame);
        }

        CommentsContract.View view = (CommentsContract.View) fragment;
        CommentsPresenter presenter = new CommentsPresenter(view,
                EntityRepository.getInstance(LocalDataSource.getInstance(this),
                        RemoteDataSource.getInstance()));
        view.setPresenter(presenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBar.removeOnOffsetChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.menu.comments:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int appBarHeight = getResources().getDimensionPixelSize(R.dimen.app_bar_height);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        int actionBarHeight = actionBar.getHeight();
        mOwnerPhoto.setVisibility(verticalOffset + (appBarHeight - actionBarHeight) / 2 > 0
                ? View.VISIBLE : View.GONE);
    }

    void showOwnerInternal(User owner) {
        if (owner != null) {
            getSupportActionBar().setTitle(owner.getName());
            Glide.with(this).load(owner.getAvatar()).into(mOwnerPhoto);
        }
    }

}

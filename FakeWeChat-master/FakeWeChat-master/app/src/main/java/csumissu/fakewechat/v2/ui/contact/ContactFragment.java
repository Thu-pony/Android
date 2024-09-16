package csumissu.fakewechat.v2.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.base.BaseFragment;
import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.msg.ScrollStateEvent;
import csumissu.fakewechat.widget.LetterView;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class ContactFragment extends BaseFragment implements ContactContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_tip)
    View mLoadingView;
    @BindView(R.id.letters_nar_bar)
    LetterView mLetterView;
    @Inject
    ContactPresenter mPresenter;
    private Unbinder mUnbinder;
    private ContactAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.whiteSmoke)
                .sizeResId(R.dimen.divider)
                .build());

        mAdapter = new ContactAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mUnbinder.unbind();
    }

    @Override
    public void showLoading(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        showMessage(message);
    }

    @Override
    public void showFriends(List<User> users) {
        if (users == null || users.isEmpty()) {
            showMessage(R.string.no_friends);
            return;
        }
        mAdapter.setData(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollStateEvent(ScrollStateEvent event) {
        if (mLetterView != null) {
            boolean show = event.getState() == ViewPager.SCROLL_STATE_IDLE;
            mLetterView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void initInject() {
        DaggerContactComponent.builder()
                .appComponent(getApplicationComponent())
                .contactModule(new ContactModule(this))
                .build()
                .inject(this);
    }
}

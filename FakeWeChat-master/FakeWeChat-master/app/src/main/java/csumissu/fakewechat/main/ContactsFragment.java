package csumissu.fakewechat.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.FriendshipResult;
import csumissu.fakewechat.widget.LetterView;

import static csumissu.fakewechat.util.Preconditions.checkNotNull;

/**
 * @author sunyaxi
 * @date 2016/6/27
 */
public class ContactsFragment extends RxFragment implements ContactsContract.View,
        LetterView.Callback {

    private ContactsContract.Presenter mPresenter;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_tip)
    View mLoadingView;
    @BindView(R.id.letters_nar_bar)
    LetterView mLetterView;
    private ContactsAdapter mAdapter;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        // 禁止多点触发
        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.whiteSmoke)
                .sizeResId(R.dimen.divider)
                .marginResId(R.dimen.tile_divider_margin)
                .build());
        mAdapter = new ContactsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    public void setLetterViewVisibility(boolean show) {
        if (mLetterView != null) {
            mLetterView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showLoading(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showFriends(FriendshipResult result) {
        if (result.getUsers().isEmpty()) {
            Toast.makeText(getContext(), R.string.no_friends, Toast.LENGTH_SHORT).show();
            return;
        }
        mAdapter.setData(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(@NonNull ContactsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void scrollToPosition(char letter) {
        mRecyclerView.scrollToPosition(mAdapter.letter2Position(letter));
    }
}

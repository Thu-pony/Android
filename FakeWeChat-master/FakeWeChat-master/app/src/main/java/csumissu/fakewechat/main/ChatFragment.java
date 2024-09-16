package csumissu.fakewechat.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.AppContext;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.User;
import csumissu.fakewechat.main.ChatAdapter.Tweet;
import csumissu.fakewechat.main.source.TuringRequest;
import csumissu.fakewechat.main.source.TuringRobotApi;
import csumissu.fakewechat.util.FontUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author sunyaxi
 * @date 2016/6/27
 */
public class ChatFragment extends Fragment {

    private static final String TAG = ChatFragment.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.send)
    TextView mSendView;
    @BindView(R.id.message)
    EditText mMessageView;
    private ChatAdapter mAdapter;
    private User mRobotUser;
    private User mOwner;
    private TuringRobotApi mTuringService;
    private InputMethodManager mInputMethodManager;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOwner = AppContext.getInstance().getLoginUser();
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TuringRobotApi.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mTuringService = retrofit.create(TuringRobotApi.class);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        FontUtils.markAsIcon(getContext(), mSendView);
        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = mMessageView.getText().toString();
                if (message.length() == 0) {
                    return;
                }
                mMessageView.getText().clear();
                Tweet tweet = new Tweet();
                tweet.content = message;
                tweet.sender = mOwner;
                mAdapter.addNewTweet(tweet);
                mTuringService.ask(new TuringRequest(message))
                        .doOnSubscribe(() -> mSendView.setClickable(false))
                        .doOnTerminate(() -> mSendView.setClickable(true))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(turingResponse -> {
                            Log.i(TAG, "response=" + turingResponse);
                            Tweet robotTweet = new Tweet();
                            robotTweet.content = turingResponse.getText();
                            if (!TextUtils.isEmpty(turingResponse.getUrl())) {
                                robotTweet.content += '\n' + turingResponse.getUrl();
                            }
                            robotTweet.sender = mRobotUser;
                            mAdapter.addNewTweet(robotTweet);
                        });
            }
        });
        // 禁止多点触发
        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new ChatAdapter(getActivity());
        mAdapter.setData(getDefaultData());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mRecyclerView.setOnTouchListener((view, motionEvent) -> {
            if (mMessageView.isFocused()) {
                mMessageView.clearFocus();
                mInputMethodManager.hideSoftInputFromWindow(mMessageView.getWindowToken(), 0);
            }
            return false;
        });
        mMessageView.setOnFocusChangeListener((view, focused) -> {
            if (!focused && mInputMethodManager.isActive()) {
                mInputMethodManager.hideSoftInputFromWindow(mMessageView.getWindowToken(), 0);
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mAdapter.unregisterAdapterDataObserver(mDataObserver);
    }

    private List<Tweet> getDefaultData() {
        mRobotUser = new User();
        mRobotUser.setUid(1234567890);
        mRobotUser.setName("小微");
        mRobotUser.setImageUrl("android.resource://" + getContext().getPackageName() + "/" + R.drawable.ic_robot);
        mRobotUser.setGender("f");

        List<Tweet> tweets = new ArrayList<>();
        Tweet tweet = new Tweet();
        tweet.content = "您好，我是聊天机器人的小微，请问有什么能帮助您。";
        tweet.sender = mRobotUser;
        tweets.add(tweet);
        return tweets;
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            System.out.println("positionStart=" + positionStart + ", itemCount=" + itemCount);
            mRecyclerView.scrollToPosition(positionStart - 1);
        }
    };

}

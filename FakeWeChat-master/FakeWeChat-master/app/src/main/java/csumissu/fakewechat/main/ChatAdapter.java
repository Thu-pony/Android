package csumissu.fakewechat.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.AppContext;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.User;
import csumissu.fakewechat.util.WeiboUtils;

/**
 * @author sunyaxi
 * @date 2016/7/4
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    private List<Tweet> mTweets = new ArrayList<>();
    private final long OWNER_UID;
    private HashSet<Long> mUserUidSet = new HashSet<>();

    public ChatAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        OWNER_UID = AppContext.getInstance().getLoginUser().getUid();
    }

    public void setData(List<Tweet> tweets) {
        mTweets.clear();
        mUserUidSet.clear();
        if (tweets != null) {
            mTweets.addAll(tweets);
            for (Tweet tweet : mTweets) {
                mUserUidSet.add(tweet.sender.getUid());
            }
        }
    }

    public void addNewTweet(Tweet tweet) {
        mTweets.add(tweet);
        mUserUidSet.add(tweet.sender.getUid());
        notifyItemInserted(mTweets.size());
    }

    @Override
    public int getItemViewType(int position) {
        return mTweets.get(position).sender.getUid() == OWNER_UID ? VIEW_TYPE_ME : VIEW_TYPE_OTHER;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ME:
                return new ViewHolder(mInflater.inflate(R.layout.list_item_chat_me, parent, false));
            case VIEW_TYPE_OTHER:
                return new ViewHolder(mInflater.inflate(R.layout.list_item_chat_other, parent, false));
            default:
                throw new IllegalArgumentException("No delegate found for position " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        Glide.with(mContext).load(tweet.sender.getImageUrl())
                .placeholder(R.drawable.ic_photo_placeholder)
                .into(holder.photoView);
        holder.nameView.setText(tweet.sender.getName());
        holder.nameView.setVisibility(mUserUidSet.size() > 2 ? View.VISIBLE : View.GONE);
        holder.contentView.setText(WeiboUtils.transformContent(mContext,
                tweet.content, (int) holder.contentView.getTextSize()));
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        public ImageView photoView;
        @BindView(R.id.name)
        public TextView nameView;
        @BindView(R.id.content)
        public TextView contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Tweet {
        public String content;
        public Date sendTime;
        public User sender;

        @Override
        public String toString() {
            return "Tweet{" +
                    "content='" + content + '\'' +
                    ", sendTime=" + sendTime +
                    ", sender=" + sender +
                    '}';
        }
    }
}

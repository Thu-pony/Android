package csumissu.fakewechat.v2.ui.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.util.ImageLoader;

/**
 * @author sunyaxi
 * @date 2016/11/20.
 */
class ContactAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Pair<User, Boolean>> mFriends;

    ContactAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mFriends = new ArrayList<>();
    }

    public void setData(List<User> users) {
        mFriends.clear();
        if (users != null && !users.isEmpty()) {
            char preChar = Character.MIN_VALUE;
            boolean showLabel;
            for (User user : users) {
                char c = user.getFirstLetter();
                if (c != preChar) {
                    showLabel = true;
                    preChar = c;
                } else {
                    showLabel = false;
                }
                mFriends.add(new Pair<>(user, showLabel));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendViewHolder(mInflater.inflate(R.layout.item_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendViewHolder viewHolder = (FriendViewHolder) holder;
        Pair<User, Boolean> pair = mFriends.get(position);

        if (pair.second) {
            viewHolder.labelView.setText(String.valueOf(pair.first.getFirstLetter()));
            viewHolder.labelView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.labelView.setVisibility(View.GONE);
        }

        ImageLoader.loadImage(mContext, viewHolder.photoView, pair.first.getImageUrl());
        viewHolder.nameView.setText(pair.first.getName());
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label)
        TextView labelView;
        @BindView(R.id.photo)
        ImageView photoView;
        @BindView(R.id.name)
        TextView nameView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

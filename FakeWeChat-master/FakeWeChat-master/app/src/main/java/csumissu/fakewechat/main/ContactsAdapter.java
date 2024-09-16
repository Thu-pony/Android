package csumissu.fakewechat.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.FriendshipResult;
import csumissu.fakewechat.data.User;

/**
 * @author sunyaxi
 * @date 2016/7/1
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private FriendshipResult mResult;

    public ContactsAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(FriendshipResult result) {
        mResult = result;
    }

    public int letter2Position(char letter) {
        int position = -1;
        if (mResult != null) {
            Integer pos = mResult.getLetterPositionMap().get(letter);
            if (pos != null) {
                position = pos;
            }
        }
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mResult.getUsers().get(position);
        if (mResult.getLetterPositionMap().values().contains(position)) {
            holder.labelView.setVisibility(View.VISIBLE);
            holder.labelView.setText(user.getPinyin().substring(0, 1).toUpperCase());
        } else {
            holder.labelView.setVisibility(View.GONE);
        }
        holder.nameView.setText(user.getName());
        Glide.with(mContext).load(user.getImageUrl())
                .placeholder(R.drawable.ic_photo_placeholder)
                .into(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return mResult != null ? mResult.getUsers().size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label)
        public TextView labelView;
        @BindView(R.id.icon)
        public ImageView photoView;
        @BindView(R.id.name)
        public TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

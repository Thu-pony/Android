package csumissu.fakewechat.comments;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.common.PictureViewFragment;
import csumissu.fakewechat.data.Status;
import csumissu.fakewechat.util.DisplayUtils;
import csumissu.fakewechat.util.FontUtils;
import csumissu.fakewechat.util.WeiboUtils;

import static csumissu.fakewechat.util.Preconditions.checkNotNull;

/**
 * @author syx
 * @date 2016/5/24.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context mContext;
    private List<Status> mStatuses = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private FragmentManager mFragmentManager;
    private SimpleDateFormat mSimpleDateFormat;
    private int mGridColumnSize;

    public CommentsAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mFragmentManager = fragmentManager;
        mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        mGridColumnSize = (Math.min(point.x, point.y) - DisplayUtils.dip2px(context,
                8 * 2/*item_padding*/ + 36/*photo_width*/ + 8/*photo_margin-right*/
                        + 12/*GridView_margin-right*/)) / 3;
    }

    public void setData(@NonNull List<Status> statuses) {
        mStatuses = checkNotNull(statuses);
    }

    public void addData(@NonNull List<Status> statuses) {
        mStatuses.addAll(checkNotNull(statuses));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status status = mStatuses.get(position);
        Glide.with(mContext).load(status.getSender().getImageUrl())
                .placeholder(R.drawable.ic_photo_placeholder)
                .into(holder.senderPhoto);
        holder.senderName.setText(status.getSender().getName());
        markAsIconForGender(holder.senderGender, status.getSender().getGender());
        holder.statusContent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.statusContent.setText(WeiboUtils.transformContent(mContext,
                status.getText(), (int) holder.statusContent.getTextSize()));
        holder.statusPictures.setAdapter(new GridAdapter(status.getPicUrls()));
        adjustGridView(holder.statusPictures, status.getPicUrls().size());
        holder.statusDate.setText(mSimpleDateFormat.format(status.getCreateAt()));
        holder.statusSource.setText(getPlainText(status.getSource()));
    }

    @Override
    public int getItemCount() {
        return mStatuses.size();
    }

    private void adjustGridView(GridView gridView, int pictureCount) {
        gridView.setColumnWidth(mGridColumnSize);
        gridView.setNumColumns(pictureCount <= 4 ? 2 : 3);
    }

    private String getPlainText(String html) {
        if (TextUtils.isEmpty(html)) {
            return html;
        }
        Matcher matcher = Pattern.compile("<a[^>]*>([^<]*)</a>").matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return html;
    }

    private void markAsIconForGender(TextView textView, String gender) {
        String icon;
        int colorResId;
        if ("m".equals(gender)) {
            icon = mContext.getString(R.string.fa_mars);
            colorResId = R.color.cornflowerBlue;
        } else if ("f".equals(gender)) {
            icon = mContext.getString(R.string.fa_venus);
            colorResId = R.color.pink;
        } else {
            icon = mContext.getString(R.string.fa_genderless);
            colorResId = R.color.gainsboro;
        }
        textView.setText(icon);
        textView.setTextColor(mContext.getResources().getColor(colorResId));
        FontUtils.markAsIcon(mContext, textView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sender_photo)
        ImageView senderPhoto;
        @BindView(R.id.sender_name)
        TextView senderName;
        @BindView(R.id.sender_gender)
        TextView senderGender;
        @BindView(R.id.status_content)
        TextView statusContent;
        @BindView(R.id.status_pictures)
        GridView statusPictures;
        @BindView(R.id.status_date)
        TextView statusDate;
        @BindView(R.id.status_source)
        TextView statusSource;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GridAdapter extends BaseAdapter {

        private List<Status.Picture> pictures;

        public GridAdapter(@NonNull List<Status.Picture> pictures) {
            this.pictures = checkNotNull(pictures);
        }

        @Override
        public int getCount() {
            return pictures.size();
        }

        @Override
        public Object getItem(int position) {
            return pictures.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (GridViewHolder) convertView.getTag(R.id.glide_tag);
            } else {
                convertView = mLayoutInflater.inflate(R.layout.grid_item_picture, parent, false);
                viewHolder = new GridViewHolder(convertView);
                convertView.setTag(R.id.glide_tag, viewHolder);
            }
            adjustImageSize(viewHolder.photo);
            Status.Picture picture = (Status.Picture) getItem(position);
            Glide.with(mContext).load(picture.getThumbnail())
                    .placeholder(R.drawable.ic_photo_placeholder)
                    .into(viewHolder.photo);
            viewHolder.photo.setOnClickListener(view ->
                    PictureViewFragment.show(mFragmentManager, pictures, position, mGridColumnSize));
            return convertView;
        }

        private void adjustImageSize(ImageView view) {
            view.getLayoutParams().width = mGridColumnSize;
            view.getLayoutParams().height = mGridColumnSize;
        }

        class GridViewHolder {
            @BindView(R.id.status_photo)
            ImageView photo;

            public GridViewHolder(View itemView) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}

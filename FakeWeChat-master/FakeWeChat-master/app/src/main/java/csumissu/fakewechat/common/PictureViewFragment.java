package csumissu.fakewechat.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.Status;
import csumissu.fakewechat.listener.SimpleGlideListener;
import me.relex.circleindicator.CircleIndicator;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author sunyaxi
 * @date 2016/5/25
 */
public class PictureViewFragment extends DialogFragment {

    public static final String KEY_PICTURES = "key_pictures";
    public static final String KEY_SELECTED = "key_selected";
    public static final String KEY_THUMBNAIL = "key_thumbnail";

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;

    private Unbinder mUnbinder;

    public PictureViewFragment() {
        // Empty constructor required for DialogFragment
    }

    public static PictureViewFragment show(FragmentManager fragmentManager, List<Status.Picture> pictures,
                                           int selectedIndex, int thumbnailSize) {
        PictureViewFragment fragment = new PictureViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PICTURES, (Serializable) pictures);
        args.putInt(KEY_SELECTED, selectedIndex);
        args.putInt(KEY_THUMBNAIL, thumbnailSize);
        fragment.setArguments(args);
        fragment.show(fragmentManager, "picture_view_fragment");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoActionBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        List<Status.Picture> pictures = (List<Status.Picture>) args.getSerializable(KEY_PICTURES);
        int selectedIndex = args.getInt(KEY_SELECTED);
        int thumbnailSize = args.getInt(KEY_THUMBNAIL);

        View rootView = inflater.inflate(R.layout.fragment_picture_view, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        PicturePagerAdapter adapter = new PicturePagerAdapter(pictures);
        adapter.setThumbnailSize(thumbnailSize);
        mViewpager.setAdapter(adapter);
        mViewpager.setCurrentItem(selectedIndex);
        mIndicator.setViewPager(mViewpager);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    class PicturePagerAdapter extends PagerAdapter {

        private List<Status.Picture> iPictures;
        private LayoutInflater iLayoutInflater;
        private int iThumbnailSize;

        public PicturePagerAdapter(List<Status.Picture> pictures) {
            iPictures = pictures;
            iLayoutInflater = LayoutInflater.from(getContext());
        }

        public void setThumbnailSize(int thumbnailSize) {
            iThumbnailSize = thumbnailSize;
        }

        @Override
        public int getCount() {
            return iPictures.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View rootView = iLayoutInflater.inflate(R.layout.pager_picture_view, container, false);
            PhotoView middlePicture = (PhotoView) rootView.findViewById(R.id.middle_picture);
            ImageView thumbnailPicture = (ImageView) rootView.findViewById(R.id.thumbnail_picture);
            adjustImageSize(thumbnailPicture);
            View loadingContainer = rootView.findViewById(R.id.loading_container);
            Status.Picture picture = iPictures.get(position);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(middlePicture);
            attacher.setOnViewTapListener((view, v, v1) -> dismiss());
            Glide.with(getContext()).load(picture.getThumbnail()).dontAnimate().into(thumbnailPicture);
            Glide.with(getContext()).load(picture.getMiddle())
                    .listener(new SimpleGlideListener<String, GlideDrawable>() {
                        @Override
                        public void onSuccess() {
                            attacher.update();
                            loadingContainer.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            loadingContainer.setVisibility(View.GONE);
                        }
                    })
                    .error(R.drawable.ic_photo_load_failed)
                    .crossFade().into(middlePicture);
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void adjustImageSize(ImageView view) {
            if (iThumbnailSize > 0) {
                view.getLayoutParams().width = iThumbnailSize;
                view.getLayoutParams().height = iThumbnailSize;
            }
        }
    }
}

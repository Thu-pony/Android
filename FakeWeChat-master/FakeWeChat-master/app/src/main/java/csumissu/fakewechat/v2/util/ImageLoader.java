package csumissu.fakewechat.v2.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @author sunyaxi
 * @date 2016/8/10
 */
public class ImageLoader {

    public static void loadImage(Context context, ImageView view, String url) {
        loadImage(context, view, url, 0);
    }

    public static void loadImage(Context context, ImageView view, String url,
                                 @DrawableRes int placeholder) {
        loadImage(context, view, url, placeholder, 0);
    }

    public static void loadImage(Context context, ImageView view, String url,
                                 @DrawableRes int placeholder, @DrawableRes int error) {
        loadImage(context, view, url, placeholder, error, false);
    }

    public static void loadImage(Context context, ImageView view, String url,
                                 @DrawableRes int placeholder, @DrawableRes int error,
                                 boolean isCenterCrop) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
        } else {
            DrawableRequestBuilder builder = Glide.with(context).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholder)
                    .error(error);
            if (isCenterCrop) {
                builder.centerCrop();
            }
            builder.into(view);

        }
    }

}

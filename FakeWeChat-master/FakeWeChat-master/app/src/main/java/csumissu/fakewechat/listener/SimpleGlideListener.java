package csumissu.fakewechat.listener;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * @author sunyaxi
 * @date 2016/5/26
 */
public abstract class SimpleGlideListener<T, R> implements RequestListener<T, R> {

    @Override
    public boolean onException(Exception e, T model, Target<R> target, boolean isFirstResource) {
        onError();
        return false;
    }

    @Override
    public boolean onResourceReady(R resource, T model, Target<R> target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        onSuccess();
        return false;
    }

    public abstract void onSuccess();

    public abstract void onError();
}

package csumissu.fakewechat.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

import static csumissu.fakewechat.util.Preconditions.checkNotNull;

/**
 * @author sunyaxi
 * @date 2016/5/23
 */
public class FontUtils {

    private static final String FRONT_PATH = "fonts/fontawesome.ttf";
    private static Typeface sTypeface;

    public static void markAsIcon(@NonNull Context context, TextView textView) {
        checkNotNull(context);
        synchronized (context.getApplicationContext()) {
            if (sTypeface == null) {
                sTypeface = Typeface.createFromAsset(context.getAssets(), FRONT_PATH);
            }
        }
        if (textView != null) {
            textView.setTypeface(sTypeface);
        }
    }

}

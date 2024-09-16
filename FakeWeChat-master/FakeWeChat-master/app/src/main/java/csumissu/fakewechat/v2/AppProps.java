package csumissu.fakewechat.v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.dagger.ForApplication;
import csumissu.fakewechat.v2.util.JsonUtils;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class AppProps {

    private static final String KEY_OWNER = "owner";
    private static SharedPreferences sSharedPrefs;

    private AppProps() {
    }

    static void init(@ForApplication Context context) {
        sSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static User getOwner() {
        String userJson = sSharedPrefs.getString(KEY_OWNER, null);
        return JsonUtils.fromJson(userJson, User.class);
    }

    public static void saveOwner(User user) {
        if (user == null) {
            sSharedPrefs.edit().remove(KEY_OWNER).apply();
        } else {
            sSharedPrefs.edit().putString(KEY_OWNER, JsonUtils.toJson(user)).apply();
        }
    }

}

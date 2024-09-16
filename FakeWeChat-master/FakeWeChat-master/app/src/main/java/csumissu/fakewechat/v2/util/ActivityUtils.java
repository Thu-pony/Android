package csumissu.fakewechat.v2.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class ActivityUtils {


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}.
     * The operation is performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.replace(frameId, fragment);
        } else {
            transaction.add(frameId, fragment);
        }
        transaction.commit();
    }

    public static void removeFragmentFromActivity(@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public static void showFragment(@NonNull FragmentManager fragmentManager,
                                    @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(frameId, fragment);
        }
        transaction.show(fragment).commit();
    }

    public static void showFragment(@NonNull FragmentManager fragmentManager,
                                    @NonNull Fragment fragment, int frameId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(frameId, fragment, tag);
        }
        transaction.show(fragment).commit();
    }

    public static void hideFragment(@NonNull FragmentManager fragmentManager,
                                    @NonNull Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment).commit();
    }
}

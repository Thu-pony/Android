package csumissu.fakewechat.v2.model.remote;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import csumissu.fakewechat.v2.api.ApiConstants;
import csumissu.fakewechat.v2.api.ApiService;
import csumissu.fakewechat.v2.bean.FriendResult;
import csumissu.fakewechat.v2.bean.Status;
import csumissu.fakewechat.v2.bean.StatusResult;
import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.model.WeiboDataSource;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Singleton
public class WeiboRemoteDataSource implements WeiboDataSource {

    private ApiService mApiService;

    public WeiboRemoteDataSource(ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Observable<User> getOwner() {
        return mApiService.getUser(ApiConstants.ACCESS_TOKEN, ApiConstants.OWNER_UID);
    }

    @Override
    public Observable<List<User>> getFriends() {
        return mApiService.getFriends(ApiConstants.ACCESS_TOKEN, ApiConstants.OWNER_UID)
                .map(FriendResult::getUsers)
                .flatMap(new Func1<List<User>, Observable<User>>() {
                    @Override
                    public Observable<User> call(List<User> users) {
                        return Observable.from(users);
                    }
                })
                .map(user -> {
                    fillPinyin(user);
                    return user;
                })
                .sorted((u1, u2) -> u1.getPinyin().compareTo(u2.getPinyin()))
                .collect(ArrayList::new, List::add);
    }

    @Override
    public Observable<List<Status>> getAllStatuses() {
        return mApiService.getPublicStatuses(ApiConstants.ACCESS_TOKEN, ApiConstants.MAX_COUNT)
                .map(StatusResult::getStatuses);
    }

    @Override
    public Observable<List<Status>> getStatuses(int pageNum) {
        throw new UnsupportedOperationException("unsupported getting paged statuses");
    }

    private void fillPinyin(User user) {
        String pinyin = PinyinHelper.convertToPinyinString(
                user.getName().trim(), "", PinyinFormat.WITHOUT_TONE);
        user.setPinyin(pinyin);
    }
}

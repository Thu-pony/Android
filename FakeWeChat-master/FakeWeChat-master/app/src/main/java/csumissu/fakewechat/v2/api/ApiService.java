package csumissu.fakewechat.v2.api;

import csumissu.fakewechat.v2.bean.FriendResult;
import csumissu.fakewechat.v2.bean.StatusResult;
import csumissu.fakewechat.v2.bean.User;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public interface ApiService {

    @GET("2/users/show.json")
    Observable<User> getUser(@Query("access_token") String accessToken,
                             @Query("uid") long uid);

    /**
     * 只返回同样授权本应用的用户，非授权用户将不返回；
     * 例如一次调用count是50，但其中授权本应用的用户只有10条，则实际只返回10条；
     */
    @GET("2/friendships/friends.json")
    Observable<FriendResult> getFriends(@Query("access_token") String accessToken,
                                        @Query("uid") long uid);

    @GET("2/statuses/public_timeline.json")
    Observable<StatusResult> getPublicStatuses(@Query("access_token") String accessToken,
                                               @Query("count") int count);
}

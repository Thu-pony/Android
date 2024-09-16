package csumissu.fakewechat.data.source.remote;

import csumissu.fakewechat.data.FriendshipResult;
import csumissu.fakewechat.data.StatusResult;
import csumissu.fakewechat.data.User;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/5/20
 */
public interface WeiboApi {

    String ACCESS_TOKEN = "2.00CWl4eCEgoNVE3f356135f5yAeu6E";
    long OWNER_UID = 2428983302L;

    @GET("2/users/show.json")
    Observable<User> getUser(@Query("access_token") String accessToken,
                             @Query("uid") long uid);

    /**
     * 只返回同样授权本应用的用户，非授权用户将不返回；
     * 例如一次调用count是50，但其中授权本应用的用户只有10条，则实际只返回10条；
     */
    @GET("2/friendships/friends.json")
    Observable<FriendshipResult> getFriends(@Query("access_token") String accessToken,
                                            @Query("uid") long uid);

    @GET("2/statuses/public_timeline.json")
    Observable<StatusResult> getPublicStatuses(@Query("access_token") String accessToken,
                                               @Query("count") int count);

}

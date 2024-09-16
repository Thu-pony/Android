package csumissu.fakewechat.data.source;

import java.util.List;

import csumissu.fakewechat.data.FriendshipResult;
import csumissu.fakewechat.data.Status;
import csumissu.fakewechat.data.StatusResult;
import csumissu.fakewechat.data.User;
import rx.Observable;

/**
 * TODO: 应该将本地的保存写成不同的接口.因为RemoteDataSource和EntityRepository均用不到数据保存.
 * TODO: 应该将该数据拆分掉,根据不同的功能做不同的接口,方便扩展.
 *
 * @author sunyaxi
 * @date 2016/5/20
 */
public interface EntityDataSource {

    Observable<User> getOwner();

    void saveOwner(User user);

    Observable<FriendshipResult> getFriends();

    void saveFriends(FriendshipResult result);

    Observable<StatusResult> getStatusResult();

    void saveStatusResult(StatusResult statusResult);

    Observable<List<Status>> getStatuses(int count, int page);
}

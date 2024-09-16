package csumissu.fakewechat.v2.model;

import java.util.List;

import csumissu.fakewechat.v2.bean.Status;
import csumissu.fakewechat.v2.bean.User;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public interface WeiboDataSource {

    Observable<User> getOwner();

    Observable<List<User>> getFriends();

    Observable<List<Status>> getAllStatuses();

    Observable<List<Status>> getStatuses(int pageNum);

}

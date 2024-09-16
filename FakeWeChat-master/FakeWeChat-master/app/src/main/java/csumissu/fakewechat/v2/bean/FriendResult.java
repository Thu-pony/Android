package csumissu.fakewechat.v2.bean;

import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class FriendResult {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "FriendResult{" +
                "users=" + users +
                '}';
    }
}

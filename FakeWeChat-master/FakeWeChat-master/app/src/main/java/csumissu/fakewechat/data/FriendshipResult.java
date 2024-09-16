package csumissu.fakewechat.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/7/1
 */
public class FriendshipResult {

    @SerializedName("users")
    private List<User> users;
    @SerializedName("total_number")
    private int totalNumber;
    @SerializedName("previous_cursor")
    private long previousCursor;
    @SerializedName("next_cursor")
    private long nextCursor;
    private HashMap<Character, Integer> letterPositionMap = new HashMap<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public long getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(long previousCursor) {
        this.previousCursor = previousCursor;
    }

    public long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(long nextCursor) {
        this.nextCursor = nextCursor;
    }

    public HashMap<Character, Integer> getLetterPositionMap() {
        return letterPositionMap;
    }

    @Override
    public String toString() {
        return "FriendshipResult{" +
                "users=" + users +
                ", totalNumber=" + totalNumber +
                ", previousCursor=" + previousCursor +
                ", nextCursor=" + nextCursor +
                ", letterPositionMap=" + letterPositionMap +
                '}';
    }
}

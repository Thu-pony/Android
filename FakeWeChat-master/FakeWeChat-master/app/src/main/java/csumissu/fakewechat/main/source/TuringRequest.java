package csumissu.fakewechat.main.source;

import com.google.gson.annotations.SerializedName;

/**
 * @author sunyaxi
 * @date 2016/7/5
 */
public class TuringRequest {

    private String key = TuringRobotApi.API_KEY;
    @SerializedName("userid")
    private long userId = 123123123;
    private String info;
    private String loc;

    public TuringRequest(String info) {
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "TuringRequest{" +
                "key='" + key + '\'' +
                ", userId=" + userId +
                ", info='" + info + '\'' +
                ", loc='" + loc + '\'' +
                '}';
    }
}

package csumissu.fakewechat.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author sunyaxi
 * @date 2016/5/20
 */
public class User implements Serializable {

    @SerializedName("id")
    private long uid;
    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("profile_image_url")
    private String imageUrl;
    @SerializedName("gender")
    private String gender;
    @SerializedName("avatar_large")
    private String avatar;
    private String pinyin;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}

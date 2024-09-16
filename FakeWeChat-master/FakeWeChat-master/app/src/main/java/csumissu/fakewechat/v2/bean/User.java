package csumissu.fakewechat.v2.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import javax.inject.Inject;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Entity
public class User implements Parcelable {

    @Id
    private Long id;
    @NotNull
    private String name;
    @SerializedName("profile_image_url")
    private String imageUrl;
    private String gender;
    @SerializedName("avatar_large")
    private String avatarUrl;
    private String pinyin;

    @Inject
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public char getFirstLetter() {
        return this.pinyin.toUpperCase().charAt(0);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.gender);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.pinyin);
    }

    protected User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.gender = in.readString();
        this.avatarUrl = in.readString();
        this.pinyin = in.readString();
    }

    @Generated(hash = 1160459318)
    public User(Long id, @NotNull String name, String imageUrl, String gender,
            String avatarUrl, String pinyin) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.pinyin = pinyin;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

package csumissu.fakewechat.v2.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class Picture implements Parcelable {

    private long statusId;
    @SerializedName("thumbnail_pic")
    private String thumbnail;

    @Inject
    public Picture() {
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMiddle() {
        if (TextUtils.isEmpty(thumbnail)) {
            return null;
        }
        return thumbnail.replaceFirst("thumbnail", "bmiddle");
    }

    @Override
    public String toString() {
        return "Picture{" +
                "statusId=" + statusId +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.statusId);
        dest.writeString(this.thumbnail);
    }

    protected Picture(Parcel in) {
        this.statusId = in.readLong();
        this.thumbnail = in.readString();
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}

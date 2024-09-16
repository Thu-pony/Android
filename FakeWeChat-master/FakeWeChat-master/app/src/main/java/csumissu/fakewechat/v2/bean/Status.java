package csumissu.fakewechat.v2.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class Status implements Parcelable {

    private long id;
    private String text;
    private String source;
    @SerializedName("user")
    private User sender;
    @SerializedName("pic_urls")
    private List<Picture> picUrls;
    @SerializedName("created_at")
    private Date createTime;

    @Inject
    public Status() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<Picture> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<Picture> picUrls) {
        this.picUrls = picUrls;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", sender=" + sender +
                ", picUrls=" + picUrls +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.text);
        dest.writeString(this.source);
        dest.writeParcelable(this.sender, flags);
        dest.writeTypedList(this.picUrls);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
    }

    protected Status(Parcel in) {
        this.id = in.readLong();
        this.text = in.readString();
        this.source = in.readString();
        this.sender = in.readParcelable(User.class.getClassLoader());
        this.picUrls = in.createTypedArrayList(Picture.CREATOR);
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel source) {
            return new Status(source);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}

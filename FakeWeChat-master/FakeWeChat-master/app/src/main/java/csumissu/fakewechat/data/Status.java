package csumissu.fakewechat.data;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/5/20
 */
public class Status {

    @SerializedName("created_at")
    private Date createAt;
    @SerializedName("id")
    private long id;
    @SerializedName("text")
    private String text;
    @SerializedName("source")
    private String source;
    @SerializedName("user")
    private User sender;
    @SerializedName("pic_urls")
    private List<Picture> picUrls;

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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

    public class Picture implements Serializable {
        @SerializedName("thumbnail_pic")
        private String thumbnail;

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
                    "thumbnail='" + thumbnail + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Status{" +
                "createAt=" + createAt +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", sender=" + sender +
                ", picUrls=" + picUrls +
                '}';
    }
}

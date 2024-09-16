package csumissu.fakewechat.main.source;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author sunyaxiT
 * @date 2016/7/5
 */
public class TuringResponse {

    private int code;
    private String text;
    private String url;
    private List<Data> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TuringResponse{" +
                "code=" + code +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", list=" + list +
                '}';
    }

    public class Data {
        // For news
        private String article;
        private String source;
        // For food
        private String name;
        private String info;
        // common
        @SerializedName("detailurl")
        private String detailUrl;
        private String icon;

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "article='" + article + '\'' +
                    ", source='" + source + '\'' +
                    ", name='" + name + '\'' +
                    ", info='" + info + '\'' +
                    ", detailUrl='" + detailUrl + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

}

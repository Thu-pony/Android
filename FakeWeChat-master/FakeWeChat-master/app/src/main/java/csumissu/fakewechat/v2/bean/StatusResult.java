package csumissu.fakewechat.v2.bean;

import java.util.List;


/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class StatusResult {

    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return "StatusResult{" +
                "statuses=" + statuses +
                '}';
    }
}

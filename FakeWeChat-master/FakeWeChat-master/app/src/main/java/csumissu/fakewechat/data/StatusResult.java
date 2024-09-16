package csumissu.fakewechat.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/5/20
 */
public class StatusResult {

    @SerializedName("statuses")
    private List<Status> statuses;
    @SerializedName("total_number")
    private int totalNumber;
    @SerializedName("previous_cursor")
    private long previousCursor;
    @SerializedName("next_cursor")
    private long nextCursor;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
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

    @Override
    public String toString() {
        return "StatusResult{" +
                "statuses=" + statuses +
                ", totalNumber=" + totalNumber +
                ", previousCursor=" + previousCursor +
                ", nextCursor=" + nextCursor +
                '}';
    }
}

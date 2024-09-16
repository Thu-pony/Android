package csumissu.fakewechat.v2.msg;

/**
 * @author sunyaxi
 * @date 2016/11/20.
 */
public class ScrollStateEvent {

    private int state;

    public ScrollStateEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ScrollStateEvent{" +
                "state=" + state +
                '}';
    }
}

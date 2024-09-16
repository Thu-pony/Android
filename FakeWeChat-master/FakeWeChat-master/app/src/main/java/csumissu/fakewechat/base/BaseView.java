package csumissu.fakewechat.base;

/**
 * @author sunyaxi
 * @date 2016/5/23
 */
public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}

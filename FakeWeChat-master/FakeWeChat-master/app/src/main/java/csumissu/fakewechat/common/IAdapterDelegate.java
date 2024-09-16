package csumissu.fakewechat.common;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/6/28
 */
public interface IAdapterDelegate {

    int getViewType();

    boolean isForViewType(List items, int position);

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder);
}

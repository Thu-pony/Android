package csumissu.fakewechat.v2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
interface ITileTypeProvider {

    int getViewType();

    boolean isForViewType(List<Object> items, int position);

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(List<Object> items, int position, RecyclerView.ViewHolder holder);

    void onViewRecycled(RecyclerView.ViewHolder holder);
}

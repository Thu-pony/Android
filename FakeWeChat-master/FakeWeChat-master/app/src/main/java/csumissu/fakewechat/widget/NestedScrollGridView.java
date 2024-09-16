package csumissu.fakewechat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author sunyaxi
 * @date 2016/5/25
 */
public class NestedScrollGridView extends GridView {

    public NestedScrollGridView(Context context) {
        super(context);
    }

    public NestedScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

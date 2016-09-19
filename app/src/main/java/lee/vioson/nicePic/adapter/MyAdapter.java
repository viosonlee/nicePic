package lee.vioson.nicePic.adapter;

import android.content.Context;

import java.util.List;

import lee.vioson.nicePic.R;
import lee.vioson.nicePic.models.ListBean;
import lee.vioson.nicePic.views.ListItem;

/**
 * Author:李烽
 * Date:2016-06-03
 * FIXME
 * Todo
 */
public class MyAdapter extends BaseRecyclerAdapter<ListBean.TngouEntity> {

    public MyAdapter(Context context, List<ListBean.TngouEntity> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, final int position, final ListBean.TngouEntity tngouEntity) {
        final ListItem item = (ListItem) holder.getView(R.id.item);
        item.addData(tngouEntity);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.item_list;
    }


}
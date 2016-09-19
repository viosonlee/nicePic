package lee.vioson.nicePic.adapter;

import android.content.Context;

import java.util.List;

import lee.vioson.nicePic.R;
import lee.vioson.nicePic.views.ListItem;
import lee.vioson.nicePic.views.ListItem_H;
import lee.vioson.xiumm.models.ListData;

/**
 * Author:李烽
 * Date:2016-06-03
 * FIXME
 * Todo
 */
public class MyAdapter_1 extends BaseRecyclerAdapter<ListData> {
    public MyAdapter_1(Context context, List<ListData> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, final int position, final ListData tngouEntity) {
        final ListItem_H item = (ListItem_H) holder.getView(R.id.item);
        item.addData(tngouEntity);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.item_list;
    }


}
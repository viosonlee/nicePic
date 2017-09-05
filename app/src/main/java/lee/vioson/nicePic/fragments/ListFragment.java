package lee.vioson.nicePic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lee.vioson.nicePic.R;
import lee.vioson.nicePic.adapter.MyAdapter;
import lee.vioson.nicePic.adapter.MyAdapter_1;
import lee.vioson.nicePic.controls.DataCotroller;
import lee.vioson.nicePic.models.ListBean;
import lee.vioson.nicePic.views.MySwipeRefreshLayout;
import lee.vioson.utils.DebugLog;
import lee.vioson.widget.UpLoadRecyclerView;
import lee.vioson.xiumm.models.ListData;
import lee.vioson.xiumm.models.Type;
import lee.vioson.xiumm.utils.DataHelper;

/**
 * Author:李烽
 * Date:2016-06-02
 * FIXME
 * Todo
 */
public class ListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        UpLoadRecyclerView.OnLoadMoreCallBack {
    public static final String ID = "id";
//    @BindView(R.id.list)
    UpLoadRecyclerView list;
//    @BindView(R.id.root_view)
    MySwipeRefreshLayout rootView;
    //    private MySwipeRefreshLayout rootView;
//    private RecyclerView list;
    private int page = 1;
    private static final int DEFLUAT_ROW = 20;
    private static int row = DEFLUAT_ROW;
    private List<ListBean.TngouEntity> mData;
    private List<ListData> mData1 = new ArrayList<>();

    private MyAdapter adapter;
    private MyAdapter_1 adapter1;

    private boolean isNews = true;

    public static ListFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ID, id);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int id;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scrollListener = (OnListScrollListener) context;
        id = getArguments().getInt(ID);
        mData = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), mData);
        adapter1 = new MyAdapter_1(getActivity(), mData1);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic_list, container, false);
//        ButterKnife.bind(this, view);
        list = (UpLoadRecyclerView) view.findViewById(R.id.list);
        rootView = (MySwipeRefreshLayout) view.findViewById(R.id.root_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView.setOnRefreshListener(this);
//        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter1);
        list.setOnLoadMoreCallBack(this);
        list.setOnScrollListener(new UpLoadRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(UpLoadRecyclerView recyclerView, int newState) {
                scrollListener.onListScroll(list.getFirstVisiblePosition(),
                        list.getLastVisiblePosition(), list.getChildCount());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
//        loadData(true, isNews);
    }

    @Deprecated
    public void loadData(final boolean isRefresh, boolean isNews) {
        DataCotroller.getInstance().loadList(isNews, id, page, row, new DataCotroller.DataHandler<ListBean>() {
            @Override
            public void onDataBack(boolean isEmpty, ListBean listBean, String msg) {
                rootView.endRefresh();
                list.loadComplete();
                if (!isEmpty) {
                    upList(listBean, isRefresh);
                } else {
                    DebugLog.d(TAG, msg);
                }
            }
        });
    }

    public void loadData(final boolean isRefresh) {
        rootView.startRefresh();
        DataHelper.loadList(page, mType.href, new DataHelper.DataHandler<ArrayList<ListData>>() {
            @Override
            public void onDataBack(boolean isEmpty, ArrayList<ListData> listDatas) {
                rootView.endRefresh();
                list.loadComplete();
                if (!isEmpty) {
                    upList(listDatas, isRefresh);
                } else {
                    list.setHasMore(false);
                    Toast.makeText(getActivity(), "没有了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDocumentNull() {
                rootView.endRefresh();
                list.loadComplete();
                list.setHasMore(false);
            }
        });

    }

    private void upList(ArrayList<ListData> listDatas, boolean isRefresh) {
        if (listDatas == null || listDatas.size() == 0) {
            list.setHasMore(false);
        } else {
            if (isRefresh)
                mData1.clear();
            mData1.addAll(listDatas);
            adapter1.notifyDataSetChanged();
        }
    }


    public void upToFirst() {
//        if (adapter.getItemCount() > 0)
//            list.scrollToPosition(0);
        if (adapter1.getItemCount() > 0)
            list.scrollToPosition(0);
    }

    private OnListScrollListener scrollListener;

    public interface OnListScrollListener {
        void onListScroll(int firstVisiblePosition, int endVisiblePosition, int totalCount);
    }

    private void upList(ListBean listBean, boolean isRefresh) {
        if (listBean != null) {
            List<ListBean.TngouEntity> tngou = listBean.getTngou();
            if (!isNews) {
                if (tngou == null || tngou.size() == 0)
                    list.setHasMore(false);
                else {
                    if (isRefresh) mData.clear();
                    mData.addAll(tngou);
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (tngou.size() < row)
                    list.setHasMore(false);
                else {
                    mData.clear();
                    mData.addAll(tngou);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 切换类型
     *
     * @param id
     */
    public void swtihType(int id, boolean isNews) {
        this.id = id;
        page = 1;
        row = DEFLUAT_ROW;
        this.isNews = isNews;
        loadData(true, isNews);
    }

    private Type mType = new Type();

    public void swtihType(Type type) {
        mType = type;
        page = 1;
        list.setHasMore(true);
        row = DEFLUAT_ROW;
        loadData(true);
    }

    @Override
    public void onRefresh() {
        if (isNews)
            row = DEFLUAT_ROW;
        else
            page = 1;
//        loadData(true, isNews);
        loadData(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onLoadMore() {
//        if (isNews)
//            row = row + 10;
//        else
        page++;
        loadData(false);
//        loadData(false, isNews);
    }


}

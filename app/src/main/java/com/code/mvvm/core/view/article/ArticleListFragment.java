package com.code.mvvm.core.view.article;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.code.mvvm.base.BaseListFragment;
import com.code.mvvm.config.Constants;
import com.code.mvvm.core.data.pojo.article.ArticleVo;
import com.code.mvvm.core.vm.ArticleViewModel;
import com.code.mvvm.util.AdapterPool;
import com.mvvm.event.LiveBus;
import com.trecyclerview.multitype.MultiTypeAdapter;

/**
 * @author：tqzhang on 18/7/2 14:40
 */
public class ArticleListFragment extends BaseListFragment<ArticleViewModel> {

    private String typeId;

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    @Override
    protected Object getStateEventKey() {
        return Constants.EVENT_KEY_ARTICLE_LIST_STATE;
    }

    @Override
    protected String getStateEventTag() {
        return typeId;
    }

    @Override
    protected void dataObserver() {
        if (getArguments() != null) {
            typeId = getArguments().getString("type_id");
        }

        LiveBus.getDefault().subscribe(Constants.EVENT_KEY_ARTICLE_LIST,typeId, ArticleVo.class).observe(this, articleVo -> {
            if (articleVo != null) {
                lastId = articleVo.data.list.get(articleVo.data.list.size() - 1).newsid;
                setData(articleVo.data.list);
            }
        });
    }

    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
        getRemoteData();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected MultiTypeAdapter createAdapter() {
        return AdapterPool.newInstance().getArticleAdapter(activity);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        getRemoteData();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getRemoteData();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getArticleList(typeId, lastId);
    }
}
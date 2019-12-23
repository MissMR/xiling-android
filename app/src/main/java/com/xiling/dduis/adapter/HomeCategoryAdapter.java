package com.xiling.dduis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.DDCategoryActivity;
import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.bean.DDHomeBanner;
import com.xiling.ddui.tools.DLog;
import com.xiling.dduis.bean.DDProductBean;
import com.xiling.dduis.bean.HomeCategoryDataBean;
import com.xiling.dduis.custom.FilterLayoutView;
import com.xiling.dduis.viewholder.HomeBannerPageViewHolder;
import com.xiling.dduis.viewholder.NoDataViewHolder;
import com.xiling.dduis.viewholder.RushBuyViewHolder;
import com.xiling.dduis.viewholder.SpuProductViewHolder;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joker.pager.BannerPager;
import com.joker.pager.PagerOptions;
import com.joker.pager.holder.ViewHolderCreator;
import com.joker.pager.transformer.ScaleTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context = null;
    FilterLayoutView.FilterListener filterListener = null;
    ArrayList<HomeCategoryDataBean> data = new ArrayList<>();

    ArrayList<DDHomeBanner> banners = new ArrayList<>();
    ArrayList<CategoryBean> menus = new ArrayList<>();

    ArrayList<DDProductBean> spus = new ArrayList<>();

    //搜索筛选规则
    String subCategory = "";
    String minPrice = "";
    String maxPrice = "";
    int orderBy = -1;
    int orderType = 0;
    int isRush = 0;
    int isFreeShip = 0;

    public void setFilterData(
            String subCategory,
            String minPrice,
            String maxPrice,
            int orderBy,
            int orderType,
            int isRush,
            int isFreeShip) {
        this.subCategory = subCategory;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.orderBy = orderBy;
        this.orderType = orderType;
        this.isRush = isRush;
        this.isFreeShip = isFreeShip;
    }

    public HomeCategoryAdapter(Context context) {
        this.context = context;
    }

    public void setFilterListener(FilterLayoutView.FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public void setBannerData(ArrayList<DDHomeBanner> data) {
        banners = data;
    }

    public void setMenuData(ArrayList<CategoryBean> data) {
        menus.clear();
        if (data.size() > 8) {
            menus.addAll(data.subList(0, 8));
        } else {
            menus.addAll(data);
        }
    }

    public void setSpuData(ArrayList<DDProductBean> data) {
        this.spus = data;
    }

    public void appendSpuData(ArrayList<DDProductBean> data) {
        this.spus.addAll(data);
    }

    public int getSpuSize() {
        return this.spus.size();
    }

    public void showData() {

        data.clear();

        if (banners != null) {
            //计算轮播数量
            int bannerSize = banners.size();
            if (bannerSize > 0) {
                //存在轮播的时候才显示这个区域
                HomeCategoryDataBean banner = new HomeCategoryDataBean();
                banner.setType(HomeCategoryDataBean.Type.Banner);
                data.add(banner);
            }
        }

        if (menus != null) {
            int categorySize = menus.size();
            for (int i = 0; i < categorySize; i++) {
                HomeCategoryDataBean menu = new HomeCategoryDataBean();
                menu.setType(HomeCategoryDataBean.Type.Menu);
                menu.setObject(menus.get(i));
                data.add(menu);
            }
        }

        //筛选条
        HomeCategoryDataBean filter = new HomeCategoryDataBean();
        filter.setType(HomeCategoryDataBean.Type.Filter);
        data.add(filter);

        if (spus != null) {
            //商品数据
            int size = spus.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    DDProductBean bean = spus.get(i);
                    HomeCategoryDataBean item = new HomeCategoryDataBean();
                    if (bean.isFlashSale()) {
                        item.setType(HomeCategoryDataBean.Type.RUSHData);
                    } else {
                        item.setType(HomeCategoryDataBean.Type.SPUData);
                    }
                    item.setObject(bean);
                    data.add(item);
                }
            } else {
                //无数据提示
                HomeCategoryDataBean item = new HomeCategoryDataBean();
                item.setType(HomeCategoryDataBean.Type.No_Data);
                data.add(item);
            }

        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeCategoryDataBean item = data.get(position);
        if (item.getType() == HomeCategoryDataBean.Type.Banner) {
            BannerItemHolder itemHolder = (BannerItemHolder) holder;
            itemHolder.render();
        } else if (item.getType() == HomeCategoryDataBean.Type.Menu) {
            MenuItemHolder menuItemHolder = (MenuItemHolder) holder;
            if (item.getObject() instanceof CategoryBean) {
                CategoryBean bean = (CategoryBean) item.getObject();
                menuItemHolder.setData(bean);
                menuItemHolder.render();
            }
        } else if (item.getType() == HomeCategoryDataBean.Type.SPUData) {
            SpuProductViewHolder itemHolder = (SpuProductViewHolder) holder;
            if (item.getObject() instanceof DDProductBean) {
                DDProductBean bean = (DDProductBean) item.getObject();
                itemHolder.setData(bean);
            }
            itemHolder.setReloadListener(new SpuProductViewHolder.ReloadListener() {
                @Override
                public void onReload(int position) {
                    notifyItemChanged(position);
                }
            });
            itemHolder.render();
        } else if (item.getType() == HomeCategoryDataBean.Type.Filter) {
            FilterItemHolder itemHolder = (FilterItemHolder) holder;
            itemHolder.render();
        } else if (item.getType() == HomeCategoryDataBean.Type.RUSHData) {
            RushBuyViewHolder rushHolder = (RushBuyViewHolder) holder;
            if (item.getObject() instanceof DDProductBean) {
                DDProductBean bean = (DDProductBean) item.getObject();
                rushHolder.setData(bean);
            }
            rushHolder.setReloadListener(new RushBuyViewHolder.ReloadListener() {
                @Override
                public void onReload(int position) {
                    notifyItemChanged(position);
                }
            });
            rushHolder.render();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HomeCategoryDataBean.Type.Menu.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_menu, parent, false);
            return new MenuItemHolder(view);
        } else if (viewType == HomeCategoryDataBean.Type.Filter.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_filter, parent, false);
            return new FilterItemHolder(view);
        } else if (viewType == HomeCategoryDataBean.Type.SPUData.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_data, parent, false);
            return new SpuProductViewHolder(view);
        } else if (viewType == HomeCategoryDataBean.Type.Banner.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_recommend_banner, parent, false);
            return new BannerItemHolder(view);
        } else if (viewType == HomeCategoryDataBean.Type.No_Data.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_no_data, parent, false);
            return new NoDataViewHolder(view);
        } else if (viewType == HomeCategoryDataBean.Type.RUSHData.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_rush, parent, false);
            return new RushBuyViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        HomeCategoryDataBean row = data.get(position);
        return row.getType().ordinal();
    }

    //轮播区域
    public class BannerItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_banner)
        LinearLayout bannerLayout;

        @BindView(R.id.banner_pager0)
        BannerPager bannerPager;

        ArrayList<String> bannerUrls = new ArrayList<>();
        PagerOptions bannerOption;

        public BannerItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int padding = ConvertUtil.dip2px(8);
            int preWidth = ConvertUtil.dip2px(15);
            bannerOption = new PagerOptions.Builder(context)
                    .setTurnDuration(5000)
                    .setPagePadding(padding)
                    .setPrePagerWidth(preWidth)
                    .setIndicatorVisibility(View.GONE)
                    .setPageTransformer(new ScaleTransformer())
                    .setIndicatorAlign(RelativeLayout.CENTER_IN_PARENT)
                    .build();
            bannerPager.setOnItemClickListener(new com.joker.pager.listener.OnItemClickListener() {
                @Override
                public void onItemClick(int location, int position) {
                    if (position < banners.size()) {
                        DDHomeBanner banner = banners.get(position);
                        String event = banner.getEvent();
                        String target = banner.getTarget();
                        DDHomeBanner.process(context, event, target);
                    }
                }
            });
        }

        void render() {
            bannerUrls.clear();
            for (DDHomeBanner item : banners) {
                bannerUrls.add(item.getImgUrl());
            }
            //设置BannerPager
            bannerPager.setPagerOptions(bannerOption)
                    .setPages(bannerUrls, new ViewHolderCreator<HomeBannerPageViewHolder>() {
                        @Override
                        public HomeBannerPageViewHolder createViewHolder() {
                            final View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_recommend_banner, null);
                            return new HomeBannerPageViewHolder(view);
                        }
                    });

            bannerPager.startTurning();
        }
    }


    //菜单区域
    class MenuItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sd_menu)
        SimpleDraweeView sdMenu;

        @BindView(R.id.tv_menu)
        TextView tvMenu;

        CategoryBean data = null;

        @OnClick(R.id.layout_menu)
        void onMenuPressed() {
            if (data != null) {
                //跳转到二级分类列表
                int index = menus.indexOf(data);
                if (index > -1) {
                    DDCategoryActivity.jumpTo(context, data.getCategoryId(), data.getCategoryName(), index);
                } else {
                    DDCategoryActivity.jumpTo(context, data.getCategoryId(), data.getCategoryName());
                }
            } else {
                DLog.d("onMenuPressed data is null.");
            }
        }

        public MenuItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(CategoryBean data) {
            this.data = data;
        }

        public void render() {
            if (data != null) {
                sdMenu.setImageURI("" + data.getIconUrl());
                tvMenu.setText("" + data.getCategoryName());
            } else {
                DLog.i("render data is null.");
            }
        }
    }

    //筛选区域
    class FilterItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_filter_and_sort)
        LinearLayout filterAndSortLayout = null;

        FilterLayoutView filterLayoutView = null;

        public FilterItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            filterLayoutView = new FilterLayoutView(filterAndSortLayout);
            filterLayoutView.setCategorys(menus);
            filterLayoutView.setListener(filterListener);
            filterLayoutView.showTopLine();
        }

        public void render() {
            filterLayoutView.setFilterData(subCategory, minPrice, maxPrice, orderBy, orderType, isRush, isFreeShip);
            filterLayoutView.setVipFlag(SessionUtil.getInstance().isMaster());
        }

    }


}

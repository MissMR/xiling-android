package com.dodomall.ddmall.dduis.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.bean.DDHomeBanner;
import com.dodomall.ddmall.ddui.config.UIConfig;
import com.dodomall.ddmall.ddui.custom.DDTagView;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.dduis.activity.RushListActivity;
import com.dodomall.ddmall.dduis.base.AvatarDemoMaker;
import com.dodomall.ddmall.dduis.base.BackgroundMaker;
import com.dodomall.ddmall.dduis.base.RushTimerManager;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.dduis.bean.DDRushHeaderBean;
import com.dodomall.ddmall.dduis.bean.DDRushSpuBean;
import com.dodomall.ddmall.dduis.bean.HomeRecommendBean;

import com.dodomall.ddmall.dduis.viewholder.HomeBannerPageViewHolder;
import com.dodomall.ddmall.dduis.viewholder.HomeBannerViewHolder;
import com.dodomall.ddmall.dduis.viewholder.HomeWhiteLineViewHolder;
import com.dodomall.ddmall.dduis.viewholder.RushBuyViewHolder;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.Tag;
import com.dodomall.ddmall.shared.component.dialog.DDMProductQrCodeDialog;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.DDHomeService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joker.pager.BannerPager;
import com.joker.pager.PagerOptions;
import com.joker.pager.holder.ViewHolderCreator;
import com.joker.pager.transformer.ScaleTransformer;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface HomeRecommendDataListener {
        /*抢购变更*/
        void onRushHeaderChanged(int position, DDRushHeaderBean header);
    }

    Context context = null;

    //首页事件需要传给Fragment处理的
    HomeRecommendDataListener listener = null;

    //整体数据
    ArrayList<HomeRecommendBean> data = new ArrayList<>();
    //轮播数据
    ArrayList<DDHomeBanner> banners = new ArrayList<>();
    //菜单数据
    ArrayList<DDHomeBanner> menus = new ArrayList<>();
    //活动数据
    ArrayList<DDHomeBanner> events = new ArrayList<>();
    //抢购分类数据
    ArrayList<DDRushHeaderBean> rushTypeDatas = new ArrayList<>();
    //抢购数据
    ArrayList<DDRushSpuBean> rushDatas = new ArrayList<>();
    //今日必推数据
    ArrayList<DDProductBean> products = new ArrayList<>();

    //618通栏图片
    DDHomeBanner event618 = null;


    //抢购状态
    int rushStatus = 0;
    //当前选中的抢购分类
    private int nowRushType = -1;
    int defaultIndex = 0;
    //抢购计时器
    RushTimerManager timerManager = null;

    public HomeRecommendAdapter(Context context) {
        this.context = context;
        timerManager = new RushTimerManager();
    }

    public void setEvent618(DDHomeBanner event618) {
        this.event618 = event618;
    }

    public void clearNowRushType() {
        this.nowRushType = -1;
    }

    public void setNowRushType(int position) {
        this.nowRushType = position;
    }

    public void setListener(HomeRecommendDataListener listener) {
        this.listener = listener;
    }

    public void setBanners(ArrayList<DDHomeBanner> banners) {
        this.banners = banners;
    }

    public void setMenus(ArrayList<DDHomeBanner> menus) {
        this.menus = menus;
    }

    public void setEvents(ArrayList<DDHomeBanner> events) {
        this.events = events;
    }

    public void setRushTypeData(ArrayList<DDRushHeaderBean> data) {
        this.rushTypeDatas = data;
        //取出默认的选中
        if (nowRushType < 0) {
            defaultIndex = 0;
            for (DDRushHeaderBean headerBean : rushTypeDatas) {
                if (headerBean.isSelected()) {
                    nowRushType = defaultIndex;
                    break;
                }
                defaultIndex++;
            }
        }
    }

    public void setRushData(ArrayList<DDRushSpuBean> data) {
        this.rushDatas = data;
    }

    public void setProducts(ArrayList<DDProductBean> products) {
        this.products = products;
    }

    public ArrayList<DDProductBean> getProducts() {
        return products;
    }

    public void appendProducts(ArrayList<DDProductBean> products) {
        this.products.addAll(products);
    }

    //合并数据进行展示
    public void showData() {

        data.clear();

        if (banners != null) {
            //有Banner构造Banner
            if (banners.size() > 0) {
                HomeRecommendBean banner = new HomeRecommendBean();
                banner.setType(HomeRecommendBean.RecommendType.Banner);
                data.add(banner);
            }
        }

        if (menus != null) {
            //填充菜单数据
            int menuSize = menus.size();
            for (int i = 0; i < menuSize; i++) {
                HomeRecommendBean menu = new HomeRecommendBean();
                menu.setType(HomeRecommendBean.RecommendType.Menu);
                menu.setObject(menus.get(i));
                data.add(menu);
                DLog.i("menu " + i + ":" + menus.get(i).getTarget());
            }

            //空白行
            HomeRecommendBean empty = new HomeRecommendBean();
            empty.setType(HomeRecommendBean.RecommendType.EmptyView);
            data.add(empty);
        }

        if (event618 != null) {
            //618
            HomeRecommendBean e618 = new HomeRecommendBean();
            e618.setType(HomeRecommendBean.RecommendType.Event618);
            e618.setObject(event618);
            data.add(e618);
        }

        //活动区域必须满足大于3的条件，否则不显示
        if (events != null && events.size() > 2) {
            HomeRecommendBean event = new HomeRecommendBean();
            event.setType(HomeRecommendBean.RecommendType.Event);
            data.add(event);
        }

        if (rushTypeDatas != null) {
            //只有抢购数据大于0的时候才显示
            int rushHeaderSize = rushTypeDatas.size();
            DLog.i("rushHeaderSize:" + rushHeaderSize);
            if (rushHeaderSize > 0) {
                HomeRecommendBean rushHeader = new HomeRecommendBean();
                rushHeader.setType(HomeRecommendBean.RecommendType.RushHeader);
                data.add(rushHeader);

                int rushSize = rushDatas.size();
                if (rushSize > 0) {
                    for (int i = 0; i < rushDatas.size(); i++) {
                        HomeRecommendBean rush = new HomeRecommendBean();
                        rush.setType(HomeRecommendBean.RecommendType.Rush);
                        rush.setObject(rushDatas.get(i));
                        data.add(rush);
                    }

                    HomeRecommendBean rushFooter = new HomeRecommendBean();
                    rushFooter.setType(HomeRecommendBean.RecommendType.RushFooter);
                    data.add(rushFooter);
                }
            }
        }

        if (products != null) {
            int productSize = products.size();
            DLog.i("productSize:" + productSize);
            //只有商品数量大于0的时候显示今日必推
            if (productSize > 0) {
                //插入头部
                HomeRecommendBean todayHeader = new HomeRecommendBean();
                todayHeader.setType(HomeRecommendBean.RecommendType.TodayHeader);
                data.add(todayHeader);
                //增加商品数据
                for (int i = 0; i < productSize; i++) {
                    HomeRecommendBean today = new HomeRecommendBean();
                    today.setType(HomeRecommendBean.RecommendType.Today);
                    today.setObject(products.get(i));
                    data.add(today);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        HomeRecommendBean.RecommendType type = HomeRecommendBean.RecommendType.values()[viewType];

        if (type == HomeRecommendBean.RecommendType.RushHeader) {
            //抢购 - 头部
            View view = inflater.inflate(R.layout.layout_home_rush_header, parent, false);
            view.setContentDescription("" + type);
            return new HomeRecommendAdapter.RushHeaderItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.Rush) {
            //抢购 - 数据
            View view = inflater.inflate(R.layout.adapter_home_rush, parent, false);
            view.setContentDescription("" + type);
            return new RushBuyViewHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.RushFooter) {
            //抢购 - 底部
            View view = inflater.inflate(R.layout.adapter_home_rush_footer, parent, false);
            view.setContentDescription("" + type);
            return new HomeRecommendAdapter.RushFooterItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.TodayHeader) {
            //今日必推 - 头部
            View view = inflater.inflate(R.layout.adapter_recommend_today_header, parent, false);
            view.setContentDescription("" + type);
            return new TodayHeaderItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.Today) {
            //今日必推 - 数据
            View view = inflater.inflate(R.layout.adapter_recommend_today, parent, false);
            view.setContentDescription("" + type);
            return new TodayItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.Banner) {
            //轮播
            View view = inflater.inflate(R.layout.layout_home_recommend_banner, parent, false);
            view.setContentDescription("" + type);
            return new BannerItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.Menu) {
            //菜单
            View view = inflater.inflate(R.layout.layout_home_category_menu, parent, false);
            view.setContentDescription("" + type);
            return new MenuItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.Event618) {
            //618 活动
            View view = inflater.inflate(R.layout.layout_home_recommend_event618, parent, false);
            view.setContentDescription("" + type);
            return new Event618ItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.Event) {
            //活动
            View view = inflater.inflate(R.layout.layout_home_recommend_event, parent, false);
            view.setContentDescription("" + type);
            return new EventItemHolder(view);
        } else if (type == HomeRecommendBean.RecommendType.EmptyView) {
            View view = inflater.inflate(HomeWhiteLineViewHolder.layoutId, parent, false);
            view.setContentDescription("" + type);
            return new HomeWhiteLineViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        HomeRecommendBean item = data.get(position);
        return item.getType().ordinal();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeRecommendBean item = data.get(position);
        HomeRecommendBean.RecommendType type = item.getType();
        if (type == HomeRecommendBean.RecommendType.Banner) {
            //轮播图
            BannerItemHolder bannerItemHolder = (BannerItemHolder) holder;
            bannerItemHolder.render();
        } else if (type == HomeRecommendBean.RecommendType.Menu) {
            //5菜单
            MenuItemHolder menuItemHolder = (MenuItemHolder) holder;
            HomeRecommendBean rowInfo = data.get(position);
            if (rowInfo.getObject() instanceof DDHomeBanner) {
                DDHomeBanner banner = (DDHomeBanner) rowInfo.getObject();
                menuItemHolder.setData(banner);
            }
            menuItemHolder.render();
        } else if (type == HomeRecommendBean.RecommendType.Event) {
            //3活动
            EventItemHolder eventItemHolder = (EventItemHolder) holder;
            eventItemHolder.render();
        } else if (type == HomeRecommendBean.RecommendType.RushHeader) {
            //抢购头部
            RushHeaderItemHolder rushHeaderItemHolder = (RushHeaderItemHolder) holder;
            rushHeaderItemHolder.loadData();
        } else if (type == HomeRecommendBean.RecommendType.Rush) {
            //抢购数据
            RushBuyViewHolder rushItemHolder = (RushBuyViewHolder) holder;
            HomeRecommendBean rowInfo = data.get(position);
            if (rowInfo.getObject() instanceof DDRushSpuBean) {
                DDRushSpuBean itemData = (DDRushSpuBean) rowInfo.getObject();
                rushItemHolder.setData(itemData);
            } else {
                DLog.e("item is null.");
            }
            rushItemHolder.setReloadListener(new RushBuyViewHolder.ReloadListener() {
                @Override
                public void onReload(int position) {
                    DLog.i("recommend reload:" + position);
                    notifyItemChanged(position);
                }
            });
            rushItemHolder.setStatus(rushStatus);
            rushItemHolder.render();
        } else if (type == HomeRecommendBean.RecommendType.Today) {
            //今日必推数据
            TodayItemHolder todayItemHolder = (TodayItemHolder) holder;
            HomeRecommendBean rowInfo = data.get(position);
            if (rowInfo.getObject() instanceof DDProductBean) {
                DDProductBean product = (DDProductBean) rowInfo.getObject();
                todayItemHolder.setData(product);
            }
            todayItemHolder.render();
        } else if (type == HomeRecommendBean.RecommendType.TodayHeader) {
            TodayHeaderItemHolder headerItemHolder = (TodayHeaderItemHolder) holder;
            headerItemHolder.render();
        } else if (type == HomeRecommendBean.RecommendType.Event618) {
            Event618ItemHolder event618ItemHolder = (Event618ItemHolder) holder;
            event618ItemHolder.setData(event618);
            event618ItemHolder.render();
        }
    }

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

    public class MenuItemHolder extends RecyclerView.ViewHolder {

        DDHomeBanner data = null;

        @BindView(R.id.sd_menu)
        SimpleDraweeView sdMenu;

        @BindView(R.id.tv_menu)
        TextView tvMenu;

        public MenuItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(DDHomeBanner data) {
            this.data = data;
        }

        public void render() {
            if (data != null) {
                String url = data.getImgUrl();
//                sdMenu.setImageURI(url);
                UITools.setGifUrl(sdMenu, url);

                String title = data.getTitle();
                tvMenu.setText("" + title);
            }
        }

        @OnClick(R.id.layout_menu)
        void onMenuPressed() {
            DLog.i("onMenuPressed" + data.getTarget());
            String event = data.getEvent();
            String target = data.getTarget();
            DDHomeBanner.process(context, event, target);
        }
    }

    public class Event618ItemHolder extends RecyclerView.ViewHolder {

        DDHomeBanner data = null;

        @BindView(R.id.sd_menu)
        SimpleDraweeView sdMenu;

        public Event618ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(DDHomeBanner data) {
            this.data = data;
        }

        public void render() {
            if (data != null) {
                String url = data.getImgUrl();
                UITools.setGifUrl(sdMenu, url);
            }
        }

        @OnClick(R.id.layout_menu)
        void onMenuPressed() {
            DLog.i("onMenuPressed" + data.getTarget());
            String event = data.getEvent();
            String target = data.getTarget();
            DDHomeBanner.process(context, event, target);
        }
    }

    public class EventItemHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.btn_event01, R.id.btn_event02, R.id.btn_event03})
        List<SimpleDraweeView> sdEvents;

        DDHomeBanner event0, event1, event2;

        public EventItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void render() {
            if (events.size() > 2) {
                event0 = events.get(0);
                event1 = events.get(1);
                event2 = events.get(2);

//                sdEvents.get(0).setImageURI(event0.getImgUrl());
//                sdEvents.get(1).setImageURI(event1.getImgUrl());
//                sdEvents.get(2).setImageURI(event2.getImgUrl());

                UITools.setGifUrl(sdEvents.get(0), event0.getImgUrl());
                UITools.setGifUrl(sdEvents.get(1), event1.getImgUrl());
                UITools.setGifUrl(sdEvents.get(2), event2.getImgUrl());
            }
        }

        @OnClick({R.id.btn_event01, R.id.btn_event02, R.id.btn_event03})
        void onEventPressed(View view) {
            String event = "";
            String target = "";
            switch (view.getId()) {
                case R.id.btn_event01:
                    DLog.i("点击了活动1");
                    if (event0 != null) {
                        event = event0.getEvent();
                        target = event0.getTarget();
                    }
                    break;
                case R.id.btn_event02:
                    DLog.i("点击了活动2");
                    if (event1 != null) {
                        event = event1.getEvent();
                        target = event1.getTarget();
                    }
                    break;
                case R.id.btn_event03:
                    DLog.i("点击了活动3");
                    if (event2 != null) {
                        event = event2.getEvent();
                        target = event2.getTarget();
                    }
                    break;
                default:
                    event = "";
                    target = "";
                    break;
            }
            DDHomeBanner.process(context, event, target);
        }

    }

    public class RushHeaderItemHolder extends RecyclerView.ViewHolder implements RushTimerManager.RushTimerListener {

        @BindView(R.id.rush_buy_magic_indicator)
        MagicIndicator rushMagicIndicator;

        @BindView(R.id.sd_rush_ad)
        SimpleDraweeView rushAdView;

        @BindView(R.id.tv_time_tips)
        TextView timeTipsTextView;

        @BindViews({R.id.tv_hour, R.id.tv_minute, R.id.tv_second})
        TextView[] times;

        @BindViews({R.id.tv_minute_dot, R.id.tv_second_dot})
        TextView[] dots;

        CommonNavigator commonNavigator = null;
        private ArrayList<String> mTabTitles = new ArrayList<>();

        public RushHeaderItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public boolean canAdjust() {
            int width = ConvertUtil.dip2px(100);
            int maxWidth = mTabTitles.size() * width;
            int screenWidth = UITools.getScreenWidth(context);
            DLog.i("screenWidth:" + screenWidth + ",itemCount:" + mTabTitles + ",maxWidth:" + maxWidth);
            return screenWidth >= maxWidth;
        }

        public void initView() {
            DLog.i("抢购分类头部初始化");
            commonNavigator = new CommonNavigator(context);
            //自动适配屏幕的逻辑
            commonNavigator.setAdjustMode(canAdjust());
            commonNavigator.setFollowTouch(true);
            commonNavigator.setSkimOver(false);
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return rushTypeDatas.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int index) {

                    CommonPagerTitleView titleView = new CommonPagerTitleView(context);
                    titleView.setContentView(R.layout.layout_rush_pager);

                    final LinearLayout layoutRush = titleView.findViewById(R.id.layout_rush);
                    final TextView tvTime = titleView.findViewById(R.id.tv_time);
                    final TextView tvStatus = titleView.findViewById(R.id.tv_status);

                    DDRushHeaderBean bean = rushTypeDatas.get(index);
                    //设置抢购时间
                    tvTime.setText("" + bean.getFormatTimeText());
                    //设置抢购状态
                    tvStatus.setText("" + bean.getStatusText(index == defaultIndex));

                    final View vDown = titleView.findViewById(R.id.v_down);
                    layoutRush.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (index != nowRushType) {

                                rushMagicIndicator.onPageSelected(index);
                                commonNavigator.notifyDataSetChanged();
                                nowRushType = index;

                                if (listener != null) {
                                    DDRushHeaderBean header = rushTypeDatas.get(index);
                                    listener.onRushHeaderChanged(index, header);
                                }

                            }
                        }
                    });
                    titleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                        @Override
                        public void onSelected(int index, int totalCount) {
                            vDown.setVisibility(View.VISIBLE);
                            DDRushHeaderBean rushHeaderBean = rushTypeDatas.get(index);
                            if (rushHeaderBean.isRushEnable()) {
                                layoutRush.setBackgroundColor(UIConfig.COLOR_RUSH_VIP);
                                BackgroundMaker.setTriangleDrawableColor(vDown, UIConfig.COLOR_RUSH_VIP);
                            } else {
                                layoutRush.setBackgroundColor(UIConfig.COLOR_RUSH_USER);
                                BackgroundMaker.setTriangleDrawableColor(vDown, UIConfig.COLOR_RUSH_USER);
                            }
                            tvTime.setTextColor(Color.WHITE);
                            tvStatus.setTextColor(Color.WHITE);
                        }

                        @Override
                        public void onDeselected(int index, int totalCount) {
                            layoutRush.setBackgroundColor(Color.WHITE);
                            vDown.setVisibility(View.INVISIBLE);
                            tvTime.setTextColor(Color.BLACK);
                            tvStatus.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                        }

                        @Override
                        public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                        }
                    });
                    return titleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    return null;
                }
            });
            rushMagicIndicator.setNavigator(commonNavigator);

            rushMagicIndicator.onPageSelected(nowRushType);
            commonNavigator.notifyDataSetChanged();

            //开始当前选中项目的倒计时
            DDRushHeaderBean headerBean = rushTypeDatas.get(nowRushType);
            if (timerManager != null) timerManager.cancel();
            timerManager.update(headerBean.getStartTime(), headerBean.getEndTime());
            timerManager.setListener(this);

        }

        @Override
        public void onRushTimer(int status, String hour, String minute, String second) {
//                    DLog.i("onRushTimer:" + status + "->" + hour + ":" + minute + ":" + second);
            if (status != rushStatus) {
                //状态改变的时候进行布局刷新
                notifyDataSetChanged();
                commonNavigator.notifyDataSetChanged();
            }
            //改变状态
            rushStatus = status;
            //设置时分秒
            times[0].setText(hour);
            times[1].setText(minute);
            times[2].setText(second);
        }

        public void loadData() {
            DLog.i("抢购分类数据加载");
            DLog.i("共有抢购分类:" + rushTypeDatas.size());
            mTabTitles.clear();

            for (DDRushHeaderBean bean : rushTypeDatas) {
                mTabTitles.add(bean.getFormatTimeText());
            }

            //初始化控件
            initView();

            DDRushHeaderBean header = rushTypeDatas.get(nowRushType);
            if (header.isRushEnable()) {
                rushAdView.setImageResource(R.mipmap.img_rush_vip_tips);
                changeStyle(UIConfig.COLOR_RUSH_VIP);
                timeTipsTextView.setText("距结束");
//                timeTipsTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
            } else {
                rushAdView.setImageResource(R.mipmap.img_rush_user_tips);
                changeStyle(UIConfig.COLOR_RUSH_USER);
                timeTipsTextView.setText("距开始");
//                timeTipsTextView.setTextColor(UIConfig.COLOR_RUSH_USER);
            }

            timerManager.fire();
        }

        public void changeStyle(int color) {
            //设置倒计时背景 - 样式
            Drawable drawable = new BackgroundMaker(context).getHomeTimeBackground(color);
            times[0].setBackground(drawable);
            times[1].setBackground(drawable);
            times[2].setBackground(drawable);
            //设置时间分割点 - 样式
            dots[0].setTextColor(color);
            dots[1].setTextColor(color);
        }
    }

    public class RushFooterItemHolder extends RecyclerView.ViewHolder {

        @OnClick(R.id.btn_show_all)
        void onShowAllPressed() {
            RushListActivity.jumpToFullList(context, rushTypeDatas.get(nowRushType).getFlashSaleId());
        }

        public RushFooterItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class TodayHeaderItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_header)
        ImageView ivHeader;

        public TodayHeaderItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void render() {
            boolean status = SessionUtil.getInstance().isMaster();
            ivHeader.setBackgroundResource(status ? R.mipmap.icon_home_recommend_header_vip : R.mipmap.icon_home_recommend_header_user);
        }
    }

    public class TodayItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sd_thumb)
        SimpleDraweeView sdThumb;

        @BindView(R.id.spuStatusView)
        TextView spuStatusView;

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.layout_tag)
        LinearLayout tagLayout;

        @BindView(R.id.tv_sale_count)
        TextView saleCountTextView;

        @BindViews({R.id.sd_avatar_01, R.id.sd_avatar_02, R.id.sd_avatar_03})
        SimpleDraweeView[] avatars;

        @BindView(R.id.layout_price_user)
        RelativeLayout userPriceLayout;

        @BindView(R.id.tv_price_now)
        TextView userPriceTextView;

        @BindView(R.id.tv_price)
        TextView marketPriceTextView;

        @BindView(R.id.layout_price_vip)
        RelativeLayout vipPriceLayout;

        @BindView(R.id.tv_price_vip)
        TextView vipPriceTextView;

        @BindView(R.id.tv_price_reward)
        TextView rewardPriceTextView;

        boolean isVip = false;

        DDProductBean data = null;

        @BindView(R.id.btn_store)
        Button storeButton;

        @BindView(R.id.btn_buy)
        Button buyButton;

        @OnClick(R.id.btn_store)
        void onStorePressed() {
            //上下架
            DLog.i("onStorePressed:上/下架");
        }

        @OnClick(R.id.btn_share)
        void onSharePressed() {
            //推广
            DLog.i("onSharePressed:推广");

            SkuInfo skuInfo = SkuInfo.fromRecommendData(data);
            DDMProductQrCodeDialog dialog = new DDMProductQrCodeDialog((Activity) context, skuInfo, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    DLog.i("onStart:" + share_media);
                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    DLog.i("onResult:" + share_media);
                    addShareCount();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    DLog.i("onError:" + share_media);
                    throwable.printStackTrace();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    DLog.i("onCancel:" + share_media);
                }
            });
            dialog.show();
        }

        void addShareCount() {
            DDHomeService homeService = ServiceManager.getInstance().createService(DDHomeService.class);
            APIManager.startRequest(homeService.addShareCount(data.getProductId()), new RequestListener<Object>() {

                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Object result) {
                    super.onSuccess(result);
                    data.addExtendTime();
                    notifyItemChanged(getAdapterPosition());
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.error(e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });

        }

        @OnClick(R.id.btn_buy)
        void onBuyPressed() {
            DLog.i("onBuyPressed:购买");
            onRowPressed();
        }

        @OnClick(R.id.layout_row)
        void onRowPressed() {
            if (data != null) {
                DDProductDetailActivity.start(context, data.getProductId());
            } else {
                ToastUtil.error(ToastUtil.ERR_DATA);
            }
        }

        public TodayItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //添加删除线
            UITools.addTextViewDeleteLine(marketPriceTextView);

            //TIPS 2.0版本，移除小店功能
            storeButton.setVisibility(View.GONE);
        }

        public void setData(DDProductBean data) {
            this.data = data;
        }

        private void clearProductTagView() {
            tagLayout.removeAllViews();
        }

        private void addItemProductTagView(String category) {
            DDTagView ddTagView = (DDTagView) LayoutInflater.from(context)
                    .inflate(R.layout.item_product_category_tag, tagLayout, false);
            if (TextUtils.isEmpty(category)) {
                return;
            }

            if (category.contains("#")) {
                ddTagView.setText(category.split("#")[0]);
                ddTagView.setTagBackground("#" + category.split("#")[1]);
            } else {
                ddTagView.setText(category);
            }

            tagLayout.addView(ddTagView);
        }

        /**
         * 渲染界面
         */
        public void render() {

            isVip = SessionUtil.getInstance().isMaster();

            //设置商品标签
            clearProductTagView();
            if (!data.getTags().isEmpty()) {
                for (Tag t : data.getTags()) {
                    addItemProductTagView(t.name);
                }
            }

            DLog.i("thumb-" + this.getAdapterPosition() + ":" + data.getThumbUrl());
            sdThumb.setImageURI(data.getThumbUrl());

            String buyText = "立即抢购";
            Drawable buyDrawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_VIP, 12);

            //是否显示抢光
            if (data.getIsFlashSale() == 1) {
                if (data.getFlashInventory() > 0) {
                    spuStatusView.setVisibility(View.INVISIBLE);
                } else {
                    buyText = "已抢光";
                    buyDrawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_PROGRESS_GARY_TEXT, 12);
                    spuStatusView.setVisibility(View.VISIBLE);
                }
            } else {
                if (data.getAllStockFlag() != 1) {
                    spuStatusView.setVisibility(View.INVISIBLE);
                } else {
                    buyText = "已抢光";
                    buyDrawable = new BackgroundMaker(context).getRadiusDrawable(UIConfig.COLOR_RUSH_PROGRESS_GARY_TEXT, 12);
                    spuStatusView.setVisibility(View.VISIBLE);
                }
            }
            buyButton.setText(buyText);
            buyButton.setBackground(buyDrawable);

            titleTextView.setText("" + data.getProductName());

            if (isVip) {
                saleCountTextView.setText("已推" + ConvertUtil.formatWan(data.getExtendTime()) + "次");
                //设置头像数量
                AvatarDemoMaker.setVisibilitys(avatars, (int) data.getExtendTime(), 3);
            } else {
                saleCountTextView.setText("已抢" + ConvertUtil.formatWan(data.getSaleCount()) + "件");
                //设置头像数量
                AvatarDemoMaker.setVisibilitys(avatars, (int) data.getSaleCount(), 3);
            }

            //设置随机头像
            AvatarDemoMaker.setDemoAvatar(avatars);

            if (data.isFlashSale()) {
                //抢购店主价格
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinScorePrice()));
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(data.getMaxBrokeragePrice()));

                //抢购会员价格
                userPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinScorePrice()));
                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMaxSalePrice()));
            } else {
                //非抢购店主价格
                vipPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinPrice()));
                rewardPriceTextView.setText("赚" + ConvertUtil.cent2yuanNoZero(data.getMaxRewardPrice()));

                //非抢购会员价格
                userPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMinPrice()));
                marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(data.getMaxMarketPrice()));
            }

            if (isVip) {
                //店主身份
                vipPriceLayout.setVisibility(View.VISIBLE);
                userPriceLayout.setVisibility(View.GONE);
            } else {
                //会员身份 or 未登录
                vipPriceLayout.setVisibility(View.GONE);
                userPriceLayout.setVisibility(View.VISIBLE);
            }
        }

    }
}


package com.xiling.ddui.manager;

import android.content.Context;

import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.HProductPostBean;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;

public class DDProductPosterManager {

    public interface ProductPosterListener {
        void onPosterMakerStart();

        void onPosterTaskStart(String url);

        void onPosterTaskSuccess(String url, String poster);

        void onPosterMakerSuccess(ArrayList<String> posters);

        void onPosterMakerError(String desc);
    }

    ProductPosterListener listener = null;

    public ProductPosterListener getListener() {
        return listener;
    }

    public void setListener(ProductPosterListener listener) {
        this.listener = listener;
    }

    Context context = null;
    CommunityDataBean data = null;

    public DDProductPosterManager(Context context) {
        this.context = context;
    }


    /**
     * 设置数据
     *
     * @param data 商品数据
     */
    public void setData(CommunityDataBean data) {
        this.data = data;
    }

    //需要合成数据的队列
    ArrayList<HProductPostBean> posterList = new ArrayList<>();
    ArrayList<String> posterFileList = new ArrayList<>();

    /**
     * 开始绘制
     */
    public void makePoster() {
        callbackStart();
        posterList.addAll(data.getPostImages());
        if (posterList != null && !posterList.isEmpty()) {
            execute();
        } else {
            callbackError("封面数据为空!");
        }
    }

    void execute() {
        ToastUtil.showLoading(context);
        if (posterList.size() > 0) {
            HProductPostBean target = posterList.get(0);
            if (target.isSelect()) {
                //只处理选中图片
                makeTask(target);
            } else {
                //未选中的图片不进行分享
                posterList.remove(target);
                //执行下一次任务
                execute();
            }
        } else {
            callbackSuccess();
            ToastUtil.hideLoading();
        }
    }

    void makeTask(HProductPostBean item) {
        callbackTaskStart(item.getUrl());
        DDProductPosterMaker maker = new DDProductPosterMaker(context);
        //设置商品数据
        maker.setData(item);
        maker.setListener(new DDProductPosterMaker.DDProductPostMakerListener() {
            @Override
            public void onPostMakerStart(String url) {
                DLog.i("=>合成任务开始:" + url);
                callbackTaskStart(url);
            }

            @Override
            public void onPostMakerDownloadUserInfo(String url, String file) {
                DLog.i("=>合成任务 - 下载用户信息:" + url);
            }

            @Override
            public void onPostMakerDownloadProductInfo(String url, String file) {
                DLog.i("=>合成任务 - 下载产品信息:" + url);
            }

            @Override
            public void onPostMakerSuccess(String url, String filePath) {
                DLog.i("=>合成任务 - 合成完成:" + filePath);
                //将结果加入缓存
                posterFileList.add(filePath);
                //回调任务成功
                callbackTaskSuccess(url, filePath);
                //移除当前任务
                if (posterList.size() > 0) {
                    posterList.remove(0);
                }
                //执行下一次任务
                execute();
            }

            @Override
            public void onPostMakerError(String desc) {
                DLog.i("=>合成任务 - 合成失败：" + desc);
                callbackError(desc);
                ToastUtil.hideLoading();
            }
        });
        //开始合成图片
        maker.render();
    }

    void callbackStart() {
        if (listener != null) {
            listener.onPosterMakerStart();
        } else {
            DLog.e("no listener register for DDProductPosterManager.onPosterMakerStart.");
        }
    }

    void callbackTaskStart(String url) {
        if (listener != null) {
            listener.onPosterTaskStart(url);
        } else {
            DLog.e("no listener register for DDProductPosterManager.onPosterTaskStart:" + url);
        }
    }

    void callbackTaskSuccess(String url, String file) {
        if (listener != null) {
            listener.onPosterTaskSuccess(url, file);
        } else {
            DLog.e("no listener register for DDProductPosterManager.onPosterTaskSuccess:" + url + "," + file);
        }
    }

    void callbackSuccess() {
        if (listener != null) {
            listener.onPosterMakerSuccess(posterFileList);
        } else {
            DLog.e("no listener register for DDProductPosterManager.onPosterMakerSuccess:" + posterFileList.size());
        }
    }

    void callbackError(String desc) {
        if (listener != null) {
            listener.onPosterMakerError(desc);
        } else {
            DLog.e("no listener register for DDProductPosterManager.onPosterMakerError:" + desc);
        }
    }


}

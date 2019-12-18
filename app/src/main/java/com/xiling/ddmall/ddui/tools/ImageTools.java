package com.xiling.ddmall.ddui.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.xiling.ddmall.ddui.manager.PosterMaker;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.ImgDownLoadUtils;
import com.xiling.ddmall.shared.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageTools {

    /* 发圈素材 图片尺寸*/
    public final static int roundImageSize = 1008;
    public final static int roundQRSize = 273;

    /*早晚安语 - 生成图片宽度*/
    public final static int greetingImageWidth = 1080;
    /*早晚安语 - 生成图片高度*/
    public final static int greetingImageHeight = 1920;
    public final static int greetingQRSize = 220;

    /**
     * 给ImageView设置本地图片
     */
    public static void setDiskImageToView(ImageView imageView, String diskImagePath) {
        String path = diskImagePath.replace("file://", "");
        Bitmap bmThumb = ImageTools.getSmallBitmap(path);
        BitmapDrawable bd = new BitmapDrawable(imageView.getContext().getResources(), bmThumb);
        imageView.setBackground(bd);
    }

    /**
     * 给ImageView设置本地图片
     */
    public static void setDiskImageToView(ImageView imageView, Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(imageView.getContext().getResources(), bitmap);
        imageView.setBackground(bd);
    }

    /**
     * 将文字转为图片
     *
     * @param text      文字
     * @param textSize  字号
     * @param textColor 字色
     * @param isBold    是否粗体
     * @param width     区域宽度
     * @param height    区域高度
     * @return Bitmap
     */
    public static Bitmap drawTextToBitmap(String text, int textSize, int textColor, boolean isBold, int width, int height) {

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        Paint paint = new Paint();

        //辅助线
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(0, 0, width, height, paint);

        paint.setTextSize(textSize);
        paint.setColor(textColor);
        if (isBold) {
            Typeface font = Typeface.create("黑体", Typeface.BOLD);
            paint.setTypeface(font);
        }

//        canvas.translate(priceX, priceY);
//        StaticLayout introTextLayout = new StaticLayout(intro, introTp, skuIntroWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//        introTextLayout.draw(canvas);

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        int y = (int) ((paint.getFontMetrics().descent - paint.getFontMetrics().top));
        //写文本
        canvas.drawText(text, 0, y, paint);

        //保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return result;
    }

    /**
     * 合并发圈素材图片
     *
     * @param thumbFilePath  大图
     * @param qrFilePath     二维码
     * @param avatarFilePath 头像
     * @param nickName       用户昵称
     * @param inviteCode     邀请码
     * @param skuInfo        商品信息
     * @param q              压缩质量
     */
    public static String mergeRoundImage(String thumbFilePath, String qrFilePath, String avatarFilePath, String nickName, String inviteCode, SkuInfo skuInfo, int q) throws FileNotFoundException {

        //整体宽度
        int width = 1080;
        //整体高度
        int height = 1920;

        //二维码尺寸
        int qrSize = 273;
        //二维码X轴
        int qrX = width - qrSize - 32;
        //二维码Y轴
        int qrY = height - qrSize - 40;

        //二维码提示宽度
        int qrHintWidth = 410;
        //二维码提示高度
        int qrHintHeight = 46;
        //二维码Y轴
        int qrHintY = height - 40 - qrHintHeight;
        //二维码X轴
        int qrHintX = width - qrHintWidth - qrSize - 32 - 32;

        //头像尺寸
        int avatarSize = 140;
        //头像X轴
        int avatarX = 40;
        //头像Y轴
        int avatarY = 54;

        //用户昵称X轴
        int nameX = avatarX + avatarSize + 40;
        //名字宽度
        int nameWidth = width - nameX - 40;
        //名字高度
        int nameHeight = 56;
        //用户昵称Y轴
        int nameY = 60;

        //邀请码X轴
        int inviteCodeX = nameX;
        //邀请码Y轴
        int inviteCodeY = nameY + nameHeight + 24;
        //邀请码宽度
        int inviteCodeWidth = width - inviteCodeX - 40;
        //邀请码高度
        int inviteCodeHeight = 56;

        //大图X轴
        int thumbX = 36;
        //大图Y轴
        int thumbY = avatarY + avatarSize + 56;
        //大图尺寸
        int thumbSize = roundImageSize;

        ///////////////////////////////////// -= 商品数据 =- ///////////////////////////////////////////////////////

        //商品名称X轴
        int skuNameX = thumbX;
        //商品名称Y轴
        int skuNameY = thumbY + thumbSize + 42;
        //商品名称宽度
        int skuNameWidth = width - thumbX * 2;
        //商品名称高度
        int skuNameHeight = 70;

        //商品简介X轴
        int skuIntroX = skuNameX;
        //商品简介Y轴
        int skuIntroY = skuNameY + skuNameHeight + 30;
        //商品简介宽度
        int skuIntroWidth = skuNameWidth;
        //商品简介高度
        int skuIntroHeight = 40;

        //价格X轴
        int priceX = skuNameX;
        //价格Y轴
        int priceY = skuIntroY + skuIntroHeight + 73;
        //价格宽度
        int priceWidth = width - priceX * 2 - qrSize - 32;
        //价格高度
        int priceHeight = 93;

        if (!TextUtils.isEmpty(thumbFilePath)) {

            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Paint p = new Paint();
            p.setColor(Color.WHITE);

            //画白底
            canvas.drawRect(0, 0, width, height, p);

            //画头像
            if (!TextUtils.isEmpty(avatarFilePath)) {
                try {
                    Bitmap headerBm = getCircleImage(avatarFilePath, avatarSize);
                    canvas.drawBitmap(headerBm, avatarX, avatarY, p);
                    //回收图片资源
                    if (!headerBm.isRecycled()) {
                        headerBm.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //写昵称
            p.setColor(Color.BLACK);
            p.setTextSize(52);
            p.setTextAlign(Paint.Align.LEFT);

            Rect nameRect = new Rect(nameX, nameY, nameWidth, nameHeight);
            p.getTextBounds(nickName, 0, nickName.length(), nameRect);
            nameY += (p.getFontMetrics().descent - p.getFontMetrics().top) / 2;
            canvas.drawText(nickName, nameX, nameY, p);

            //写邀请码
            p.setColor(Color.BLACK);
            p.setTextSize(48);
            p.setTextAlign(Paint.Align.LEFT);

            inviteCode = "邀请码 " + inviteCode;
            Rect inviteRect = new Rect(inviteCodeX, inviteCodeY, inviteCodeWidth, inviteCodeHeight);
            p.getTextBounds(inviteCode, 0, inviteCode.length(), inviteRect);
            inviteCodeY += (p.getFontMetrics().descent - p.getFontMetrics().top) / 2;
            canvas.drawText(inviteCode, inviteCodeX, inviteCodeY, p);

            try {
                //画缩略图
                Bitmap thumbBitmap = getSmallBitmap(thumbFilePath, thumbSize, thumbSize);
                canvas.drawBitmap(thumbBitmap, thumbX, thumbY, p);
                //回收图片资源
                if (!thumbBitmap.isRecycled()) {
                    thumbBitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //写商品信息
            if (skuInfo != null) {
                String skuName = skuInfo.name;
                String intro = skuInfo.desc;
                String price = "￥" + ConvertUtil.cent2yuan2(skuInfo.retailPrice);

                Bitmap skuNameBm = drawTextToBitmap(skuName, 48, Color.BLACK, true, skuNameWidth, skuNameHeight);
                canvas.drawBitmap(skuNameBm, skuNameX, skuNameY, p);
                //回收图片资源
                if (!skuNameBm.isRecycled()) {
                    skuNameBm.recycle();
                }

                //写商品描述
                Bitmap skuIntroBm = drawTextToBitmap(intro, 24, Color.GRAY, false, skuIntroWidth, skuIntroHeight);
                canvas.drawBitmap(skuIntroBm, skuIntroX, skuIntroY, p);
                //回收图片资源
                if (!skuIntroBm.isRecycled()) {
                    skuIntroBm.recycle();
                }

                //写商品价格
                Bitmap skuPriceBm = drawTextToBitmap(price, 32, Color.RED, false, priceWidth, priceHeight);
                canvas.drawBitmap(skuPriceBm, priceX, priceY, p);
                //回收图片资源
                if (!skuPriceBm.isRecycled()) {
                    skuPriceBm.recycle();
                }

            } else {
                // 使用缺省图片进行绘制
                //img_cover_product_mask.png
            }

            //画二维码
            if (!TextUtils.isEmpty(qrFilePath)) {
                try {
                    Bitmap qrBm = getSmallBitmap(qrFilePath, qrSize, qrSize);
                    canvas.drawBitmap(qrBm, qrX, qrY, p);
                    //回收图片资源
                    if (!qrBm.isRecycled()) {
                        qrBm.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //写二维码提示
                String qrHint = skuInfo != null ? "长按二维码购买商品" : "识别二维码注册会员";
                Paint qPaint = new Paint();
                qPaint.setColor(Color.GRAY);
                qPaint.setTextSize(24);
                Rect qRect = new Rect();
                qPaint.getTextBounds(qrHint, 0, qrHint.length(), qRect);
                //得到真实Width
                Bitmap qrHintBm = drawTextToBitmap(qrHint, 24, Color.GRAY, false, qRect.width(), qrHintHeight);
                //构造真实X坐标
                qrHintX = qrX - 10 - qRect.width();
                canvas.drawBitmap(qrHintBm, qrHintX, qrHintY, p);
                //回收图片资源
                if (!qrHintBm.isRecycled()) {
                    qrHintBm.recycle();
                }
            }

            //保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            File outputFile = new File(thumbFilePath);
            FileOutputStream out = new FileOutputStream(outputFile);
            boolean compressStatus = result.compress(Bitmap.CompressFormat.JPEG, q, out);

            if (!result.isRecycled()) {
                result.recycle();
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return outputFile.getPath();
        } else {
            return "";
        }
    }

    public static String mergeGreetingImage(String backgroundFilePath, String qrFilePath, String qrDescFilePath, int q) throws FileNotFoundException {

        //二维码尺寸
        int qrSize = greetingQRSize;

        int bmpWidth = greetingImageWidth;
        int bmpHeight = greetingImageHeight;

        if (!TextUtils.isEmpty(backgroundFilePath)) {

            Bitmap backgroundBm = getSmallBitmap(backgroundFilePath, bmpWidth, bmpHeight);

            Bitmap result = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Paint p = new Paint();
            String familyName = "黑体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            p.setColor(Color.WHITE);
            p.setTypeface(font);
            p.setTextSize(48);
//            p.setTextAlign(Paint.Align.RIGHT);

            //画底图
            canvas.drawBitmap(backgroundBm, 0, 0, p);

            //画二维码
            if (!TextUtils.isEmpty(qrFilePath)) {
                Bitmap qrBm = getSmallBitmap(qrFilePath, qrSize, qrSize);
                if (qrBm != null && !qrBm.isRecycled()) {

                    int x = bmpWidth - qrSize - 75;
                    int y = bmpHeight - qrSize - 35;
                    canvas.drawBitmap(qrBm, x, y, p);

                    try {
                        if (!qrBm.isRecycled()) {
                            qrBm.recycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!TextUtils.isEmpty(qrDescFilePath)) {
                int descWidth = 25;
                int descHeight = 190;
                Bitmap descBm = getSmallBitmap(qrDescFilePath, descWidth, descHeight);
                if (descBm != null && !descBm.isRecycled()) {

                    int x = bmpWidth - descWidth/*图片本身宽度*/ - 25/*与二维码之间的间隔*/;
                    int y = bmpHeight - descHeight/*图片本身高度*/;
                    canvas.drawBitmap(descBm, x, y, p);

                    try {
                        if (!descBm.isRecycled()) {
                            descBm.recycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            //保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            File outputFile = new File(backgroundFilePath);
            FileOutputStream out = new FileOutputStream(outputFile);
            boolean compressStatus = result.compress(Bitmap.CompressFormat.JPEG, q, out);

            if (!backgroundBm.isRecycled()) {
                backgroundBm.recycle();
            }

            if (!result.isRecycled()) {
                result.recycle();
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return outputFile.getPath();
        } else {
            return "";
        }
    }


    /**
     * 专属海报合并
     *
     * @param backgroundFilePath 背景图路径
     * @param headerFilePath     头像图路径
     * @param qrFilePath         二维码图路径
     * @param nickName           用户昵称
     * @param q                  压缩质量
     */
    public static String mergePoster(String backgroundFilePath, String headerFilePath, String qrFilePath, String nickName, int q) throws FileNotFoundException {

        int bmpWidth = 0;
        int bmpHeight = 0;

        if (!TextUtils.isEmpty(backgroundFilePath)) {

            Bitmap backgroundBm = getSmallBitmap(backgroundFilePath);

            int degree = readPictureDegree(backgroundFilePath);
            if (degree != 0) {//旋转照片角度
                backgroundBm = rotateBitmap(backgroundBm, degree);
            }

            try {
                if (backgroundBm != null) {
                    bmpWidth = backgroundBm.getWidth();
                    bmpHeight = backgroundBm.getHeight();
                }
            } catch (Exception e) {

            }

            Bitmap result = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Paint p = new Paint();
            String familyName = "黑体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            p.setColor(Color.WHITE);
            p.setTypeface(font);
            p.setTextSize(48);
//            p.setTextAlign(Paint.Align.RIGHT);

            //画底图
            canvas.drawBitmap(backgroundBm, 0, 0, p);

            //画头像 132x132
            int headerSize = 132;
            if (!TextUtils.isEmpty(headerFilePath)) {
                try {
                    Bitmap headerBm = getCircleImage(headerFilePath, headerSize);
                    canvas.drawBitmap(headerBm, 48, 342, p);
                    if (headerBm != null && !headerBm.isRecycled()) {
                        headerBm.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //画二维码
            if (!TextUtils.isEmpty(qrFilePath)) {
                int qrSize = PosterMaker.PosterQRSize;
                Bitmap qrBm = getSmallBitmap(qrFilePath, qrSize, qrSize);
                canvas.drawBitmap(qrBm, bmpWidth - 48 - qrSize, bmpHeight - 102 - qrSize, p);
            }

            //写昵称
            Rect bounds = new Rect();
            p.getTextBounds(nickName, 0, nickName.length(), bounds);

            int textWidth = bounds.width();
            int textHeight = bounds.height();

            canvas.drawText(nickName, 180 + 10, 342 + (headerSize / 2) + (textHeight / 3), p);

            //保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            File outputFile = new File(backgroundFilePath);
            FileOutputStream out = new FileOutputStream(outputFile);
            boolean compressStatus = result.compress(Bitmap.CompressFormat.JPEG, q, out);

            if (!backgroundBm.isRecycled()) {
                backgroundBm.recycle();
            }

            if (!result.isRecycled()) {
                result.recycle();
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return outputFile.getPath();
        } else {
            return "";
        }
    }

    /**
     * 创业礼包+专属粉丝的图片合并
     *
     * @param backgroundFilePath 背景图路径
     * @param qrFilePath         二维码路径
     * @param q                  压缩质量
     */
    public static String mergeShare(String backgroundFilePath, String qrFilePath, int q) throws FileNotFoundException {

        int bmpWidth = 0;
        int bmpHeight = 0;

        if (!TextUtils.isEmpty(backgroundFilePath)) {

            Bitmap backgroundBm = getSmallBitmap(backgroundFilePath);

            int degree = readPictureDegree(backgroundFilePath);
            if (degree != 0) {//旋转照片角度
                backgroundBm = rotateBitmap(backgroundBm, degree);
            }

            try {
                if (backgroundBm != null) {
                    bmpWidth = backgroundBm.getWidth();
                    bmpHeight = backgroundBm.getHeight();
                }
            } catch (Exception e) {

            }

            Bitmap result = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Paint p = new Paint();
            String familyName = "黑体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            p.setColor(Color.WHITE);
            p.setTypeface(font);
            p.setTextSize(48);

            //画底图
            canvas.drawBitmap(backgroundBm, 0, 0, p);

            //画二维码
            if (!TextUtils.isEmpty(qrFilePath)) {
                Bitmap qrBm = getSmallBitmap(qrFilePath);
                canvas.drawBitmap(qrBm, bmpWidth / 2 - 234 / 2, bmpHeight - 158 - 234, p);
            }

            //保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            File outputFile = new File(backgroundFilePath);
            FileOutputStream out = new FileOutputStream(outputFile);
            boolean compressStatus = result.compress(Bitmap.CompressFormat.JPEG, q, out);

            if (!backgroundBm.isRecycled()) {
                backgroundBm.recycle();
            }

            if (!result.isRecycled()) {
                result.recycle();
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return outputFile.getPath();
        } else {
            return "";
        }
    }

    /**
     * 获取一个圆形图片
     *
     * @param srcPath 图片路径
     * @param size    图片尺寸
     */
    public static Bitmap getCircleImage(String srcPath, int size) {
        Bitmap bitmap = getSmallBitmap(srcPath, size, size);

        int radius = size / 2;

        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Bitmap target = Bitmap.createBitmap(size, size, bitmap.getConfig());
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(radius, radius, radius, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 给Canvas加上抗锯齿标志
         */
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(bitmap, 0, 0, paint);

        //回收资源
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {

        }

        return target;
    }

    /**
     * 保存图片到SD卡
     *
     * @param bitmap     图片数据
     * @param pathToFile 保存的文件路径
     * @param q          图片压缩质量
     * @param tipFlag    是否提示状态
     */
    public static boolean saveBitmapToSD(Bitmap bitmap, String pathToFile, int q, boolean tipFlag) {
        try {
            File outputFile = new File(pathToFile);
            FileOutputStream out = new FileOutputStream(outputFile);
            boolean status = bitmap.compress(Bitmap.CompressFormat.JPEG, q, out);
            if (tipFlag && status) {
                ToastUtil.success("保存成功");
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (out != null) {
                out.flush();
                out.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存图片到相册
     *
     * @param context   上下文
     * @param bitmap    要保存的图片数据
     * @param imageName 文件名字
     */
    public static void saveBitmapToAlbum(Context context, Bitmap bitmap, String imageName) {
        String fileName = ImgDownLoadUtils.getAlbumPath() + imageName;

//        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
//            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + imageName;
//        } else {  // Meizu 、Oppo
//            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + imageName;
//        }

        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 发送广播，通知刷新图库的显示
        ImgDownLoadUtils.sendScanFileBroadcast(context, file);
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }

    /**
     * 裁剪图片到指定的尺寸
     */
    public static boolean cutImage(String filePath, int width, int height) {
        if (!TextUtils.isEmpty(filePath)) {
            Bitmap bm = getSmallBitmap(filePath);
            int degree = readPictureDegree(filePath);
            if (degree != 0) {//旋转照片角度
                bm = rotateBitmap(bm, degree);
            }

            Bitmap scale = Bitmap.createScaledBitmap(bm, width, height, true);

            File outputFile = new File(filePath);
            try {
                FileOutputStream out = new FileOutputStream(outputFile);
                boolean compressStatus = scale.compress(Bitmap.CompressFormat.JPEG, 85, out);

                if (!bm.isRecycled()) {
                    bm.recycle();
                }

                if (!scale.isRecycled()) {
                    scale.recycle();
                }

                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return compressStatus;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            DLog.e("cut image file not exist.");
            return false;
        }
    }

    public static String watermark(String filePath, int q, String text) throws FileNotFoundException {
        int bmpWidth = 0;
        int bmpHeight = 0;

        if (!TextUtils.isEmpty(filePath)) {

            Bitmap bm = getSmallBitmap(filePath);

            int degree = readPictureDegree(filePath);
            if (degree != 0) {//旋转照片角度
                bm = rotateBitmap(bm, degree);
            }
            try {
                if (bm != null) {
                    bmpWidth = bm.getWidth();
                    bmpHeight = bm.getHeight();
                }
            } catch (Exception e) {

            }
            Bitmap watermark = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(watermark);

            Paint p = new Paint();
            String familyName = "黑体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            p.setColor(Color.RED);
            p.setTypeface(font);
            p.setTextSize(72);
//            p.setTextAlign(Paint.Align.RIGHT);

            Rect bounds = new Rect();
            p.getTextBounds(text, 0, text.length(), bounds);
            int textWidth = bounds.width();
            int textHeight = bounds.height();

            //画原图
            canvas.drawBitmap(bm, 0, 0, p);
            //写水印
            canvas.drawText(text, bmpWidth - textWidth - 100, bmpHeight - textHeight, p);
            //保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            File outputFile = new File(filePath);
            FileOutputStream out = new FileOutputStream(outputFile);
            boolean compressStatus = watermark.compress(Bitmap.CompressFormat.JPEG, q, out);

            if (!bm.isRecycled()) {
                bm.recycle();
            }

            if (!watermark.isRecycled()) {
                watermark.recycle();
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return outputFile.getPath();
        } else {
            return "";
        }
    }

    /*
     *
     * 压缩图片，处理某些手机拍照角度旋转的问题
     * */
    public static String compressImage(String filePath, int q) throws FileNotFoundException {
        if (!TextUtils.isEmpty(filePath)) {
            Bitmap bm = getSmallBitmap(filePath);
//            int degree = readPictureDegree(filePath);
//            if (degree != 0) {//旋转照片角度
//                bm = rotateBitmap(bm, degree);
//            }
            File outputFile = new File(filePath);
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, q, out);
            return outputFile.getPath();
        } else {
            return "";
        }
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获取获取1080x1920采样的图片
     *
     * @param filePath 图片位置
     */
    public static Bitmap getSmallBitmap(String filePath) {
        return getSmallBitmap(filePath, 1080, 1920);
    }


    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath 图片位置
     * @param width    采样宽
     * @param height   采样宽
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * base64转为bitmap
     *
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        try {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

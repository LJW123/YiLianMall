package com.leshan.ylyj.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.view.activity.creditmodel.KnowMoreCreditActivity;
import com.vondear.rxtools.RxImageTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 了解信用画图
 */
public class CreditScoreView extends View {

    private Context context;
    //数据个数
    public static int dataCount = 5;
    //每个角的弧度
    private float radian = (float) (Math.PI * 2 / dataCount);
    //雷达图半径
    private float radius;
    //中心X坐标
    private int centerX;
    //中心Y坐标
    private int centerY;
    //各维度标题
    public static String[] titles = {"履约能力", "信用历史", "人脉关系", "行为偏好", "身份特质"};
    //各维度图标
    public static int[] icons = {R.mipmap.ic_msg, R.mipmap.ic_msg, R.mipmap.ic_msg,
            R.mipmap.ic_msg, R.mipmap.ic_msg};
    public static String[] iconss = {"", "", "", "", ""};
    public static Bitmap[] iconsb = {};
    public static boolean iconsbool = false;
    //各维度分值
    public static float[] data = {0, 0, 0, 0, 0};
    //标题颜色
    public static float[] titlecolor = {0, 0, 0, 0, 0};
    //数据最大值
    public static float maxValue = 190;
    //雷达图与标题的间距
    private int radarMargin = 0;
    //雷达区画笔
    private Paint mainPaint;
    //数据区画笔
    private Paint valuePaint;
    //分数画笔
    private Paint scorePaint;
    //标题画笔
    private Paint titlePaint;
    //图标画笔
    private Paint iconPaint;
    //分数大小
    private int scoreSize = 0;
    //标题文字大小
    private int titleSize = 0;
    //    //分数
    public static int score = 0;
    public static int[] coordinateX = {};
    public static int[] coordinateY = {};


    public CreditScoreView(Context context) {
        this(context, null);
        this.context = context;
    }

    public CreditScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CreditScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();

    }

    public void Datas(float[] data) {
        this.data = data;
        init();
    }

    private void init() {
        radarMargin = RxImageTool.dp2px(10);//雷达图与标题的间距
        scoreSize = RxImageTool.dp2px(22); //分数大小
        titleSize = RxImageTool.dp2px(12);  //标题文字大小

        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setStrokeWidth(0.3f);
        mainPaint.setColor(Color.WHITE);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.WHITE);
        valuePaint.setAlpha(120);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        scorePaint = new Paint();
        scorePaint.setAntiAlias(true);
        scorePaint.setTextSize(scoreSize);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setStyle(Paint.Style.FILL);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(Color.WHITE);
        titlePaint.setStyle(Paint.Style.FILL);

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //雷达图半径
        radius = Math.min(h, w) / 2 * 0.5f;
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawPolygon(canvas);
        drawLines(canvas);
        drawRegion(canvas);
        drawScore(canvas);
        drawTitle(canvas);
        drawIcon(canvas);
    }

    /**
     * 绘制多边形
     *
     * @param canvas 画布
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            if (i == 0) {
                path.moveTo(getPoint(i).x, getPoint(i).y);
            } else {
                path.lineTo(getPoint(i).x, getPoint(i).y);
            }
        }
        //闭合路径
        path.close();
        canvas.drawPath(path, mainPaint);
    }

    /**
     * 绘制连接线
     *
     * @param canvas 画布
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            path.lineTo(getPoint(i).x, getPoint(i).y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制覆盖区域
     *
     * @param canvas 画布
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();

        for (int i = 0; i < dataCount; i++) {
            //计算百分比
            float percent = data[i] / maxValue;
            int x = getPoint(i, 0, percent).x;
            int y = getPoint(i, 0, percent).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }


        //绘制填充区域的边界
        path.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);

        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        valuePaint.setColor(getResources().getColor(R.color.white_lucency_back));
        canvas.drawPath(path, valuePaint);
    }

    /**
     * 绘制分数
     *
     * @param canvas 画布
     */
    private void drawScore(Canvas canvas) {
//        int score = 0;
////        计算总分
//        for (int i = 0; i < dataCount; i++) {
//            score += data[i];
//        }
//        canvas.drawText(score+300 + "", centerX, centerY + scoreSize / 2, scorePaint);
        canvas.drawText(score + "", centerX, centerY + scoreSize / 2, scorePaint);
    }

    /**
     * 绘制标题
     *
     * @param canvas 画布
     */
    private void drawTitle(Canvas canvas) {
        for (int i = 0; i < dataCount; i++) {
            int x = getPoint(i, radarMargin, 1).x;
            int y = getPoint(i, radarMargin, 1).y;
            Bitmap bitmap;
            if (iconsbool) {
                bitmap = iconsb[i];
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), icons[i]);
            }
//            Bitmap bitmap =returnBitmap(iconss[i]);
//            Bitmap bitmap =iconsb[i];
            if (bitmap != null) {
                int iconHeight = bitmap.getHeight();
                float titleWidth = titlePaint.measureText(titles[i]);
                if(titlecolor[i]==0){
                    titlePaint.setColor(getResources().getColor(R.color.white_lucency));
                }else{
                    titlePaint.setColor(getResources().getColor(R.color.white));
                }


                //底下两个角的坐标需要向下移动半个图片的位置（1、2）
                if (i == 1) {
                    y += (iconHeight / 2);
                } else if (i == 2) {
                    x -= titleWidth;
                    y += (iconHeight / 2);
                } else if (i == 3) {
                    x -= titleWidth;
                } else if (i == 4) {
                    x -= titleWidth / 2;
                }
                canvas.drawText(titles[i], x, y, titlePaint);
            }

        }
    }

//    private Bitmap returnBitmap(String url) {
//        URL fileUrl = null;
//        Bitmap bitmap = null;
//        try {
//            fileUrl = new URL(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        try {
//            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }

    /**
     * 绘制图标
     *
     * @param canvas 画布
     */
    private void drawIcon(Canvas canvas) {
        coordinateX = new int[5];
        coordinateY = new int[5];
        for (int i = 0; i < dataCount; i++) {
            int x = getPoint(i, radarMargin, 1).x;
            int y = getPoint(i, radarMargin, 1).y;
            Bitmap bitmap;
            if (iconsbool) {

                bitmap = iconsb[i];
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), icons[i]);
            }
            if (bitmap != null) {

//            Bitmap bitmap =returnBitmap(iconss[i]);
//            Bitmap bitmap =iconsb[i];
                int iconWidth = bitmap.getWidth();
                int iconHeight = bitmap.getHeight();


                float titleWidth = titlePaint.measureText(titles[i]);
                if(titlecolor[i]==0){
                    titlePaint.setColor(getResources().getColor(R.color.white_lucency));
                }else{
                    titlePaint.setColor(getResources().getColor(R.color.white));
                }
                //上面获取到的x、y坐标是标题左下角的坐标
                //需要将图标移动到标题上方居中位置
                if (i == 0) {
                    x += (titleWidth - iconWidth) / 2;
                    y -= (iconHeight + getTextHeight(titlePaint));
                    coordinateX[0] = x;
                    coordinateY[0] = y;
                } else if (i == 1) {
                    x += (titleWidth - iconWidth) / 2;
                    y -= (iconHeight / 2 + getTextHeight(titlePaint));
                    coordinateX[1] = x;
                    coordinateY[1] = y;
                } else if (i == 2) {
                    x -= (iconWidth + (titleWidth - iconWidth) / 2);
                    y -= (iconHeight / 2 + getTextHeight(titlePaint));
                    coordinateX[2] = x;
                    coordinateY[2] = y;
                } else if (i == 3) {
                    x -= (iconWidth + (titleWidth - iconWidth) / 2);
                    y -= (iconHeight + getTextHeight(titlePaint));
                    coordinateX[3] = x;
                    coordinateY[3] = y;
                } else if (i == 4) {
                    x -= iconWidth / 2;
                    y -= (iconHeight + getTextHeight(titlePaint));
                    coordinateX[4] = x;
                    coordinateY[4] = y;
                }
                // 计算缩放比例.
                float scaleWidth = ((float) 50) / iconWidth;
                float scaleHeight = ((float) 50) / iconHeight;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片.
                Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, iconWidth, iconHeight, matrix, true);
                canvas.drawBitmap(newbm, x, y, titlePaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        whichCircle(x, y);
        int clickimg;
        if (iconsbool) {
            if ((event.getX(0) >= coordinateX[0]) &&
                    (event.getY(0) >= coordinateY[0]) &&
                    (event.getX(0) <= coordinateX[0] + 100) &&
                    (event.getY(0) <= coordinateY[0] + 100)) {
                //rectangle selected
//            Toast.makeText(context, "---000", Toast.LENGTH_SHORT).show();
                if (activity != null) {
                    activity.setSite(0);
                }
            } else if ((event.getX(0) >= coordinateX[1]) &&
                    (event.getY(0) >= coordinateY[1]) &&
                    (event.getX(0) <= coordinateX[1] + 100) &&
                    (event.getY(0) <= coordinateY[1] + 100)) {
                //rectangle selected
//            Toast.makeText(context, "---111", Toast.LENGTH_SHORT).show();
                if (activity != null) {
                    activity.setSite(1);
                }
            } else if ((event.getX(0) >= coordinateX[2]) &&
                    (event.getY(0) >= coordinateY[2]) &&
                    (event.getX(0) <= coordinateX[2] + 100) &&
                    (event.getY(0) <= coordinateY[2] + 100)) {
                //rectangle selected
//            Toast.makeText(context, "---222", Toast.LENGTH_SHORT).show();
                if (activity != null) {
                    activity.setSite(2);
                }
            } else if ((event.getX(0) >= coordinateX[3]) &&
                    (event.getY(0) >= coordinateY[3]) &&
                    (event.getX(0) <= coordinateX[3] + 100) &&
                    (event.getY(0) <= coordinateY[3] + 100)) {
                //rectangle selected
//            Toast.makeText(context, "---333", Toast.LENGTH_SHORT).show();
                if (activity != null) {
                    activity.setSite(3);
                }
            } else if ((event.getX(0) >= coordinateX[4]) &&
                    (event.getY(0) >= coordinateY[4]) &&
                    (event.getX(0) <= coordinateX[4] + 100) &&
                    (event.getY(0) <= coordinateY[4] + 100)) {
                //rectangle selected
//            Toast.makeText(context, "---444", Toast.LENGTH_SHORT).show();
                if (activity != null) {
                    activity.setSite(4);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private KnowMoreCreditActivity activity;

    public void setContext(KnowMoreCreditActivity activity) {
        this.activity = activity;
    }

    /**
     * 确定点击的点在哪
     *
     * @param x
     * @param y
     */
    private void whichCircle(float x, float y) {
        Toast.makeText(context, x + "---" + y, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取雷达图上各个点的坐标
     *
     * @param position 坐标位置（右上角为0，顺时针递增）
     * @return 坐标
     */
    private Point getPoint(int position) {
        return getPoint(position, 0, 1);
    }

    /**
     * 获取雷达图上各个点的坐标（包括维度标题与图标的坐标）
     *
     * @param position    坐标位置
     * @param radarMargin 雷达图与维度标题的间距
     * @param percent     覆盖区的的百分比
     * @return 坐标
     */
    private Point getPoint(int position, int radarMargin, float percent) {
        int x = 0;
        int y = 0;

        if (position == 0) {
            x = (int) (centerX + (radius + radarMargin) * Math.sin(radian) * percent);
            y = (int) (centerY - (radius + radarMargin) * Math.cos(radian) * percent);

        } else if (position == 1) {
            x = (int) (centerX + (radius + radarMargin) * Math.sin(radian / 2) * percent);
            y = (int) (centerY + (radius + radarMargin) * Math.cos(radian / 2) * percent);

        } else if (position == 2) {
            x = (int) (centerX - (radius + radarMargin) * Math.sin(radian / 2) * percent);
            y = (int) (centerY + (radius + radarMargin) * Math.cos(radian / 2) * percent);

        } else if (position == 3) {
            x = (int) (centerX - (radius + radarMargin) * Math.sin(radian) * percent);
            y = (int) (centerY - (radius + radarMargin) * Math.cos(radian) * percent);

        } else if (position == 4) {
            x = centerX;
            y = (int) (centerY - (radius + radarMargin) * percent);
        }

        return new Point(x, y);
    }

    /**
     * 获取文本的高度
     *
     * @param paint 文本绘制的画笔
     * @return 文本高度
     */
    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) (fontMetrics.descent - fontMetrics.ascent);
    }
}

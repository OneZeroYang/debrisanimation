package com.ysj.debrisanimation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PageAnimationView extends View {
    private Context context;

    private int abscissa = 0, ordinate = 0;

    private Canvas canvas;

    private float x, y;

    private Paint paint1;

    private Paint mPaint;

    private boolean isStart = false;
    /**
     * 重力加速度
     */
    private double G = 9800 / 2;

    /**
     * 水平速度
     */
    private float v1 = 0.2f;

    private Bitmap bitmap;


    /**
     * 随机因子
     */
    private List<Integer> listX = new ArrayList<>();

    List<Dat> state = new ArrayList<>();

    private float bx,by;

    public void start(View view) {
        isStart = true;
        state = new ArrayList<>();
        ist = new boolean[10][20];
        Drawable background = view.getBackground();
        bitmap =((BitmapDrawable)background).getBitmap();
        bitmap= Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
        bx=bitmap.getWidth()/10;
        by=bitmap.getHeight()/20;
        new Run1().start();
    }


    public PageAnimationView(Context context) {
        this(context, null);
    }

    public PageAnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        mPaint = new Paint();
        canvas = new Canvas();
        for (int a = 0; a < 100; a++) {
            listX.add(a);
        }
        for (int a = 0; a < 200; a++) {
            listX.add(a);
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (isStart) {
            if (state.size() != 0) {
                for (int a = 0; a < state.size(); a++) {

                    /**
                     * 画遮罩层
                     */
                    mPaint.setColor(Color.WHITE);
                    mPaint.setStyle(Paint.Style.FILL);//实心矩形框
                    canvas.drawRect(state.get(a).getStartX(), state.get(a).getStartY(), state.get(a).getStartX() + x, state.get(a).getStartY() + y, mPaint);

                    if (state.get(a).getAbscissa() == -1) {
                        //消失
                    } else {
                        /**
                         * 话运动轨迹
                         */

                        /**
                         * 使用原图
                         */
                        canvas.drawBitmap(state.get(a).getMbitmap(),  state.get(a).getAbscissa(),  state.get(a).getOrdinate(), mPaint);


                        /**
                         * 使用纯色
                         */

//                        mPaint.setColor(state.get(a).getRandomColor());
//                        mPaint.setStyle(Paint.Style.FILL);//实心矩形框
//                        canvas.drawRect(state.get(a).getAbscissa(),
//                                state.get(a).getOrdinate(),
//                                state.get(a).getAbscissa() + x,
//                                state.get(a).getOrdinate() + y,
//                                mPaint);
//                        mPaint.setStyle(Paint.Style.STROKE);//空心矩形框
//                        mPaint.setColor(Color.BLACK);
//                        canvas.drawRect(state.get(a).getAbscissa(),
//                                state.get(a).getOrdinate(),
//                                state.get(a).getAbscissa() + x,
//                                state.get(a).getOrdinate() + y,
//                                mPaint);
                    }
                }
            }

            if (state.size() != 200) {
                evaluation();
                int randomColor = getRandomColor();
                mPaint.setColor(randomColor);
                mPaint.setStyle(Paint.Style.FILL);//实心矩形框
                canvas.drawRect(abscissa * x, ordinate * y, abscissa * x + x, ordinate * y + y, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);//空心矩形框
                mPaint.setColor(Color.BLACK);
                canvas.drawRect(abscissa * x, ordinate * y, abscissa * x + x, ordinate * y + y, mPaint);
                Dat dat = new Dat(abscissa * x, ordinate * y, randomColor);
                dat.setMbitmap(Bitmap.createBitmap(bitmap,(int)(abscissa*bx),(int)(ordinate*by),(int)bx,(int)by));
                state.add(dat);
                new Thread(dat).start();
                super.onDraw(canvas);
            }


        } else {
            x = getWidth() / 10;
            y = getHeight() / 20;
            float v = getHeight() - (0 * y + y);
            time = Math.sqrt(v * 2 / G) +//第一次下落阶段  全高度 时间
                    Math.sqrt(v / 2 / G) +//第一次弹起阶段   1/4高度 时间
                    Math.sqrt(v / 2 / G) + // 第二次下落阶段 1/4高度 时间
                    Math.sqrt(v / 4 / G) +//第二次弹起阶段   1/8高度 时间
                    Math.sqrt(v / 4 / G) +//第二次下落阶段   1/8高度 时间
                    3;//其他时间 + 平滑时间
            time = 10;
        }
    }


    boolean[][] ist = new boolean[10][20];

    private void evaluation() {
        int x = (int) (Math.random() * 10);
        int y = (int) (Math.random() * 20);


        if (ist[x][y]) {
            evaluation();
        } else {
            abscissa = x;
            ordinate = y;
            ist[x][y] = true;
        }
    }

    public static int getRandomColor() {
        Random random = new Random();
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < 2; i++) {
            //       result=result*10+random.nextInt(10);
            int temp = random.nextInt(16);
            r = r * 16 + temp;
            temp = random.nextInt(16);
            g = g * 16 + temp;
            temp = random.nextInt(16);
            b = b * 16 + temp;
        }
        return Color.rgb(r, g, b);
    }


    private double time;


    class Run1 extends Thread {
        @Override
        public void run() {
            for (double a = 0; a <= time; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                postInvalidate();
            }

        }
    }

    class Dat implements Runnable {
        private float abscissa;
        private float ordinate;
        private int randomColor;
        private float beginY;
        private float startX;
        private float startY;
        private boolean isLeft;
        private Bitmap mbitmap;

        public void setMbitmap(Bitmap mbitmap) {
            this.mbitmap = mbitmap;
        }

        public Bitmap getMbitmap() {
            return mbitmap;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        public void setAbscissa(float abscissa) {
            this.abscissa = abscissa;
        }

        public void setOrdinate(float ordinate) {
            this.ordinate = ordinate;
        }

        public void setRandomColor(int randomColor) {
            this.randomColor = randomColor;
        }

        public Dat(float abscissa, float ordinate, int randomColor) {
            this.abscissa = abscissa;
            this.ordinate = ordinate;
            this.randomColor = randomColor;
            beginY = ordinate;
            startX = abscissa;
            startY = ordinate;
            int g = (int) (Math.random() * 2);
            if (g == 0) {
                isLeft = true;
            } else {
                isLeft = false;
            }
        }


        public float getAbscissa() {
            return abscissa;
        }

        public float getOrdinate() {
            return ordinate;
        }

        public int getRandomColor() {
            return randomColor;
        }

        @Override
        public void run() {
            /**
             * 下落一阶段
             */
            double time = Math.sqrt((getHeight() - ordinate - y) * 2 / G);
            for (double a = 0; a <= time; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * a * a) / 2) + beginY;
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }

            }


            /**
             * 弹起一阶段
             */
            double time1 = Math.sqrt((getHeight() - beginY - y) / G);
            for (; time1 >= 0; time1 -= 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * time1 * time1) / 2) + (getHeight() - (getHeight() - beginY - y) / 2);
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }

            /**
             * 下落二阶段
             */

            double time2 = Math.sqrt((getHeight() - ordinate - y) * 2 / G);
            beginY = ordinate;
            for (double a = 0; a <= time2; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * a * a) / 2) + beginY;
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }


            /**
             * 弹起二阶段
             */
            double time3 = Math.sqrt((getHeight() - beginY - y) / 2 / G);
            for (; time3 >= 0; time3 -= 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * time3 * time3) / 2) + (getHeight() - (getHeight() - beginY - y) / 2);
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }


            /**
             * 下落三阶段
             */

            double time4 = Math.sqrt((getHeight() - ordinate - y) * 2 / G);
            beginY = ordinate;
            for (double a = 0; a <= time4; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * a * a) / 2) + beginY;
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }


            /**
             * 弹起三阶段
             */
            double time5 = Math.sqrt((getHeight() - beginY - y) / 4 / G);
            for (; time5 >= 0; time5 -= 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * time5 * time5) / 2) + (getHeight() - (getHeight() - beginY - y) / 2);
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }


            /**
             * 下落四阶段
             */

            double time6 = Math.sqrt((getHeight() - ordinate - y) * 2 / G);
            beginY = ordinate;
            for (double a = 0; a <= time6; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                float hh = (float) ((G * a * a) / 2) + beginY;
                ordinate = hh;
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }

            double time7 = 2d;

            for (double a = 0; a <= time7; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (isLeft) {
                    abscissa -= v1;
                } else {
                    abscissa += v1;
                }
            }

            double time8 = 0.01;
            for (double a = 0; a <= time8; a += 0.001) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                abscissa = -1;
                ordinate = -1;
            }

        }


    }
}

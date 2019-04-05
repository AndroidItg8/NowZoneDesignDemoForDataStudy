package itg8.com.nowzonedesigndemo.breath_history.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.ArrayList;



import itg8.com.nowzonedesigndemo.sleep.widget_custom_progressbar.ProgressItem;

/**
 * Created by Android itg 8 on 9/19/2017.
 */

public class CustomProgressBarHistory  extends android.support.v7.widget.AppCompatSeekBar {




        private ArrayList<ProgressItem> mProgressItemsList;

        public CustomProgressBarHistory(Context context) {
            super(context);
            mProgressItemsList = new ArrayList<ProgressItem>();
        }

        public CustomProgressBarHistory(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomProgressBarHistory(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void initData(ArrayList<ProgressItem> progressItemsList) {
            this.mProgressItemsList = progressItemsList;
        }

        @Override
        protected synchronized void onMeasure(int widthMeasureSpec,
                                              int heightMeasureSpec) {
            // TODO Auto-generated method stub
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        protected void onDraw(Canvas canvas) {
            if (mProgressItemsList.size() > 0) {
                int progressBarWidth = getWidth();
                int progressBarHeight = getHeight();
                int thumboffset = getThumbOffset();
                int lastProgressX = 0;
                int progressItemWidth, progressItemRight;
                for (int i = 0; i < mProgressItemsList.size(); i++) {
                    ProgressItem progressItem = mProgressItemsList.get(i);
                    Paint progressPaint = new Paint();
                    progressPaint.setColor(getResources().getColor(
                            progressItem.color));

                    progressItemWidth = (int) (progressItem.progressItemPercentage
                            * progressBarWidth / 100);

                    progressItemRight = lastProgressX + progressItemWidth;

                    // for last item give right to progress item to the width
                    if (i == mProgressItemsList.size() - 1
                            && progressItemRight != progressBarWidth) {
                        progressItemRight = progressBarWidth;
                    }
                    RectF progressRect = new RectF();
                    progressRect.set(lastProgressX, thumboffset / 2,
                            progressItemRight, progressBarHeight - thumboffset / 2);
                    canvas.drawRoundRect(progressRect, 1,1,progressPaint);
                   // canvas.drawRect(10, 10,10,10,progressPaint);
//                Paint paintText = new Paint();
//                paintText.setColor(Color.WHITE);
//                canvas.drawText("% values",progressRect.width()/2,progressRect.height()/2,paintText);
                    lastProgressX = progressItemRight;
                }
                super.onDraw(canvas);
            }

        }

    }

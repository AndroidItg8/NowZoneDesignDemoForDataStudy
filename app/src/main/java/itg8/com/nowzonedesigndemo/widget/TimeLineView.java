package itg8.com.nowzonedesigndemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Android itg 8 on 9/26/2017.
 */

public class TimeLineView extends View {


    private Paint paint;

    public TimeLineView(Context context) {
        super(context);
         init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
         paint = new Paint(Paint.ANTI_ALIAS_FLAG);
         paint.setColor(Color.BLACK);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0,0,20,20, paint);
        canvas.drawLine(20,0,0,20, paint);

    }
}

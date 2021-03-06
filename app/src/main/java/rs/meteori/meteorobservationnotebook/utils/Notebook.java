package rs.meteori.meteorobservationnotebook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import rs.meteori.meteorobservationnotebook.R;

/**
 * Created by vladi on 10/24/2017.
 */

public class Notebook extends View {

    protected static final float TOUCH_TOLERANCE = 1;
    protected static final float PAINT_WIDTH = 11f;

    public void enable() { enabled = true; }
    public void disable() { enabled = false; }
    public boolean isEnabled() { return enabled; }
    public NotebookDrawing getDrawing() { return drawing; }
    public boolean isBlank() { return blank; }

    private boolean enabled = false;

    protected NotebookDrawing drawing = null;
    protected Paint pathPaint;
    protected Paint touchPaint;
    protected boolean blank = true;
    protected int lastFingerId = -1;

    public void clear() {
        drawing.setBitmap(Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888));
        drawing.setCanvas(new Canvas(drawing.getBitmap()));
        drawing.getCanvas().drawColor(Color.WHITE);

        drawing.getPath().reset();

        blank = true;
    }

    public Notebook(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(getResources().getColor(R.color.vantablack));
        setKeepScreenOn(true);

        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeWidth(PAINT_WIDTH);

        touchPaint = new Paint();
        touchPaint.setAntiAlias(true);
        touchPaint.setColor(Color.BLACK);
        touchPaint.setStyle(Paint.Style.FILL);
        touchPaint.setStrokeJoin(Paint.Join.ROUND);
        touchPaint.setStrokeWidth(PAINT_WIDTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (drawing == null) {
            drawing = new NotebookDrawing();

            drawing.setPath(new Path());
            clear();
        }
    }

    private void touchDown(float x, float y, int fingerId) {
        drawing.getCanvas().drawCircle(x, y, PAINT_WIDTH / 2.0f, touchPaint);

        drawing.getPath().reset();
        drawing.getPath().moveTo(x, y);

        lastFingerId = fingerId;
    }

    private void touchMove(float x, float y, int fingerId) {
        if (fingerId != lastFingerId) {
            drawing.getCanvas().drawPath(drawing.getPath(), pathPaint);
            drawing.getPath().reset();
            drawing.getPath().moveTo(x, y);
        } else {
            drawing.getPath().lineTo(x, y);
        }

        lastFingerId = fingerId;
    }

    private void touchUp(float x, float y, int fingerId) {
        drawing.getCanvas().drawCircle(x, y, PAINT_WIDTH / 2.0f, touchPaint);

        drawing.getPath().lineTo(x, y);
        drawing.getCanvas().drawPath(drawing.getPath(), pathPaint);
        drawing.getPath().reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enabled) return true;

        float x = event.getX();
        float y = event.getY();

        int fingerId = event.getPointerId(event.getActionIndex());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y, fingerId);
                blank = false;
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y, fingerId);
                blank = false;
                break;
            case MotionEvent.ACTION_UP:
                touchUp(x, y, fingerId);
                blank = false;
                break;
        }

        return true;
    }

}

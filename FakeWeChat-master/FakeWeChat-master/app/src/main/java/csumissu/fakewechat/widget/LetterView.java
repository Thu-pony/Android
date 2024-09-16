package csumissu.fakewechat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.HashMap;

import csumissu.fakewechat.R;

/**
 * @author sunyaxi
 * @date 2016/7/1
 */
public class LetterView extends View {

    private static final String TAG = LetterView.class.getSimpleName();
    private Paint mPaint;
    private Toast mTipToast;
    private String[] mLetters;
    private HashMap<String, Float> mLettersMap;
    private static final float MARGIN_FACTOR = 0.2F;
    private static final int INVALID_POSITION = -1;
    private int mPrePosition = INVALID_POSITION;
    private Callback mCallback;
    private Runnable mShowTipToast = new Runnable() {
        @Override
        public void run() {
            mTipToast.show();
        }
    };
    private Runnable mHideTipToast = new Runnable() {
        @Override
        public void run() {
            mTipToast.cancel();
        }
    };
    public interface Callback {
        void scrollToPosition(char letter);
    }

    public LetterView(Context context) {
        this(context, null);
    }

    public LetterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.DKGRAY);

        mLetters = context.getResources().getStringArray(R.array.letters);
        mLettersMap = new HashMap<>(mLetters.length);

        mTipToast = new Toast(context.getApplicationContext());
        mTipToast.setGravity(Gravity.CENTER, 0, 0);
        mTipToast.setDuration(Toast.LENGTH_SHORT);
        mTipToast.setView(LayoutInflater.from(context).inflate(R.layout.letter_tip, null));
        updateToastAnim(mTipToast);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLettersMap.isEmpty()) {
            return false;
        }
        if (event.getX() < 0 || event.getX() > getWidth()
                || event.getY() < 0 || event.getY() > getHeight()) {
            setPressed(false);
            return false;
        }
        final int position = pointToPosition(event);
        if (position == INVALID_POSITION) {
            return false;
        }
        mPrePosition = position;
        final String letter = mLetters[mPrePosition];
        ((TextView) mTipToast.getView()).setText(letter);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setPressed(true);
                getHandler().removeCallbacks(mHideTipToast);
                getHandler().post(mShowTipToast);
                if (mCallback != null) {
                    mCallback.scrollToPosition(letter.charAt(0));
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                getHandler().postDelayed(mHideTipToast, 500);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLetterBar(canvas);
    }

    private void drawLetterBar(Canvas parentCanvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        final float freeWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final float freeHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        final float textSize = Math.min(freeWidth, freeHeight
                / ((mLetters.length + 1) * MARGIN_FACTOR + mLetters.length));
        mPaint.setTextSize(textSize);

        final float startX = freeWidth / 2;
        for (int i = 0; i < mLetters.length; i++) {
            String letter = mLetters[i];
            float startY = getPaddingTop() + textSize * (i + 1) + textSize * MARGIN_FACTOR * (i + 1);
            canvas.drawText(letter, startX, startY, mPaint);
            // canvas.drawCircle(startX, startY, 3, mPaint);
            mLettersMap.put(letter, startY);
        }
        parentCanvas.drawBitmap(bitmap, getPaddingLeft(), getPaddingTop(), mPaint);
    }

    private int pointToPosition(MotionEvent ev) {
        final float POINT_Y = ev.getY();
        for (int i = 0; i < mLetters.length; i++) {
            final float START_Y = mLettersMap.get(mLetters[i]);
            if (POINT_Y <= START_Y) {
                return i;
            }
        }
        return INVALID_POSITION;
    }

    private void updateToastAnim(Toast toast) {
        try {
            Object mTN = getField(toast, "mTN");
            if (mTN == null) {
                return;
            }
            Object mParams = getField(mTN, "mParams");
            if (mParams != null && mParams instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mParams;
                layoutParams.windowAnimations = R.style.WindowAnimStyle;
                layoutParams.alpha = 0.8F;
            }

        } catch (Exception e) {
            Log.e(TAG, "update tip toast anim failed.", e);
        }
    }

    private Object getField(Object instance, String name)
            throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Object object = null;
        if (instance != null && !TextUtils.isEmpty(name)) {
            Field field = instance.getClass().getDeclaredField(name);
            if (field != null) {
                field.setAccessible(true);
                object = field.get(instance);
            }
        }
        return object;
    }
}

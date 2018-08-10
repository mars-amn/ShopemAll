package elamien.abdullah.shopemall;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.lid.lib.LabelViewHelper;

public class LabelKenBurnsView extends KenBurnsView {

    final LabelViewHelper mUtils;

    public LabelKenBurnsView(Context context) {
        this(context, null);
    }

    public LabelKenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelKenBurnsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mUtils = new LabelViewHelper(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mUtils.onDraw(canvas, getMeasuredWidth(), getMeasuredHeight());
    }

    public int getLabelHeight() {
        return mUtils.getLabelHeight();
    }

    public void setLabelHeight(int height) {
        mUtils.setLabelHeight(this, height);
    }

    public int getLabelDistance() {
        return mUtils.getLabelDistance();
    }

    public void setLabelDistance(int distance) {
        mUtils.setLabelDistance(this, distance);
    }

    public boolean isLabelVisual() {
        return mUtils.isLabelVisual();
    }

    public void setLabelVisual(boolean enable) {
        mUtils.setLabelVisual(this, enable);
    }

    public int getLabelOrientation() {
        return mUtils.getLabelOrientation();
    }

    public void setLabelOrientation(int orientation) {
        mUtils.setLabelOrientation(this, orientation);
    }

    public int getLabelTextColor() {
        return mUtils.getLabelTextColor();
    }

    public void setLabelTextColor(int textColor) {
        mUtils.setLabelTextColor(this, textColor);
    }

    public int getLabelBackgroundColor() {
        return mUtils.getLabelBackgroundColor();
    }

    public void setLabelBackgroundColor(int backgroundColor) {
        mUtils.setLabelBackgroundColor(this, backgroundColor);
    }

    public String getLabelText() {
        return mUtils.getLabelText();
    }

    public void setLabelText(String text) {
        mUtils.setLabelText(this, text);
    }

    public int getLabelTextSize() {
        return mUtils.getLabelTextSize();
    }

    public void setLabelTextSize(int textSize) {
        mUtils.setLabelTextSize(this, textSize);
    }
}

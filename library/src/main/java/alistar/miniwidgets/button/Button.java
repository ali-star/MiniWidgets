package alistar.miniwidgets.button;

import android.graphics.Typeface;

public interface Button {

    void setText(String text);

    String getText();

    void setCornerRadius(int cornerRadius);

    void setColor(int color);

    void setTypeface(Typeface typeface);

    void setTextSize(int textSize);

    void setTextColor(int textColor);

    int getTextColor();

    void setRippleColor(int rippleColor);

    void setStrokeColor(int strokeColor);

    void setAllowFastClick(boolean allowFastClick);

    void setIcon(int icon);

    void setIconSize(int iconSize);

    void setIconColor(int iconColor);

    void setShadowColor(int shadowColor);

    int getShadowColor();

    void setShadowSize(int shadowSize);

    int getShadowSize();

    void changeColor(int fromColor, int toColor);

    void changeColor(int color);

    void setStrokeWidth(int strokeWidth);

    boolean isOpen();

    void setOpen(boolean open);

    void open(long delay);

    void open(From from, long delay);

    void close(long delay);

    void close(From from, long delay);

    void setOpenFrom(From openFrom);

    void setCloseFrom(From closeFrom);

    void setAnimatedValue(int animatedValue);

    void showDot(boolean showDot);

}

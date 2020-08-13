package alistar.miniwidgets.utils;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

public class Utils {

    public static float dipToPix(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static int dipToPix(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pixToDip(float dp) {
        if (dp == 0) return 0;
        return dp / Resources.getSystem().getDisplayMetrics().density;
    }

    public static int pixToDip(int dp) {
        if (dp == 0) return 0;
        return (int) (dp / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int spToPix(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem()
                .getDisplayMetrics());
    }

    public static int adjustColorAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static void drawCenterText(String text, Rect rect, float l, float t, Canvas canvas, Paint paint) {
        float cHeight = rect.height();
        float cWidth = rect.width();
        paint.getTextBounds(text, 0, text.length(), rect);
        float x = cWidth / 2f - rect.width() / 2f - rect.left;
        float lineHeight = rect.height();
        float y = (cHeight / 2f) + (lineHeight / 2f) - rect.bottom;
        canvas.save();
        canvas.translate(l, t);
        canvas.drawText(text, x, y, paint);
        canvas.restore();
    }

}

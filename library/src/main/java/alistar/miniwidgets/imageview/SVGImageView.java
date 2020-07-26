package alistar.miniwidgets.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.Nullable;
import java.util.List;
import alistar.miniwidgets.R;

public class SVGImageView extends View {

    private int startColor = 0;
    private int endColor = 0;
    private int svgImageRecourse = -1;
    private SvgUtils svgUtils;
    private List<SvgUtils.SvgPath> paths;
    private Paint logoPaint = new Paint();
    private Paint shadowPaint = new Paint();
    private int size = -1;
    private int color = 0;
    private int shadowRadius = 0;
    private int start = 0, end = 2;
    private Shader shader;

    public SVGImageView(Context context) {
        super(context);
    }

    public SVGImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public SVGImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SVGImageView);
        svgImageRecourse = typedArray.getResourceId(R.styleable.SVGImageView_siv_src, 0);
        shadowRadius = typedArray.getDimensionPixelSize(R.styleable.SVGImageView_siv_srcShadowRadius, 0);
        color = typedArray.getColor(R.styleable.SVGImageView_siv_srcColor, 0);
        startColor = typedArray.getColor(R.styleable.SVGImageView_siv_srcGradientStartColor, 0);
        endColor = typedArray.getColor(R.styleable.SVGImageView_siv_srcGradientEndColor, 0);
        start = typedArray.getInt(R.styleable.SVGImageView_siv_srcGradientStart, start);
        end = typedArray.getInt(R.styleable.SVGImageView_siv_srcGradientEnd, end);
        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
        if (startColor != 0) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int[] startXY = getXY(start);
                    int[] endXY = getXY(end);
                    shader = new LinearGradient(startXY[0], startXY[1], endXY[0], endXY[1], startColor, endColor, Shader.TileMode.CLAMP);
                    logoPaint.setShader(shader);
                    logoPaint.setAntiAlias(true);
                    logoPaint.setStyle(Paint.Style.FILL);
                    return true;
                }
            });
        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (paths != null) {
            paths.clear();
            paths = null;
            svgUtils = null;
        }
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        logoPaint.setAntiAlias(true);
        logoPaint.setStyle(Paint.Style.FILL);
        if (color != 0)
            logoPaint.setColor(color);
        if (shadowRadius > 0)
            shadowPaint.setShadowLayer(shadowRadius, 0, shadowRadius / 2, Color.parseColor("#50000000"));

        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (svgUtils == null) {
            if (svgImageRecourse != -1) {
                svgUtils = new SvgUtils(logoPaint);
                size = Math.max(getWidth(), getHeight()) - shadowRadius;
                svgUtils.load(getContext(), svgImageRecourse);
                paths = svgUtils.getPathsForViewport(size, size);
            }
        }

        canvas.translate((getWidth() / 2) - (size / 2), (getHeight() / 2) - (size / 2));

        if (color == 0) {
            svgUtils.drawSvgAfter(canvas, size, size);
        } else {
            for (SvgUtils.SvgPath sPath : paths) {
                if (shadowRadius > 0)
                    canvas.drawPath(sPath.getPath(), shadowPaint);
                canvas.drawPath(sPath.getPath(), logoPaint);
            }
        }
    }

    public void setSvgImageRecourse(int svgImageRecourse) {
        this.svgImageRecourse = svgImageRecourse;
        if (size != -1) {
            if (paths != null)
                paths.clear();
            svgUtils = new SvgUtils(logoPaint);
            svgUtils.load(getContext(), svgImageRecourse);
            paths = svgUtils.getPathsForViewport(size, size);
            invalidate();
        }
    }

    public void setColor(int color) {
        this.color = color;
        logoPaint.setColor(color);
        invalidate();
    }

    private int[] getXY(int position) {
        int x = 0;
        int y = 0;
        switch (position) {
            case 0:
                x = getWidth() / 2;
                y = 0;
                break;
            case 1:
                x = getWidth();
                y = getHeight() / 2;
                break;
            case 2:
                x = getWidth() / 2;
                y = getHeight();
                break;
            case 3:
                x = 0;
                y = getHeight() / 2;
                break;
            case 4:
                x = 0;
                y = 0;
                break;
            case 5:
                x = getWidth();
                y = 0;
                break;
            case 6:
                x = 0;
                y = getHeight();
                break;
            case 7:
                x = getWidth();
                y = getHeight();
                break;
        }
        return new int[]{x, y};
    }
}

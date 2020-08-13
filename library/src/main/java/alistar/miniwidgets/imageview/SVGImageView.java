package alistar.miniwidgets.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;
import alistar.miniwidgets.R;

public class SVGImageView extends View {

    private int gradientStartColor = 0;
    private int gradientEndColor = 0;
    private int svgImageRecourse = -1;
    private SvgUtils svgUtils;
    private List<SvgUtils.SvgPath> paths;
    private Paint svgPaint = new Paint();
    private Paint shadowPaint = new Paint();
    private int size = -1;
    private int color = 0;
    private int shadowRadius = 0;
    private int start = 0, end = 2;
    private int shadowDy = 0, shadowDx;

    public SVGImageView(Context context) {
        super(context);
        init();
    }

    public SVGImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public SVGImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SVGImageView);
        svgImageRecourse = typedArray.getResourceId(R.styleable.SVGImageView_siv_src, 0);
        shadowRadius = typedArray.getDimensionPixelSize(R.styleable.SVGImageView_siv_srcShadowRadius, 0);
        color = typedArray.getColor(R.styleable.SVGImageView_siv_srcColor, 0);
        gradientStartColor = typedArray.getColor(R.styleable.SVGImageView_siv_srcGradientStartColor, 0);
        gradientEndColor = typedArray.getColor(R.styleable.SVGImageView_siv_srcGradientEndColor, 0);
        start = typedArray.getInt(R.styleable.SVGImageView_siv_srcGradientStart, start);
        end = typedArray.getInt(R.styleable.SVGImageView_siv_srcGradientEnd, end);
        typedArray.recycle();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        svgPaint.setAntiAlias(true);
        svgPaint.setStyle(Paint.Style.FILL);

        if (color != 0)
            svgPaint.setColor(color);

        if (shadowRadius > 0)
            svgPaint.setColor(color);

        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setGradientShader();
        initSvg();
    }

    private void initSvg() {
        if (svgImageRecourse != -1) {
            svgUtils = new SvgUtils(svgPaint);
            size = Math.max(getWidth(), getHeight()) - (shadowRadius * 2);
            svgUtils.load(getContext(), svgImageRecourse);
            paths = svgUtils.getPathsForViewport(size, size);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2f - size / 2f, getHeight() / 2f - size / 2f);

        if (color == 0 && gradientStartColor == 0 && gradientEndColor == 0) {
            svgUtils.drawSvgAfter(canvas, size, size);
        } else {
            for (SvgUtils.SvgPath sPath : paths) {
                if (shadowRadius > 0) {
                    canvas.save();
                    canvas.translate(shadowDx, shadowDy);
                    canvas.drawPath(sPath.getPath(), shadowPaint);
                    canvas.restore();
                }
                canvas.drawPath(sPath.getPath(), svgPaint);
            }
        }
    }

    public void setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;

        if (shadowRadius > 0)
            svgPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL));
        else
            svgPaint.setMaskFilter(null);

        initSvg();

        invalidate();
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public void setSvgImageRecourse(int svgImageRecourse) {
        this.svgImageRecourse = svgImageRecourse;
        if (size != -1) {
            if (paths != null)
                paths.clear();
            svgUtils = new SvgUtils(svgPaint);
            svgUtils.load(getContext(), svgImageRecourse);
            paths = svgUtils.getPathsForViewport(size, size);
            invalidate();
        }
    }

    public int getSvgImageRecourse() {
        return svgImageRecourse;
    }

    public void setColor(int color) {
        this.color = color;
        svgPaint.setShader(null);
        svgPaint.setColor(color);
        shadowPaint.setShader(null);
        shadowPaint.setColor(color);
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public int getGradientStartColor() {
        return gradientStartColor;
    }

    public void setGradientStartColor(int gradientStartColor) {
        this.gradientStartColor = gradientStartColor;
        setGradientShader();
        invalidate();
    }

    public int getGradientEndColor() {
        return gradientEndColor;
    }

    public void setGradientEndColor(int gradientEndColor) {
        this.gradientEndColor = gradientEndColor;
        setGradientShader();
        invalidate();
    }

    private void setGradientShader() {
        if (gradientStartColor != 0 && gradientEndColor != 0) {
            int[] startXY = getXY(start);
            int[] endXY = getXY(end);
            Shader shader = new LinearGradient(startXY[0], startXY[1], endXY[0], endXY[1],
                    gradientStartColor, gradientEndColor, Shader.TileMode.CLAMP);
            svgPaint.setShader(shader);
            svgPaint.setAntiAlias(true);
            svgPaint.setStyle(Paint.Style.FILL);

            shadowPaint.setShader(shader);
            shadowPaint.setAntiAlias(true);
            shadowPaint.setStyle(Paint.Style.FILL);
        }
    }

    public int getShadowDy() {
        return shadowDy;
    }

    public void setShadowDy(int shadowDy) {
        this.shadowDy = shadowDy;
        initSvg();
        invalidate();
    }

    public int getShadowDx() {
        return shadowDx;
    }

    public void setShadowDx(int shadowDx) {
        this.shadowDx = shadowDx;
        initSvg();
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

/**
 * Custom view that draws a rectangular overlay with padding and a transparent center.
 *
 * @constructor
 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
 */
class CoverView: View {

    /**
     * Paint object used for drawing.
     */
    private var paint: Paint

    /**
     * Background color of the overlay.
     */
    private var backColor: Int

    /**
     * Horizontal padding for the transparent center rectangle.
     */
    private var horizontalPadding: Int

    /**
     * Vertical padding for the transparent center rectangle.
     */
    private var verticalPadding: Int

    /**
     * Xfermode used to define how source and destination colors are combined.
     */
    private var porterDuffXfermode: PorterDuffXfermode

    /**
     * Rectangle defining the transparent center area.
     */
    private var rect: Rect

    /**
     * Constructor that initializes the view with the given context.
     *
     * @param context The Context the view is running in.
     */
    constructor(context: Context): this(context, null)

    /**
     * Constructor that initializes the view with the given context and attributes.
     *
     * @param context The Context the view is running in.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    /**
     * Constructor that initializes the view with the given context, attributes, and default style.
     *
     * @param context The Context the view is running in.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        // Initialization code
    }

    /**
     * Draws the view on the given canvas.
     *
     * @param canvas The Canvas to which the view is rendered.
     */
    override fun onDraw(canvas: Canvas?) {
        // Drawing code
    }
}

        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)

        rect.apply {
            left = horizontalPadding
            top = verticalPadding
            right = width.minus(horizontalPadding)
            bottom = height.minus(verticalPadding)

            paint.color = Color.TRANSPARENT
            canvas?.drawRect(this, paint)
        }

        if (layerId != null) {
            canvas.restoreToCount(layerId)
        }
    }
}
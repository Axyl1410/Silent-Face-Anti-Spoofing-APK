/**
 * A custom view that draws a rectangle with rounded corners and a confidence score text.
 *
 * @constructor
 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
 */
class RectView : View {

    // The confidence score to be displayed inside the rectangle.
    private var confidence: Float = 0F

    // The rectangle to be drawn.
    private var rect: RectF

    // The paint used to draw the rectangle and text.
    private var paint: Paint

    // The color of the rectangle.
    private var color: Int

    // The radius of the rounded corners of the rectangle.
    private var radius: Float

    // The padding around the text inside the rectangle.
    private var textPadding: Int

    // The format used to display the confidence score.
    private var decimalFormat: DecimalFormat = DecimalFormat("0.000")

    // The length of the lines used to draw the rectangle.
    private var lineLength: Float

    // Rectangles used to draw the rounded corners.
    private var leftTopAcrRectF: RectF = RectF()
    private var rightTopAcrRectF: RectF = RectF()
    private var leftBottomAcrRectF: RectF = RectF()
    private var rightBottomAcrRectF: RectF = RectF()

    // Rectangles used to draw the background and bounds of the text.
    private var textBackgroundRect: Rect = Rect()
    private var textBoundsRect: Rect = Rect()

    // The width and height of the text.
    private var textWidth: Int
    private var textHeight: Int

    /**
     * Sets the left coordinate of the rectangle.
     * @param v The new left coordinate.
     */
    fun setX1(v: Int) {
        rect.left = v.toFloat()
    }

    /**
     * Sets the right coordinate of the rectangle.
     * @param v The new right coordinate.
     */
    fun setX2(v: Int) {
        rect.right = v.toFloat()
    }

    /**
     * Sets the top coordinate of the rectangle.
     * @param v The new top coordinate.
     */
    fun setY1(v: Int) {
        rect.top = v.toFloat()
    }

    /**
     * Sets the bottom coordinate of the rectangle.
     * @param v The new bottom coordinate.
     */
    fun setY2(v: Int) {
        rect.bottom = v.toFloat()
    }

    /**
     * Sets the color of the rectangle.
     * @param v The new color.
     */
    fun setColor(v: Int) {
        color = v
        paint.color = color
    }

    /**
     * Sets the confidence score to be displayed inside the rectangle.
     * @param v The new confidence score.
     */
    fun setConfidence(v: Float) {
        confidence = v
    }

    /**
     * Draws the rectangle with rounded corners and the confidence score text.
     * @param canvas The Canvas to which the view is rendered.
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Drawing logic...
    }

    companion object {
        const val sampleText = "0.789"
        const val defaultLeft = 0
        const val defaultTop = 0
        const val defaultRight = 0
        const val defaultBottom = 0
        const val defaultTextSize = 50F
        const val defaultTextPadding = 8
        const val defaultRadius = 5F
        const val defaultLineLength = 45F
        const val defaultColor = Color.RED
    }
}
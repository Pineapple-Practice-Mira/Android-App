package site.pnpl.mira.ui.statistic.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import site.pnpl.mira.R
import site.pnpl.mira.ui.extensions.toPx


class FactorAnalysisView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var fixHeight: Int = 46.toPx
    private val bottomOffset = 12.toPx
    private var offsetEmotion: Int = 8.toPx
    private var emotionIconSize: Int = 46.toPx

    private var negativeCount: Int = 0
    private var positiveCount: Int = 0
    private var animationDuration: Long = 1000L

    @DrawableRes
    private var positiveSmileResId: Int = R.drawable.emotion_joy

    @DrawableRes
    private var negativeSmileResId: Int = R.drawable.emotion_sadness

    @DrawableRes
    private var arrowIcon: Int = R.drawable.icon_arrow_factor_view

    @ColorRes
    var colorPositive: Int = R.color.primary

    @ColorRes
    private var colorNegative: Int = R.color.primary_dark

    @ColorRes
    private var colorText: Int = R.color.white

    @ColorRes
    private var colorArrow: Int = R.color.white

    private var sizeText = 14f.toPx

    private var cornerRadius: Int = 18.toPx

    var factorName: String = ""

    init {

//        if (attrs != null) {
//            val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.FactorAnalysisView, 0, 0)
//
//            fixHeight = attributes.getInt(R.styleable.FactorAnalysisView_favHeight, fixHeight)
//            bottomOffset = attributes.getInt(R.styleable.FactorAnalysisView_favBottomOffset, bottomOffset)
//            offsetEmotion = attributes.getInt(R.styleable.FactorAnalysisView_favOffsetEmotion, offsetEmotion)
//            emotionIconSize = attributes.getInt(R.styleable.FactorAnalysisView_favEmotionIconSize, emotionIconSize)
//
//            positiveCount = attributes.getInt(R.styleable.FactorAnalysisView_favPositiveCount, positiveCount)
//            negativeCount = attributes.getInt(R.styleable.FactorAnalysisView_favNegativeCount, negativeCount)
//            animationDuration = attributes.getInt(R.styleable.FactorAnalysisView_favAnimationDuration, animationDuration.toInt()).toLong()
//
//            positiveSmileResId = attributes.getResourceId(R.styleable.FactorAnalysisView_favPositiveIconResId, positiveSmileResId)
//            negativeSmileResId = attributes.getResourceId(R.styleable.FactorAnalysisView_favNegativeIconResId, negativeSmileResId)
//            arrowIcon = attributes.getResourceId(R.styleable.FactorAnalysisView_favArrowResId, arrowIcon)
//
//            colorPositive = attributes.getResourceId(R.styleable.FactorAnalysisView_favColorPositive, colorPositive)
//            colorNegative = attributes.getResourceId(R.styleable.FactorAnalysisView_favColorNegative, colorNegative)
//            colorText = attributes.getResourceId(R.styleable.FactorAnalysisView_favColorText, colorText)
//            colorArrow = attributes.getResourceId(R.styleable.FactorAnalysisView_favColorArrow, colorArrow)
//
//            sizeText = attributes.getDimension(R.styleable.FactorAnalysisView_favTextSize, sizeText)
//            cornerRadius = attributes.getInt(R.styleable.FactorAnalysisView_favCornerRadius, cornerRadius)
//
//            factorName = attributes.getString(R.styleable.FactorAnalysisView_favFactorName) ?: factorName
//
//            attributes.recycle()
//
//        }

    }

    private var resolvedWidth = 0
    private var resolvedHeight = fixHeight

    private val positiveEmoji: Bitmap by lazy {
        ContextCompat.getDrawable(context, positiveSmileResId)?.toBitmap()!!
    }
    private val negativeEmoji: Bitmap by lazy {
        ContextCompat.getDrawable(context, negativeSmileResId)?.toBitmap()!!
    }
    private val arrow: Bitmap by lazy {
        ContextCompat.getDrawable(context, arrowIcon)?.toBitmap()!!
    }

    private var negativePaint = Paint()
    private var positivePaint = Paint()
    private var textPaint = Paint()
    private var arrowPaint = Paint()

    private val negativePath = Path()
    private val positivePath = Path()

    private var positiveRect = RectF()
    private var negativeRect = RectF()

    private var totalLengthForChart = 0f
    private var positivePercentage = 0f
    private var offsetWithEmotion = 0f

    private var animatorMaxValue = 100

    private var negativeCorners = floatArrayOf(
        0f, 0f,   // Top left radius in px
        cornerRadius.toFloat(), cornerRadius.toFloat(),   // Top right radius in px
        cornerRadius.toFloat(), cornerRadius.toFloat(),     // Bottom right radius in px
        0f, 0f      // Bottom left radius in px
    )
    private var positiveCorners = floatArrayOf(
        cornerRadius.toFloat(), cornerRadius.toFloat(),   // Top left radius in px
        0f, 0f,  // Top right radius in px
        0f, 0f,     // Bottom right radius in px
        cornerRadius.toFloat(), cornerRadius.toFloat(),      // Bottom left radius in px
    )

    private var resultText: String = ""

    constructor(
        context: Context,
        positiveCount: Int,
        negativeCount: Int,
        factorName: String,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : this(context, attrs, defStyleAttr) {

        initialization(positiveCount, negativeCount, factorName)
    }

    fun init(positiveCount: Int, negativeCount: Int, factorName: String, ) {
        initialization(positiveCount, negativeCount, factorName)
    }

    private fun initialization(positiveCount: Int, negativeCount: Int, factorName: String,) {
        this.positiveCount = positiveCount
        this.negativeCount = negativeCount
        this.factorName = factorName

        initPaints()
        initValues()
        invalidate()
    }

    private fun initValues() {
        doOnLayout {
            totalLengthForChart = width - (emotionIconSize * 2f + offsetEmotion * 2f)
            positivePercentage = positiveCount.toFloat() / (positiveCount + negativeCount)
            offsetWithEmotion = offsetEmotion.toFloat() + emotionIconSize

            if (negativeCount == 0) {
                positiveCorners = floatArrayOf(
                    cornerRadius.toFloat(), cornerRadius.toFloat(),   // Top left radius in px
                    cornerRadius.toFloat(), cornerRadius.toFloat(),  // Top right radius in px
                    cornerRadius.toFloat(), cornerRadius.toFloat(),     // Bottom right radius in px
                    cornerRadius.toFloat(), cornerRadius.toFloat(),      // Bottom left radius in px
                )
            }

            if (positiveCount == 0) {
                negativeCorners = floatArrayOf(
                    cornerRadius.toFloat(), cornerRadius.toFloat(),   // Top left radius in px
                    cornerRadius.toFloat(), cornerRadius.toFloat(),   // Top right radius in px
                    cornerRadius.toFloat(), cornerRadius.toFloat(),     // Bottom right radius in px
                    cornerRadius.toFloat(), cornerRadius.toFloat()      // Bottom left radius in px
                )
            }
        }
    }

    private fun initPaints() {

        negativePaint = Paint().apply {
            color = ContextCompat.getColor(context, colorNegative)
            style = Paint.Style.FILL
        }
        positivePaint = Paint().apply {
            color = ContextCompat.getColor(context, colorPositive)
            style = Paint.Style.FILL
        }
        textPaint = Paint().apply {
            color = ContextCompat.getColor(context, colorText)
            style = Paint.Style.FILL
            typeface = ResourcesCompat.getFont(context, R.font.wix_madefor_display_medium)
            textSize = sizeText
        }
        arrowPaint = Paint().apply {
            color = ContextCompat.getColor(context, colorArrow)
            style = Paint.Style.FILL
        }
    }

    fun startAnimation(maxValue: Int? = null) {
        if (maxValue != null) animatorMaxValue = maxValue
        if (positiveCount == 0) animatorMaxValue = 1
        val animator = ValueAnimator.ofInt(1, animatorMaxValue).apply {
            duration = animationDuration
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { valueAnimator ->
                recalculateDimensionsChart(valueAnimator.animatedValue as Int)
                recalculateResultString(valueAnimator.animatedValue as Int)
                invalidate()
            }
        }
        animator.start()
    }

    private fun recalculateDimensionsChart(percent: Int) {
        val positiveLength = (totalLengthForChart * positivePercentage) / animatorMaxValue * percent

        positiveRect.apply {
            left = offsetWithEmotion
            top = 0f
            right = offsetWithEmotion + positiveLength
            bottom = height.toFloat() - bottomOffset
        }

        positivePath.apply {
            reset()
            addRoundRect(positiveRect, positiveCorners, Path.Direction.CW)
        }

        if (negativeCount != 0) {
            negativeRect.apply {
                left = offsetWithEmotion + positiveLength
                top = 0f
                right = width - offsetWithEmotion
                bottom = height.toFloat() - bottomOffset
            }

            negativePath.apply {
                reset()
                addRoundRect(negativeRect, negativeCorners, Path.Direction.CW)
            }
        }
    }

    private fun recalculateResultString(percent: Int) {
        val totalEmotions = positiveCount + negativeCount
        val number = totalEmotions.toFloat() / animatorMaxValue * percent
        resultText = "$factorName (${number.toInt()})"
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //Считаем полный размер с паддингами
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) + paddingLeft + paddingRight
        //Получаем конечные размеры View, с учетом режима
        resolvedWidth = resolveSize(widthSize, widthMeasureSpec)
        //Устанавливаем итоговые размеры
        setMeasuredDimension(resolvedWidth, resolvedHeight + bottomOffset)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (negativeCount != 0) canvas.drawPath(negativePath, negativePaint)
        canvas.drawPath(positivePath, positivePaint)
        drawEmoji(canvas)
        drawTextCentered(width / 2, (height - bottomOffset) / 2, canvas)
    }

    private fun drawEmoji(canvas: Canvas) {
        canvas.drawBitmap(positiveEmoji, 0f, 0f, null)
        canvas.drawBitmap(negativeEmoji, width - emotionIconSize.toFloat(), 0f, null)
        canvas.drawBitmap(arrow, width - offsetWithEmotion - emotionIconSize, 0f, arrowPaint)
    }

    private fun drawTextCentered(x: Int, y: Int, canvas: Canvas) {
        val xPos = x - (textPaint.measureText(resultText) / 2).toInt()
        val yPos = (y - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
        canvas.drawText(resultText, xPos.toFloat(), yPos.toFloat(), textPaint)
    }

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
//        println("onSaveInstanceState positiveCount $positiveCount negativeCount $negativeCount factorName $factorName")
        return bundleOf(
            Pair(KEY_STATE, super.onSaveInstanceState()),
            Pair(KEY_POSITIVE_COUNT, positiveCount),
            Pair(KEY_NEGATIVE_COUNT, negativeCount),
            Pair(KEY_FACTOR_NAME, factorName),
            Pair(KEY_MAX_ANIMATOR_VALUE, animatorMaxValue)
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var viewState = state
        if (state != null && state is Bundle) {
            @Suppress("DEPRECATION")
            viewState = state.getParcelable(KEY_STATE)
            positiveCount = state.getInt(KEY_POSITIVE_COUNT)
            negativeCount = state.getInt(KEY_NEGATIVE_COUNT)
            factorName = state.getString(KEY_FACTOR_NAME).toString()
            animatorMaxValue = state.getInt(KEY_MAX_ANIMATOR_VALUE)
//            println("onRestoreInstanceState positiveCount $positiveCount negativeCount $negativeCount factorName $factorName")
            init(positiveCount, negativeCount, factorName)
            startAnimation(animatorMaxValue)
        }
        super.onRestoreInstanceState(viewState)
    }

    companion object {
        const val KEY_STATE = "KEY_STATE"
        const val KEY_POSITIVE_COUNT = "KEY_POSITIVE_COUNT"
        const val KEY_NEGATIVE_COUNT = "KEY_NEGATIVE_COUNT"
        const val KEY_FACTOR_NAME = "KEY_FACTOR_NAME"
        const val KEY_MAX_ANIMATOR_VALUE = "KEY_MAX_ANIMATOR_VALUE"
    }
}
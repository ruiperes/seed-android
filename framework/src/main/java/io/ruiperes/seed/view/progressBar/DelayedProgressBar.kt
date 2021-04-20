package io.ruiperes.seed.view.progressBar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat


/**
 * ContentLoadingProgressBar implements a ProgressBar that waits a minimum time to be
 * dismissed before showing. Once visible, the progress bar will be visible for
 * a minimum amount of time to avoid "flashes" in the UI when an event could take
 * a largely variable time to complete (from none, to a user perceivable amount)
 */
class DelayedProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs, 0) {
    private var mStartTime: Long = -1
    private var mPostedHide = false
    private var mPostedShow = false
    private var mDismissed = false
    private var animate = false
    private var showEndAction: Runnable? = null
    private var hideEndAction: Runnable? = null
    private var animator: ViewPropertyAnimatorCompat? = null
    private val mEmptyEndAction = Runnable { }
    private val mDelayedHide = Runnable {
        mPostedHide = false
        doHide(animate)
        mStartTime = -1
    }
    private val mDelayedShow = Runnable {
        mPostedShow = false
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis()
            if (animate) {
                alpha = 0f
                visibility = VISIBLE
                if (animator != null) {
                    animator?.cancel()
                }
                animator = ViewCompat.animate(this@DelayedProgressBar)
                    .alpha(1.0f)
                    .setInterpolator(AccelerateInterpolator())
                    .withEndAction(showEndAction)
            } else {
                visibility = VISIBLE
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val progressBar = ProgressBar(this.context)
        progressBar.apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        }

        this.addView(progressBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(mDelayedHide)
        mPostedHide = false
        removeCallbacks(mDelayedShow)
        mPostedShow = false
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @JvmOverloads
    fun hide(animate: Boolean = false, @NonNull endAction: Runnable? = mEmptyEndAction) {
        this.animate = animate
        hideEndAction = endAction
        mDismissed = true
        removeCallbacks(mDelayedShow)
        mPostedShow = false
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= MIN_SHOW_TIME || mStartTime == -1L) {
            doHide(animate)
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    private fun doHide(animate: Boolean) {
        // The progress spinner has been shown long enough
        // OR was not shown yet. If it wasn't shown yet,
        // it will just never be shown.
        if (animate && mStartTime != -1L) {
            if (animator != null) {
                animator?.cancel()
            }
            animator = ViewCompat.animate(this)
                .alpha(0f).withEndAction {
                    visibility = GONE
                    hideEndAction!!.run()
                }
        } else {
            visibility = GONE
            hideEndAction!!.run()
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @JvmOverloads
    fun show(animate: Boolean = false, @NonNull endAction: Runnable? = mEmptyEndAction) {
        this.animate = animate
        showEndAction = endAction
        // Reset the start time.
        mStartTime = -1
        mDismissed = false
        removeCallbacks(mDelayedHide)
        mPostedHide = false
        if (!mPostedShow) {
            postDelayed(mDelayedShow, MIN_DELAY.toLong())
            mPostedShow = true
        }
    }

    companion object {
        private const val MIN_SHOW_TIME = 100 // ms
        private const val MIN_DELAY = 100 // ms
    }
}

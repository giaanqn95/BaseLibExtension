package vn.com.baselibextension.extensions

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by antdg-intelin on 09/06/2020.
 */

private val View.viewFinder: View.(Int) -> View?
    get() = { findViewById(it) }
private val Activity.viewFinder: Activity.(Int) -> View?
    get() = { findViewById(it) }
private val RecyclerView.ViewHolder.viewFinder: RecyclerView.ViewHolder.(Int) -> View?
    get() = { itemView.findViewById(it) }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(
    id: Int,
    finder: T.(Int) -> View?,
    onInitializedListener: ((V) -> Unit)? = null
) = Lazy { t: T, desc ->
    t.finder(id) as V? ?: viewNotFound(id, desc)
}.apply { this.onInitializedListener = onInitializedListener }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")

private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V>,
    LifecycleObserver {

    var onInitializedListener: ((V) -> Unit)? = null

    private object EMPTY

    private var value: Any? = EMPTY
    private var attachedToLifecycleOwner = false

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        checkAddToLifecycleOwner(thisRef)
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        val v = value as V

        onInitializedListener?.invoke(v)
        return v
    }

    private fun checkAddToLifecycleOwner(thisRef: T) {
        if (!attachedToLifecycleOwner && thisRef is LifecycleOwner) {
            thisRef.lifecycle.addObserver(this)
            attachedToLifecycleOwner = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun destroy() {
        value = EMPTY
    }
}

fun <V : View> View.bindView(id: Int)
        : ReadOnlyProperty<View, V> = required(id, viewFinder)

fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> = required(id, viewFinder)

fun <V : View> RecyclerView.ViewHolder.bindView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V> = required(id, viewFinder)


///////////////////////////////////////////////////////////////////////////
// Colors & Dimensions
///////////////////////////////////////////////////////////////////////////

fun View.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context, id)
}

fun View.bindDimen(@DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.resources.getDimensionPixelSize(id)
}

fun View.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.getString(id)
}

fun Activity.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(this, id)
}

fun Activity.bindDimen(@DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    resources.getDimensionPixelSize(id)
}

fun Activity.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    getString(id)
}

fun Fragment.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context!!, id)
}

fun Fragment.bindDimen(@DimenRes id: Int): kotlin.Lazy<Int> = lazy(LazyThreadSafetyMode.NONE) {
    context!!.resources.getDimensionPixelSize(id)
}

fun Fragment.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context!!.getString(id)
}

fun Any.bindColor(context: Context, @ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context, id)
}

fun Any.bindDimen(context: Context, @DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.resources.getDimension(id)
}

fun Any.bindString(context: Context, @StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.getString(id)
}

fun Any.bindColor(view: View, @ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(view.context, id)
}

fun Any.bindDimen(view: View, @DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    view.context.resources.getDimension(id)
}

fun Any.bindString(view: View, @StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    view.context.getString(id)
}

fun View.bindDrawableToBitMap(context: Context, @DrawableRes id: Int)= lazy(LazyThreadSafetyMode.NONE)  {
    ContextCompat.getDrawable(context, id)?.toBitmap()
}

inline fun getValueAnimator(
    forward: Boolean = true,
    duration: Long,
    interpolator: TimeInterpolator,
    crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a =
        if (forward) ValueAnimator.ofFloat(0f, 1f)
        else ValueAnimator.ofFloat(1f, 0f)
    a.addUpdateListener { updateListener(it.animatedValue as Float) }
    a.duration = duration
    a.interpolator = interpolator
    return a
}

inline fun getValueAnimatorColor(
    forward: Boolean = true,
    duration: Long,
    colorFrom: Int,
    colorTo: Int,
    view: ImageView,
    crossinline updateListener: (progress: Int) -> Unit
): ValueAnimator {
    val animator =
        if (forward) ObjectAnimator.ofInt(view, "colorFilter", colorFrom, colorTo)
        else ObjectAnimator.ofInt(view, "colorFilter", colorTo, colorFrom)
    animator.addUpdateListener { animator ->
        updateListener(animator.animatedValue as Int)
    }
    animator.setEvaluator(ArgbEvaluator())
    animator.duration = duration
    animator.interpolator = DecelerateInterpolator(2f)
    return animator
}
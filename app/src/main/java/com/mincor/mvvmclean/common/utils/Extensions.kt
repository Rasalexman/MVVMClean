package com.mincor.mvvmclean.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.android.material.appbar.AppBarLayout
import com.mincor.mvvmclean.BuildConfig
import com.mincor.mvvmclean.viewmodel.factory.ViewModelFactory
import group.infotech.drawable.dsl.shapeDrawable
import group.infotech.drawable.dsl.solidColor
import group.infotech.drawable.dsl.stroke
import org.jetbrains.anko.dip
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

data class ScrollPosition(var index: Int = 0, var top: Int = 0) {
    fun drop() {
        index = 0
        top = 0
    }
}

inline fun log(lambda: () -> String?) {
    if (BuildConfig.DEBUG) {
        Log.d("----------->", lambda() ?: "")
    }
}

/***
 * Custom View For somethings like lines
 **/
fun roundedBg(
    col: Int,
    corners: Float = 1000f,
    withStroke: Boolean = false,
    strokeColor: Int = Color.LTGRAY,
    strokeWeight: Int = 2
) = shapeDrawable {
    shape = GradientDrawable.RECTANGLE
    solidColor = col
    cornerRadius = corners

    if (withStroke) {
        stroke {
            width = strokeWeight
            color = strokeColor
        }
    }
}

fun Button.scalableOnTouch(scaleFactor: Float = 0.95f) {
    this.onTouch { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.scaleX = scaleFactor
                v.scaleY = scaleFactor
            }
            MotionEvent.ACTION_UP -> {
                v.scaleX = 1f
                v.scaleY = 1f
            }
        }
    }
}


/**
 * UTILS SECTION
 * */
fun View.drawable(@DrawableRes resource: Int): Drawable? = ContextCompat.getDrawable(context, resource)

fun View.color(@ColorRes resource: Int): Int = ContextCompat.getColor(context, resource)
fun View.string(stringRes: Int): String = context.getString(stringRes)
fun View.wdthProc(proc: Float): Int = (context.displayMetrics.widthPixels * proc).toInt()
fun Context.hdthProc(proc: Float): Int = (this.displayMetrics.heightPixels * proc).toInt()
fun View.hdthProc(proc: Float): Int = (context.displayMetrics.heightPixels * proc).toInt()
fun View.hdthFrom(parent: View, proc: Float): Int = (parent.height * proc).toInt()
fun String.fromHTML(): Spanned {
    return if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this)
    else Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}

fun Controller.stringLazy(@StringRes stringRes: Int): Lazy<String> = lazy { this.string(stringRes) }
fun Controller.string(@StringRes stringRes: Int): String = this.resources?.getString(stringRes).orEmpty()
fun Controller.colorLazy(@ColorRes colorRes: Int): Lazy<Int> = lazy { color(colorRes) }
fun Controller.color(@ColorRes colorRes: Int) = applicationContext?.let { ContextCompat.getColor(it, colorRes) } ?: 0

fun gradientBg(
    colors: Array<Int>,
    orient: GradientDrawable.Orientation = GradientDrawable.Orientation.BOTTOM_TOP,
    corners: Float = 0f,
    withStroke: Boolean = false,
    strokeColor: Int = Color.LTGRAY,
    strokeWeight: Int = 2
): GradientDrawable = GradientDrawable(orient, colors.toIntArray()).apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = corners
    if (withStroke) setStroke(strokeWeight, strokeColor)
}

/**
 * GLIDE IMAGE LOADING
 * */
typealias IPhotoLoaderListener = (Bitmap?) -> Unit

val Context.glide: GlideRequests
    get() = GlideApp.with(this.applicationContext)

fun ImageView.load(path: String, progress: ProgressBar? = null, loaderHandler: IPhotoLoaderListener? = null) {
    // мы не загружаем пустые картинки
    if (path.isEmpty()) return

    val layParams = this.layoutParams
    progress?.show()

    if (path.contains(".gif")) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(layParams.width, layParams.height)

        val reqListener = object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.hide(true)
                loaderHandler?.let { it(null) }
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.hide(true)
                loaderHandler?.let { it(null) }
                return false
            }
        }
        context.glide.asGif().load(path).listener(reqListener).apply(requestOptions).into(this)
    } else {
        val requestOptions = RequestOptions().dontTransform()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .dontAnimate()
            .override(layParams.width, layParams.height)
            .encodeFormat(Bitmap.CompressFormat.WEBP)
            .format(DecodeFormat.PREFER_RGB_565)

        val reqListener = object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.hide(true)
                loaderHandler?.let { it(null) }
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progress?.hide(true)
                loaderHandler?.let { it(resource) }
                return false
            }
        }
        context.glide.asBitmap().load(path).listener(reqListener).apply(requestOptions).into(this)
    }
}

fun ImageView.load(pathRes: Int) {
    context.glide.load(pathRes).into(this)
}

fun ImageView.load(bitmapRes: Bitmap) {
    context.glide.load(bitmapRes).into(this)
}

fun ImageView.load(
    urlStr: String,
    transformation: BitmapTransformation,
    loaderHandler: IPhotoLoaderListener? = null,
    progress: ProgressBar? = null
) {
    // мы не загружаем пустые картинки
    if (urlStr.isEmpty()) return

    val reqListener = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            progress?.hide(true)
            loaderHandler?.let { it(null) }
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progress?.hide(true)
            this@load.setImageBitmap(resource)
            loaderHandler?.let { it(resource) }
            return true
        }
    }
    context.glide.asBitmap().load(urlStr).listener(reqListener).apply(bitmapTransform(transformation))
        .into(this) // .transform(transformation)
}

fun ImageView.clear(isOnlyImage: Boolean = false) {
    try {
        this.setImageResource(0)
        this.setImageBitmap(null)
        this.setImageDrawable(null)
        if (isOnlyImage) this.setOnClickListener(null)
        clearByGlide()
    } catch (ex: IllegalArgumentException) {
        log { "IMAGE Clear $this has an IllegalArgumentException, ${this.tag}" }
    }
}

fun ImageView.clearByGlide() {
    try {
        context.glide.clear(this)
    } catch (ex: IllegalArgumentException) {
        log { "GLIDE Clear $this has an IllegalArgumentException, ${this.tag}" }
    }
}

fun Button.clear(isClearText: Boolean = true) {
    if (isClearText) this.text = null
    this.setOnClickListener(null)
}

fun TextView.clear() {
    this.text = null
    this.setOnClickListener(null)
}

fun CheckBox.clear() {
    this.setOnCheckedChangeListener(null)
}

fun ViewGroup.clear() {
    var childView: View
    repeat(this.childCount) {
        childView = this.getChildAt(it)
        when (childView) {
            is ViewGroup -> (childView as ViewGroup).clear()
            is ImageView -> (childView as ImageView).clear()
            is Button -> (childView as Button).clear()
            is TextView -> (childView as TextView).clear()
            is CheckBox -> (childView as CheckBox).clear()
        }
    }
}

fun ViewGroup.childrenVisible(visible: Boolean = true) {
    repeat(this.childCount) {
        val child = this.getChildAt(it)
        if(child !is AppBarLayout)
            child.visible = visible
    }
}

var View.visible
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) GONE else INVISIBLE
}

fun View.show() {
    visibility = VISIBLE
}

/**
 * Оптимизированные отступы
 */
private var dip8: Int = 0
private var dip16: Int = 0
fun View.dip8(): Int {
    if (dip8 == 0) {
        dip8 = dip(8)
    }
    return dip8
}

fun View.dip16(): Int {
    if (dip16 == 0) {
        dip16 = dip(16)
    }
    return dip16
}

fun <T : Controller> Router?.pushController(
    controller: T,
    pushChangeHandler: HorizontalChangeHandler = HorizontalChangeHandler(),
    popChangeHandler: HorizontalChangeHandler = HorizontalChangeHandler()
) {
    this?.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(pushChangeHandler).popChangeHandler(popChangeHandler)
    )
}

fun <T : Controller> Controller.pushController(
    controller: T,
    pushChangeHandler: HorizontalChangeHandler = HorizontalChangeHandler(),
    popChangeHandler: HorizontalChangeHandler = HorizontalChangeHandler()
) {
    this.router?.pushController(controller, pushChangeHandler, popChangeHandler)
}

fun Controller.popCurrentController() {
    this.router?.popCurrentController()
}

/**
 * KODEIN EXT
 **/
inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : LifecycleController {
    return lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this.activity as AppCompatActivity, direct.instance<ViewModelFactory>())[VM::class.java]
    }
}

/**
 * LIVE DATA
 */
fun <T> LiveData<T>.getDistinct(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null
        override fun onChanged(obj: T?) {
            if (!initialized) {
                initialized = true
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            } else if ((obj == null && lastObj != null)
                || obj != lastObj) {
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}

inline fun <T> T.applyIf(ifValue:Boolean, block: T.() -> Unit): T {
    if(ifValue) block(this)
    return this
}

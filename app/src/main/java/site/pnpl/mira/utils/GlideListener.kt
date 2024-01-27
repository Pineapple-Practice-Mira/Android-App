package site.pnpl.mira.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object GlideListener_ {

    object OnCompletedGif : RequestListener<GifDrawable> {

        private lateinit var onComplete: () -> Unit

        operator fun invoke(onComplete: () -> Unit): OnCompletedGif {
            OnCompletedGif.onComplete = { onComplete() }
            return this
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>, isFirstResource: Boolean): Boolean {
            onComplete()
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable,
            model: Any,
            target: Target<GifDrawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            onComplete()
            return false
        }
    }

    object OnCompletedDrawable : RequestListener<Drawable> {

        private lateinit var onComplete: () -> Unit

        operator fun invoke(onComplete: () -> Unit): OnCompletedDrawable {
            OnCompletedDrawable.onComplete = { onComplete() }
            return this
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            onComplete()
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            onComplete()
            return false
        }
    }
}

class GlideListener {

    object OnCompletedGif : RequestListener<GifDrawable> {

        private lateinit var onComplete: () -> Unit

        operator fun invoke(onComplete: () -> Unit): OnCompletedGif {
            OnCompletedGif.onComplete = { onComplete() }
            return this
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>, isFirstResource: Boolean): Boolean {
            onComplete()
            return false
        }

        override fun onResourceReady(
            resource: GifDrawable,
            model: Any,
            target: Target<GifDrawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            onComplete()
            return false
        }
    }

    object OnCompletedDrawable : RequestListener<Drawable> {

        private lateinit var onComplete: () -> Unit

        operator fun invoke(onComplete: () -> Unit): OnCompletedDrawable {
            OnCompletedDrawable.onComplete = { onComplete() }
            return this
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            onComplete()
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            onComplete()
            return false
        }
    }
}
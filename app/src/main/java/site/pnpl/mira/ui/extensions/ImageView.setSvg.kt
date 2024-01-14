package site.pnpl.mira.ui.extensions

import android.widget.ImageView
import com.pixplicity.sharp.Sharp
import java.io.File

fun ImageView.setSvg(path: String) {
    try {
        val svg = Sharp.loadFile(File(path))
        svg.into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
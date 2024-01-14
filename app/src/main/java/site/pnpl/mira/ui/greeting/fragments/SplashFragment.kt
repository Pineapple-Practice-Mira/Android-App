package site.pnpl.mira.ui.greeting.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.pixplicity.sharp.Sharp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import site.pnpl.mira.App
import site.pnpl.mira.R
import site.pnpl.mira.domain.SettingsProvider
import site.pnpl.mira.databinding.FragmentSplashBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

//        val gifDrawable = GifDrawable(resources, R.drawable.gif_logo).apply {
//            addAnimationListener {
//                if (it == 0) {
//                    if (settingsProvider.isFirstLaunch()) {
//                        findNavController().navigate(R.id.action_splashFragment2_to_greeting_graph)
//                    } else {
//                        findNavController().navigate(R.id.action_splash_to_navigation_home)
//                    }
//                }
//            }
//        }

//        FragmentSplashBinding.bind(view).animation.setImageDrawable(gifDrawable)
        val svgImageSaver = SvgImageSaver(requireContext())

        val imageUrl = "https://svgur.com/i/11t3.svg"
        val fileName = "your_svg_image.svg"

        lifecycleScope.launch(Dispatchers.Main) {
            val filePath = svgImageSaver.saveSvgImage(imageUrl, fileName)
            println(filePath)

            if (!filePath.isNullOrEmpty()) {
                val imageView: ImageView = FragmentSplashBinding.bind(view).animation
                svgImageSaver.loadSvgIntoImageView(filePath, imageView)
            } else {
                println("ошибочка вышла")
            }
        }
    }
}
//https://svgur.com/i/11t3.svg
class SvgImageSaver(private val context: Context) {

    suspend fun saveSvgImage(url: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)

                // Открываем InputStream из URL
                val inputStream = URL(url).openStream()

                // Сохраняем InputStream в файл
                saveInputStreamToFile(inputStream, file)

                // Возвращаем путь к сохраненному файлу
                file.absolutePath
            } catch (e: Exception) {
                println("start")
                e.printStackTrace()
                println("end")
                null
            }
        }
    }

    private fun saveInputStreamToFile(inputStream: InputStream, file: File) {
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    suspend fun loadSvgIntoImageView(filePath: String, imageView: ImageView) {
        withContext(Dispatchers.Main) {
            try {
                val svg = Sharp.loadFile(File(filePath))
                svg.into(imageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

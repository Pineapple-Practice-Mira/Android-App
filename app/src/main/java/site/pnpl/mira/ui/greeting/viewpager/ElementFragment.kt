package site.pnpl.mira.ui.greeting.viewpager

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.pnpl.mira.databinding.FragmentElementBinding

const val ARG_OBJECT = "element"
class ElementFragment : Fragment() {
    private var _binding: FragmentElementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentElementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { bundle ->
            bundle.containsKey(ARG_OBJECT)
        }?.apply {

            val vpElement: VpElement = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARG_OBJECT, VpElement::class.java)!!
            } else {
                getParcelable<VpElement>(ARG_OBJECT)!!
            }

            with(binding) {
                animation.setAnimation(vpElement.animation)
                title.text = resources.getString(vpElement.title)
                text.text = resources.getString(vpElement.text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
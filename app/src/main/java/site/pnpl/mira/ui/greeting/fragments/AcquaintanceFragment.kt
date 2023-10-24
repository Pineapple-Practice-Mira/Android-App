package site.pnpl.mira.ui.greeting.fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import site.pnpl.mira.App
import site.pnpl.mira.ui.greeting.viewpager.VpAdapter
import site.pnpl.mira.ui.greeting.viewpager.VpElement
import site.pnpl.mira.ui.greeting.viewpager.VpElementsGenerator
import site.pnpl.mira.ui.main.MainActivity
import site.pnpl.mira.R
import site.pnpl.mira.data.SettingsProvider
import site.pnpl.mira.databinding.FragmentAcquaintanceBinding
import javax.inject.Inject

class AcquaintanceFragment : Fragment() {
    private var _binding: FragmentAcquaintanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VpAdapter
    private lateinit var viewPager: ViewPager2
    private val vpElements: List<VpElement> = VpElementsGenerator.generate()

    @Inject lateinit var settingsProvider: SettingsProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcquaintanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.appComponent.inject(this)

        adapter = VpAdapter(requireActivity(), vpElements)
        viewPager = binding.viewPager
        viewPager.adapter = adapter

        binding.indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)

        binding.btnSkip.paintFlags = (binding.btnSkip.paintFlags and Paint.UNDERLINE_TEXT_FLAG)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                with(binding) {
                    btnPrevious.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                    btnNext.visibility = if (position == vpElements.size - 1) View.INVISIBLE else View.VISIBLE

                    btnSkip.text = if (position == vpElements.size - 1) resources.getString(R.string.complete) else resources.getString(R.string.skip)
                    btnSkip.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
            }
        })
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnNext.setOnClickListener {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }

            btnPrevious.setOnClickListener {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            }

            btnSkip.setOnClickListener {
                settingsProvider.firstLaunchCompleted()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
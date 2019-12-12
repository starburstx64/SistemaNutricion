package com.teampermanente.sistemanutricion.ui.main.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.teampermanente.sistemanutricion.R

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery) as TextView
        galleryViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(root.context, activity!!.supportFragmentManager)
        val viewPager: ViewPager = root.findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = root.findViewById(R.id.tabs) as TabLayout
        tabs.setupWithViewPager(viewPager)

        return root
    }
}
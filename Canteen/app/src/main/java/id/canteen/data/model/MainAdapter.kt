package id.canteen.data.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.canteen.ui.gallery.GalleryFragment
import id.canteen.ui.home.HomeFragment

class MainAdapter (fm:FragmentManager): FragmentPagerAdapter(fm){

    private val fragments = arrayOf(
        HomeFragment(),
        GalleryFragment()
    )

    private val title = arrayOf(
        "Fragment 1",
        "Fragment 2"
    )
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = title[position]

}
package id.canteen

//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayoutMediator
//import id.canteen.ui.gallery.GalleryFragment
//import id.canteen.ui.home.HomeFragment
//import kotlinx.android.synthetic.main.app_bar_main.*
//
//@SuppressLint("Registered")
//class ViewPager2 : FragmentActivity() {
//    private lateinit var mpager: ViewPager2
//    val NM_PAGER = 2
//    val name_tab = arrayOf("Home", "Galery")
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.app_bar_main)
//
//        val pagerAdapter = ScreenSlidePagerAdapter(this)
//        view_pager.adapter = pagerAdapter
//
//        val tabLayoutMediator = TabLayoutMediator(tab, view_pager, true,
//            TabLayoutMediator.OnConfigureTabCallback { tab, position ->
//
//            })
//        tabLayoutMediator.attach();
//
//        for (i in 0..1) {
//            tab.getTabAt(i)!!.setText(name_tab.get(i));
//        }
//
//    }
//
//    override fun onBackPressed() {
//        if (view_pager.currentItem == 0) {
//            super.onBackPressed()
//        }else {
//            view_pager.currentItem = mpager.currentItem - 1
//        }
//    }
//
//    private inner class ScreenSlidePagerAdapter(fa : FragmentActivity) : FragmentStateAdapter(fa){
//        override fun createFragment(position: Int): Fragment {
//            if (position == 0){
//                return HomeFragment()
//            }else{
//                return GalleryFragment()
//            }
//        }
//
//        override fun getItemCount(): Int  = NM_PAGER
//    }
//}



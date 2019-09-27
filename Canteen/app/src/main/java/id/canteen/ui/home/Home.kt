package id.canteen.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.canteen.MenuActivity
import id.canteen.R
import kotlinx.android.synthetic.main.fragment_home.*


class Home : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_home)
//        daftar.setOnClickListener {
//            val i = Intent(this, MenuActivity::class.java)
//            startActivity(i)
//        }
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
//        val onClick: RelativeLayout = root.findViewById(R.id.daftar)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
            daftar.setOnClickListener {
                startActivity(Intent(activity, MenuActivity::class.java))
            }
        })
        return root
    }

}

//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        setContentView(R.layout.fragment_home)
//        daftar.setOnClickListener {
////            startActivity(Intent(contex,MenuActivity::javaClass))
//        }
//    }
//
//}
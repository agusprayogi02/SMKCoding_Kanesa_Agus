package id.canteen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.*
import id.canteen.R
import id.canteen.data.Warung
import id.canteen.data.AdapterWarung


class Home : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var ref: DatabaseReference
    private lateinit var listW : MutableList<Warung>
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.saldoku)
//        val onClick: RelativeLayout = root.findViewById(R.id.daftar)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
            ref = FirebaseDatabase.getInstance().getReference("Warung")
            listW = mutableListOf()
            listView = root.findViewById(R.id.list_Warung)
            ref.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        listW.clear()
                        for (v in p0.children){
                            val war = v.getValue(Warung::class.java)
                            listW.add(war!!)
                        }
                        val adapter = AdapterWarung(root.context,R.layout.list_warung,listW)
                        listView.adapter = adapter
                    }
                }

            })

        })
        return root
    }

}
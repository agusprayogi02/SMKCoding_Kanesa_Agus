package id.canteen.ui.histori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.canteen.R

class HistoriFragment : Fragment() {

    private lateinit var historiViewModel: HistoriViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historiViewModel =
            ViewModelProviders.of(this).get(HistoriViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_histori, container, false)
        val textView: TextView = root.findViewById(R.id.text_tools)
        historiViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}
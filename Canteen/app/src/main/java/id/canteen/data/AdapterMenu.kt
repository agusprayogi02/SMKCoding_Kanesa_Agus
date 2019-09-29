package id.canteen.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import id.canteen.R
import kotlinx.android.synthetic.main.activity_menu.view.*

class  Adapter(val mCtx: Context, val layoutResId: Int, val list: List<Menu> )
    : ArrayAdapter<Menu>(mCtx,layoutResId,list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

//        val image = view.findViewById<ImageView>(R.id.image_menu)
        val nama = view.findViewById<TextView>(R.id.name_menu)
        val harga = view.findViewById<TextView>(R.id.name_menu)

        val menu = list[position]

//        image.text = user.image
        nama.text = menu.nama
        harga.text = menu.harga

        return view
    }

}
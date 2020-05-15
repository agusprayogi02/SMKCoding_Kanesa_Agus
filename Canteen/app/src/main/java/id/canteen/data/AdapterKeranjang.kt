package id.canteen.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import id.canteen.R

class AdapterKeranjang(val mCtx: Context, val layoutId: Int, val list: List<Menus>) :
    ArrayAdapter<Menus>(mCtx, layoutId, list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val LytInf: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = LytInf.inflate(layoutId, null)
        val image = view.findViewById<CircleImageView>(R.id.ItemImage)
        val ItemName = view.findViewById<TextView>(R.id.ItemName)
        val ItemH = view.findViewById<TextView>(R.id.ItemHarga)
        val ItemJ = view.findViewById<TextView>(R.id.ItemJumlah)
        val btnPlus = view.findViewById<Button>(R.id.ItemPlus)
        val btnMin = view.findViewById<Button>(R.id.ItemMinus)

        btnPlus.setOnClickListener {
            val jum = ItemJ.text.toString().toInt() + 1
            ItemJ.text = jum.toString()
        }

        btnMin.setOnClickListener {
            var jum = ItemJ.text.toString().toInt()
            if (jum >= 0){
                jum += 1
                ItemJ.text = jum.toString()
            }
        }

        val menu = list[position]
        Glide.with(context).load(menu.image)
            .apply(RequestOptions())
            .into(image)
        ItemName.text = menu.nama
        ItemH.text = menu.harga



        return view
    }
}
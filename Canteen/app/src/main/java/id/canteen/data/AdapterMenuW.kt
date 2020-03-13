package id.canteen.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import id.canteen.R

@SuppressLint("ParcelCreator")
class AdapterMenuW(val mCtx: Context, val layoutResId: Int, val list: List<Menus>) :
    ArrayAdapter<Menus>(mCtx, layoutResId, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val img = view.findViewById<ImageView>(R.id.imgMenu)
        val nama = view.findViewById<TextView>(R.id.nmMenu)
        val harga = view.findViewById<TextView>(R.id.hrgMenu)
        val btn = view.findViewById<Button>(R.id.btnMenu)

        val menu = list[position]

//        Picasso.get().load(menu.Url).into(img)
        Glide.with(context).load(menu.Url)
            .apply(RequestOptions())
            .into(img)
        nama.text = menu.nama
        harga.text = menu.harga

        btn.setOnClickListener {

        }

        return view

    }


}
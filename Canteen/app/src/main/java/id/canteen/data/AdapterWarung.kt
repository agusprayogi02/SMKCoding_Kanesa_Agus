package id.canteen.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import id.canteen.DafWarung
import id.canteen.R
import id.canteen.session.Session
import android.widget.ArrayAdapter as ArrayAdapter1


@SuppressLint("ParcelCreator")
class AdapterWarung(val mCtx: Context, val layoutResId: Int, val list: List<Warung> )
    : ArrayAdapter1<Warung>(mCtx,layoutResId,list), Parcelable {


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val img = view.findViewById<ImageView>(R.id.imgWarung)
        val nama = view.findViewById<TextView>(R.id.nmWarung)
        val item = view.findViewById<CardView>(R.id.item)

        val warung = list[position]

        Picasso.get().load(warung.link).into(img)
        nama.text = "Warung ${warung.Nama}"

        item.setOnClickListener {
            Session.Session(mCtx)
            Session["Nama"] = warung.idUser
            val intent = Intent(context, DafWarung::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        return view

    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
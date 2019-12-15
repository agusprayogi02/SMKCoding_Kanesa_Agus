package id.canteen.data

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import id.canteen.R
import com.squareup.picasso.Picasso
import id.canteen.MenuActivity

class  AdapterMenu(val mCtx: Context, val layoutResId: Int, val list: List<Menus> )
    : ArrayAdapter<Menus>(mCtx,layoutResId,list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val alertDialog = AlertDialog.Builder(mCtx)
        alertDialog.setTitle("Edit Data!!")

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

//        val image = view.findViewById<ImageView>(R.id.image_menu)
        val nama = view.findViewById<TextView>(R.id.name_menu)
        val harga = view.findViewById<TextView>(R.id.harga_menu)
        val image = view.findViewById<ImageView>(R.id.image_menu)
        val btn = view.findViewById<ImageButton>(R.id.btn_Beli)

        val menu = list[position]

        Picasso.get().load(menu.Url).into(image)
        nama.text = menu.nama
        harga.text = menu.harga
        btn.setOnClickListener {
            val fram = LayoutInflater.from(mCtx)
            val inflata = fram.inflate(R.layout.update_brg,null)
            val upname = inflata.findViewById<EditText>(R.id.upUser)
            val upharga = inflata.findViewById<EditText>(R.id.upPass)

            upname.setText(menu.nama)
            upharga.setText(menu.harga)

            alertDialog.setView(inflata)

            alertDialog.setNegativeButton("Batal"){dialog, which ->
                Toast.makeText(mCtx,"perubahan dibatalkan!!",Toast.LENGTH_SHORT).show()
            }

            alertDialog.setPositiveButton("Ubah"){dialog, which ->
                val dbUsers = FirebaseDatabase.getInstance().getReference("Menus")
                val nama = upname.text.toString().trim()
                val harga = upharga.text.toString().trim()

                if (nama.isEmpty()){
                    upname.error = "please enter name"
                    upname.requestFocus()
                    return@setPositiveButton
                }

                if (harga.isEmpty()){
                    upharga.error = "please enter status"
                    upharga.requestFocus()
                    return@setPositiveButton
                }

                val user = Menus(menu.id,menu.idUser,menu.War,menu.Url,menu.image,nama,harga)

                dbUsers.child(menu.id).setValue(user).addOnCompleteListener {
                    Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
                }
            }

            alertDialog.create().show()

        }

        return view
    }

}
package id.canteen.data

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.canteen.R
import com.squareup.picasso.Picasso


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
        val delete = view.findViewById<ImageButton>(R.id.btn_delete)

        val menu = list[position]

        Picasso.get().load(menu.Url).into(image)
        nama.text = menu.nama
        harga.text = menu.harga

        delete.setOnClickListener {
            alertDialog.setTitle("Apakah anda ingin Menghapus data "+menu.nama+" ??")
            alertDialog.setNegativeButton("Batal"){dialog, which ->
                Toast.makeText(mCtx,"dibatalkan!!",Toast.LENGTH_SHORT).show()
            }

            alertDialog.setPositiveButton("Hapus"){dialog, which ->
                val progressDialog = ProgressDialog(
                    mCtx,
                    R.style.Theme_MaterialComponents_Light_Dialog
                )
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Pembuatan...")
                progressDialog.show()
                val storageRef = FirebaseStorage.getInstance().reference
                val desertRef = storageRef.child("images/Menu/${menu.image}")
                desertRef.delete().addOnSuccessListener {
                    val delete = FirebaseDatabase.getInstance().getReference("Menus")
                    delete.child(menu.id).removeValue()
                    Toast.makeText(mCtx,"data berhasil dihapus!!",Toast.LENGTH_SHORT).show()
                    progressDialog.hide()
                }.addOnFailureListener {
                    // Uh-oh, an error occurred!
                    Toast.makeText(mCtx,"Gagal Menghapus !!"+it,Toast.LENGTH_SHORT).show()
                    progressDialog.hide()
                }

            }
            alertDialog.create().show()

        }

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
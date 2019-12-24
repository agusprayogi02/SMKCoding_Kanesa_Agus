package id.canteen

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.Sampler
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import id.canteen.data.Warung
import kotlinx.android.synthetic.main.activity_setting.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.random.Random
import kotlin.system.exitProcess

class Setting : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 100
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var ref: DatabaseReference
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val default = "war.jpg"
    lateinit var link: String
    lateinit var nama: String
    lateinit var wrg: String
    lateinit var def: String
    lateinit var process: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        ref = FirebaseDatabase.getInstance().getReference("Warung")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (h in p0.children){
                        val warung = h.getValue(Warung::class.java)
                        if (warung!!.idUser == mAuth!!.currentUser!!.uid){
                            Picasso.get().load(warung.link).into(addback)
                            def = warung.Background
                            addback.tag = def
                            addmnuser.setText(warung.Penjual)
                            addNamaWarung.setText(warung.Nama)
                        }
                    }
                }
            }

        })

        add_menu_back.setOnClickListener {
            masukkanImage()
        }

        addbtn.setOnClickListener {
            upload()
        }
    }

    private fun upload() {
        val error = "Harus di ISi"
        nama = addmnuser.text.toString()
        wrg = addNamaWarung.text.toString()
        if(nama.isEmpty()){
            addmnuser.error = error
        }else if(wrg.isEmpty()){
            addNamaWarung.error = error
        }else if (addback.tag.toString().equals(def, true)){
            savadata(def)
        }else if (filePath != null) {
            if(def!=default){
                val storageRef = FirebaseStorage.getInstance().reference
                val desertRef = storageRef.child("images/foto/${def}")
                desertRef.delete().addOnCompleteListener {
                    Toast.makeText(this,def,Toast.LENGTH_SHORT).show()
                }
            }
            val rand = Random(1000).toString()
            val mountainImagesRef = storageReference!!.child("images/foto/$rand")
            addback.isDrawingCacheEnabled = true
            addback.buildDrawingCache()
            val bitmap = (addback.drawable as BitmapDrawable).bitmap
            val resized = Bitmap.createScaledBitmap(bitmap, (bitmap.width * 0.3).toInt(),
                (bitmap.height * 0.3).toInt(), true)
            val baos = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            val uploadTask = mountainImagesRef.putBytes(data)
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                process = ProgressDialog(
                    this,
                    R.style.Theme_MaterialComponents_Light_Dialog
                )
                process.isIndeterminate = true
                process.setMessage("Upload is $progress% done")
                process.show()
            }.addOnPausedListener {
                System.out.println("Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    process.hide()
                    val url = it.toString()
                    val imageLink = rand
                    Toast.makeText(this, imageLink, Toast.LENGTH_SHORT).show()
                    link = imageLink
                    savadata(url)
                }
            }
        }else{
            Toast.makeText(this, "Masukkan Gambar Background !!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savadata(url: String) {
        val userId = mAuth!!.currentUser!!.uid
        val warung = Warung(userId, wrg, nama, link, url)
        ref.child(userId).setValue(warung).addOnCompleteListener {
            Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
            val i = Intent(this, MenuActivity::class.java)
            startActivity(i)
        }
        mAuth = FirebaseAuth.getInstance()
    }

    private fun masukkanImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data!!.data != null) {
                filePath = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
                    addback.setImageBitmap(bitmap)
                    addback.tag = filePath.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}

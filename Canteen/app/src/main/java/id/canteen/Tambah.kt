package id.canteen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import id.canteen.data.Menus
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_tambah.*
import kotlinx.android.synthetic.main.masukkan.*
import java.io.IOException

class Tambah : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 100
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var ref: DatabaseReference
    private var mAuth: FirebaseAuth? = null
    lateinit var link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val iin = intent
        val b = iin.getExtras()!!.get("warung").toString()
        ref = FirebaseDatabase.getInstance().getReference("$b/Menus")
        add_menu_image.setOnClickListener {
            launchGallery()
        }

        add_menu_btn.setOnClickListener {
            insertdata()
        }
    }

    private fun launchGallery() {
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
                    addImage.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun insertdata() {
        if (filePath != null) {
            val metadata = StorageMetadata.Builder()
                .setContentType("image/*")
                .build()

//            val com = Compressor(this).setDestinationDirectoryPath(filePath)
// Upload file and metadata to the path 'images/mountains.jpg'
            val uploadTask =
                storageReference!!.child("images/Menu/${filePath!!.lastPathSegment}")
                    .putFile(
                        filePath!!, metadata
                    )
// Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress =
                    (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                System.out.println("Upload is $progress% done")
            }.addOnPausedListener {
                System.out.println("Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val imageLink = it.toString()
                    Toast.makeText(this, "$imageLink", Toast.LENGTH_SHORT).show()
                    link = imageLink
                    savadata()
                }
            }
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Tambah::class.java))
        }
    }

    private fun savadata() {
        val error = "Harus di ISi"
        val nama = add_menu_nama.text.toString()
        val harga = add_menu_harga.text.toString()
        if (add_menu_nama.text!!.isEmpty()) {
            add_menu_nama.error = error
        } else if (add_menu_harga.text!!.isEmpty()) {
            add_menu_harga.error = error
        } else {
            val userId = ref.push().key.toString()
            val iin = intent
            val b = iin.getExtras()!!.get("warung")
            val idWar = b.toString()

            val Menu = Menus(userId, idWar, link, nama, harga)
            ref.child(userId).setValue(Menu).addOnCompleteListener {
                Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
                val i = Intent(this, MenuActivity::class.java)
                startActivity(i)
            }
        }
        mAuth = FirebaseAuth.getInstance()
    }
}

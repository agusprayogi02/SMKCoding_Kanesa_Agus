package id.canteen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.canteen.data.Menus
import kotlinx.android.synthetic.main.activity_tambah.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.random.Random


@Suppress("DEPRECATION")
class Tambah : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 100
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var ref: DatabaseReference
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    lateinit var image: String
    lateinit var nama: String
    lateinit var harga: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        prosessBar.hide()

        val anim = AnimationUtils.loadAnimation(this, R.anim.shake)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        ref = FirebaseDatabase.getInstance().getReference("Menus")
        add_menu_image.setOnClickListener {
            it.startAnimation(anim)
            launchGallery()
        }

        add_menu_btn.setOnClickListener {
            it.startAnimation(anim)
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
        val error = "Harus di ISi"
        nama = add_menu_nama.text.toString()
        harga = add_menu_harga.text.toString()
        if (add_menu_nama.text!!.isEmpty()) {
            add_menu_nama.error = error
        } else if (add_menu_harga.text!!.isEmpty()) {
            add_menu_harga.error = error
        } else if (filePath != null) {
//            val metadata = StorageMetadata.Builder()
//                .setContentType("image/*")
//                .build()
//
////            val com = Compressor(this).setDestinationDirectoryPath(filePath)
//// Upload file and metadata to the path 'images/mountains.jpg'
//            val uploadTask =
//                storageReference!!.child("images/Menu/${filePath!!.lastPathSegment}")
//                    .putFile(
//                        filePath!!, metadata
//                    )
// Listen for state changes, errors, and completion of the upload.
            val rand = Random(1000).toString()
            val mountainImagesRef = storageReference!!.child("images/Menu/$rand")
            addImage.isDrawingCacheEnabled = true
            addImage.buildDrawingCache()
            val bitmap = (addImage.drawable as BitmapDrawable).bitmap
            val resized = Bitmap.createScaledBitmap(
                bitmap, (bitmap.width * 0.3).toInt(),
                (bitmap.height * 0.3).toInt(), true
            )
            val baos = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            val uploadTask = mountainImagesRef.putBytes(data)
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                println("Upload is $progress% done")
                prosessBar.isIndeterminate = true
                prosessBar.setBackgroundResource(R.color.white)
                prosessBar.show()
                prosessBar.progress = progress.toInt()
            }.addOnPausedListener {
                System.out.println("Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    prosessBar.hide()
                    val url = it.toString()
                    val imageLink = rand
                    Toast.makeText(this, imageLink, Toast.LENGTH_SHORT).show()
                    image = imageLink
                    savadata(url)
                }
            }
        } else {
            Toast.makeText(this, "Masukkan Gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savadata(url: String) {
        val userId = ref.push().key.toString()
        val iin = intent
        val b = iin.extras!!.get("warung")
        val idWar = b.toString()
        val User = mAuth!!.currentUser!!.uid
        val Menu = Menus(userId, User, idWar, url, image, nama, harga)
        ref.child(userId).setValue(Menu).addOnCompleteListener {
            Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
            val i = Intent(this, MenuActivity::class.java)
            startActivity(i)
        }
        mAuth = FirebaseAuth.getInstance()
    }
}

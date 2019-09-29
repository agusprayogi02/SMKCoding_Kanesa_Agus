package id.canteen.ui.user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import id.canteen.R
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.File
import java.io.IOException
import java.util.*

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private val PICK_IMAGE_REQUEST = 100
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val textView: TextView = root.findViewById(R.id.text_user)
        userViewModel.text.observe(this, Observer {
            textView.text = it
            account_image.setOnClickListener {
                launchGallery()
//                acc_name.setText("Ini Adalah foto saya")
            }
        })
        return root
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode == PICK_IMAGE_REQUEST){
                if (resultCode == Activity.RESULT_OK && data!!.data != null){
                    filePath = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, filePath)
                        account_image.setImageBitmap(bitmap)


//                        val metadata = StorageMetadata.Builder()
//                            .setContentType("image/jpeg")
//                            .build()
//// Upload file and metadata to the path 'images/mountains.jpg'
//                        val uploadTask = storageReference!!.child("images/${filePath!!.lastPathSegment}").putFile(
//                            filePath!!, metadata)
//
//// Listen for state changes, errors, and completion of the upload.
//                        uploadTask.addOnProgressListener { taskSnapshot ->
//                            val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
//                            System.out.println("Upload is $progress% done")
//                        }.addOnPausedListener {
//                            System.out.println("Upload is paused")
//                        }.addOnFailureListener {
//                            // Handle unsuccessful uploads
//                        }.addOnSuccessListener {
//                            // Handle successful uploads on complete
//                            // ...
//                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

    }

}
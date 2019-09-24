package id.canteen.ui.user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.canteen.R
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.File

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: String? = null
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
            }
        })
        return root
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            if (data == null && data!!.data == null){
                return
            }
            filePath = data.dataString
            acc_name.setText(filePath)
            val file = Uri.fromFile(File(filePath))
            val riversRef: StorageReference? = storageReference!!.child(filePath.toString())
            riversRef!!.putFile(file)
            val bitmap: Bitmap? = null
            account_image.setImageBitmap(BitmapFactory.decodeFile(filePath.toString()))
        }
    }
}
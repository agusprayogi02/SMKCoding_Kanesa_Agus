package id.canteen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.canteen.data.Users

class HomeViewModel : ViewModel() {

    val ref = FirebaseAuth.getInstance().currentUser!!.uid
    val dat = FirebaseDatabase.getInstance()

    private val _text = MutableLiveData<String>().apply {
        dat.getReference("Users").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (t in p0.children){
                        val user = t.getValue(Users::class.java)
                        if (ref == user!!.current_user){
                            value = "Rp." + user.uang
                        }
                    }
                }
            }

        })

    }
    val text: LiveData<String> = _text

}
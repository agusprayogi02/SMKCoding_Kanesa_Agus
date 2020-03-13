package id.canteen.session

import com.google.firebase.auth.FirebaseAuth

object userNow {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun currrenuser(): String? {
        return mAuth.currentUser!!.uid
    }

}
package id.canteen.BackEnd

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.canteen.data.Users

object Database {
    private var db = FirebaseDatabase.getInstance()
    fun getUserLevel(auth: String): String {
        var data = auth
        db.getReference("Users").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    data = "Error: $p0"
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (dt in p0.children) {
                            val user = dt.getValue(Users::class.java)
                            if (user?.current_user == auth) {
                                data = user.level
                                break
                            }
                        }
                    }
                }
            })
        return data
    }
}
package id.canteen.session

import android.content.Context
import android.content.SharedPreferences

object SessionDaftar {
    private var shar: SharedPreferences? = null

    fun Session(cntx: Context?) {
        shar = cntx!!.getSharedPreferences("MyAttack", Context.MODE_PRIVATE)
    }

    operator fun set(key: String?, value: Int) {
        val i = shar!!.all.keys
        for (h in i) {
            if (h == key) {
                shar!!.edit().putInt(key, value).apply()
            }
        }
    }

    operator fun get(key: String?): String? {
        return shar!!.getString(key, "")
    }
}
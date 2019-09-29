package id.canteen.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Aplikasi ini digunakan untuk mempermudah para siswa dan guru untuk memesan makanan dan minuman dikantin sekolah"
    }
    val text: LiveData<String> = _text
}
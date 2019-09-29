package id.canteen.ui.histori

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoriViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Histori Pesanan"
    }
    val text: LiveData<String> = _text
}
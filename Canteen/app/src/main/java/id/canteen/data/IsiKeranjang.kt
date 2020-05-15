package id.canteen.data

class IsiKeranjang(
    var id: String,
    var idUser: String,
    var War: String,
    var Url: String,
    var image: String,
    var nama: String,
    var harga: String,
    var jumlah: String
) {
    constructor() : this("", "", "", "", "", "", "", "") {

    }
}
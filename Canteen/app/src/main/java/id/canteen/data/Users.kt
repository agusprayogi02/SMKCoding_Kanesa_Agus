package id.canteen.data

class Users (var current_user: String ,var image : String,var nama : String, var username: String, var password: String, var jenisK: String , var level : String , var uang: Int){

    constructor() : this("","","", "", "","","",0) {

    }
}
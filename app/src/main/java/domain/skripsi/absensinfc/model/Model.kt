package domain.skripsi.absensinfc.model

import com.google.gson.annotations.SerializedName

class ResponseModel(
    val status: Boolean,
    val message: String,
    val data: ArrayList<ResponseData>,
)

class ResponseModelDataIsObject(
    val status: Boolean,
    val message: String,
    val token: String,
    val data: ResponseData,
)

class ResponseData(
    val id: Int,
    val username: String,
    val roles: String,
    val user: ArrayList<ResponseUser>,

    val pembagian_jadwal_id: Int,
    val pembagian_jadwal: ResponsePembagianJadwal,

    val jadwal_id: Int,
    val status: String,
    val nim: String,
    val pertemuan: String,
    val waktu_absen: String,
    val user_id: Int,
    val jadwal: ResponseJadwal,

    val hari: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val kelas: ResponseKelas,

    val mata_kuliah_id: Int,
    val matkul: ResponseMatkul

)

class ResponseUser(
    val id: Int,
    val nip: Int,
    val nim: Int,
    val nama: String,
    val tgl_lhr: String,
    val tempat_lhr: String,
    val jkl: String,
    val no_tlp: String,
    val alamat: String,
    val email: String,
    val foto: String,
    val user_id: String,
    val created_at: String,
    val updated_at: String,

    val kode_nfc: Int,
)

class ResponsePembagianJadwal(
    val id: Int,
    val user_id: Int,
    val kelas_id: Int,
    val mata_kuliah_id: Int,
    val hari: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val kelas: ResponseKelas,
    val matkul: ResponseMatkul,
)

class ResponseKelas(
    val id: String,
    val kode_kelas: String,
    val kelas: String,
    val created_at: String,
    val updated_at: String,
)

class ResponseMatkul(
    val id: Int,
    val kode_matkul: String,
    val nama_matkul: String,
    val sks: String,
    val created_at: String,
    val updated_at: String,
)

class ResponseJadwal(
    val id: Int,
    val mahasiswa_id: Int,
    val mahasiswa: ResponseUser
)
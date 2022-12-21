package domain.skripsi.absensinfc.network

import domain.skripsi.absensinfc.model.ResponseModel
import domain.skripsi.absensinfc.model.ResponseModelDataIsObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login") // no token
    fun apiLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): Call<ResponseModelDataIsObject>

    @GET("jadwal-dosen/")
    fun apiJadwalDosen(): Call<ResponseModel>

    @GET("matkul-all-dosen")
    fun apiMatkulAllDosen(): Call<ResponseModel>

    @GET("status-mhs-hadir/{code-jadwal}")
    fun apiStatusMahasiswa(
        @Path("code-jadwal") codejadwal: Int
    ): Call<ResponseModel>

    @GET("matkul-kelas/{code-matkul}")
    fun apiGetKelas(
        @Path("code-matkul") codeMatkul: Int
    ): Call<ResponseModel>

    @GET("bio-mhs/{code-mhs}")
    fun apiBiodataMahasiswa(
        @Path("code-mahasiswa") codeMhs: Int
    ): Call<ResponseModelDataIsObject>

    @GET("status-all-mhs/{code-pertemuan}")
    fun apiStatusAllMahasiswa(
        @Path("code-pertemuan") codePertemuan: Int
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("absen/{code-pertemuan}")
    fun apiAbsen(
        @Path("code-pertemuan") codePertemuan: Int,
        @Field("kode_nfc") kodeNfc: String,
//        @Field("nim") nim: String,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("update-profil-dosen/{code-dosen}")
    fun apiUpdateProfilDosen(
        @Path("code-dosen") codeDosen: Int
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("status-pertemuan/{code-pembagian-jadwal}")
    fun apiAbsenPertemuan(
        @Path("code-pembagian-jadwal") pembagianJadwal: Int,
        @Field("pertemuan") pertemuan: String,
    ): Call<ResponseModel>

    @GET("show-pertemuan/{code-kelas}")
    fun apiShowPertemuan(
        @Path("code-kelas") codeKelas: String,
    ): Call<ResponseModel>

    @GET("laporan-absen-mhs/{code-pembagian-jadwal}")
    fun apiDownloadReport(
        @Path("code-pembagian-jadwal") pembagianJadwal: Int,
    ): Call<ResponseModel>
}
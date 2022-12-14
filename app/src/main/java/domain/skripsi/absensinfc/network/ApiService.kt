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
        @Field("nim") nim: String,
        ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("update-profil-dosen/{code-dosen}")
    fun apiUpdateProfilDosen(
        @Path("code-dosen") codeDosen: Int
    ): Call<ResponseModel>
}
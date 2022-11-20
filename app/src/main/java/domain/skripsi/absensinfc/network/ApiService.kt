package domain.skripsi.absensinfc.network

import domain.skripsi.absensinfc.model.ResponseModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login") // no token
    fun apiLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @GET("jadwal-dosen/")
    fun apiJadwalDosen(): Call<ResponseModel>

    @GET("status-mhs-hadir/{code-hadir}")
    fun apiStatusMahasiswa(
        @Path("code-hadir") codeHadir:Int
    ): Call<ResponseModel>

    @GET("bio-mhs/{code-mhs}")
    fun apiBiodataMahasiswa(
        @Path("code-mahasiswa") codeMhs:Int
    ): Call<ResponseModel>

    @GET("status-all-mhs/{code-pertemuan}")
    fun apiStatusAllMahasiswa(
        @Path("code-pertemuan") codePertemuan:Int
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("absen/{code-pertemuan}")
    fun apiAbsen(
        @Path("code-pertemuan") codePertemuan:Int
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("update-profil-dosen/{code-dosen}")
    fun apiUpdateProfilDosen(
        @Path("code-dosen") codeDosen:Int
    ): Call<ResponseModel>
}
package domain.skripsi.absensinfc.adapter

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData
import domain.skripsi.absensinfc.ui.*
import java.util.*


class ClassAdapter(
    private val list: ArrayList<ResponseData>, private val type: String
) : RecyclerView.Adapter<ClassAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvClassName: TextView by lazy { itemView.findViewById(R.id.tvClassName) }
        private val tvClassTime: TextView by lazy { itemView.findViewById(R.id.tvClassTime) }
        private val tvClassRoom: TextView by lazy { itemView.findViewById(R.id.tvClassRoom) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemClassTime) }

        fun bindData(list: ResponseData) {

            if (type == "today") {
                tvClassName.text = list.matkul.nama_matkul
                tvClassTime.text = "Jadwal : ${list.jam_mulai} - ${list.jam_selesai}"
                tvClassRoom.text = "Kode/Kelas : ${list.kelas.kode_kelas}/${list.kelas.kelas}"

                item.setOnClickListener {
//                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()
                }

            } else if (type == "all") {
                tvClassRoom.visibility = View.GONE

                tvClassName.text = list.matkul.nama_matkul
                tvClassTime.text = "Semester " + list.semester.semester

                item.setOnClickListener {
//                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()

                    ContextCompat.startActivity(
                        itemView.context,
                        Intent(
                            itemView.context,
                            ListkelasActivity::class.java
                        ).putExtra("matkul_name", list.matkul.nama_matkul)
                            .putExtra("id", list.matkul.id.toString()),
                        null
                    )

                }
            } else if (type == "kelas") {
                tvClassTime.visibility = View.GONE
                tvClassRoom.visibility = View.GONE

                tvClassName.text = "kelas " + list.kelas.kelas
                tvClassName.typeface = Typeface.DEFAULT

                item.setOnClickListener {
//                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()

                    ContextCompat.startActivity(
                        itemView.context,
                        Intent(
                            itemView.context,
                            ListPertemuanActivity::class.java
                        ).putExtra("kelas", list.kelas.kelas)
                            .putExtra("kelas_id", list.kelas.id.toString()),
                        null
                    )

                }
            } else if (type == "pertemuan") {
                tvClassTime.visibility = View.GONE
                tvClassRoom.visibility = View.GONE

                tvClassName.text = "Pertemuan " + list.pertemuan
                tvClassName.typeface = Typeface.DEFAULT

                item.setOnClickListener {
//                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()

                    ContextCompat.startActivity(
                        itemView.context,
                        Intent(
                            itemView.context,
                            ListAbsenActivity::class.java
                        ).putExtra("id_pembagian_jadwal", list.pembagian_jadwal_id.toString())
                            .putExtra("pertemuan", list.pertemuan),
                        null
                    )

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}
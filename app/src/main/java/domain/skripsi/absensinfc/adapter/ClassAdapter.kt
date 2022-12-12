package domain.skripsi.absensinfc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData
import java.util.*


class ClassAdapter(
    private val list: ArrayList<ResponseData>,
    private val type: String
) :
    RecyclerView.Adapter<ClassAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvClassName: TextView by lazy { itemView.findViewById(R.id.tvClassName) }
        private val tvClassTime: TextView by lazy { itemView.findViewById(R.id.tvClassTime) }
        private val tvClassRoom: TextView by lazy { itemView.findViewById(R.id.tvClassRoom) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemClassTime) }

        fun bindData(list: ResponseData) {

            tvClassName.text = list.pembagian_jadwal.matkul.nama_matkul

            if (type == "today") {
                tvClassTime.text =
                    "Jadwal : ${list.pembagian_jadwal.jam_mulai} - ${list.pembagian_jadwal.jam_selesai}"
                tvClassRoom.text =
                    "Kode/Kelas : ${list.pembagian_jadwal.kelas.kode_kelas}/${list.pembagian_jadwal.kelas.kelas}"

                item.setOnClickListener {
                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()
                }

            } else {

                item.setOnClickListener {
                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_class_time, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}
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
import domain.skripsi.absensinfc.ui.DetailClassActivity
import domain.skripsi.absensinfc.ui.DetailStudentActivity
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

            if (type == "today") {
                tvClassName.text = list.matkul.nama_matkul
                tvClassTime.text =
                    "Jadwal : ${list.jam_mulai} - ${list.jam_selesai}"
                tvClassRoom.text =
                    "Kode/Kelas : ${list.kelas.kode_kelas}/${list.kelas.kelas}"

                item.setOnClickListener {
                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()

//                    intentToDetailClass()
                }

            } else {
                tvClassTime.visibility = View.GONE
                tvClassRoom.visibility = View.GONE

                tvClassName.text = list.matkul.nama_matkul
                tvClassName.typeface = Typeface.DEFAULT

                item.setOnClickListener {
                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()

//                    intentToDetailClass()
                }
            }

        }

        private fun intentToDetailClass() {
            ContextCompat.startActivity(
                itemView.context,
                Intent(itemView.context, DetailClassActivity::class.java)
//                            .putExtra("id", list.id)
                , null
            )
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
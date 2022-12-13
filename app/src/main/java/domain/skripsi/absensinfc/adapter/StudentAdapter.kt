package domain.skripsi.absensinfc.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import de.hdodenhof.circleimageview.CircleImageView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData
import domain.skripsi.absensinfc.ui.DetailStudentActivity
import domain.skripsi.absensinfc.utils.Constant
import domain.skripsi.absensinfc.utils.PreferencesHelper
import java.util.*

class StudentAdapter(
    private val list: ArrayList<ResponseData>,
    private val type: String
) :
    RecyclerView.Adapter<StudentAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgStudent: CircleImageView by lazy { itemView.findViewById(R.id.imgStudent) }
        private val tvStudentName: TextView by lazy { itemView.findViewById(R.id.tvStudentName) }
        private val tvStudentNim: TextView by lazy { itemView.findViewById(R.id.tvStudentNim) }
        private val tvStudentStatus: TextView by lazy { itemView.findViewById(R.id.tvStudentStatus) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemStudent) }

        fun bindData(list: ResponseData) {

            tvStudentName.text = list.jadwal.mahasiswa.nama
            tvStudentNim.text = list.jadwal.mahasiswa.nim.toString()
            tvStudentStatus.text = list.status

            imgStudent.load(Constant.URL_IMAGE+"images/"+list.jadwal.mahasiswa.foto) {
                crossfade(true)
                crossfade(400)
                placeholder(R.drawable.logo_unm)
                transformations(CircleCropTransformation())
            }

            item.setOnClickListener {
                if (type == "cek-status") {
                    Toast.makeText(itemView.context, "type : $type", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(itemView.context, "type : detail", Toast.LENGTH_SHORT).show()

                    ContextCompat.startActivity(
                        itemView.context,
                        Intent(itemView.context, DetailStudentActivity::class.java)
//                            .putExtra("id", list.id)
                        , null
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}
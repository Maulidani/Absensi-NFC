package domain.skripsi.absensinfc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.absensinfc.R
import domain.skripsi.absensinfc.model.ResponseData


class ClassAdapter(
    private val list: ArrayList<ResponseData>,
    type: String
) :
    RecyclerView.Adapter<ClassAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val item: CardView by lazy { itemView.findViewById(R.id.itemClassTime) }

        fun bindData(list: ResponseData) {

            item.setOnClickListener {
                Log.e(this@ClassAdapter.toString(), "bindData: " + list.pembagian_jadwal.matkul.nama_matkul)
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
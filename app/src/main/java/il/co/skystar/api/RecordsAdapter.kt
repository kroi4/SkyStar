package il.co.skystar.api

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.common.io.Resources.getResource
import il.co.skystar.R
import il.co.skystar.data.model.Record
import il.co.skystar.databinding.ItemFlightLayoutBinding
import il.co.skystar.utils.Constants
import il.co.skystar.utils.setStatusColor
import java.util.*

class RecordsAdapter(val records: List<Record>, val callBack: ItemListener) :
    RecyclerView.Adapter<RecordsAdapter.ItemViewHolder>() {

    val currentLocale = Locale.getDefault()
    val language = currentLocale.language


    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(index: Int)
    }

    inner class ItemViewHolder(private val binding: ItemFlightLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return false
        }

        fun bind(record: Record) {
            binding.tvFlightNumber.text = record.CHOPER + record.CHFLTN

            binding.apply {

               if (language == "iw") {
                    tvCountryCity.text =
                        record.CHLOC1TH + ", " + record.CHLOC1CH
                    tvFlightStatus.text = record.CHRMINH
                    tvFlightNumber.gravity = Gravity.END
                } else {
                    tvCountryCity.text =
                        record.CHLOC1T + ", " + record.CHLOCCT
                    tvFlightStatus.text = record.CHRMINE
                }

                setStatusColor(tvFlightStatus)

                val year = record.CHSTOL.subSequence(0, 4)
                val month = record.CHSTOL.subSequence(5, 7)
                val day = record.CHSTOL.subSequence(8, 10)
                val date = "${day}/${month}/${year}"
                val time = record.CHSTOL.subSequence(11, 16)

                tvDateTime.text = "${date} ${time}"
            }
        }
    }

    fun itemAt(position: Int) = records[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemFlightLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(records[position])
        Glide.with(holder.itemView)
            .load(Constants.airlineDict.get(records[position].CHOPERD))
            .placeholder(R.drawable.generalairlien)
            .override(200, 200)
            .into(holder.itemView.findViewById(R.id.ivAirline))
    }


    override fun getItemCount() =
        records.size

}
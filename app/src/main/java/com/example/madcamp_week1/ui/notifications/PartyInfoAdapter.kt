import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.model.PartyInfo

class PartyInfoAdapter(private var partyList: List<PartyInfo>) :
    RecyclerView.Adapter<PartyInfoAdapter.PartyInfoViewHolder>() {

    // ViewHolder 클래스
    class PartyInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val partyNameTextView: TextView = itemView.findViewById(R.id.party_name)
        private val partySeatsTextView: TextView = itemView.findViewById(R.id.party_seats)

        // 정당 데이터를 바인딩하는 메서드
        fun bind(partyInfo: PartyInfo) {
            // 텍스트 업데이트
            partyNameTextView.text = partyInfo.name
            partySeatsTextView.text = "${partyInfo.seats}석"

            // 텍스트 색상 설정 (정당의 색상)
            partyNameTextView.setTextColor(Color.parseColor(partyInfo.color))
            partySeatsTextView.setTextColor(Color.parseColor(partyInfo.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyInfoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_party_info, parent, false)
        return PartyInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PartyInfoViewHolder, position: Int) {
        holder.bind(partyList[position])
    }

    override fun getItemCount(): Int {
        return partyList.size
    }

    // RecyclerView 데이터 업데이트 메서드
    fun updateData(newPartyList: List<PartyInfo>) {
        partyList = newPartyList
        notifyDataSetChanged()
    }
}
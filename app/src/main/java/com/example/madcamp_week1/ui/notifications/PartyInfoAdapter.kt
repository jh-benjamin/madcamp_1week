import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week1.R
import com.example.madcamp_week1.model.PartyInfo

class PartyInfoAdapter(private var partyList: List<PartyInfo>) :
    RecyclerView.Adapter<PartyInfoAdapter.PartyViewHolder>() {

    class PartyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val partyName: TextView = view.findViewById(R.id.party_name)
        val partySeats: TextView = view.findViewById(R.id.party_seats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_party_info, parent, false)
        return PartyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        val party = partyList[position]
        holder.partyName.text = party.name
        holder.partySeats.text = "${party.seats}석"
    }

    override fun getItemCount(): Int = partyList.size

    // RecyclerView 데이터 업데이트
    fun updateData(newPartyList: List<PartyInfo>) {
        this.partyList = newPartyList
        notifyDataSetChanged()
    }
}
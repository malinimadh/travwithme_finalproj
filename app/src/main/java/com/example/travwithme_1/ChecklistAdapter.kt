import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travwithme_1.R

class ChecklistAdapter(private val items: List<String>,
                       private val onItemLongClickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checklist, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener.invoke(holder.adapterPosition)
            true // Indicate that the long click event is handled
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemTextView: TextView = itemView.findViewById(R.id.itemTextView)

        fun bind(item: String) {
            itemTextView.text = item
        }
    }
}

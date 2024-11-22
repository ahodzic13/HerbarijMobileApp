import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration : RecyclerView.ItemDecoration() {

    private val evenColor = Color.parseColor("#FFFFFF") // Bijela boja
    private val oddColor = Color.parseColor("#E0FFE0")  // Svijetlo zelena boja

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val color = if (position % 2 == 0) evenColor else oddColor
            child.setBackgroundColor(color)
        }
    }
}

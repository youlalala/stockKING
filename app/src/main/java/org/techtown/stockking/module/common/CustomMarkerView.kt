import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import org.techtown.stockking.R

class CustomMarkerView(
    context: Context,
    layout: Int,
    private val labels: ArrayList<String>
    ): MarkerView(context, layout) {

    private val date: TextView = findViewById(R.id.date_tv)
    private val price: TextView = findViewById(R.id.price_tv)


    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat()+120)
    }


    // entry content의 텍스트에 지정
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val xIdx = e?.x?.toInt()
        date.text = labels[xIdx!!].toString()
        price.text = e?.y.toString()
        super.refreshContent(e, highlight)
    }
}



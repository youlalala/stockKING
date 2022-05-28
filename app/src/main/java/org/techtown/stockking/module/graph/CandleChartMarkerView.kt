package org.techtown.stockking.module.graph

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import org.techtown.stockking.R

class CandleChartMarkerView (
    context: Context,
    layout: Int,
    private val labels: ArrayList<String>,
    private val entries: ArrayList<CandleEntry>,
): MarkerView(context, layout) {
    private val date: TextView = findViewById(R.id.date_tv)
    private val high: TextView = findViewById(R.id.high_tv)
    private val low: TextView = findViewById(R.id.low_tv)
    private val open: TextView = findViewById(R.id.open_tv)
    private val close: TextView = findViewById(R.id.close_tv)

    private val uiScreenWidth = resources.displayMetrics.widthPixels

    override fun draw(canvas: Canvas, posx: Float, posy: Float) {
        // Check marker position and update offsets.
        var posx = posx
        if (uiScreenWidth/2f - posx < 0) {
            posx -= width.toFloat()
        }

        // translate to the correct position and draw
        canvas.translate(posx, 0f)
        draw(canvas)
        canvas.translate(-posx, 0f)
    }

    // entry content의 텍스트에 지정
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val xIdx = e?.x?.toInt()
        date.text = labels[xIdx!!]
        high.text = entries[xIdx!!].high.toString()+" $"
        low.text = entries[xIdx!!].low.toString()+" $"
        open.text = entries[xIdx!!].open.toString()+" $"
        close.text = entries[xIdx!!].close.toString()+" $"
        super.refreshContent(e, highlight)
    }
}

package org.techtown.stockking.module.common

import CustomMarkerView
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.channels.ticker
import org.techtown.stockking.R
import org.techtown.stockking.R.layout.custom_marker_view
import org.techtown.stockking.databinding.ActivityDetailBinding
import org.techtown.stockking.model.StockList
import org.techtown.stockking.network.ApiWrapper

class DetailActivity : AppCompatActivity(){
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent
        val ticker= intent.getStringExtra("ticker").toString()
        binding.tickerTv.text = ticker
        binding.coNameKrTv.text = "테슬라"
//        binding.coNameUsTv.text = intent.getStringExtra("coName")
//        binding.priceTv.text = intent.getStringExtra("price")
//        val percent = intent.getStringExtra("percent")
//        if(percent?.substring(0,1)=="-"){
//            binding.percentTv.setTextColor(Color.RED)
//        }else{
//            binding.percentTv.setTextColor(Color.BLUE)
//        }
//        binding.percentTv.text = percent
        binding.star.setOnClickListener {
            showPopup(ticker)
        }
        binding.backBtn.setOnClickListener{
            finish()
        }
        ApiWrapper.getStockDetail(ticker) {
            Log.i("SSS",it.toString())
            drawGraph(it)
        }
    }

    private fun drawGraph(stockList : List<StockList>){
        val lineChart: LineChart = binding.chart

        val dateList = mutableListOf<String>()
        val priceList = mutableListOf<String>()

        stockList.forEach { element ->
            dateList.add(0,element.timestamp)
            priceList.add(0,element.high)
        }

        val entries = ArrayList<Entry>()
        for(i in 0 until priceList.size){
            entries.add(Entry(i.toFloat(), priceList[i].toFloat()))
        }


        val dataset= LineDataSet(entries, null)
        dataset.color = resources.getColor(R.color.main_color)
        dataset.setCircleColor(R.color.sub_color)

        val labels = ArrayList<String>()
        for(i in 0 until dateList.size){
            labels.add(dateList[i])
        }

        val xAxis=lineChart.xAxis
        val yAxisL=lineChart.axisLeft
        val yAxisR=lineChart.axisRight

        lineChart.xAxis.valueFormatter=IndexAxisValueFormatter(labels)
        //description 지우기
        lineChart.description=null
        //x축 y축 숨기기
        xAxis.isEnabled=false
        yAxisR.isEnabled=false

        //최대 y축 설정
        val space = dataset.yMax/100
        yAxisL.axisMaximum = dataset.yMax+space


        //데이터 값 표시 X
        dataset.setDrawValues(false)

        //marker
        val marker = CustomMarkerView(this, custom_marker_view,labels)
        lineChart.marker = marker

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val data = LineData(dataset)

        lineChart.data = data
        lineChart.legend.isEnabled=false
        lineChart.invalidate()
    }
    
    private fun showPopup(ticker: String){
        val alertDialog : AlertDialog
        val msg = "XXX 님의 즐겨찾기에 $ticker 를 추가합니다."
        val btnOK = "OK"
        val btnNO = "cancel"
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg) //팝업창 내용 지정
        builder.setCancelable(false) //외부 레이아웃 클릭시도 팝업창이 사라지지않게 설정
        builder.setPositiveButton(btnOK, DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(application, btnOK, Toast.LENGTH_SHORT).show()
            binding.star.setBackgroundResource(R.drawable.img_star_full)
        })
        builder.setNegativeButton(btnNO, DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(application, btnNO, Toast.LENGTH_SHORT).show()
            binding.star.setBackgroundResource(R.drawable.img_star_border)
        })
        alertDialog = builder.create()
        alertDialog.show()
    }
}



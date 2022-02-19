package org.techtown.stockking.module.common.detail_page

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.techtown.stockking.R
import org.techtown.stockking.R.layout.custom_marker_view
import org.techtown.stockking.databinding.ActivityDetailBinding
import org.techtown.stockking.model.StockDetailList
import org.techtown.stockking.model.StockModel

import org.techtown.stockking.network.ApiWrapper

class DetailActivity : AppCompatActivity(){
    lateinit var binding: ActivityDetailBinding
    private val TAG = this.javaClass.simpleName
    private val NOINFOMATION = "정보 없음"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent
        val ticker= intent.getStringExtra("ticker").toString()

            ApiWrapper.getCompanyInfo(ticker){
                Log.i("hmm",it.toString())
                if(it.isEmpty()){
                    binding.tickerTv.text = NOINFOMATION
                    binding.coNameUsTv.text=NOINFOMATION
                    binding.coNameKrTv.text=NOINFOMATION
                    binding.description.text=NOINFOMATION
                    binding.cap.text=NOINFOMATION
                }else{
                    binding.tickerTv.text = it[0].symbol
                    binding.coNameUsTv.text=it[0].name
                    binding.coNameKrTv.text=it[0].kr_name
                    binding.description.text=it[0].kr_desc
                    binding.cap.text=it[0].cap
                }
            }


        binding.priceTv.text = intent.getStringExtra("price")
        val percent = intent.getStringExtra("percent")
        if(percent?.substring(0,1)=="-"){
            binding.percentTv.setTextColor(Color.RED)
        }else{
            binding.percentTv.setTextColor(Color.BLUE)
        }
        binding.percentTv.text = percent
        binding.star.setOnClickListener {
            showPopup(ticker)
        }
        binding.backBtn.setOnClickListener{
            finish()
        }

        //한달 그래프
        binding.oneMonth.setOnClickListener{
            ApiWrapper.getStockIntraday(ticker){
                drawGraph(it)
            }
        }

        ApiWrapper.getStockDetail(ticker) {
            binding.priceTv.text=it[it.size-1].high
            drawTEMP(it)
        }
    }

    fun drawGraph(stockList : List<StockModel>){
        val lineChart: LineChart = binding.chart

        val dateList = ArrayList<String>()
        //val priceList = mutableListOf<String>()
        val priceList = ArrayList<String>()

        stockList.forEach { element ->
            dateList.add(element.timestamp)
            priceList.add(element.high)
        }

        //entry
        val entries = ArrayList<Entry>()
        for(i in 0 until priceList.size){
            entries.add(Entry(i.toFloat(), priceList[i].toFloat()))
        }

        val dataset= LineDataSet(entries, null)
        dataset.color = getColor(R.color.main_color)
        //원 없애기
        dataset.setDrawCircles(false)

        val xAxis=lineChart.xAxis
        val yAxisL=lineChart.axisLeft
        val yAxisR=lineChart.axisRight

        lineChart.xAxis.valueFormatter= IndexAxisValueFormatter(dateList)
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
        val marker = CustomMarkerView(this, R.layout.custom_marker_view,dateList)
        lineChart.marker = marker

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val data = LineData(dataset)

        lineChart.data = data
        lineChart.legend.isEnabled=false
        lineChart.invalidate()
    }


    fun drawTEMP(stockList : List<StockDetailList>){
        val lineChart: LineChart = binding.chart
        val dateList = mutableListOf<String>()
        val priceList = mutableListOf<String>()

        stockList.forEach { element ->
            dateList.add(element.timestamp)
            priceList.add(element.high)
        }

        val entries = ArrayList<Entry>()
        for(i in 0 until priceList.size){
            entries.add(Entry(i.toFloat(), priceList[i].toFloat()))
        }


        val dataset= LineDataSet(entries, null)
        dataset.color = getColor(R.color.main_color)
        dataset.setCircleColor(R.color.sub_color)

        val labels = ArrayList<String>()
        for(i in 0 until dateList.size){
            labels.add(dateList[i])
        }

        val xAxis=lineChart.xAxis
        val yAxisL=lineChart.axisLeft
        val yAxisR=lineChart.axisRight

        lineChart.xAxis.valueFormatter= IndexAxisValueFormatter(labels)
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
        val marker = CustomMarkerView(this, R.layout.custom_marker_view,labels)
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



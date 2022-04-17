package org.techtown.stockking.module.common.detail_page

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.util.Log.i
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.techtown.stockking.R
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityDetailBinding
import org.techtown.stockking.model.BookmarkModel
import org.techtown.stockking.model.StockModel

import org.techtown.stockking.network.ApiWrapper

class DetailActivity : AppCompatActivity(){
    lateinit var binding: ActivityDetailBinding
    private val NOINFOMATION = "정보 없음"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent
        val ticker= intent.getStringExtra("ticker").toString()

        //즐겨찾기 확인
        ApiWrapper.getBookmark(MySharedPreferences.getToken(this)){
            i("SSS","test it"+it)
            for(i in it.indices){
                if(it[i].symbol==ticker){
                    binding.star.isSelected = true
                }
            }
        }

        //회사 정보 입력
        ApiWrapper.getCompanyInfo(ticker){
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

                //더보기
                i("SSS","maxline"+binding.description.lineCount)
                if(binding.description.lineCount>3){
                    binding.description.maxLines=3
                    binding.viewMore.visibility= View.VISIBLE
                    binding.viewMore.setOnClickListener {
                        binding.description.maxLines=binding.description.lineCount
                        binding.viewMore.visibility= View.GONE
                    }
                }
            }
        }




        binding.priceTv.text = intent.getStringExtra("price")
        val percent = intent.getStringExtra("percent")
        if(percent?.substring(0,1)=="-"){
            binding.percentTv.setTextColor(Color.BLUE)
        }else{
            binding.percentTv.setTextColor(Color.RED)
        }
        binding.percentTv.text = percent

        binding.star.setOnClickListener {

            if(binding.star.isSelected){
                ApiWrapper.postBookmark(
                    BookmarkModel(
                        token = MySharedPreferences.getToken(this),
                        request = "delete",
                        symbol = ticker)
                ){}
                binding.star.isSelected = false
            } else{
                val vibrator = getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(80)
                ApiWrapper.postBookmark(
                    BookmarkModel(
                        token = MySharedPreferences.getToken(this),
                        request = "add",
                        symbol = ticker)
                ){}
                binding.star.isSelected = true
            }
        }
        binding.backBtn.setOnClickListener{
            finish()
        }



        ApiWrapper.getStockIntraday(ticker){
            binding.priceTv.text=it[it.size-1].high
            drawLineChart(it)
            drawCandleChart(it)
        }

        binding.chartBtn.setOnClickListener{
            if (binding.chartBtn.text == "line"){
                binding.lineChart.visibility=View.GONE
                binding.candlestickChart.visibility=View.VISIBLE
                binding.chartBtn.text = "candle"
            }else{
                binding.lineChart.visibility=View.VISIBLE
                binding.candlestickChart.visibility=View.GONE
                binding.chartBtn.text = "line"
            }
        }

        //한달 그래프
        binding.oneMonthBtn.setOnClickListener{
            ApiWrapper.getStockIntraday(ticker){
                binding.priceTv.text=it[it.size-1].high
                drawLineChart(it)
            }
        }
        //3달 그래프
        binding.threeMonthBtn.setOnClickListener{
            ApiWrapper.getStockDaily(ticker){
                binding.priceTv.text=it[it.size-1].high
                drawLineChart(it)
            }
        }




    }

    fun drawLineChart(stockList : List<StockModel>){
        val lineChart = binding.lineChart

        val dateList = ArrayList<String>()
        val priceList = ArrayList<String>()


        stockList.forEach { element ->
            val day=element.timestamp.subSequence(0,10).toString()
            val time=element.timestamp.subSequence(11,16)
            dateList.add(day+" "+time)
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
        val marker = LineChartMarkerView(this, R.layout.linechart_marker_view,dateList)
        lineChart.marker = marker

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val data = LineData(dataset)

        lineChart.data = data
        lineChart.legend.isEnabled=false
        lineChart.invalidate()
    }

    fun drawCandleChart(stockList : List<StockModel>){
        val candleChart = binding.candlestickChart

        val dateList = ArrayList<String>()
        val entries = ArrayList<CandleEntry>()
        for(i in 0..30){
            val day=stockList[i].timestamp.subSequence(0,10).toString()
            val time=stockList[i].timestamp.subSequence(11,16)
            dateList.add(day+" "+time)
            entries.add(CandleEntry(
                i.toFloat(),
                stockList[i].high.toFloat(),
                stockList[i].low.toFloat(),
                stockList[i].open.toFloat(),
                stockList[i].close.toFloat()
            ))
        }
        i("SSS","candle"+entries[3])


        val dataset= CandleDataSet(entries, null).apply{
            // 심지 부분
            shadowColor = Color.LTGRAY
            shadowWidth = 0.1F

            // 음봄
            decreasingColor = Color.BLUE
            decreasingPaintStyle = Paint.Style.FILL
            // 양봉
            increasingColor = Color.RED
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.DKGRAY
            // 터치시 노란 선 제거
            //highLightColor = Color.TRANSPARENT
        }
        val xAxis=candleChart.xAxis
        val yAxisL=candleChart.axisLeft
        val yAxisR=candleChart.axisRight
        //description 지우기
        candleChart.description=null
        //x축 y축 숨기기
        xAxis.isEnabled=false
        yAxisR.isEnabled=false
        //데이터 값 표시 X
        dataset.setDrawValues(false)
        //marker
        val marker = CandleChartMarkerView(this, R.layout.candlechart_marker_view,dateList,entries)
        candleChart.marker = marker

        candleChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        candleChart.legend.isEnabled=false
        candleChart.data=CandleData(dataset)


    }
}



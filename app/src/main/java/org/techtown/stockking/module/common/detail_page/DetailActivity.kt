package org.techtown.stockking.module.common.detail_page

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Vibrator
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
import org.techtown.stockking.model.StockModel2

import org.techtown.stockking.network.ApiWrapper
import java.lang.Math.round
import java.text.DecimalFormat

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
                binding.shareout.text=NOINFOMATION
            }else{
                binding.tickerTv.text = it[0].symbol
                binding.coNameUsTv.text=it[0].name_en
                binding.coNameKrTv.text=it[0].name_kr
                binding.description.text=it[0].desc_kr
                binding.shareout.text=it[0].shareout

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
        var percent = intent.getStringExtra("percent")
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

        ApiWrapper.getStockDaily(ticker){
            calPercnet(it)
            drawLineChart(it)
            drawCandleChart(it)
        }
        //1일 그래프

        binding.oneDayBtn.setOnClickListener{
            ApiWrapper.getStockDaily(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //1주 그래프
        binding.oneWeekBtn.setOnClickListener{
            ApiWrapper.getStockWeekly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //1달 그래프
        binding.oneMonthBtn.setOnClickListener{
            ApiWrapper.getStockMonthly(ticker){
                calPercnet2(it)
                drawLineChart2(it)
                //drawCandleChart(it)
            }
        }
        //3달 그래프
        binding.threeMonthBtn.setOnClickListener{
            ApiWrapper.getStock3Monthly(ticker){
                calPercnet2(it)
                drawLineChart2(it)
                //drawCandleChart(it)
            }
        }
        //1년 그래프
        binding.oneYearBtn.setOnClickListener{
            ApiWrapper.getStockYearly(ticker){
                calPercnet2(it)
                drawLineChart2(it)
                //drawCandleChart(it)
            }
        }

    }

    fun calPercnet(it: List<StockModel>){
        val df = DecimalFormat("#.##")
        var percent = df.format((it.last().high.toFloat()-it[0].high.toFloat())/it[0].high.toFloat()*100).toString()
        if(percent?.substring(0,1)=="-"){
            binding.percentTv.setTextColor(Color.BLUE)
            percent = percent+"%"
        }else{
            binding.percentTv.setTextColor(Color.RED)
            percent = "+"+percent+"%"
        }
        binding.percentTv.text = percent
    }
    fun calPercnet2(it: List<StockModel2>){
        val df = DecimalFormat("#.##")
        var percent = df.format((it.last().high.toFloat()-it[0].high.toFloat())/it[0].high.toFloat()*100).toString()
        if(percent?.substring(0,1)=="-"){
            binding.percentTv.setTextColor(Color.BLUE)
            percent = percent+"%"
        }else{
            binding.percentTv.setTextColor(Color.RED)
            percent = "+"+percent+"%"
        }
        binding.percentTv.text = percent
    }

    fun drawLineChart(stockList: List<StockModel>){
        val lineChart = binding.lineChart

        val dateList = ArrayList<String>()
        val priceList = ArrayList<String>()

        stockList.forEach { element ->
            val day=element.datetime.subSequence(0,10).toString()
            val time=element.datetime.subSequence(11,16)
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
        yAxisL.isEnabled=false
        yAxisR.isEnabled=false

        //라인굵기
        dataset.lineWidth=2.0F

        //최대 , 최소 y축 설정
        val space = dataset.yMax/100
        yAxisL.axisMaximum = dataset.yMax+space
        yAxisL.axisMinimum = dataset.yMin-space


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

    fun drawLineChart2(stockList: List<StockModel2>){
        val lineChart = binding.lineChart

        val dateList = ArrayList<String>()
        val priceList = ArrayList<String>()

        stockList.forEach { element ->
            dateList.add(element.date.subSequence(0,10).toString())
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
        yAxisL.isEnabled=false
        //yAxisR.isEnabled=false

        //라인굵기
        dataset.lineWidth=2.0F

        //최대, 최소 y축 설정
        val space = dataset.yMax/100
        yAxisL.axisMaximum = dataset.yMax+space
        yAxisL.axisMinimum = dataset.yMin-space


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
            val day=stockList[i].datetime.subSequence(0,10).toString()
            val time=stockList[i].datetime.subSequence(11,16)
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
        yAxisL.isEnabled=false
        //yAxisR.isEnabled=false
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



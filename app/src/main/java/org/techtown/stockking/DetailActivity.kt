package org.techtown.stockking

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.os.Vibrator
import android.util.Log.i
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityDetailBinding

import org.techtown.stockking.model.BookmarkModel
import org.techtown.stockking.model.StockModel
import org.techtown.stockking.module.graph.CandleChartMarkerView
import org.techtown.stockking.module.graph.LineChartMarkerView

import org.techtown.stockking.network.ApiWrapper
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

        //즐겨찾기 check
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
                binding.coNameKrTv.text=NOINFOMATION
                binding.description.text=NOINFOMATION
//                binding.shareout.text=NOINFOMATION
            }else{
                binding.tickerTv.text = it[0].symbol
                binding.coNameKrTv.text=it[0].name_kr
                binding.description.text=it[0].desc_kr
//                binding.shareout.text=it[0].shareout

                //img
                //SVG string content
                val svgString = it[0].img

                //convert SVG string to an object of type SVG
                val svg = SVG.getFromString(svgString)

                //create a drawable from svg
                val drawable = PictureDrawable(svg.renderToPicture())

                //finally load the drawable with Glide.
                Glide.with(this)
                    .load(drawable)
                    .circleCrop()
                    .into(binding.logoImg)

                //더보기
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

        binding.lineChartBtn.setOnClickListener{
            binding.lineChart.visibility=View.VISIBLE
            binding.candlestickChart.visibility=View.GONE
            binding.lineChartBtn.setShadowLayer(1F, 2F, 2F, Color.BLACK)
            binding.candleChartBtn.setShadowLayer(0F, 0F, 0F, Color.WHITE)
        }
        binding.candleChartBtn.setOnClickListener {
            binding.lineChart.visibility=View.GONE
            binding.candlestickChart.visibility=View.VISIBLE
            binding.candleChartBtn.setShadowLayer(1F, 2F, 2F, Color.BLACK)
            binding.lineChartBtn.setShadowLayer(0F, 0F, 0F, Color.WHITE)
        }

        binding.oneDayBtn.isSelected=true
        binding.oneWeekBtn.isSelected=false
        binding.oneMonthBtn.isSelected=false
        binding.threeMonthBtn.isSelected=false
        binding.oneYearBtn.isSelected=false
        binding.fiveYearBtn.isSelected=false
        ApiWrapper.getStockDaily(ticker){
            calPercnet(it)
            drawLineChart(it)
            drawCandleChart(it)
        }
        //1일 그래프

        binding.oneDayBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=true
            binding.oneWeekBtn.isSelected=false
            binding.oneMonthBtn.isSelected=false
            binding.threeMonthBtn.isSelected=false
            binding.oneYearBtn.isSelected=false
            binding.fiveYearBtn.isSelected=false
            ApiWrapper.getStockDaily(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //1주 그래프
        binding.oneWeekBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=false
            binding.oneWeekBtn.isSelected=true
            binding.oneMonthBtn.isSelected=false
            binding.threeMonthBtn.isSelected=false
            binding.oneYearBtn.isSelected=false
            binding.fiveYearBtn.isSelected=false
            ApiWrapper.getStockWeekly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //1달 그래프
        binding.oneMonthBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=false
            binding.oneWeekBtn.isSelected=false
            binding.oneMonthBtn.isSelected=true
            binding.threeMonthBtn.isSelected=false
            binding.oneYearBtn.isSelected=false
            binding.fiveYearBtn.isSelected=false
            ApiWrapper.getStockMonthly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //3달 그래프
        binding.threeMonthBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=false
            binding.oneWeekBtn.isSelected=false
            binding.oneMonthBtn.isSelected=false
            binding.threeMonthBtn.isSelected=true
            binding.oneYearBtn.isSelected=false
            binding.fiveYearBtn.isSelected=false
            ApiWrapper.getStock3Monthly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //1년 그래프
        binding.oneYearBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=false
            binding.oneWeekBtn.isSelected=false
            binding.oneMonthBtn.isSelected=false
            binding.threeMonthBtn.isSelected=false
            binding.oneYearBtn.isSelected=true
            binding.fiveYearBtn.isSelected=false
            ApiWrapper.getStock3Monthly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //5년그래프
        //1년 그래프
        binding.fiveYearBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=false
            binding.oneWeekBtn.isSelected=false
            binding.oneMonthBtn.isSelected=false
            binding.threeMonthBtn.isSelected=false
            binding.oneYearBtn.isSelected=false
            binding.fiveYearBtn.isSelected=true
            ApiWrapper.getStock3Monthly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
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

    fun drawLineChart(stockList: List<StockModel>){
        val lineChart = binding.lineChart

        val dateList = ArrayList<String>()
        val priceList = ArrayList<String>()

        stockList.forEach { element ->
            if (element.date.isNullOrEmpty()){
                val day=element.datetime.subSequence(0,10).toString()
                val time=element.datetime.subSequence(11,16)
                dateList.add(day+" "+time)
            }else{
                dateList.add(element.date)
            }
            priceList.add(element.high)
        }

        //entry
        val entries = ArrayList<Entry>()
        for(i in 0 until priceList.size){
            entries.add(Entry(i.toFloat(), priceList[i].toFloat()))
        }

        val dataset= LineDataSet(entries, null)
        dataset.color = getColor(R.color.main_green_color)
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
        yAxisR.isEnabled=true

        yAxisR.setLabelCount(2,true)
        yAxisR.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisR.textColor=getColor(R.color.main_green_color)
        yAxisR.axisLineColor = Color.TRANSPARENT;
        yAxisR.gridColor = Color.TRANSPARENT;

        //라인굵기
        dataset.lineWidth=1.5F

        //최대 , 최소 y축 설정
        val space = dataset.yMax/100
        yAxisL.axisMaximum = dataset.yMax+space
        yAxisL.axisMinimum = dataset.yMin-space


        dataset.apply {
            setGradientColor(1,0)
            //데이터 값 표시 X
            setDrawValues(false)
            setDrawVerticalHighlightIndicator(true)
            setDrawHorizontalHighlightIndicator(false)
            highLightColor = getColor(R.color.main_green_color)
            highlightLineWidth = 1f
        }
        lineChart.setPinchZoom(false)
        lineChart.isDoubleTapToZoomEnabled=false
        lineChart.setScaleEnabled(false)



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
        for(i in 0..29){
            if (stockList[i].date.isNullOrEmpty()){
                val day=stockList[i].datetime.subSequence(0,10).toString()
                val time=stockList[i].datetime.subSequence(11,16)
                dateList.add(day+" "+time)
            }else{
                dateList.add(stockList[i].date)
            }
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
        yAxisR.isEnabled=true
        yAxisR.setLabelCount(2,true)
        yAxisR.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisR.textColor=getColor(R.color.main_green_color)
        yAxisR.axisLineColor = Color.TRANSPARENT;
        yAxisR.gridColor = Color.TRANSPARENT;
        //최대 , 최소 y축 설정
        val space = dataset.yMax/1000
        yAxisL.axisMaximum = dataset.yMax+space
        yAxisL.axisMinimum = dataset.yMin-space

        dataset.apply {
            setGradientColor(1,0)
            //데이터 값 표시 X
            setDrawValues(false)
            setDrawVerticalHighlightIndicator(true)
            setDrawHorizontalHighlightIndicator(false)
            highLightColor = getColor(R.color.main_green_color)
            highlightLineWidth = 1f
        }
        //marker
        val marker = CandleChartMarkerView(this, R.layout.candlechart_marker_view,dateList,entries)
        candleChart.marker = marker

        candleChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        candleChart.legend.isEnabled=false
        candleChart.data=CandleData(dataset)
        candleChart.invalidate()


    }
}



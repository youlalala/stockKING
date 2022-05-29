package org.techtown.stockking

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.PictureDrawable
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationEffect.EFFECT_CLICK
import android.os.Vibrator
import android.util.Log.i
import android.view.HapticFeedbackConstants.*
import android.view.View
import androidx.annotation.RequiresApi
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
import org.techtown.stockking.network.ApiWrapperBookmark
import org.techtown.stockking.network.ApiWrapperChart
import retrofit2.http.Header
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity(){
    lateinit var binding: ActivityDetailBinding
    private val NOINFOMATION = "정보 없음"
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent
        val ticker= intent.getStringExtra("ticker").toString()

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;
        val vibrationEffectClick = VibrationEffect.createPredefined(EFFECT_CLICK)

        //즐겨찾기 check
        ApiWrapperBookmark.getBookmark(MySharedPreferences.getToken(this)){
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
                binding.closeTv.text=NOINFOMATION
//                binding.shareout.text=NOINFOMATION
            }else{
                binding.tickerTv.text = it[0].symbol
                binding.coNameKrTv.text=it[0].name_kr
                binding.description.text=it[0].desc_kr
                binding.closeTv.text=it[0].close + " 달러"
//                binding.shareout.text=it[0].shareout

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

                //more button
                if(binding.description.lineCount>3){
                    binding.description.maxLines=3
                    binding.unfoldBtn.visibility= View.VISIBLE
                    binding.foldBtn.setOnClickListener {
                        binding.description.maxLines=3
                        binding.foldBtn.visibility= View.GONE
                        binding.unfoldBtn.visibility=View.VISIBLE
                    }
                    binding.unfoldBtn.setOnClickListener {
                        binding.description.maxLines=binding.description.lineCount
                        binding.unfoldBtn.visibility= View.GONE
                        binding.foldBtn.visibility=View.VISIBLE
                    }
                }
            }
        }

        binding.star.setOnClickListener {
            if(binding.star.isSelected){
                ApiWrapperBookmark.postBookmark(
                    token = MySharedPreferences.getToken(this),
                    BookmarkModel(
                        request = "delete",
                        symbol = ticker)
                )
                binding.star.isSelected = false
            } else{
                //vibrator
                vibrator.vibrate(vibrationEffectClick)
                ApiWrapperBookmark.postBookmark(
                    token = MySharedPreferences.getToken(this),
                    BookmarkModel(
                        request = "add",
                        symbol = ticker)
                )
                binding.star.isSelected = true
            }
        }
        binding.backBtn.setOnClickListener{
            finish()
            overridePendingTransition(0,R.anim.horizon_exit )
        }

        binding.lineChartBtn.setShadowLayer(1F, 2F, 2F, Color.BLACK)
        binding.candleChartBtn.setShadowLayer(0F, 0F, 0F, Color.WHITE)
        binding.lineChartBtn.setOnClickListener{
            binding.lineChart.visibility=View.VISIBLE
            binding.candleChart.visibility=View.GONE
            binding.lineChartBtn.setShadowLayer(1F, 2F, 2F, Color.BLACK)
            binding.candleChartBtn.setShadowLayer(0F, 0F, 0F, Color.WHITE)
        }
        binding.candleChartBtn.setOnClickListener {
            binding.lineChart.visibility=View.GONE
            binding.candleChart.visibility=View.VISIBLE
            binding.candleChartBtn.setShadowLayer(1F, 2F, 2F, Color.BLACK)
            binding.lineChartBtn.setShadowLayer(0F, 0F, 0F, Color.WHITE)
        }

        binding.lineChart.setOnClickListener{
            it.performHapticFeedback(CLOCK_TICK)
        }
        binding.candleChart.setOnClickListener{
            it.performHapticFeedback(CLOCK_TICK)
        }

        binding.oneDayBtn.isSelected=true
        binding.oneWeekBtn.isSelected=false
        binding.oneMonthBtn.isSelected=false
        binding.threeMonthBtn.isSelected=false
        binding.oneYearBtn.isSelected=false
        binding.fiveYearBtn.isSelected=false
        ApiWrapperChart.getStockDaily(ticker){
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
            ApiWrapperChart.getStockDaily(ticker){
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
            ApiWrapperChart.getStockWeekly(ticker){
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
            ApiWrapperChart.getStockMonthly(ticker){
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
            ApiWrapperChart.getStock3Monthly(ticker){
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
            ApiWrapperChart.getStockYearly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }
        //5년그래프
        binding.fiveYearBtn.setOnClickListener{
            binding.oneDayBtn.isSelected=false
            binding.oneWeekBtn.isSelected=false
            binding.oneMonthBtn.isSelected=false
            binding.threeMonthBtn.isSelected=false
            binding.oneYearBtn.isSelected=false
            binding.fiveYearBtn.isSelected=true
            ApiWrapperChart.getStock5Yearly(ticker){
                calPercnet(it)
                drawLineChart(it)
                drawCandleChart(it)
            }
        }

    }

    fun calPercnet(it: List<StockModel>){
        val df = DecimalFormat("#.##")
        var value = it.last().high.toFloat()-it[0].high.toFloat()
        var value_format = df.format(value)
        var percent_format = df.format(value/it[0].high.toFloat()*100)
        if(percent_format?.substring(0,1)=="-"){
            binding.changePercentTv.setTextColor(Color.BLUE)
            binding.changePercentTv.text = resources.getString(R.string.down)+"${percent_format.substring(1)}%"
            binding.changeValueTv.setTextColor(Color.BLUE)
            binding.changeValueTv.text = "$value_format 달러"
        }else{
            binding.changePercentTv.setTextColor(Color.RED)
            binding.changePercentTv.text = resources.getString(R.string.up)+"$percent_format%"
            binding.changeValueTv.setTextColor(Color.RED)
            binding.changeValueTv.text = "+$value_format 달러"
        }
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
            priceList.add(element.close)
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
        val candleChart = binding.candleChart
        val dateList = ArrayList<String>()
        val entries = ArrayList<CandleEntry>()
        for(i in 0 until stockList.size){
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
        val dataset= CandleDataSet(entries, null).apply{
            // 심지 부분
            shadowColor = Color.DKGRAY
            shadowWidth = 0.1F

            // 음봄
            decreasingColor = Color.BLUE
            decreasingPaintStyle = Paint.Style.FILL
            // 양봉
            increasingColor = Color.RED
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.DKGRAY
            // 터치시 노란 선 제거
            highLightColor = getColor(R.color.main_green_color)
            highlightLineWidth = 1f
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

        candleChart.setPinchZoom(false)
        candleChart.isDoubleTapToZoomEnabled=false
        candleChart.setScaleEnabled(false)

        candleChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        candleChart.legend.isEnabled=false
        candleChart.data=CandleData(dataset)
        candleChart.invalidate()


    }
}



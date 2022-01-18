package org.techtown.stockking

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import org.techtown.stockking.R
import org.techtown.stockking.databinding.ActivityDetailBinding
import org.techtown.stockking.network.ApiWrapper

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {
    lateinit var symbol: String
    lateinit var binding: ActivityDetailBinding

    lateinit var alertDialog : AlertDialog
    lateinit var builder : AlertDialog.Builder
    var starIs: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        //액티비티 화면 출력
        setContentView(binding.root)

        supportActionBar?.title = ""

        //인텐트에서 데이터를 가져오기
        var stockintent=getIntent()
        binding.nameTv.text = stockintent.getStringExtra("name")
        symbol= stockintent.getStringExtra("symbol").toString()
        binding.symbolTv.text = symbol
        binding.priceTv.text = stockintent.getStringExtra("price")
        binding.percentTv.text = stockintent.getStringExtra("percent")

        binding.star.setOnClickListener {
            showPopup(symbol)
        }

        binding.backBtn.setOnClickListener{
            finish()
        }


        ApiWrapper.getStockDetail(symbol) {
            val lineChart: LineChart = binding.chart

            val dateList = mutableListOf<String>()
            val priceList = mutableListOf<String>()

            it.forEach { element ->
                dateList.add(element.date)
                priceList.add(element.price)
            }

            val entries = ArrayList<Entry>()
            for(i in 0 until priceList.size){
                entries.add(Entry(i.toFloat(), priceList[i].toFloat()))
            }

            val dataset= LineDataSet(entries, "")

            val labels = ArrayList<String>()
            for(i in 0 until dateList.size){
                labels.add(dateList[i])
            }

            lineChart.xAxis.valueFormatter=IndexAxisValueFormatter(labels)

            //클릭시 x축 label 출력
            lineChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry, h: Highlight){
                    val xAxisLabel = e.x.let{
                        lineChart.xAxis.valueFormatter.getAxisLabel(it, lineChart.xAxis)
                    }
                    binding.test.text= xAxisLabel
                }
                override fun onNothingSelected() {
                }
            })

            lineChart.getTransformer(YAxis.AxisDependency.LEFT)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            val data = LineData(dataset)

            lineChart.data = data
            lineChart.invalidate()
        }

    }

    private fun showPopup(symbol: String){
        var msg = "XXX 님의 즐겨찾기에 $symbol 를 추가합니다."
        var btnOK = "OK"
        var btnNO = "cancel"
        builder = AlertDialog.Builder(this)
        builder.setMessage(msg) //팝업창 내용 지정
        builder.setCancelable(false) //외부 레이아웃 클릭시도 팝업창이 사라지지않게 설정
        builder.setPositiveButton(btnOK, DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(application, btnOK, Toast.LENGTH_SHORT).show()
            binding.star.setBackgroundResource(R.drawable.img_star_full)
            starIs = true
        })
        builder.setNegativeButton(btnNO, DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(application, btnNO, Toast.LENGTH_SHORT).show()
            binding.star.setBackgroundResource(R.drawable.img_star_border)
            starIs = false
        })
        alertDialog = builder.create()
        alertDialog.show()
    }

}



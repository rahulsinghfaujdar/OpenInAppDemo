package com.openinapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.highsoft.highcharts.common.HIColor
import com.highsoft.highcharts.common.HIGradient
import com.highsoft.highcharts.common.HIStop
import com.highsoft.highcharts.common.hichartsclasses.HIArea
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject
import com.highsoft.highcharts.common.hichartsclasses.HICredits
import com.highsoft.highcharts.common.hichartsclasses.HIExporting
import com.highsoft.highcharts.common.hichartsclasses.HIHover
import com.highsoft.highcharts.common.hichartsclasses.HILegend
import com.highsoft.highcharts.common.hichartsclasses.HIMarker
import com.highsoft.highcharts.common.hichartsclasses.HIOptions
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions
import com.highsoft.highcharts.common.hichartsclasses.HIStates
import com.highsoft.highcharts.common.hichartsclasses.HITitle
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis
import com.openinapp.R
import com.openinapp.adapter.CARDS
import com.openinapp.adapter.CardRecyclerAdapter
import com.openinapp.adapter.LinksRecyclerAdapter
import com.openinapp.adapter.LinksRecyclerAdapterHelper
import com.openinapp.databinding.ActivityMainBinding
import com.openinapp.datamodel.DateValuePair
import com.openinapp.datamodel.Item
import com.openinapp.datamodel.ResDashboard
import com.openinapp.extension.showSnackBar
import com.openinapp.network.CustomCallback
import com.openinapp.network.LoaderManager
import com.openinapp.network.Status
import com.openinapp.utils.CommonUtil
import com.openinapp.utils.CommonUtil.createRoundedCornerDrawableWithOutline
import com.openinapp.utils.SpaceItemDecoration
import com.openinapp.viewmodel.DashboardActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.LinkedList
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var loader: LoaderManager

    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: DashboardActivityViewModel by viewModels()
    private var linksRVAdapter =
        LinksRecyclerAdapter(emptyList(), helper = LinksRecyclerAdapterHelper {})

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setUpObserver()
        mainActivityViewModel.initApi(
            CommonUtil.loadDataFromJsonAsset(
                context = this,
                jsonName = "dashboardnew"
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpObserver() {
        mainActivityViewModel.dashboardApiData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    loader.showLoader()
                }

                Status.ERROR -> {
                    loader.hideLoader()
                    binding.root.showSnackBar("${it.message}", Snackbar.LENGTH_SHORT)
                }

                Status.SUCCESS -> {
                    loader.hideLoader()
                    initData(it)
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData(it: CustomCallback<ResDashboard>) {
        if (it.data != null) {
            var name: String = it.data.name
            var todaysClick: Int = it.data.todayClicks
            var topLocation: String = it.data.topLocation
            var topSource: String = it.data.topSource
            var startTime: String = it.data.startTime
            var topLinks: List<Item> = it.data.data.topLinks
            var recentLinks: List<Item> = it.data.data.recentLinks
            var overallUrlChart: Any = it.data.data.overallUrlChart
            val dateValuePairs = gson.fromJson<Map<String, Int>>(
                overallUrlChart.toString(),
                Map::class.java
            ).map {
                val date = LocalDate.parse(it.key)
                val milliseconds: Long =
                    date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                DateValuePair(milliseconds.toLong(), it.value)
            }
            binding.content.tvName.text = name
            val todaysClickPair = Pair(CARDS.TODAYS_CLICKS, todaysClick.toString())
            val topLocationPair = Pair(CARDS.TOP_LOCATION, topLocation)
            val topSourcePair = Pair(CARDS.TOP_SOURCE, topSource)
            val startTimePair = Pair(CARDS.START_TIME, startTime)
            val top_cards: List<Pair<CARDS, String>> =
                listOf(todaysClickPair, topLocationPair, topSourcePair, startTimePair)
            initCardsRecycler(top_cards)
            initTabLayout(topLinks = topLinks, recentLinks = recentLinks)
            initSearch()
            binding.content.viewViewAnalytics.setOnClickListener(View.OnClickListener {

            })
            //initHighChart()
        }
    }

    private fun initViews() {
        initHighChart()
        binding.content.tvWishTitle.text = CommonUtil.getGreeting()
        val roundedCornerDrawable = createRoundedCornerDrawableWithOutline(
            this,
            resources.getDimension(R.dimen.corner_radius),
            R.color.white,
            R.color.black,
            resources.getDimensionPixelSize(R.dimen.outline_width)
        )
        binding.content.tvGraphDate.background = roundedCornerDrawable
        val roundedCornerDrawableAnalytics = createRoundedCornerDrawableWithOutline(
            this,
            resources.getDimension(R.dimen.corner_radius),
            android.R.color.transparent,
            R.color.black,
            resources.getDimensionPixelSize(R.dimen.outline_width)
        )
        val roundedCornerDrawableTalkWithUs = createRoundedCornerDrawableWithOutline(
            this,
            resources.getDimension(R.dimen.corner_radius),
            R.color.tintWhatsapp,
            R.color.tintWhatsappOutline,
            resources.getDimensionPixelSize(R.dimen.outline_width_2)
        )
        val roundedCornerDrawableFrequentlyAskedQuestions = createRoundedCornerDrawableWithOutline(
                this,
                resources.getDimension(R.dimen.corner_radius),
                R.color.frequentlyAskedQuestions,
                R.color.colorPrimary,
                resources.getDimensionPixelSize(R.dimen.outline_width_2)
            )
        binding.content.tvTalkWithUs.background = roundedCornerDrawableTalkWithUs
        binding.content.viewViewAnalytics.background = roundedCornerDrawableAnalytics
        binding.content.viewViewAllLinks.background = roundedCornerDrawableAnalytics
        binding.content.tvFrequentlyAskedQuestions.background = roundedCornerDrawableFrequentlyAskedQuestions
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        val itemDecoration = SpaceItemDecoration(spacingInPixels)
        binding.content.rvTabContainer.addItemDecoration(itemDecoration)
        binding.content.rvTopCards.addItemDecoration(itemDecoration)
    }

    private fun initSearch() {
        binding.content.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                linksRVAdapter.filter.filter(newText)
                return false
            }

        })

        val textView =
            binding.content.searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        textView.setTextColor(resources.getColor(R.color.black))
        val cancelIcon =
            binding.content.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(resources.getColor(R.color.colorPrimary))
        val searchIcon =
            binding.content.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(resources.getColor(R.color.colorPrimary))
        binding.content.ivSearch.setOnClickListener(View.OnClickListener {
            if (binding.content.searchView.visibility == View.VISIBLE) {
                binding.content.searchView.visibility = View.GONE
            } else {
                binding.content.searchView.visibility = View.VISIBLE
            }
        })
    }

    private fun initTabLayout(topLinks: List<Item>, recentLinks: List<Item>) {
        binding.content.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                if (position == 0) {
                    initTabRecycler(topLinks)
                } else {
                    initTabRecycler(recentLinks)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab!!.position
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                if (position == 0) {
                    initTabRecycler(topLinks)
                }
            }

        })
        binding.content.tab.selectTab(binding.content.tab.getTabAt(0))
    }

    private fun initCardsRecycler(items: List<Pair<CARDS, String>>) {
        binding.content.rvTabContainer.layoutManager = LinearLayoutManager(this)
        val cardsRVAdapter = CardRecyclerAdapter(items)
        binding.content.rvTopCards.adapter = cardsRVAdapter
    }

    private fun initTabRecycler(items: List<Item>) {
        binding.content.rvTabContainer.layoutManager = LinearLayoutManager(this)
        linksRVAdapter =
            LinksRecyclerAdapter(mList = items, helper = LinksRecyclerAdapterHelper { it ->
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Link", it)
                clipboardManager.setPrimaryClip(clipData)
                binding.root.showSnackBar("Link Copy\n$it", Snackbar.LENGTH_SHORT)
            })
        binding.content.rvTabContainer.adapter = linksRVAdapter
    }

    private fun initHighChart() {
        binding.content.hc.theme = "grid-light"
        val options: HIOptions = HIOptions()

        //Title
        val title: HITitle = HITitle()
        title.text = "Overview"
        title.align = "left"
        val titleStyle = HICSSObject()
        titleStyle.fontSize = "14px"
        titleStyle.color = HIColor.initWithRGB(153, 156, 160)
        title.style = titleStyle
        title.y = 0
        title.x = 0
        options.title = title

        //X-Axis
        val xaxis = HIXAxis()
        xaxis.type = "datetime"
        options.xAxis = ArrayList(listOf(xaxis))

        //Y-Axis
        val yaxis = HIYAxis()
        yaxis.title = HITitle()
        yaxis.title.text = ""
        yaxis.addTitle(false)
        options.yAxis = ArrayList(listOf(yaxis))

        //Legend
        val legend = HILegend()
        legend.enabled = false
        options.legend = legend

        //HIPlotOptions
        val plotoptions = HIPlotOptions()
        plotoptions.area = HIArea()
        val stops = LinkedList<HIStop>()
        stops.add(HIStop(0f, HIColor.initWithRGBA(14, 111, 255, 0.3)))
        stops.add(HIStop(1f, HIColor.initWithRGBA(14, 111, 255, 0.0)))
        plotoptions.area.fillColor = HIColor.initWithLinearGradient(HIGradient(), stops)
        plotoptions.area.lineColor = HIColor.initWithRGB(14, 111, 255)
        plotoptions.area.marker = HIMarker()
        plotoptions.area.marker.radius = 0
        plotoptions.area.lineWidth = 3
        val state = HIStates()
        state.hover = HIHover()
        state.hover.lineWidth = 3
        plotoptions.area.states = state
        options.plotOptions = plotoptions

        val area = HIArea()
        area.name = "Clicks"
        val areaData: Array<Array<Long>> = arrayOf(
            arrayOf(1683504000000L, 0),
            arrayOf(1683590400000L, 0),
            arrayOf(1683676800000L, 0),
            arrayOf(1683763200000L, 0),
            arrayOf(1683849600000L, 1),
            arrayOf(1683936000000L, 1),
            arrayOf(1684022400000L, 0),
            arrayOf(1684108800000L, 0),
            arrayOf(1684195200000L, 0),
            arrayOf(1684281600000L, 0),
            arrayOf(1684368000000L, 0),
            arrayOf(1684454400000L, 9),
            arrayOf(1684540800000L, 4),
            arrayOf(1684627200000L, 1),
            arrayOf(1684713600000L, 10),
            arrayOf(1684800000000L, 7),
            arrayOf(1684886400000L, 9),
            arrayOf(1684972800000L, 0),
            arrayOf(1685059200000L, 2),
            arrayOf(1685145600000L, 2),
            arrayOf(1685232000000L, 1),
            arrayOf(1685318400000L, 0),
            arrayOf(1685404800000L, 2),
            arrayOf(1685491200000L, 1),
            arrayOf(1685577600000L, 1),
            arrayOf(1685664000000L, 5),
            arrayOf(1685750400000L, 1),
            arrayOf(1685836800000L, 2),
            arrayOf(1685923200000L, 19),
            arrayOf(1686009600000L, 0),
            arrayOf(1686096000000L, 1)
        )
        area.data = ArrayList(listOf(*areaData))
        options.series = ArrayList(listOf(area))
        binding.content.hc.options = options
        binding.content.hc.options!!.exporting = HIExporting().apply { enabled = false }
        binding.content.hc.options!!.credits = HICredits().apply { enabled = false }
        val simpleDateFormat = SimpleDateFormat("dd MMM")
        val min = simpleDateFormat.format(areaData[0][0])
        val max = simpleDateFormat.format(areaData[areaData.size - 1][0])
        binding.content.tvGraphDate.text = "${min} - ${max}"
    }

}
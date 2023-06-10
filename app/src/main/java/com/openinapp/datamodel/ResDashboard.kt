package com.openinapp.datamodel

import com.google.gson.annotations.SerializedName

data class ResDashboard(

	@field:SerializedName("top_location")
	val topLocation: String,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("extra_income")
	val extraIncome: Any,

	@field:SerializedName("total_links")
	val totalLinks: Int,

	@field:SerializedName("top_source")
	val topSource: String,

	@field:SerializedName("total_clicks")
	val totalClicks: Int,

	@field:SerializedName("startTime")
	val startTime: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("applied_campaign")
	val appliedCampaign: Int,

	@field:SerializedName("support_whatsapp_number")
	val supportWhatsappNumber: String,

	@field:SerializedName("today_clicks")
	val todayClicks: Int,

	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("links_created_today")
	val linksCreatedToday: Int
)

data class Item(

	@field:SerializedName("app")
	val app: String,

	@field:SerializedName("thumbnail")
	val thumbnail: Any,

	@field:SerializedName("smart_link")
	val smartLink: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("url_id")
	val urlId: Int,

	@field:SerializedName("web_link")
	val webLink: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("times_ago")
	val timesAgo: String,

	@field:SerializedName("url_prefix")
	val urlPrefix: String,

	@field:SerializedName("domain_id")
	val domainId: String,

	@field:SerializedName("url_suffix")
	val urlSuffix: String,

	@field:SerializedName("original_image")
	val originalImage: String,

	@field:SerializedName("total_clicks")
	val totalClicks: Int
)

data class Data(

	@field:SerializedName("top_links")
	val topLinks: List<Item>,

	@field:SerializedName("recent_links")
	val recentLinks: List<Item>,

	@field:SerializedName("overall_url_chart")
	val overallUrlChart: Any
)

data class DateValuePair(val date: Long, val value: Int)
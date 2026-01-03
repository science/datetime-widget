package com.science.datetimewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.RemoteViews

class DateTimeWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            updateWidget(context, appWidgetManager, appWidgetId, options)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        updateWidget(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        options: Bundle
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Get widget dimensions in dp
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 40)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 40)

        // Calculate font sizes based on available space
        // Time format "10:30" = 5 chars, Date format "Sep 30" = 6 chars (worst case)
        // We have 2 lines, so split height roughly 60/40 for time/date
        val (timeSizeSp, dateSizeSp) = calculateFontSizes(minWidth, minHeight)

        // Apply font sizes (using SP for accessibility scaling)
        views.setTextViewTextSize(R.id.time_text, TypedValue.COMPLEX_UNIT_SP, timeSizeSp)
        views.setTextViewTextSize(R.id.date_text, TypedValue.COMPLEX_UNIT_SP, dateSizeSp)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun calculateFontSizes(widthDp: Int, heightDp: Int): Pair<Float, Float> {
        // Padding takes ~8dp (4dp on each side)
        val availableWidth = (widthDp - 8).coerceAtLeast(32)
        val availableHeight = (heightDp - 8).coerceAtLeast(32)

        // Time: "10:30" = 5 chars, average char width ~0.6 of font size
        // Date: "Sep 30" = 6 chars
        val timeChars = 5
        val dateChars = 6

        // Calculate max font size that fits width
        // Font size * 0.6 * charCount ≈ text width
        val timeSizeByWidth = (availableWidth / (timeChars * 0.6)).toFloat()
        val dateSizeByWidth = (availableWidth / (dateChars * 0.6)).toFloat()

        // Calculate max font size that fits height
        // Two lines with some spacing: time takes ~55%, date takes ~45%
        val timeSizeByHeight = (availableHeight * 0.55 / 1.2).toFloat()  // 1.2 accounts for line height
        val dateSizeByHeight = (availableHeight * 0.45 / 1.2).toFloat()

        // Use the smaller of width/height constraints
        var timeSizeSp = minOf(timeSizeByWidth, timeSizeByHeight)
        var dateSizeSp = minOf(dateSizeByWidth, dateSizeByHeight)

        // Clamp to reasonable range (8sp min for readability, 72sp max)
        timeSizeSp = timeSizeSp.coerceIn(8f, 72f)
        dateSizeSp = dateSizeSp.coerceIn(8f, 72f)

        return Pair(timeSizeSp, dateSizeSp)
    }
}

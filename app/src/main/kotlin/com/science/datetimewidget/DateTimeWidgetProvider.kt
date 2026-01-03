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

        // Calculate font size - both lines equal size
        val fontSizeSp = calculateFontSize(minWidth, minHeight)

        // Apply same font size to both lines
        views.setTextViewTextSize(R.id.day_of_week, TypedValue.COMPLEX_UNIT_SP, fontSizeSp)
        views.setTextViewTextSize(R.id.month_day, TypedValue.COMPLEX_UNIT_SP, fontSizeSp)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun calculateFontSize(widthDp: Int, heightDp: Int): Float {
        // Padding takes ~8dp (4dp on each side)
        val availableWidth = (widthDp - 8).coerceAtLeast(32)
        val availableHeight = (heightDp - 8).coerceAtLeast(32)

        // Line 1: "Sat" = 3 chars
        // Line 2: "Sep 30" = 6 chars (worst case)
        // Use longer text (6 chars) to ensure both fit
        val maxChars = 6

        // Calculate max font size that fits width
        // Character width is roughly 0.5x font size for bold sans-serif
        val sizeByWidth = (availableWidth / (maxChars * 0.5)).toFloat()

        // Calculate max font size that fits height
        // Two lines, each takes ~50% of height (no extra line spacing needed)
        val sizeByHeight = (availableHeight * 0.45).toFloat()

        // Use the smaller constraint
        var fontSizeSp = minOf(sizeByWidth, sizeByHeight)

        // Clamp to reasonable range
        fontSizeSp = fontSizeSp.coerceIn(12f, 120f)

        return fontSizeSp
    }
}

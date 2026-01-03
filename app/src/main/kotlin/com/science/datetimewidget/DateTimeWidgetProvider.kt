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
        // Approximate: font size * 0.6 * charCount ≈ text width
        val sizeByWidth = (availableWidth / (maxChars * 0.6)).toFloat()

        // Calculate max font size that fits height
        // Two equal lines: each gets 50% of height
        // Account for line height (~1.2x font size)
        val sizeByHeight = (availableHeight * 0.5 / 1.2).toFloat()

        // Use the smaller constraint
        var fontSizeSp = minOf(sizeByWidth, sizeByHeight)

        // Clamp to reasonable range
        fontSizeSp = fontSizeSp.coerceIn(8f, 96f)

        return fontSizeSp
    }
}

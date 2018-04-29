package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue

class AutoFitGridLayoutManager @JvmOverloads constructor(context: Context, spanCount: Int = 3, columnWidth: Int = 110, orientation: Int = 0, reverseLayout: Boolean = false)
    : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    init {
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    private var mColumnWidth: Int = 0
    private var mColumnWidthChanged = true

    private fun checkedColumnWidth(context: Context, columnWidth: Int): Int {
        var columnW = columnWidth
        if (columnW <= 0) {
            /* Set default columnW value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            columnW = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f,
                    context.resources.displayMetrics).toInt()
        }
        return columnW
    }

    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth
            mColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        val width = width
        val height = height
        if (mColumnWidthChanged && mColumnWidth > 0 && width > 0 && height > 0) {
            val totalSpace: Int = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / mColumnWidth)
            setSpanCount(spanCount)
            mColumnWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }
}
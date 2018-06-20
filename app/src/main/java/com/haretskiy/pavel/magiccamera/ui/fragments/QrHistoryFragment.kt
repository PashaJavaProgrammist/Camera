package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.QrHistoryAdapter
import com.haretskiy.pavel.magiccamera.convertToDate
import com.haretskiy.pavel.magiccamera.ui.activities.HostActivity
import com.haretskiy.pavel.magiccamera.ui.views.QRHistory
import com.haretskiy.pavel.magiccamera.ui.views.QrHistoryHolder
import com.haretskiy.pavel.magiccamera.utils.interfaces.DeleteListener
import com.haretskiy.pavel.magiccamera.viewModels.QrHistoryVewModel
import kotlinx.android.synthetic.main.fragment_qrhistory.*
import org.koin.android.architecture.ext.viewModel


class QrHistoryFragment : Fragment(), QRHistory {

    private val qrHistoryVewModel: QrHistoryVewModel by viewModel()

    private val adapter by lazy {
        context?.let { QrHistoryAdapter(it, this, emptyList()) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_qrhistory, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFab()

        initRecycler()

        initRecyclerListener()

        initItemTouchHelper()
    }

    private fun initFab() {
        fab_qr_history.setOnClickListener {
            qrHistoryVewModel.turnOnQRDetector()
            (activity as HostActivity).selectItemCamera()
        }
    }

    private fun initRecycler() {
        rv_qr_history.layoutManager = LinearLayoutManager(context)
        rv_qr_history.adapter = adapter

        qrHistoryVewModel.storageBarCodesLiveData.observe(this, Observer {
            if (it != null) adapter?.list = it
            adapter?.notifyDataSetChanged()
        })
    }

    private fun initRecyclerListener() {
        rv_qr_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (fab_qr_history != null)
                    if (dy > 0 && fab_qr_history.visibility == View.VISIBLE) {
                        fab_qr_history.hide()
                    } else if (dy < 0 && fab_qr_history.visibility != View.VISIBLE) {
                        fab_qr_history.show()
                    }
            }
        })
    }

    private fun initItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    qrHistoryVewModel.deleteQrCodeFromDB(
                            childFragmentManager,
                            (viewHolder as QrHistoryHolder).barCode.code,
                            object : DeleteListener {
                                override fun onConfirm() {}

                                override fun onDismiss() {
                                    adapter?.notifyDataSetChanged()
                                }
                            })
                } else
                    if (direction == ItemTouchHelper.RIGHT) {
                        val holder = viewHolder as QrHistoryHolder
                        if (direction == ItemTouchHelper.RIGHT) {
                            qrHistoryVewModel.startBarcodeActivity(holder.barCode.code, holder.barCode.date.convertToDate())
                        }
                    }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView

                    val p = Paint()
                    val icon: Bitmap

                    if (dX > 0) {
                        icon = BitmapFactory.decodeResource(
                                context?.resources, R.drawable.ic_show2)

//                        context?.let { p.color = getColor(it, R.color.green) }
//                        c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX,
//                                itemView.bottom.toFloat(), p)

                        c.drawBitmap(icon,
                                itemView.left.toFloat() + convertDpToPx(16),
                                itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                                p)
                    } else {
                        icon = BitmapFactory.decodeResource(
                                context?.resources, R.drawable.ic_trash2)

//                        context?.let { p.color = getColor(it, R.color.red) }
//                        c.drawRect(itemView.right.toFloat() + dX, itemView.top.toFloat(),
//                                itemView.right.toFloat(), itemView.bottom.toFloat(), p)

                        c.drawBitmap(icon,
                                itemView.right.toFloat() - convertDpToPx(16) - icon.width,
                                itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                                p)
                    }

                    viewHolder.itemView.translationX = dX

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

        }).apply {
            attachToRecyclerView(rv_qr_history)
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.notifyDataSetChanged()
    }

    private fun convertDpToPx(dp: Int): Int {
        return Math.round(dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    override fun onClickHistoryItem(content: String, date: String, contentView: TextView, dateView: TextView) {
        activity?.let { activity -> context?.let { context -> qrHistoryVewModel.startBarcodeActivityWithAnimation(context, activity, content, date, contentView, dateView) } }
    }

    override fun onLongClickHistoryItem(content: String) {
        qrHistoryVewModel.deleteQrCodeFromDB(childFragmentManager, content)
    }
}

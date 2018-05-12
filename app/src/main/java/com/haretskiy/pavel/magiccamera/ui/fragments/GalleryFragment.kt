package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.GalleryPhotoAdapter
import com.haretskiy.pavel.magiccamera.ui.activities.HostActivity
import com.haretskiy.pavel.magiccamera.ui.views.PhotoGallery
import com.haretskiy.pavel.magiccamera.ui.views.PhotoHolder
import com.haretskiy.pavel.magiccamera.utils.AutoFitGridLayoutManager
import com.haretskiy.pavel.magiccamera.utils.DiffCallBack
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.DeleteListener
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.viewModels.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment(), PhotoGallery {

    private val galleryViewModel: GalleryViewModel by inject()
    private val diffCallBack: DiffCallBack by inject()
    private val imageLoader: ImageLoader by inject()
    private val toaster: Toaster by inject()

    private val galleryAdapter = GalleryPhotoAdapter(diffCallBack, imageLoader, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryViewModel.checkedPhotosData.observe(this, Observer {
            if (it != null) {
                showActionButtons(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionButtons()
        val c = context
        if (c != null) {
            rcv_gallery_list.layoutManager = AutoFitGridLayoutManager(
                    c,
                    3,
                    resources.getDimension(R.dimen.card_width).toInt(),
                    GridLayoutManager.VERTICAL, false)
        }

        rcv_gallery_list.adapter = galleryAdapter

        galleryViewModel.getAllUserPhotosLiveData().observe(this, Observer { galleryAdapter.submitList(it) })

        rcv_gallery_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (fab_gallery != null)
                    if (dy > 0 && fab_gallery.visibility == View.VISIBLE) {
                        fab_gallery.hide()
                    } else if (dy < 0 && fab_gallery.visibility != View.VISIBLE) {
                        fab_gallery.show()
                    }
            }
        })

        fab_gallery.setOnClickListener {
            galleryViewModel.turnOffQrDetector()
            (activity as HostActivity).selectItemCamera()
        }

        bt_share.setOnClickListener {
            galleryViewModel.shareImages()
        }

        bt_delete_photos.setOnClickListener {
            galleryViewModel.deleteSelectedPhotos(childFragmentManager, object : ImageSaverImpl.DeletingListener {
                override fun onSuccess() {
                    activity?.runOnUiThread {
                        toaster.showToast(getString(R.string.photos_ch_del), false)
                        showActionButtons()
                    }
                }

                override fun onError(errorMessage: String) {
                    activity?.runOnUiThread {
                        toaster.showToast(errorMessage, false)
                    }
                }
            })
        }

        bt_unchecked_all.setOnClickListener {
            galleryViewModel.clearCheckedItems()
            showActionButtons()
            galleryAdapter.notifyDataSetChanged()
        }

        initTouchListener()
    }

    override fun onClickPhoto(uri: String, date: Long) {
        galleryViewModel.runDetailActivity(uri, date)
    }

    override fun onLongClickPhoto(uri: String, listener: GalleryViewModel.OnCheckedListener): Boolean {
        galleryViewModel.fillShareContainer(uri, listener)
        return true
    }

    override fun isPhotoCheckedToShare(uri: String) = galleryViewModel.isPhotoCheckedToShare(uri)

    private fun initTouchListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    galleryViewModel.deletePhoto(
                            childFragmentManager,
                            (viewHolder as PhotoHolder).uri,
                            object : DeleteListener {
                                override fun onConfirm() {}

                                override fun onDismiss() {
                                    galleryAdapter.notifyDataSetChanged()
                                }
                            })
                }
            }

        }).apply {
            attachToRecyclerView(rcv_gallery_list)
        }
    }

    private fun showActionButtons(count: Int) {
        actions_container.visibility = when {
            count > 0 -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun showActionButtons() {
        actions_container.visibility = when {
            galleryViewModel.isPhotosChecked() -> View.VISIBLE
            else -> View.GONE
        }
    }

}

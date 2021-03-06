package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getDrawable
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
import kotlinx.android.synthetic.main.item_actions.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment(), PhotoGallery {

    private val galleryViewModel: GalleryViewModel by viewModel()
    private val diffCallBack: DiffCallBack by inject()
    private val imageLoader: ImageLoader by inject()
    private val toaster: Toaster by inject()
    private val layoutManager: AutoFitGridLayoutManager? by lazy {
        context?.let {
            AutoFitGridLayoutManager(
                    it,
                    3,
                    resources.getDimension(R.dimen.card_width).toInt(),
                    GridLayoutManager.VERTICAL, false)
        }
    }

    private val galleryAdapter = GalleryPhotoAdapter(diffCallBack, imageLoader, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showActionButtons()

        initButtons()

        initRecycler()

        initObservers()

        initTouchListener()
    }

    override fun onResume() {
        super.onResume()
        rcv_gallery_list.scrollToPosition(galleryViewModel.rvPosition)
    }

    override fun onClickPhoto(uri: String, date: Long) {
        galleryViewModel.runDetailActivity(uri, date)
    }

    override fun onLongClickPhoto(uri: String, listener: GalleryViewModel.OnSelectedPhotoListener): Boolean {
        galleryViewModel.fillShareContainer(uri, listener)
        setActionSelectDrawable()
        return true
    }

    override fun isPhotoCheckedToShare(uri: String) = galleryViewModel.isPhotoAlreadySelected(uri)

    private fun initObservers() {
        galleryViewModel.selectedPhotosData.observe(this, Observer {
            showActionButtons()
        })

        galleryViewModel.getAllUserPhotosLiveData().observe(this, Observer {
            if (it != null) {
                galleryAdapter.submitList(it)
                galleryViewModel.listOfPhotos = it.toMutableList()
                rcv_gallery_list.scrollToPosition(galleryViewModel.rvPosition)
            }
        })
    }

    private fun initRecycler() {
        rcv_gallery_list.layoutManager = layoutManager
        rcv_gallery_list.adapter = galleryAdapter
        rcv_gallery_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager?.findFirstCompletelyVisibleItemPosition() != 0)
                    galleryViewModel.rvPosition = layoutManager?.findFirstCompletelyVisibleItemPosition() ?: 0
            }

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
    }

    private fun initButtons() {

        fab_gallery.setOnClickListener {
            galleryViewModel.turnOffQrDetector()
            (activity as HostActivity).selectItemCamera()
        }

        bt_share.setOnClickListener {
            galleryViewModel.shareImages()
        }

        bt_delete_photos.setOnClickListener {
            galleryViewModel.deleteSelectedPhotos(childFragmentManager, object : ImageSaverImpl.DeletingPhotoListener {
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
            if (galleryViewModel.isAllItemsSelected()) {
                unSelectAll()
            } else {
                selectAll()
            }
        }
    }

    private fun selectAll() {
        galleryViewModel.selectAllItems()
        showActionButtons()
        bt_unchecked_all.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_unselect_all) })
        tv_action_select_photos.setText(R.string.unselect_all)
        galleryAdapter.notifyDataSetChanged()
    }

    private fun unSelectAll() {
        galleryViewModel.clearSelectedItems()
        showActionButtons()
        bt_unchecked_all.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_select_all) })
        tv_action_select_photos.setText(R.string.select_all)
        galleryAdapter.notifyDataSetChanged()
    }

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

    private fun setActionSelectDrawable() {
        if (galleryViewModel.isAllItemsSelected()) {
            bt_unchecked_all.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_unselect_all) })
            tv_action_select_photos.setText(R.string.unselect_all)
        } else {
            bt_unchecked_all.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_select_all) })
            tv_action_select_photos.setText(R.string.select_all)
        }
    }

    private fun showActionButtons() {
        actions_container.visibility = when {
            galleryViewModel.isAtLeastOnePhotoSelected() -> View.VISIBLE
            else -> View.GONE
        }
    }

}

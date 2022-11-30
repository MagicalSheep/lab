package cn.magicalsheep.myapplication

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val photos = ArrayList<Bitmap>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView

        init {
            imageView = view.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.image_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val photo = photos[position]
        viewHolder.imageView.setImageBitmap(photo)
    }

    override fun getItemCount() = photos.size

    fun add(bitmap: Bitmap) = synchronized(Unit) {
        photos.add(bitmap)
        notifyItemInserted(itemCount)
    }
}
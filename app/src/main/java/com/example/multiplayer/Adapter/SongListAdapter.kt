package Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.multiplayer.Interface.CustomItemClickListener
import com.example.multiplayer.Model.SongModel
import com.example.multiplayer.R
import com.example.multiplayer.Service.PlayMusicService
import kotlinx.android.synthetic.main.music_list.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class SongListAdapter(SongModel: ArrayList<SongModel>,context:Context):RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {

    var mcontext=context
    var msongModel = SongModel
    var allMusicList : ArrayList<String> = ArrayList()

    companion object{
        val MUSICLIST = "musiclist"
        var MUSICITEMPOS="pos"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {

        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.music_list,parent,false)
        return SongListViewHolder(view)

    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {

        var model = msongModel[position]
        var songName = model.mSongName
        var songDuration = toMandS(model.mSongDuration.toLong())
        holder!!.songTV.text=songName
        holder.durationTV.text= songDuration
        holder.setOnCustomItemClickListener(object : CustomItemClickListener {
            override fun onCustomItemClick(view: View, pos: Int) {

                for (i in 0 until msongModel.size){

                    allMusicList.add(msongModel[i].mSongName)

                }

                Toast.makeText(mcontext, "Song Title"+songName, Toast.LENGTH_SHORT).show()

                var musicDataIntent = Intent(mcontext,PlayMusicService::class.java)
//put data to services
                musicDataIntent.putStringArrayListExtra(MUSICLIST,allMusicList)

                musicDataIntent.putExtra(MUSICITEMPOS,pos)

                mcontext.startService(musicDataIntent)
            }
        })

    }

    fun toMandS(millis:Long):String{
        var duration=String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millis),
        TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MILLISECONDS.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(millis)
        ))
        return duration
    }

    override fun getItemCount(): Int {
        return msongModel.size
    }

    class SongListViewHolder(itemView : View): RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var songTV : TextView
        var durationTV : TextView
        var albumnArt : ImageView
        var mCustomItemClickListener:CustomItemClickListener?=null

        init {
            songTV=itemView.findViewById(R.id.song_name_tv)
            durationTV=itemView.findViewById(R.id.song_duration_tv)
            albumnArt=itemView.findViewById(R.id.al_img_view)

            itemView.setOnClickListener(this)

        }
        fun setOnCustomItemClickListener(customItemClickListener: CustomItemClickListener){
            this.mCustomItemClickListener=customItemClickListener
        }

        override fun onClick(view: View?) {
            this.mCustomItemClickListener!!.onCustomItemClick(view!!,adapterPosition)
        }

    }


}
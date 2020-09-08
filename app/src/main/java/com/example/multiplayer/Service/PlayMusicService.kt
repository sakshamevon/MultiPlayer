package com.example.multiplayer.Service

import Adapter.SongListAdapter
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class PlayMusicService : Service() {

    var currentPos: Int = 0
    var musicDataList: ArrayList<String> = ArrayList()
    var mp: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null) {
            musicDataList =
                intent.getStringArrayListExtra(SongListAdapter.MUSICLIST) as ArrayList<String>
            Log.e("MUSIC_DATA_LIST", "" + musicDataList)
        }

        currentPos = intent!!.getIntExtra(SongListAdapter.MUSICITEMPOS, 0)
        Log.e("CURRENT_POS", "" + currentPos)

        if (mp != null) {
            mp!!.stop()
            mp!!.release()
            mp = null
        }

        mp = MediaPlayer()
        mp!!.setDataSource(musicDataList.get(currentPos))
        mp!!.prepare()
        mp!!.setOnPreparedListener {
            mp!!.start()
        }

        return super.onStartCommand(intent, flags, startId)
    }
}
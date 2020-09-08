package com.example.multiplayer

import Adapter.SongListAdapter
import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multiplayer.Model.SongModel
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Permission
import java.util.*

class MainActivity : AppCompatActivity() {

    var songModelData:ArrayList<SongModel> = ArrayList()
    var songListAdapter: SongListAdapter?=null

    companion object{
        val PERMISSION_REQUEST_CODE=12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)

        }else{
            loadData()
        }



    }

    fun loadData(){
        var songcursor:Cursor?= contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,null,null,null)
        while(songcursor != null && songcursor.moveToNext())
        {

            var songName=songcursor.getString(songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            var songDuration = songcursor.getString(songcursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            var songPath: String = songcursor.getString(songcursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            songModelData.add(SongModel(songName,songDuration,songPath))
        }
        songListAdapter = SongListAdapter(songModelData,applicationContext)
        var layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.layoutManager=layoutManager
        recycler_view.adapter=songListAdapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
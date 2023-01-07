package com.example.nmedia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dao.PostRemoteKeyDao
import com.example.nmedia.entity.PostEntity
import com.example.nmedia.entity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class, PostRemoteKeyEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase(){
  abstract fun postDao():PostDao
  abstract fun postRemoteKeyDao(): PostRemoteKeyDao

}
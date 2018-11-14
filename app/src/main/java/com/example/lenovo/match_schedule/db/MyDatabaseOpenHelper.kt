package com.example.lenovo.match_schedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import java.awt.font.TextAttribute

class MyDatabaseOpenHelper(ctx : Context): ManagedSQLiteOpenHelper(ctx, "FavoriteMatch.db", null, 1){
    companion object {
        private var instance : MyDatabaseOpenHelper? = null

        fun getInstance(ctx: Context) : MyDatabaseOpenHelper{
            if(instance == null){
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(FavoriteMatch.TABLE_FAVORITE_MATCH, true,
                FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteMatch.ID_EVENT to TEXT + UNIQUE,
                FavoriteMatch.TEAM_HOME_ID to TEXT,
                FavoriteMatch.TEAM_AWAY_ID to TEXT,
                //FavoriteMatch.TEAM_HOME_NAME to TEXT,
                //FavoriteMatch.TEAM_AWAY_NAME to TEXT,
                FavoriteMatch.TEAM_HOME_IMAGE to TEXT,
                FavoriteMatch.TEAM_AWAY_IMAGE to TEXT,
                FavoriteMatch.TEAM_HOME_SCORE to TEXT,
                FavoriteMatch.TEAM_AWAY_SCORE to TEXT,
                FavoriteMatch.DATE_EVENT to TEXT

                )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteMatch.TABLE_FAVORITE_MATCH, true)
    }

}
val Context.database : MyDatabaseOpenHelper
get() = MyDatabaseOpenHelper.getInstance(applicationContext)
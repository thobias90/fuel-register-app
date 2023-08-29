package com.stahlt.fuel_register_app

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var etFuelCode: EditText
    private lateinit var etCity: EditText
    private lateinit var etFuelAmount: EditText
    private lateinit var dataBase: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFuelCode = findViewById(R.id.etFuelCode)
        etCity = findViewById(R.id.etCity)
        etFuelAmount = findViewById(R.id.etFuelAmount)

        dataBase = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("dbfile.sqlite"),
            null)
        dataBase.execSQL("CREATE TABLE IF NOT EXISTS refuel " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, fuel TEXT, city TEXT, amount TEXT)")
    }

    fun btAddOnClick(view: View) {}
    fun btSearchOnClick(view: View) {}
}
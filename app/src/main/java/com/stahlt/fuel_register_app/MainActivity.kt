package com.stahlt.fuel_register_app

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

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

    fun btAddOnClick(view: View) {
        val register = ContentValues()
        register.put("fuel", etFuelCode.text.toString())
        register.put("city", etCity.text.toString())
        register.put("amount", etFuelAmount.text.toString())
        dataBase.insert("refuel", null, register)

        val registers = dataBase.query("refuel", null, null,
            null, null, null, null)
        var amountOfRegisters = 0
        while(registers.moveToNext()) {
            amountOfRegisters += 1
        }
        registers.close()
        Toast.makeText(this, "Element was added successfully. " +
                "There is $amountOfRegisters registers now", Toast.LENGTH_SHORT).show()
    }
}
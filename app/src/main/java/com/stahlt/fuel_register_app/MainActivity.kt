package com.stahlt.fuel_register_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var etFuelCode: EditText
    private lateinit var etCity: EditText
    private lateinit var etFuelAmount: EditText
    private lateinit var dataBase: FuelDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFuelCode = findViewById(R.id.etFuelCode)
        etCity = findViewById(R.id.etCity)
        etFuelAmount = findViewById(R.id.etFuelAmount)

        dataBase = FuelDatabase(this)
    }

    fun btAddOnClick(view: View) {
        if (etFuelCode.text.isEmpty()) {
            etFuelCode.error = "Fuel Code must be filled"
        } else if (etCity.text.isEmpty()) {
            etCity.error = "City must be filled"
        } else if (etFuelAmount.text.isEmpty()) {
            etFuelAmount.error = "Amount must be filled"
        } else {
            if (VALID_FUELS.contains(etFuelCode.text.toString().toInt())) {
                dataBase.addRegister(
                    etFuelCode.text.toString(),
                    etCity.text.toString(),
                    etFuelAmount.text.toString())
                val amountOfRegisters = dataBase.getRegisterAmount(etFuelCode.text.toString())
                Toast.makeText(this, "Element was added successfully. " +
                            "There is $amountOfRegisters registers now", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(ContextThemeWrapper(this, com.google.android.material.R.style.AlertDialog_AppCompat))
                with(builder) {
                    setTitle("Valid Fuel Codes")
                    setMessage("1 - Gas\n2 - Ethanol\n3 - Diesel\n4 - Natural Gas")
                    setPositiveButton("OK") { _, _ ->
                        closeContextMenu()
                    }
                    show()
                }
                etFuelCode.error = "Fuel Code must be valid"
            }
        }
    }

    fun btSearchOnClick(view: View) {
        if (etFuelCode.text.isEmpty()) {
            etFuelCode.error = "Fuel Code must be filled"
        } else {
            if (VALID_FUELS.contains(etFuelCode.text.toString().toInt())) {
                val amountOfRegisters = dataBase.getRegisterAmount(etFuelCode.text.toString())
                Toast.makeText(this, "There is $amountOfRegisters registers for ${etFuelCode.text}", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(ContextThemeWrapper(this, com.google.android.material.R.style.AlertDialog_AppCompat))
                with(builder) {
                    setTitle("Valid Fuel Codes")
                    setMessage("1 - Gas\n2 - Ethanol\n3 - Diesel\n4 - Natural Gas")
                    setPositiveButton("OK") { _, _ ->
                        closeContextMenu()
                    }
                    show()
                }
                etFuelCode.error = "Fuel Code must be valid"
            }
        }
    }

    fun btStatisticsOnClick(view: View) {
        val output = StringBuilder()
        var amount = dataBase.getAmount(GAS_ID)
        output.append("1-GASOLINE: $amount\n")
        amount = dataBase.getAmount(ETHANOL_ID)
        output.append("2-ETHANOL: $amount\n")
        amount = dataBase.getAmount(DIESEL_ID)
        output.append("3-DIESEL: $amount\n")
        amount = dataBase.getAmount(NATURAL_GAS_ID)
        output.append("4-NATURAL GAS: $amount\n")

        val builder = AlertDialog.Builder(ContextThemeWrapper(this, com.google.android.material.R.style.AlertDialog_AppCompat))
        with(builder) {
            setTitle("Statistics")
            setMessage(output)
            setPositiveButton("OK") { _, _ ->
                closeContextMenu()
            }
            show()
        }
    }

    companion object {
        const val GAS_ID = 1
        const val ETHANOL_ID = 2
        const val DIESEL_ID = 3
        const val NATURAL_GAS_ID = 4
        val VALID_FUELS = arrayOf(GAS_ID, ETHANOL_ID, DIESEL_ID, NATURAL_GAS_ID)
    }
}

class FuelDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_FUEL TEXT, $KEY_CITY TEXT, $KEY_AMOUNT TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //TODO implements
    }

    fun addRegister(fuel: String, city: String, amount: String) {
        val register = ContentValues()
        register.put(KEY_FUEL, fuel)
        register.put(KEY_CITY, city)
        register.put(KEY_AMOUNT, amount)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, register)
        db.close()
    }

    fun getRegisterAmount(fuel: String): Int {
        val db = this.readableDatabase
        val query = "SELECT _id FROM $TABLE_NAME WHERE $KEY_FUEL=$fuel;"
        val cursor = db.rawQuery(query, null)
        val registerAmount = cursor.count
        cursor.close()
        db.close()
        return registerAmount
    }

    fun getAmount(fuel: Int): Int {
        val db = this.readableDatabase
        val query = "SELECT $KEY_AMOUNT FROM $TABLE_NAME WHERE $KEY_FUEL=$fuel"
        val cursor = db.rawQuery(query, null)
        var amount = 0
        while(cursor.moveToNext()) {
            amount += cursor.getInt(0)
        }
        cursor.close()
        return amount
    }

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "refuel"
        const val KEY_FUEL = "fuel"
        const val KEY_CITY = "city"
        const val KEY_AMOUNT = "amount"
    }
}

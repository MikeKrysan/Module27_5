package com.mikekrysan.module27_5


import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.CursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf<String>()  //создаем список

    private val suggestions = arrayOf("Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8" )  //импровизированная база данных для возможности выбрать из списка

    private lateinit var mAdapter: SimpleCursorAdapter  //адаптер, который будет помагать осущесвлять предложения по поиску

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val from = arrayOf("items")
        val to = intArrayOf(android.R.id.text1)

        //Инициализируем адаптер в методе onCreate()
        mAdapter = SimpleCursorAdapter(this,
            R.layout.item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

    }

//        for (i in 0..9) {   //создам элементы, из которых будем осуществлять поиск
//            list.add("item$i")
//        }


    //наполняем функционалом метод, который будет сравнивать по каждому изменению текста, есть ли он в нашей импровизированной базе данных.
    //Если такой строки нет, то будет появляться надпись "Not in list" и текст будет красного цвета, а если строка есть , то текст будет  "In list"
    //и цвет текста будет у нас зеленым
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        //Надуваема наше меню
//        menuInflater.inflate(R.menu.search_menu, menu)
//        //Находим наш пункт меню с поиском
//        val menuItem = menu.findItem(R.id.search)
//        //Привязываем его как поле для поиска
//        val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView
//        //Задаем слушатель изменений ввода текста
//        searchView.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            //Здесь выполняется код по нажатию на кнопку поиска
////            override fun onQueryTextSubmit(query: String?): Boolean {
////                return false
////            }
//
//            //Здесь выполняется код при любом изменении текста
//            override fun onQueryTextChange(newText: String?): Boolean {
////                if (list.contains(newText)) {
////                    textView.text = "In list"
////                    textView.setTextColor(
////                        ContextCompat.getColor(
////                            this@MainActivity,
////                            R.color.teal_200
////                        )
////                    )
////                } else {
////                    textView.text = "Not in list"
////                    textView.setTextColor(
////                        ContextCompat.getColor(
////                            this@MainActivity,
////                            R.color.purple_200
////                        )
////                    )
////                }
//                return false
//            }
//            //Cделаем поиск только при нажатии на кнопку поиска:
//            override fun onQueryTextSubmit(querry: String): Boolean {
//                if (list.contains(querry)) {
//                    textView.text = "In list"
//                    textView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.teal_200))
//                } else {
//                    textView.text = "Not in list"
//                    textView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
//                }
//                return false
//            }
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

    //Реализуем поиск по списку с предлагаемыми вариантами: когда мы начинаем ввод в поле поиска, нам предлагает система наиболее подходящие варианты.
    //Один из вариантов реализации:

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem = menu.findItem(R.id.search)

        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    populateAdapter(newText)
                }
                return false
            }
        })
        searchView.suggestionsAdapter = mAdapter

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }
            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                val cursor: Cursor = mAdapter!!.getItem(position) as Cursor
                val txt: String = cursor.getString(cursor.getColumnIndex("items"))
                searchView.setQuery(txt, false)
                searchView.clearFocus()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun populateAdapter(query: String) {
        val c = MatrixCursor(arrayOf(BaseColumns._ID, "items"))
        for(i in suggestions.indices) {
            if(suggestions[i].toLowerCase().contains(query.toLowerCase())) c.addRow(arrayOf(i, suggestions[i]))
        }
        mAdapter!!.changeCursor(c)
    }

}
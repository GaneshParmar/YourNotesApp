package com.example.yournotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.yournotes.Adapter.NotesAdapter
import com.example.yournotes.Database.NoteDatabase
import com.example.yournotes.Models.Note
import com.example.yournotes.Models.NoteViewModel
import com.example.yournotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),NotesAdapter.NotesItemClickListener,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var binding:ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel:NoteViewModel
    lateinit var adapter:NotesAdapter
    lateinit var selectedNote: Note

    private val updateNote= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

        if(result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as Note

            if(note!=null){
                Log.d("My Updated Note",note.title)
                viewModel.updateNote(note)
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // initializing the Ui
        initUi()

        viewModel = ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        //check if frstTime

        forFirstTime()

        viewModel.allNotes.observe(this) { list ->
            list.let {
                adapter.updateList(list)
            }
        }

        database= NoteDatabase.getDatabase(this)



    }

    private fun forFirstTime() {
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)

        if (isFirstLaunch) {

            val searchNoteHelper = Note(null,"Feature: seaerch \uD83D\uDD0D","You can use the above search to search for any note","date unknown")
            val addNoteHelper = Note(null,"Feature: how to add note","You can add new note clicking on the corner floating option (+ icon )","date unknown")
            val updateNoteHelper = Note(null,"Feature note update \uD83C\uDD95","You can click on particular note to land on update page","date unknown")
            val deleteNoteHelper = Note(null,"Feature ❌","To delete note long press on any note and press Delete","date unknown")

            var listNotes:List<Note> = listOf(searchNoteHelper,addNoteHelper,updateNoteHelper,deleteNoteHelper)

            for(note in listNotes){
                viewModel.insert(note)
            }

            var editor:Editor = sharedPreferences.edit()

            editor.putBoolean("is_first_launch",false).commit()
        }

    }

    private fun initUi() {

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
            adapter = NotesAdapter(applicationContext, this@MainActivity)
            recyclerView.adapter = adapter

            val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                   if(result.resultCode == Activity.RESULT_OK){

                       val note = result.data?.getSerializableExtra("note") as Note
                       if(note != null){
                           viewModel.insert(note)
                       }

                   }

            }

            floatingActionButton.setOnClickListener{

                val intent= Intent(this@MainActivity, AddNote::class.java)

                getContent.launch(intent)

            }



        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText != null){

                    adapter.filterList(newText)

                }
                return true

            }

        })


    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)

        intent.putExtra("current_note",note)
        updateNote.launch(intent)

    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {

        selectedNote = note

        popUpDisplay(cardView)

    }

    private fun popUpDisplay(cardView: CardView) {

        val popup = PopupMenu(this, cardView)

        popup.setOnMenuItemClickListener(this@MainActivity)

        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.delete_node){

            viewModel.deleteNode(selectedNote)

            return true

        }

        return false


    }
}

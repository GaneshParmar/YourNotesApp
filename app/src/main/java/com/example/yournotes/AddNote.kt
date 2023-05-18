package com.example.yournotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.yournotes.Models.Note
import com.example.yournotes.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class AddNote : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdate= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        try{

            old_note = intent.getSerializableExtra("current_note") as Note
            binding.txtNoteTitle.setText(old_note.title)
            binding.txtNoteDetails.setText(old_note.note)
            isUpdate= true



        }
        catch (e: Exception){
            e.printStackTrace()
        }

        binding.apply {
            addBtn.setOnClickListener(){

                val title= txtNoteTitle.text.toString()
                val note_desc = txtNoteDetails.text.toString()

                if(title.isNotEmpty() || note_desc.isNotEmpty()){

                    var formatter=SimpleDateFormat("EEE,d MMM yyyy HH:mm a")

                    if(isUpdate){

                        Log.d("old id",old_note.id.toString())
                        note = Note(
                            old_note.id,
                            title,
                            note_desc,
                            formatter.format(Date())
                        )

                    }
                    else{
                        Log.d("Its here","Hello")

                        note = Note(
                            null,
                            title,
                            note_desc,
                            formatter.format(Date())
                        )
                    }

                    val intent = Intent()
                    intent.putExtra("note",note)

                    setResult(Activity.RESULT_OK,intent)
                    finish()

                }
                else{

                    Toast.makeText(this@AddNote,

                        "Please Provide the data",
                        Toast.LENGTH_SHORT
                        ).show()

                }

            }

            backBtn.setOnClickListener{

                onBackPressed()

            }

        }


    }
}
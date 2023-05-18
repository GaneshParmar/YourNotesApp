package com.example.yournotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.yournotes.Models.Note
import com.example.yournotes.R
import com.example.yournotes.databinding.ActivityMainBinding
import kotlin.random.Random


class NotesAdapter(private val context: Context, val listener: NotesItemClickListener):RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    private val NoteList= ArrayList<Note>()
    private  val fullList= ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )

    }

    override fun getItemCount(): Int {
        return NoteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentNote=NoteList[position]

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(RandomColor(),null))

        holder.title.text = currentNote.title
        holder.title.isSelected= true
        holder.title.requestFocus()
        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(currentNote)
        }

        holder.notes_layout.setOnLongClickListener{
            listener.onLongItemClicked(currentNote, holder.notes_layout)
            true
        }

    }

    fun filterList(search: String){

        NoteList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search)==true ||
                item.note?.lowercase()?.contains(search)==true){

                NoteList.add(item)

            }
        }

        notifyDataSetChanged()

    }

    fun updateList(nodeList: List<Note>){
        fullList.clear()
        fullList.addAll(nodeList)
        NoteList.clear()

        NoteList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun RandomColor():Int{
        val noteColors = listOf(
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6
        )

        val seed = System.currentTimeMillis().toInt()

        val randomIndex= Random(seed).nextInt(noteColors.size)

        return noteColors[randomIndex]

    }

    inner class NoteViewHolder(rootView: View): RecyclerView.ViewHolder(rootView){

        val notes_layout= rootView.findViewById<CardView>(R.id.note_card)
        val title=rootView.findViewById<TextView>(R.id.tv_title)
        val note=rootView.findViewById<TextView>(R.id.tv_note)
        val date= rootView.findViewById<TextView>(R.id.tv_date)

    }

    interface NotesItemClickListener{

        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)

    }

}
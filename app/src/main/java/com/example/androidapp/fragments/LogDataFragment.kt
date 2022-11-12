package com.example.androidapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentLogDataBinding


class LogDataFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentLogDataBinding>(
            inflater,
            R.layout.fragment_log_data, container, false
        )

        var recyclerView = binding.logDataRecycler.findViewById<RecyclerView>(R.id.logDataRecycler)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MyAdapter()
        }

        //Navigointi takaisin home_fragmentille, (Aika turha kylläkin, takaisin napista pääsee myös, voi poistaa)
        binding.logHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_logDataFragment_to_homeFragment)
        }

        return binding.root
    }

    class MyAdapter: RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        var Lista = mutableListOf<String>()

        init {
            //populate list from server for recycler
            // for testing
            (1..210).forEach{
                Lista.add("Testi")
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_logdata,parent,false)
            return ViewHolder(view)
        }

        override fun getItemCount() = Lista.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("jpk", "onBindViewHolder, position= " + position.toString())
            holder.bind(position,Lista)
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val tekstikentta = view.findViewById<TextView>(R.id.logData)
            fun bind (position: Int, clicked: MutableList<String>){
                Log.d("asd","testi" + position.toString())
                tekstikentta.setText("")


            }
        }
    }
}






/*
* class MyAdapter: RecyclerView.Adapter<MyAdapter.ViewHolder>() {
            var clicked = mutableListOf<Boolean>()

            init {
                //populate list for recycler
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_logdata,parent,false)
                return RecyclerView.ViewHolder(view)
            }

            override fun getItemCount() = clicked.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                Log.d("jpk", "onBindViewHolder, position= " + position.toString())
                holder.bind(position,clicked)
            }

            class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
                private val button = view.findViewById<Button>(R.id.button)

                fun bind (position: Int, clicked: MutableList<Boolean>){

                    if(clicked[position]){
                        button.setBackgroundColor(Color.BLACK)
                        button.setTextColor(Color.CYAN)
                        button.setText("Clicked and Recycled")
                    }
                    else{
                        button.setBackgroundColor(Color.WHITE)
                        button.setTextColor(Color.MAGENTA)
                        button.setText(position.toString())
                    }

                    button.setOnClickListener{
                        button.setBackgroundColor(Color.BLACK)
                        button.setTextColor(Color.CYAN)
                        button.setText("Clicked")
                        clicked[position]= true
                    }
                }
            }*/

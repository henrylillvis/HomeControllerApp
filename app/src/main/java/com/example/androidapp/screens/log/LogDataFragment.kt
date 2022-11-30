package com.example.androidapp.screens.log

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentLogDataBinding
import com.example.androidapp.network.LogProperty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.lifecycle.*
import com.example.androidapp.network.HomeApi


class LogDataFragment : Fragment() {
    private lateinit var binding: FragmentLogDataBinding
    companion object{ lateinit var logData: List<LogProperty> }
    private val apiVM: HomeApi by activityViewModels()
    private val _response = MutableLiveData<String>()
    private var searching = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLogDataBinding>(
            inflater,
            R.layout.fragment_log_data, container, false
        )
        //Navigointi takaisin home_fragmentille, (Aika turha kylläkin, takaisin napista pääsee myös, voi poistaa)
        binding.logHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_logDataFragment_to_homeFragment)
        }

        getData()




        return binding.root
    }

    private fun getData() {
        Log.d("jb", "getData started")
        lifecycleScope.launch {
            searching = true
            try {

                //Service ei kerkeä latautumaan ensimmäisellä kerralla?
                if(apiVM.service == null){
                    Log.d("jb", apiVM.service.toString())
                    //delay(1000)
                    if (apiVM.service == null){throw Exception("Virhe yhteyden muodostamisessa.")
                    }
                }

                logData = apiVM.service?.getLogs()!!
                _response.value = "Haku onnistui"
            }catch (e: Exception){
                val errorMsg = "Haku epäonnistui: ${e}"
                _response.value = errorMsg
            }

            Log.d("jb", "Api response: " + _response.value)
            searching = false

            var recyclerView = binding.logDataRecycler.findViewById<RecyclerView>(R.id.logDataRecycler)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = MyAdapter()
            }
        }
    }
   // private fun showError(msg: String?){
     //   binding.errorMessage.text = msg
       // binding.errorCard.visibility = View.VISIBLE
  //  }

    class MyAdapter: RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_logdata,parent,false)
            return ViewHolder(view)
        }

        override fun getItemCount() = logData.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("jpk", "onBindViewHolder, position= " + position.toString())
            holder.bind(position,logData)
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val tekstikentta = view.findViewById<TextView>(R.id.logData)
            fun bind (position: Int, Data: List<LogProperty>){
                Log.d("asd","testi" + position.toString())
                tekstikentta.setText(Data[position].toString())
                var info = "Time: "+Data[position].timestamp
                info += " Action: "+Data[position].action.toString()+ " Values:"
                for(x in Data[position].values){ info += " ["+x.toString()+"]"}
                tekstikentta.setText(info)
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

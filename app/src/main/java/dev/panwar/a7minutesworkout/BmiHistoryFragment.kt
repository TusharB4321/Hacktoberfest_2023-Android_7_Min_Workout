package dev.panwar.a7minutesworkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.panwar.a7minutesworkout.databinding.FragmentBmiHistoryBinding
import kotlinx.coroutines.launch

class BmiHistoryFragment : Fragment() {

    private  var binding:FragmentBmiHistoryBinding?=null
    private lateinit var list:ArrayList<BMIModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentBmiHistoryBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao=(requireContext().applicationContext as WorkOutApp).dbBmi.bmiDao()
        getAllCompletedBmiHistory(dao)
    }

    private fun getAllCompletedBmiHistory(dao: BMIDao) {
        lifecycleScope.launch {
            dao.fetchALlWeight().collect { allCompletedDatesList->

                if (allCompletedDatesList.isNotEmpty()) {
                    // Here if the List size is greater then 0 we will display the item in the recycler view or else we will show the text view that no data is available.
                    binding?.rvBmiHistory?.visibility = View.VISIBLE
                    binding!!.tvBmiHistory.visibility=View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    // Creates a vertical Layout Manager
                    binding?.rvBmiHistory?.layoutManager = LinearLayoutManager(requireContext())

                    list=ArrayList()
                    for (date in allCompletedDatesList){
                        list.add(date)
                    }
                    val historyAdapter = HistoryBmiAdapter(ArrayList(list))

                    binding?.rvBmiHistory?.adapter = historyAdapter
                } else {
                    binding?.rvBmiHistory?.visibility = View.INVISIBLE
                    binding!!.tvBmiHistory.visibility=View.INVISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
                // END
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
// reset the binding to null to avoid memory leak
        binding = null
    }

}
package com.niky.proyectohrc.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.niky.proyectohrc.R
import com.niky.proyectohrc.entities.Aula

class AulasListAdapter (private var aulasList : MutableList<Aula>, val onItemClick: (Int) -> Boolean): RecyclerView.Adapter<AulasListAdapter.AulasHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AulasHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_aula,parent,false)
        return AulasHolder(view)
    }

    override fun getItemCount(): Int {
        return aulasList.size
    }

    fun setData(newData: ArrayList<Aula>){
        this.aulasList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AulasHolder, position: Int) {
        holder.setName(aulasList[position].numero)
        holder.setCO2(aulasList[position].co2)
        holder.setOrientacion(aulasList[position].orientacion)

        holder.setColor(aulasList[position].co2.toFloat())
        holder.getCardLayout().setOnClickListener{
            onItemClick(position)
        }
    }

    class AulasHolder (v : View) : RecyclerView.ViewHolder(v){
        private var v : View
        init
        {
            this.v = v
        }

        fun setColor (co2: Float)
        {
            val tvAula : TextView = v.findViewById(R.id.txt_name_aula)
            if (co2 >= 700)
            {
                tvAula.setBackgroundColor(Color.rgb(226, 96, 68))
            }

            else if (co2 < 700 && co2 >= 500)
            {
                tvAula.setBackgroundColor(Color.rgb(243, 226, 11))
            }

            else
            {
                tvAula.setBackgroundColor(Color.rgb(106, 224, 105))
            }
        }

        fun setName (name: String)
        {
            val tvName : TextView = v.findViewById(R.id.txt_name_aula)
            tvName.text = name
        }

        fun setOrientacion (orien: String)
        {
            val tvOrien : TextView = v.findViewById(R.id.txt_orientacion)
            tvOrien.text = orien
        }

        fun setCO2 (co2: String)
        {
            val tvCo2: TextView = v.findViewById(R.id.txt_co2)
            tvCo2.text = co2
        }

        fun getCardLayout () : CardView
        {
            return v.findViewById(R.id.card_package_item_aula)
        }
    }
}
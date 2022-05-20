package com.example.memo.view

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.memo.databinding.RecyclerviewMainBinding
import com.example.memo.model.Memo
import com.example.memo.viewModel.MemoViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainRecyclerAdapter(val checkBoxVisible: CheckBoxVisible) : RecyclerView.Adapter<MainRecyclerAdapter.Holder>(){

    private var listDate:List<Memo> = listOf()
    private var visible = 0
    private var checkAll = false
    var selectListData:ArrayList<Int> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerviewMainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(listDate[position])
    }

    override fun getItemCount(): Int {
        return listDate.size
    }

    inner class Holder(val binding: RecyclerviewMainBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(memo: Memo){
            with(binding) {
                title.text = memo.title
                content.text = memo.content

                var sdf: SimpleDateFormat? = null
                if(Date(memo.datetime).year < Date(System.currentTimeMillis()).year){
                    sdf = SimpleDateFormat("yyyy년 MM월 dd일")
                }
                else if((Date(memo.datetime).month <= Date(System.currentTimeMillis()).month)
                        && (Date(memo.datetime).date < Date(System.currentTimeMillis()).date)){
                    sdf = SimpleDateFormat("MM월 dd일")
                }
                else if(Date(memo.datetime).date == Date(System.currentTimeMillis()).date){
                    sdf = SimpleDateFormat("HH:mm")
                }
                date.text = sdf?.format(memo.datetime)

                //체크박스
                if(visible == 0)
                    checkbox.visibility = View.INVISIBLE
                else
                    checkbox.visibility = View.VISIBLE

                // 체크박스 전체 선택/해제
                checkbox.isChecked = checkAll

                contentBox.setOnClickListener {
                    if(visible == 0) {
                        val intent = Intent(contentBox.context, WriteActivity::class.java)
                        intent.putExtra("mode", "read")
                        intent.putExtra("no", memo.no)
                        ContextCompat.startActivity(contentBox.context, intent, null)
                    }
                    else {
                        checkbox.isChecked = !checkbox.isChecked
                    }
                }

                contentBox.setOnLongClickListener{
                    checkBoxVisible.adapterUpdate(1)
                    return@setOnLongClickListener true
                }

                checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(checkbox.isChecked) {
                        memo.no?.let { selectListData.add(it) }
                    }
                    else {
                        memo.no?.let { selectListData.remove(it) }
                    }
                }
            }
        }
    }

    fun setMemo(memo: List<Memo>){
        this.listDate = memo
        notifyDataSetChanged()
    }

    fun updateCheckBox(n : Int){
        visible = n
    }

    fun setCheckAll(boolean: Boolean){
        checkAll = boolean
    }
}
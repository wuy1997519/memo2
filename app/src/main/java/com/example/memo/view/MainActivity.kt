package com.example.memo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memo.R
import com.example.memo.databinding.ActivityMainBinding
import com.example.memo.model.Memo
import com.example.memo.viewModel.MemoViewModel
import java.util.*

class MainActivity : AppCompatActivity(), CheckBoxVisible {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    lateinit var memoViewModel: MemoViewModel
    lateinit var adapter: MainRecyclerAdapter

    var checkAll = false //전체 선택/해제

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        memoViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)

        adapter = MainRecyclerAdapter(this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(this,3)


        memoViewModel.allMemo.observe(this, Observer {
            adapter.setMemo(it)
        })

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("mode", "write")
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.edit -> {
                adapterUpdate(1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            var item = menu.add(0,1,0,"전체 선택")

            item.isCheckable = true

            menu.add(0, 2, 0, "삭제")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                1 -> {
                    if(!checkAll) {
                        adapter.setCheckAll(true)
                        checkAll = true
                        item.title = "전체 해제"
                    }
                    else{
                        adapter.setCheckAll(false)
                        checkAll = false
                        item.title = "전체 선택"
                    }
                    adapter.notifyDataSetChanged()
                    true
                }
                2 -> {
                    if(adapter.selectListData.isNotEmpty()) {
                        for (i in adapter.selectListData)
                            memoViewModel.delete(i)
                        adapter.notifyDataSetChanged()
                    }
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            adapterUpdate(0)
            checkAll = false
            adapter.setCheckAll(checkAll)
        }
    }


    override fun adapterUpdate(n : Int){
        if(n == 1)
            startActionMode(actionModeCallback)
        adapter.updateCheckBox(n)
        adapter.notifyDataSetChanged()
    }

}
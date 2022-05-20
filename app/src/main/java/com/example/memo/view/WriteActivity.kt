package com.example.memo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.memo.R
import com.example.memo.databinding.ActivityWriteBinding
import com.example.memo.model.Memo
import com.example.memo.viewModel.MemoViewModel
import java.util.*

class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding
    lateinit var memoViewModel: MemoViewModel
    private var no: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        binding.lifecycleOwner = this

        memoViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
        binding.memoViewModel = memoViewModel

        setSupportActionBar(binding.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

        if (intent.getStringExtra("mode").equals("read")){
            memoViewModel.isEnabled(1)
        }


        if (intent.getIntExtra("no", -1) != -1){
            no = intent.getIntExtra("no", -1)
            memoViewModel.getMemo(no)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.store -> {
                var content = binding.content.text.toString()

                var title: String?
                if(isEmpty(binding.title.text)){
                    title = "텍스트 노트 ${(Calendar.getInstance().get(Calendar.MONTH) + 1)}" +
                            "_${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}"
                }
                else{
                   title =  binding.title.text.toString()
                }

                if(intent.getStringExtra("mode").equals("read")){
                    val memo = Memo(no,title,content,System.currentTimeMillis())
                    memoViewModel.update(memo)
                }else{
                    val memo = Memo(null,title,content,System.currentTimeMillis())
                    memoViewModel.insert(memo)
                }
                finish()
                true
            }
            R.id.modify -> {
                memoViewModel.isEnabled(0)
                true
            }
            R.id.delete -> {
                memoViewModel.delete(no)
                finish()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
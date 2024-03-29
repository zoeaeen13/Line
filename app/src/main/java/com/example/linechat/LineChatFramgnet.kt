package com.example.linechat


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LineChatFramgnet : Fragment() {
    private val myAdapter = MyAdapter(itemList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        setHasOptionsMenu(true)     //重要！告訴 FragmentActivity 這個 Fragment 有另外的 optionsMenu
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        recycleview1.layoutManager = LinearLayoutManager(this.context)
        recycleview1.adapter = myAdapter

        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId ){
            R.id.menu_search -> {
                val intent = Intent(this.context, SearchActivity::class.java)
                startActivity(intent)

            }
            R.id.menu_edit_messages -> {
                val intentEdit = Intent(this.context, EditActivity::class.java)
                startActivityForResult(intentEdit, 0)
            }
            R.id.menu_sort -> {
                val list_sort = arrayOf("收到的時間", "未讀訊息", "我的最愛")
                AlertDialog.Builder(this.context)
                    .setItems(list_sort){_, i ->
                        when(i){
                            0 -> sort()
                            1 -> unread()
                        }
                    }
                    .show()
            }
            R.id.menu_finished -> {
                AlertDialog.Builder(this.context)
                    .setMessage("要將所有訊息標為已讀嗎？")
                    .setPositiveButton("標為已讀"){_,_ ->
                        val list= itemList.map { it.copy(num = 0) }
                        myAdapter.update(list)
//                        val act = activity
//                        if (act is MainActivity){
//                            act.unread_num(list)                    //安全的作法一
//                        }
                        (activity as MainActivity).removeBadge()
//                        (activity as MainActivity).unread_num(list)    //作法二

                    }
                    .setNegativeButton("取消"){dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun sort(){
        itemList.sortWith(compareBy({it.time},{it.num}))
//        itemList.sortByDescending { it.time }
        myAdapter.notifyDataSetChanged()

    }
    fun unread(){
        itemList.sortWith(compareBy({it.num},{it.time}))
//        itemList.sortByDescending { it.num }
        myAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        myAdapter.notifyDataSetChanged()    //更新畫面寫在返回時就要做，不放在 onResume()!!!
        super.onActivityResult(requestCode, resultCode, data)
    }
}
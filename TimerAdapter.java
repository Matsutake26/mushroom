package com.example.timerapri

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.timerapri.databinding.ItemTimerBinding

import kotlin.Unit;

// ListAdapterを継承するクラスを作成します。
// 第一引数: データモデルのクラス (例: TimerItem)
// 第二引数: ViewHolderクラス (TimerAdapter.TimerViewHolder)
class TimerAdapter(
        // 「Start/Stop」ボタンがクリックされたときに呼び出す処理（ラムダ式）を渡せるようにします
        private val onToggleClick: (TimerItem) -> Unit
) : ListAdapter<TimerItem, TimerAdapter.TimerViewHolder>(DiffCallback) {

        /**
         * ViewHolder: 1行分のレイアウト(item_timer.xml)に含まれるUI要素を保持するクラスです。
         * ViewBindingを使って、効率的かつ安全にUI要素にアクセスします。
         */
        class TimerViewHolder(private val binding: ItemTimerBinding) : RecyclerView.ViewHolder(binding.root) {

    // onBindViewHolderから呼び出されるメソッド
    fun bind(item: TimerItem, onToggleClick: (TimerItem) -> Unit) {
        // データ（TimerItem）をUIに表示します

        // 1. 残り秒数を「分:秒」の形式に変換してTextViewに設定
        val minutes = item.remainingSeconds / 60
        val seconds = item.remainingSeconds % 60
        binding.timeText.text = String.format("%02d:%02d", minutes, seconds)

        // 2. タイマーが動作中かどうかに応じて、ボタンのテキストを切り替え
        binding.toggleButton.text = if (item.isRunning) "Stop" else "Start"

        // 3. ボタンにクリックリスナーを設定
        binding.toggleButton.setOnClickListener {
            onToggleClick(item) // ボタンがクリックされたら、Activity/Fragmentに通知
        }
    }
}

        /**
         * 新しいViewHolder（1行分のレイアウト）が必要になったときに呼び出されます。
         * ここでレイアウトを生成（Inflate）します。
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
    // ViewBindingを使ってレイアウトを生成
    val binding = ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return TimerViewHolder(binding)
}

        /**
         * ViewHolderにデータを表示するときに呼び出されます。
         * スクロールして新しい行が表示されるたびに実行されます。
         */
        override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
    val currentItem = getItem(position) // 現在表示する位置のデータを取得
    holder.bind(currentItem, onToggleClick) // ViewHolderのbindメソッドを呼び出してUIを更新
}

        /**
         * DiffUtil.ItemCallback: リストが更新されたときに、どの項目が変更されたかを効率的に
         * 計算するための仕組みです。これにより、アニメーションがスムーズになります。
         */
        companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TimerItem>() {
        // 2つのアイテムが「同じもの」であるかをIDで判定
        override fun areItemsTheSame(oldItem: TimerItem, newItem: TimerItem): Boolean {
    return oldItem.id == newItem.id
}

        // 2つのアイテムの「内容」が同じであるかを判定
        override fun areContentsTheSame(oldItem: TimerItem, newItem: TimerItem): Boolean {
    // TimerItemがdata classなので、== で全プロパティの比較ができる
    return oldItem == newItem
}
        }
                }
                }

/**
 * データモデルとなるクラスです。
 * `data class` にすることで、`equals`, `hashCode`, `toString` などのメソッドが自動生成され、
 * DiffUtilでの比較が簡単になります。
 */
        data class TimerItem(
        val id: Int,
        val remainingSeconds: Int,
        val isRunning: Boolean
)

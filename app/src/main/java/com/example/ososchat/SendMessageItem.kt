package com.example.ososchat

import com.example.ososchat.databinding.ItemMessageReciveBinding
import com.example.ososchat.databinding.ItemMessageSendBinding
import com.xwray.groupie.databinding.BindableItem

class SendMessageItem(private val message: Message) : BindableItem<ItemMessageSendBinding>() {
    override fun getLayout(): Int {
        return R.layout.item_message_send
    }

    override fun bind(viewBinding: ItemMessageSendBinding, position: Int) {
        viewBinding.message = message
    }
}

class ReceiveMessageItem(private val message: Message) : BindableItem<ItemMessageReciveBinding>() {
    override fun getLayout(): Int {
        return R.layout.item_message_recive
    }

    override fun bind(viewBinding: ItemMessageReciveBinding, position: Int) {
        viewBinding.message = message
    }
}
/*
 * Copyright 2021 BeetleStance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beetlestance.aphid.feature_chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.beetlestance.aphid.base_android.AphidViewModel
import com.beetlestance.aphid.domain.executors.SendMessage
import com.beetlestance.aphid.domain.invoke
import com.beetlestance.aphid.domain.observers.ObserveChat
import com.beetlestance.aphid.domain.watchStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ChatViewModel @ViewModelInject constructor(
    observeChat: ObserveChat,
    private val fetchMessage: SendMessage
) : AphidViewModel<ChatViewState>(ChatViewState()) {

    private val pendingActions = Channel<ChatActions>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            observeChat.observe().collectAndSetState {
                copy(messages = it)
            }
        }

        observeChat()

        viewModelScope.launch {
            pendingActions.consumeAsFlow().collect { action ->
                when (action) {
                    is ChatActions.SendMessage -> sendMessage(action)
                }
            }
        }
    }

    private fun sendMessage(action: ChatActions.SendMessage) {
        viewModelScope.launch {
            fetchMessage(action.message).watchStatus {
            }
        }
    }

    fun submitAction(action: ChatActions) {
        viewModelScope.launch {
            if (!pendingActions.isClosedForSend) {
                pendingActions.send(action)
            }
        }
    }
}

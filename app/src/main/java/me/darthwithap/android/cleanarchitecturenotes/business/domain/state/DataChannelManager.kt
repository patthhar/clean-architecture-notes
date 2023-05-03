package me.darthwithap.android.cleanarchitecturenotes.business.domain.state

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.darthwithap.android.cleanarchitecturenotes.util.logD

@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class DataChannelManager<ViewState> {

  private val dataChannel = BroadcastChannel<DataState<ViewState>>(Channel.BUFFERED)
  private var channelScope: CoroutineScope? = null
  private val stateEventManager: StateEventManager = StateEventManager()

  val messageStack = MessageStack()

  val shouldDisplayProgressBar = stateEventManager.shouldDisplayProgressBar

  fun setupChannel() {
    cancelJobs()
    initChannel()
  }

  private fun initChannel() {
    dataChannel
      .asFlow()
      .onEach { dataState ->
        withContext(Dispatchers.Main) {
          dataState.data?.let { data ->
            handleNewData(data)
          }
          dataState.stateMessage?.let { stateMessage ->
            handleNewStateMessage(stateMessage)
          }
          dataState.stateEvent?.let { stateEvent ->
            removeStateEvent(stateEvent)
          }
        }
      }
      .launchIn(getChannelScope())
  }

  abstract fun handleNewData(data: ViewState)

  private fun offerToDataChannel(dataState: DataState<ViewState>) {
    dataChannel.let {
      if (!it.isClosedForSend) {
        logD("DCM", "offer to channel!")
        it.trySend(dataState).isSuccess
      }
    }
  }

  fun launchJob(
    stateEvent: StateEvent,
    jobFunction: Flow<DataState<ViewState>?>
  ) {
    if (canExecuteNewStateEvent(stateEvent)) {
      logD("DCM", "launching job: ${stateEvent.eventName()}")
      addStateEvent(stateEvent)
      jobFunction
        .onEach { dataState ->
          dataState?.let { dState ->
            offerToDataChannel(dState)
          }
        }
        .launchIn(getChannelScope())
    }
  }

  private fun canExecuteNewStateEvent(stateEvent: StateEvent): Boolean {
    // If a job is already active, do not allow duplication
    if (isJobAlreadyActive(stateEvent)) {
      return false
    }
    // if a dialog is showing, do not allow new StateEvents
    if (!isMessageStackEmpty()) {
      return false
    }
    return true
  }

  fun isMessageStackEmpty(): Boolean {
    return messageStack.isStackEmpty()
  }

  private fun handleNewStateMessage(stateMessage: StateMessage) {
    appendStateMessage(stateMessage)
  }

  private fun appendStateMessage(stateMessage: StateMessage) {
    messageStack.add(stateMessage)
  }

  fun clearStateMessage(index: Int = 0) {
    logD("DataChannelManager", "clear state message")
    messageStack.removeAt(index)
  }

  fun clearAllStateMessages() = messageStack.clear()

  fun printStateMessages() {
    for (message in messageStack) {
      logD("DCM", "${message.response.message}")
    }
  }

  // for debugging
  fun getActiveJobs() = stateEventManager.getActiveJobNames()

  fun clearActiveStateEventCounter() = stateEventManager.clearActiveStateEventCounter()

  fun addStateEvent(stateEvent: StateEvent) = stateEventManager.addStateEvent(stateEvent)

  fun removeStateEvent(stateEvent: StateEvent?) = stateEventManager.removeStateEvent(stateEvent)

  private fun isStateEventActive(stateEvent: StateEvent) =
    stateEventManager.isStateEventActive(stateEvent)

  fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
    return isStateEventActive(stateEvent)
  }

  fun getChannelScope(): CoroutineScope {
    return channelScope ?: setupNewChannelScope(CoroutineScope(Dispatchers.IO))
  }

  private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope {
    channelScope = coroutineScope
    return channelScope as CoroutineScope
  }

  fun cancelJobs() {
    if (channelScope != null) {
      if (channelScope?.isActive == true) {
        channelScope?.cancel()
      }
      channelScope = null
    }
    clearActiveStateEventCounter()
  }

}
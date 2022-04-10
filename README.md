<h1 align="center">🚨 HyriAPI</h1>

# 💬 How to use chat handlers ?

### 🚧 How can I setup a chat handler ?

- First, create a class which extends from ``IHyriChatChannelHandler``, and override these methods:
  - ``getChannel()``: return the channel associated to the handler. ⚠️ An Enum is highly recommended to avoid mistakes, eg: [HyriDefaultChatChannel](https://github.com/Hyriode/HyriAPI/blob/feature/ChatChannels/API/src/main/java/fr/hyriode/api/chat/HyriDefaultChatChannel.java).
  - ``getRequiredRank()``: return the rank required to talk in the channel. It might be ``null`` if the channel cannot be joined.
  - ``isAcrossNetwork()``: return ``true`` if messages in this channel should be sent across the network.
  - ``canBeJoined()``: return ``true`` if the channel can be joined by a player. It should be ``false`` if the channel is special like the [PluginChatHandler](https://github.com/Hyriode/HyriAPI/blob/feature/ChatChannels/Server/src/main/java/fr/hyriode/api/impl/server/chat/PluginChatHandler.java).
  - ``getPrefix()`` and ``getMessageFormat()``: useful methods to format the messages. They are optional.

- Now, you can handle the messages with these methods: ⚠️ These methods have a ``force`` parameter, used to skip the checks like friends, ranks, etc. Don't forget to include it.
  - ``onMessage()``: triggered when a message is sent in the channel. The message should not be sent in this method. Refer to the [GlobalChatHandler](https://github.com/Hyriode/HyriAPI/blob/feature/ChatChannels/Server/src/main/java/fr/hyriode/api/impl/server/chat/GlobalChatHandler.java#L52).
  - ``onMessageToPlayer()``: triggered when a message is sent in the channel, but with a specific receiver. The message should be sent to player in this method. Refer to the [GlobalChatHandler](https://github.com/Hyriode/HyriAPI/blob/feature/ChatChannels/Server/src/main/java/fr/hyriode/api/impl/server/chat/GlobalChatHandler.java#L77).

- Finally, you can register your handler with the ``IHyriChatChannelManager``, like in the [HyriAPI Implementation](https://github.com/Hyriode/HyriAPI/blob/feature/ChatChannels/Server/src/main/java/fr/hyriode/api/impl/server/HyriAPIImplementation.java#L76).

### 🤔 My handler is registered, but how can I use it ?

- To use your handler, you can simply call the [sendMessage()](https://github.com/Hyriode/HyriAPI/blob/feature/ChatChannels/API/src/main/java/fr/hyriode/api/chat/IHyriChatChannelManager.java#L68) method, like this:
```java
HyriAPI.get().getChatChannelManager().sendMessage(ChatChannel.MY_CHANNEL.getChannel(), "I love potatoes!", null, false);
```
- You can also send messages to a specific player, like this:
```java
HyriAPI.get().getChatChannelManager().sendMessageToPlayer(ChatChannel.MY_CHANNEL.getChannel(), "I love potatoes!", UUID.fromString("3c7bd188-7695-49ee-894f-b222bd79aecb"), null, false);
```

😎 These methods have a lot of parameters, so you can use their shortcuts to avoid repeating "null" or "false" everywhere.

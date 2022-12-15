package fr.hyriode.api.impl.common.language;

import com.google.gson.stream.JsonReader;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.language.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by AstFaster
 * on 14/07/2022 at 22:43
 */
public class HyriLanguageManager implements IHyriLanguageManager {

    private final Map<UUID, HyriLanguage> languageCache = new HashMap<>();

    private final Set<HyriLanguageMessage> messages;
    private final Map<Class<?>, IHyriLanguageAdapter<?>> adapters;

    public HyriLanguageManager() {
        this.messages = ConcurrentHashMap.newKeySet();
        this.adapters = new HashMap<>();

        HyriAPI.get().getEventBus().register(this);
    }

    @HyriEventHandler
    public void onLanguageUpdated(HyriLanguageUpdatedEvent event) {
        this.languageCache.put(event.getPlayerId(), event.getLanguage());
    }

    public void setCache(UUID player, HyriLanguage language) {
        this.languageCache.put(player, language);
    }

    public void removeCache(UUID player) {
        this.languageCache.remove(player);
    }

    public HyriLanguage getCache(UUID player) {
        return this.languageCache.get(player);
    }

    @Override
    public List<HyriLanguageMessage> loadLanguagesMessages(File folder) {
        final List<HyriLanguageMessage> messages = new ArrayList<>();
        final File[] files = folder.listFiles();

        if (files == null) {
            return messages;
        }

        for (HyriLanguage language : HyriLanguage.values()) {
            final String fileName = language.getCode() + ".json";
            File file = null;

            for (File availableFile : files) {
                if (availableFile.getName().equals(fileName)) {
                    file = availableFile;
                }
            }

            if (file == null) {
                continue;
            }

            try (final BufferedReader reader = Files.newBufferedReader(file.toPath())) {
                final JsonReader jsonReader = new JsonReader(reader);
                final Map<String, String> map = HyriAPI.GSON.fromJson(jsonReader, Map.class);

                HyriAPI.get().log("Loading " + language.getCode() + " language from " + fileName + "...");

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    final String key = entry.getKey();
                    final String value = HyriChatColor.translateAlternateColorCodes('&', entry.getValue());

                    HyriLanguageMessage message = this.getMessage(key);

                    if (message == null) {
                        message = new HyriLanguageMessage(key);
                    } else {
                        this.messages.remove(message);
                    }

                    message.addValue(language, value);

                    this.messages.add(message);

                    messages.remove(message);
                    messages.add(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return messages;
    }

    @Override
    public <T> void registerAdapter(Class<T> clazz, IHyriLanguageAdapter<T> adapter) {
        this.adapters.put(clazz, adapter);
    }

    @Override
    public void addMessage(HyriLanguageMessage message) {
        final String key = message.getKey();

        if (this.getMessage(key) != null) {
            throw new IllegalStateException("A message with the key '" + key + "' already exists!");
        }

        this.messages.add(message);
    }

    @Override
    public void removeMessage(HyriLanguageMessage message) {
        this.messages.remove(message);
    }

    @Override
    public void removeMessage(String key) {
        this.messages.remove(this.getMessage(key));
    }

    @Override
    public HyriLanguageMessage getMessage(String key) {
        for (HyriLanguageMessage message : this.messages) {
            if (message.getKey().equalsIgnoreCase(key)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public Set<HyriLanguageMessage> getMessages() {
        return this.messages;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> IHyriLanguageAdapter<T> getAdapter(Class<T> clazz) {
        for (Map.Entry<Class<?>, IHyriLanguageAdapter<?>> entry : this.adapters.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                return (IHyriLanguageAdapter<T>) entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Map<Class<?>, IHyriLanguageAdapter<?>> getAdapters() {
        return this.adapters;
    }

}

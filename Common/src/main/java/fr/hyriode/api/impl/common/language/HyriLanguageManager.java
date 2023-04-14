package fr.hyriode.api.impl.common.language;

import com.google.gson.stream.JsonReader;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.language.*;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created by AstFaster
 * on 14/07/2022 at 22:43
 */
public class HyriLanguageManager implements IHyriLanguageManager {

    private final Map<UUID, HyriLanguage> languageCache = new HashMap<>();

    private final Map<String, HyriLanguageMessage> messages;
    private final Map<Class<?>, IHyriLanguageAdapter<?>> adapters;

    public HyriLanguageManager() {
        this.messages = new ConcurrentHashMap<>();
        this.adapters = new HashMap<>();

        HyriAPI.get().getEventBus().register(this);
    }

    @HyriEventHandler
    public void onLanguageUpdated(HyriLanguageUpdatedEvent event) {
        this.setCache(event.getPlayerId(), event.getLanguage());
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
    public Collection<HyriLanguageMessage> loadLanguagesMessages(File folder) {
        final Map<String, HyriLanguageMessage> messages = new HashMap<>();
        final File[] files = folder.listFiles();

        if (files == null) {
            return messages.values();
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

                    HyriLanguageMessage message = messages.get(key);
                    if (message == null) {
                        message = new HyriLanguageMessage(key);
                    }

                    message.addValue(language, value);
                    messages.put(key, message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.messages.putAll(messages); // Finally, add all loaded messages

        return messages.values();
    }

    @Override
    public Collection<HyriLanguageMessage> loadLanguagesMessages(Path folder, String resourceFolder, Function<String, InputStream> resourceProvider) {
        for (HyriLanguage language : HyriLanguage.values()) {
            final String path = resourceFolder + language.getCode() + ".json";

            try (final InputStream inputStream = resourceProvider.apply(path)) {
                if (inputStream == null) {
                    System.err.println("Cannot get resource from '" + path + "'!");
                    continue;
                }

                final Path langFile = Paths.get(folder.toString(), language.getCode() + ".json");
                final boolean exists = Files.exists(langFile);

                if (!exists) {
                    FileUtils.copyInputStreamToFile(inputStream, langFile.toFile());
                    continue;
                }

                try (final InputStream existingInput = Files.newInputStream(langFile); final InputStream clonedInput = resourceProvider.apply(path)) {
                    if (!this.toMD5(clonedInput).equals(this.toMD5(existingInput)) && !HyriAPI.get().getConfig().isDevEnvironment()) {
                        continue;
                    }

                    Files.delete(langFile);
                    FileUtils.copyInputStreamToFile(inputStream, langFile.toFile());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.loadLanguagesMessages(folder.toFile());
    }

    public String toMD5(InputStream inputStream) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] data = new byte[8195];

            int read;
            while ((read = inputStream.read(data)) != -1) {
                digest.update(data, 0, read);
            }

            final StringBuilder builder = new StringBuilder();

            for (byte aByte : digest.digest()) {
                builder.append(Integer.toString(((aByte & 0xFF) + 0x100), 16).substring(1));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
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

        this.messages.put(message.getKey(), message);
    }

    @Override
    public void removeMessage(HyriLanguageMessage message) {
        this.removeMessage(message.getKey());
    }

    @Override
    public void removeMessage(String key) {
        this.messages.remove(key);
    }

    @Override
    public HyriLanguageMessage getMessage(String key) {
        return this.messages.getOrDefault(key, HyriLanguageMessage.from(key)); // By default, Return a blank message (to avoid errors)
    }

    @Override
    public Collection<HyriLanguageMessage> getMessages() {
        return this.messages.values();
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

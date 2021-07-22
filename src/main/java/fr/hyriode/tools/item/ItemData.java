package fr.hyriode.tools.item;

import org.bukkit.event.Event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ItemData implements Serializable {

    private final Map<Class<? extends Event>, ItemConsumer<ItemSupplier<? extends Event>>> events;

    public ItemData() {
        this.events = new HashMap<>();
    }

    public void addEvent(Class<? extends Event> eventClass, ItemConsumer<ItemSupplier<? extends Event>> eventConsumer) {
        this.events.put(eventClass, eventConsumer);
    }

    public ItemConsumer<ItemSupplier<? extends Event>> getEventConsumer(Class<? extends Event> eventClass) {
        return this.events.get(eventClass);
    }

}

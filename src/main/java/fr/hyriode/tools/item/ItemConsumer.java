package fr.hyriode.tools.item;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface ItemConsumer<T> extends Consumer<T>, Serializable {

    void accept(T t);

}

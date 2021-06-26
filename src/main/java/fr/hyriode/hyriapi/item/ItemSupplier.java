package fr.hyriode.hyriapi.item;

import java.io.Serializable;
import java.util.function.Supplier;

@FunctionalInterface
public interface ItemSupplier<T> extends Supplier<T>, Serializable {

    T get();

}

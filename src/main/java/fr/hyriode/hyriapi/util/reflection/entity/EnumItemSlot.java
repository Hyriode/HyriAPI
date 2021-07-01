package fr.hyriode.hyriapi.util.reflection.entity;

import fr.hyriode.hyriapi.util.reflection.Reflection;

public enum EnumItemSlot {

    MAIN_HAND("mainhand", 0),
    BOOTS("feet", 1),
    LEGGINGS("legs", 2),
    CHESTPLATE("chest", 3),
    HELMET("head", 4);

    private final String name;
    private final int slot;

    EnumItemSlot(String name, int slot) {
        this.name = name;
        this.slot = slot;
    }

    public Object getEnum() {
        return Reflection.invokeStaticMethod(Reflection.getNMSClass("EnumItemSlot"), "a", name);
    }

    public int getSlot() {
        return slot;
    }
    
}

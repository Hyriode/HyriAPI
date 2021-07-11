package fr.hyriode.hyriapi.tools.scoreboard;

public enum ObjectiveSlot {

    LIST(0),
    SIDEBAR(1),
    BELOW_NAME(2);

    private final int id;

    ObjectiveSlot(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}

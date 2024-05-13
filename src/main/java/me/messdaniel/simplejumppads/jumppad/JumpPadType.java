package me.messdaniel.simplejumppads.jumppad;

public enum JumpPadType {

    VECTOR("Vector"), FALLINGENTITY("FallingEntity");


    private String name;

    JumpPadType(String name) {
        this.name = name;
    }

    public static JumpPadType get(String name) {
        for (JumpPadType direction : JumpPadType.values()) {
            if (direction.name.equalsIgnoreCase(name)) return direction;
        }
        return null;
    }

    public String getName() {
        return name;
    }
}

package me.messdaniel.simplejumppads.jumppad;

public enum JumpPadDirection {

    UP("Up",0,1,0), DOWN("Down",0,-1,0),FACING("Facing",0,0,0),NORTH("North",0,0,-1),EAST("East",1,0,0),SOUTH("South",0,0,1),WEST("West",0,0,-1);


    private String name;
    private double x;
    private double y;
    private double z;

    JumpPadDirection(String name,double x,double y,double z) {
        this.name = name;
    }

    public static JumpPadDirection get(String name) {
        for (JumpPadDirection jumpPadDirection : JumpPadDirection.values()) {
            if (jumpPadDirection.name.equalsIgnoreCase(name)) return jumpPadDirection;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}

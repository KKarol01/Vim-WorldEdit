package com.vimworldedit;

public class Command implements Cloneable {
    public void clear() {
        command = "";
        directions = "";
        number = "";
        flags = "";
        mask = "";
    }

    @Override
    public Command clone() {
        try {
            Command clone = (Command) super.clone();
            clone.command = this.command;
            clone.directions = this.directions;
            clone.number = this.number;
            clone.flags = this.flags;
            clone.mask = this.mask;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String toString() {
        String cmd = "";
        cmd += this.command + " ";
        if (!this.number.isEmpty()) {
            cmd += this.number + " ";
        }
        if (!this.directions.isEmpty()) {
            cmd += this.directions + " ";
        }
        if (!this.flags.isEmpty()) {
            cmd += this.flags;

            if (cmd.endsWith("-m") && !mask.isEmpty()) {
                cmd += " " + mask;
            }
        }


        return cmd;
    }

    String command = "", directions = "", number = "", flags = "", mask = "";
}

package com.vimworldedit;

public class Action implements Comparable<Action> {
    Action(String command, int category, int glfw_key, int glfw_mod) {
        this.command = command;
        this.category = category;
        this.glfw_key = glfw_key;
        this.glfw_mod = glfw_mod;
    }

    @Override
    public int compareTo(Action a) {
        int x = Integer.compare(this.glfw_key, a.glfw_key);
        if (x != 0) {
            return x;
        }

        return Integer.compare(this.glfw_mod, a.glfw_mod);
    }

    String command;
    int glfw_key, glfw_mod, category;
}
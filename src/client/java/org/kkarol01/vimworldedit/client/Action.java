package org.kkarol01.vimworldedit.client;

public class Action {
    Action(String command, KeyCategory category, int keycode, int modifierKey) {
        this.command = command;
        this.category = category;
        this.keyBinding = keycode;
        this.modifierKey = modifierKey;
    }

    String command;
    KeyCategory category;
    int modifierKey;
    int keyBinding;
}
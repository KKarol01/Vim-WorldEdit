package com.vimworldedit;

import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Action {
    String command;
    int modifierKey;
    KeyCategory category;
    int keyBinding;

    Action(String command, KeyCategory category, int keycode, int modifierKey) {
        this.command = command;
        this.category = category;
        this.keyBinding = keycode;
        this.modifierKey = modifierKey;
    }

    Action(String name, String command, KeyCategory category, int keycode) {
        this(name, command, category, keycode, 0);
    }
    Action(String name, String command, KeyCategory category, int keycode, int modifierKey) {
        this(command, category, keycode, modifierKey);
    }



}
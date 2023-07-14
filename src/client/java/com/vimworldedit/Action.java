package com.vimworldedit;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Action {
    String command;
    int modifierKey;
    KeyCategory category;
    KeyBinding keyBinding;

    Action(String translationKey, String command, KeyCategory category, int keycode, int modifierKey, String categoryTranslationKey) {
        this.command = command;
        this.category = category;
        this.keyBinding = new KeyBinding(
                "key.vimworldedit." + translationKey,
                InputUtil.Type.KEYSYM,
                keycode,
                categoryTranslationKey
        );
        this.modifierKey = modifierKey;
    }

    Action(String name, String command, KeyCategory category, int keycode) {
        this(name, command, category, keycode, 0, category.translationKey);
    }
    Action(String name, String command, KeyCategory category, int keycode, int modifierKey) {
        this(name, command, category, keycode, modifierKey, category.translationKey);
    }



}
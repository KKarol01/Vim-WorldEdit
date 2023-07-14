package com.vimworldedit;

public enum KeyCategory {
	COMMAND_KEY(0, "key.category.vimworldedit.commands"),
	DIRECTION_KEY(1, "key.category.vimworldedit.directions"),
	FLAGS_KEY(2, "key.category.vimworldedit.flags");

	final int value;

	final String translationKey;

	KeyCategory(int value, String translationKey) {
		this.value = value;
		this.translationKey = translationKey;
	}
}

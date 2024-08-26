package com.vimworldedit;

import net.minecraft.util.math.Vec3d;

public class SavedPosition implements Cloneable {
    public SavedPosition(Vec3d position) {
        this.position = position;
    }

    public String get_pos_string() {
        return String.format("%f %f %f", position.x, position.y, position.z);
    }

    @Override
    public SavedPosition clone() {
        try {
            SavedPosition clone = (SavedPosition) super.clone();
            clone.position = position;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Vec3d position;
}

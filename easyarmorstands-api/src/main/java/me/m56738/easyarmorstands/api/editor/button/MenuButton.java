package me.m56738.easyarmorstands.api.editor.button;

import me.m56738.easyarmorstands.api.editor.Session;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;

public interface MenuButton {
    Button getButton();

    Component getName();

    void onClick(Session session, @Nullable Vector3dc cursor);
}

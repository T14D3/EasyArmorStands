package me.m56738.easyarmorstands.display.editor.button;

import me.m56738.easyarmorstands.api.editor.Session;
import me.m56738.easyarmorstands.api.editor.button.Button;
import me.m56738.easyarmorstands.api.editor.button.MenuButton;
import me.m56738.easyarmorstands.api.editor.button.PointButton;
import me.m56738.easyarmorstands.api.editor.node.Node;
import me.m56738.easyarmorstands.api.particle.ParticleColor;
import me.m56738.easyarmorstands.display.editor.tool.DisplayBoxResizeTool;
import me.m56738.easyarmorstands.editor.node.AxisMoveToolNode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;

public class DisplayBoxResizeButton implements MenuButton {
    private final Session session;
    private final Component name;
    private final ParticleColor color;
    private final DisplayBoxResizeTool tool;

    public DisplayBoxResizeButton(Session session, Component name, ParticleColor color, DisplayBoxResizeTool tool) {
        this.session = session;
        this.name = name;
        this.color = color;
        this.tool = tool;
    }

    @Override
    public @NotNull Button getButton() {
        PointButton button = new PointButton(session, tool, tool);
        button.setPriority(2);
        button.setColor(color);
        return button;
    }

    @Override
    public @NotNull Component getName() {
        return name;
    }

    @Override
    public void onClick(@NotNull Session session, @Nullable Vector3dc cursor) {
        Node node = new AxisMoveToolNode(session, tool.start(), name, color, 3, tool.getPosition(), tool.getRotation(), tool.getAxis());
        session.pushNode(node, cursor);
    }
}

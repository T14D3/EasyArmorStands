package me.m56738.easyarmorstands.group.tool;

import me.m56738.easyarmorstands.api.Axis;
import me.m56738.easyarmorstands.api.editor.tool.AxisMoveTool;
import me.m56738.easyarmorstands.api.editor.tool.AxisMoveToolSession;
import me.m56738.easyarmorstands.api.util.PositionProvider;
import me.m56738.easyarmorstands.api.util.RotationProvider;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

import java.util.ArrayList;
import java.util.List;

public class GroupAxisMoveTool implements AxisMoveTool {
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;
    private final Axis axis;
    private final List<AxisMoveTool> tools;

    public GroupAxisMoveTool(PositionProvider positionProvider, RotationProvider rotationProvider, Axis axis, List<AxisMoveTool> tools) {
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
        this.axis = axis;
        this.tools = new ArrayList<>(tools);
    }

    @Override
    public @NotNull Vector3dc getPosition() {
        return positionProvider.getPosition();
    }

    @Override
    public @NotNull Quaterniondc getRotation() {
        return rotationProvider.getRotation();
    }

    @Override
    public @NotNull Axis getAxis() {
        return axis;
    }

    @Override
    public @NotNull AxisMoveToolSession start() {
        return new SessionImpl();
    }

    private class SessionImpl extends GroupToolSession<AxisMoveToolSession> implements AxisMoveToolSession {
        private SessionImpl() {
            super(tools);
        }

        @Override
        public void setChange(double change) {
            for (AxisMoveToolSession session : sessions) {
                session.setChange(change);
            }
        }
    }
}
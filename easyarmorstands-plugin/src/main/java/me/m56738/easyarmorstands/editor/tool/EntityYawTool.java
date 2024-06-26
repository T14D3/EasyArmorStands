package me.m56738.easyarmorstands.editor.tool;

import me.m56738.easyarmorstands.api.Axis;
import me.m56738.easyarmorstands.api.editor.Snapper;
import me.m56738.easyarmorstands.api.editor.tool.AxisRotateTool;
import me.m56738.easyarmorstands.api.editor.tool.AxisRotateToolSession;
import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import me.m56738.easyarmorstands.api.property.type.EntityPropertyTypes;
import me.m56738.easyarmorstands.api.util.PositionProvider;
import me.m56738.easyarmorstands.api.util.RotationProvider;
import me.m56738.easyarmorstands.message.Message;
import me.m56738.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class EntityYawTool implements AxisRotateTool {
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;

    public EntityYawTool(PropertyContainer properties, PositionProvider positionProvider, RotationProvider rotationProvider) {
        this.properties = properties;
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
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
        return Axis.Y;
    }

    @Override
    public @NotNull AxisRotateToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return locationProperty.canChange(player);
    }

    private class SessionImpl extends AbstractToolSession implements AxisRotateToolSession {
        private final Location originalLocation;
        private final Vector3dc originalOffset;
        private final Vector3d offsetChange = new Vector3d();

        public SessionImpl() {
            super(properties);
            this.originalLocation = locationProperty.getValue().clone();
            this.originalOffset = Util.toVector3d(originalLocation).sub(getPosition());
        }

        @Override
        public void setChange(double change) {
            originalOffset.rotateY(change, offsetChange).sub(originalOffset);

            Location location = originalLocation.clone();
            location.add(offsetChange.x(), offsetChange.y(), offsetChange.z());
            location.setYaw(location.getYaw() - (float) Math.toDegrees(change));
            locationProperty.setValue(location);
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            double original = Math.toRadians(originalLocation.getYaw());
            change -= original;
            change = context.snapAngle(change);
            change += original;
            return change;
        }

        @Override
        public void setValue(double value) {
            setChange(Math.toRadians(originalLocation.getYaw()) - value);
        }

        @Override
        public void revert() {
            locationProperty.setValue(originalLocation);
        }

        @Override
        public @Nullable Component getStatus() {
            return Util.formatDegrees(locationProperty.getValue().getYaw());
        }

        @Override
        public @Nullable Component getDescription() {
            return Message.component("easyarmorstands.history.rotate-yaw");
        }
    }
}

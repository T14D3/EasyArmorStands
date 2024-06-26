package me.m56738.easyarmorstands.display.editor.tool;

import me.m56738.easyarmorstands.api.Axis;
import me.m56738.easyarmorstands.api.editor.Snapper;
import me.m56738.easyarmorstands.api.editor.tool.AxisMoveTool;
import me.m56738.easyarmorstands.api.editor.tool.AxisMoveToolSession;
import me.m56738.easyarmorstands.api.property.PendingChange;
import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import me.m56738.easyarmorstands.api.property.type.EntityPropertyTypes;
import me.m56738.easyarmorstands.api.util.PositionProvider;
import me.m56738.easyarmorstands.api.util.RotationProvider;
import me.m56738.easyarmorstands.display.api.property.type.DisplayPropertyTypes;
import me.m56738.easyarmorstands.editor.tool.AbstractToolSession;
import me.m56738.easyarmorstands.message.Message;
import me.m56738.easyarmorstands.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class DisplayBoxResizeTool implements AxisMoveTool {
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final Property<Vector3fc> translationProperty;
    private final Property<Float> widthProperty;
    private final Property<Float> heightProperty;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;
    private final Axis axis;
    private final boolean end;

    public DisplayBoxResizeTool(PropertyContainer properties, PositionProvider positionProvider, RotationProvider rotationProvider, Axis axis, boolean end) {
        this.properties = properties;
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.translationProperty = properties.get(DisplayPropertyTypes.TRANSLATION);
        this.widthProperty = properties.get(DisplayPropertyTypes.BOX_WIDTH);
        this.heightProperty = properties.get(DisplayPropertyTypes.BOX_HEIGHT);
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
        this.axis = axis;
        this.end = end;
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

    @Override
    public boolean canUse(@NotNull Player player) {
        Property<?> property;
        if (axis == Axis.Y) {
            property = heightProperty;
        } else {
            property = widthProperty;
        }
        return property.canChange(player) &&
                locationProperty.canChange(player) &&
                translationProperty.canChange(player);
    }

    private class SessionImpl extends AbstractToolSession implements AxisMoveToolSession {
        private final Location originalLocation;
        private final Vector3fc originalTranslation;
        private final float originalWidth;
        private final float originalHeight;
        private final float originalValue;

        public SessionImpl() {
            super(properties);
            originalLocation = locationProperty.getValue().clone();
            originalTranslation = new Vector3f(translationProperty.getValue());
            originalWidth = widthProperty.getValue();
            originalHeight = heightProperty.getValue();
            if (axis == Axis.Y) {
                originalValue = originalHeight;
            } else {
                originalValue = originalWidth;
            }
        }

        @Override
        public void setChange(double change) {
            if (!end) {
                change = -change;
            }

            Vector3d offset = new Vector3d();

            List<PendingChange> changes = new ArrayList<>(3);

            if (axis == Axis.Y) {
                float height = originalHeight + (float) change;
                changes.add(heightProperty.prepareChange(Math.abs(height)));
                // move entity down/up to compensate for increased/decreased height
                if (height < 0) {
                    if (end) {
                        offset.y = height;
                    } else {
                        offset.y = originalHeight;
                    }
                } else if (!end) {
                    offset.y = -change;
                }
            } else {
                changes.add(widthProperty.prepareChange(Math.abs(originalWidth + (float) change)));
                axis.setValue(offset, change / 2);
                if (!end) {
                    offset.negate();
                }
            }

            // Move box by modifying the location
            Location location = originalLocation.clone();
            location.add(offset.x(), offset.y(), offset.z());
            changes.add(locationProperty.prepareChange(location));

            // Make sure the display stays in the same place by performing the inverse using the translation
            Vector3fc rotatedDelta = offset.get(new Vector3f())
                    .rotate(Util.getRoundedYawPitchRotation(location, new Quaternionf()).conjugate());
            Vector3fc translation = originalTranslation.sub(rotatedDelta, new Vector3f());
            changes.add(translationProperty.prepareChange(translation));

            // Only execute changes if all of them are allowed
            if (changes.contains(null)) {
                return;
            }
            for (PendingChange pendingChange : changes) {
                if (!pendingChange.execute()) {
                    return;
                }
            }
        }

        @Override
        public double snapChange(double change, @NotNull Snapper context) {
            if (!end) {
                change = -change;
            }
            change += originalValue;
            change = context.snapPosition(change);
            change -= originalValue;
            if (!end) {
                change = -change;
            }
            return change;
        }

        @Override
        public void setValue(double value) {
            double change = value - originalValue;
            if (!end) {
                change = -change;
            }
            setChange(change);
        }

        @Override
        public void revert() {
            locationProperty.setValue(originalLocation);
            translationProperty.setValue(originalTranslation);
            widthProperty.setValue(originalWidth);
            heightProperty.setValue(originalHeight);
        }

        @Override
        public @Nullable Component getStatus() {
            float value;
            if (axis == Axis.Y) {
                value = heightProperty.getValue();
            } else {
                value = widthProperty.getValue();
            }
            return Component.text(Util.SCALE_FORMAT.format(value));
        }

        @Override
        public @Nullable Component getDescription() {
            return Message.component("easyarmorstands.history.resize-box");
        }
    }
}

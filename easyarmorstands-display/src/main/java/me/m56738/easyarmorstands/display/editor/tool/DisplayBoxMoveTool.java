package me.m56738.easyarmorstands.display.editor.tool;

import me.m56738.easyarmorstands.api.editor.Snapper;
import me.m56738.easyarmorstands.api.editor.tool.MoveTool;
import me.m56738.easyarmorstands.api.editor.tool.MoveToolSession;
import me.m56738.easyarmorstands.api.property.PendingChange;
import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import me.m56738.easyarmorstands.api.property.type.EntityPropertyTypes;
import me.m56738.easyarmorstands.api.util.PositionProvider;
import me.m56738.easyarmorstands.api.util.RotationProvider;
import me.m56738.easyarmorstands.display.api.property.type.DisplayPropertyTypes;
import me.m56738.easyarmorstands.display.editor.DisplayBoxPositionProvider;
import me.m56738.easyarmorstands.display.editor.DisplayOffsetProvider;
import me.m56738.easyarmorstands.editor.EntityPositionProvider;
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

public class DisplayBoxMoveTool implements MoveTool {
    private final PropertyContainer properties;
    private final Property<Location> locationProperty;
    private final Property<Vector3fc> translationProperty;
    private final PositionProvider positionProvider;
    private final RotationProvider rotationProvider;
    private final PositionProvider boxPositionProvider;
    private final PositionProvider displayPositionProvider;

    public DisplayBoxMoveTool(PropertyContainer properties, PositionProvider positionProvider, RotationProvider rotationProvider) {
        this.properties = properties;
        this.locationProperty = properties.get(EntityPropertyTypes.LOCATION);
        this.translationProperty = properties.get(DisplayPropertyTypes.TRANSLATION);
        this.positionProvider = positionProvider;
        this.rotationProvider = rotationProvider;
        this.boxPositionProvider = new DisplayBoxPositionProvider(properties);
        this.displayPositionProvider = new EntityPositionProvider(properties, new DisplayOffsetProvider(properties));
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
    public @NotNull MoveToolSession start() {
        return new SessionImpl();
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return locationProperty.canChange(player) && translationProperty.canChange(player);
    }

    private class SessionImpl extends AbstractToolSession implements MoveToolSession {
        private final Location originalLocation;
        private final Vector3fc originalTranslation;
        private final Vector3dc originalBoxPosition;
        private final Vector3d offset = new Vector3d();

        public SessionImpl() {
            super(properties);
            originalLocation = locationProperty.getValue().clone();
            originalTranslation = new Vector3f(translationProperty.getValue());
            originalBoxPosition = getPosition();
        }

        @Override
        public void setChange(@NotNull Vector3dc change) {
            this.offset.set(change);

            // Move box by modifying the location
            Location location = originalLocation.clone();
            location.add(change.x(), change.y(), change.z());
            PendingChange locationChange = locationProperty.prepareChange(location);

            // Make sure the display stays in the same place by performing the inverse using the translation
            Vector3fc rotatedDelta = change.get(new Vector3f())
                    .rotate(Util.getRoundedYawPitchRotation(location, new Quaternionf()).conjugate());
            Vector3fc translation = originalTranslation.sub(rotatedDelta, new Vector3f());
            PendingChange translationChange = translationProperty.prepareChange(translation);

            // Only execute changes if both are allowed
            if (locationChange != null && translationChange != null) {
                if (locationChange.execute()) {
                    translationChange.execute();
                }
            }
        }

        @Override
        public void snapChange(@NotNull Vector3d change, @NotNull Snapper context) {
            change.add(originalBoxPosition);
            context.snapPosition(change);
            change.sub(originalBoxPosition);
        }

        @Override
        public @NotNull Vector3dc getPosition() {
            return boxPositionProvider.getPosition()
                    .sub(displayPositionProvider.getPosition(), new Vector3d());
        }

        @Override
        public void setPosition(@NotNull Vector3dc position) {
            setChange(position.sub(originalBoxPosition, offset));
        }

        @Override
        public void revert() {
            locationProperty.setValue(originalLocation);
            translationProperty.setValue(originalTranslation);
        }

        @Override
        public @Nullable Component getStatus() {
            return Util.formatOffset(offset);
        }

        @Override
        public @Nullable Component getDescription() {
            return Message.component("easyarmorstands.history.move-box", Util.formatOffset(offset));
        }
    }
}

package me.m56738.easyarmorstands.display.editor.button;

import me.m56738.easyarmorstands.api.editor.Session;
import me.m56738.easyarmorstands.node.AxisAlignedBoxButton;
import me.m56738.easyarmorstands.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class DisplayButton<T extends Display> extends AxisAlignedBoxButton {
    private final T entity;

    public DisplayButton(Session session, T entity) {
        super(session);
        this.entity = entity;
    }

    @Override
    protected Vector3dc getPosition() {
        return Util.toVector3d(entity.getLocation());
    }

    @Override
    protected Vector3dc getCenter() {
        Location location = entity.getLocation();
        return new Vector3d(location.getX(), location.getY() + entity.getDisplayHeight() / 2, location.getZ());
    }

    @Override
    protected Vector3dc getSize() {
        double width = entity.getDisplayWidth();
        double height = entity.getDisplayHeight();
        return new Vector3d(width, height, width);
    }

}

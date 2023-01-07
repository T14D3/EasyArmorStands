package gg.bundlegroup.easyarmorstands.api.event;

import gg.bundlegroup.easyarmorstands.math.Matrix3x3;
import gg.bundlegroup.easyarmorstands.api.BoneType;
import gg.bundlegroup.easyarmorstands.api.Session;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.EulerAngle;

public class PlayerEditArmorStandPoseEvent extends ArmorStandSessionEvent implements Cancellable {
    private final BoneType bone;
    private final Matrix3x3 rotation;
    private final EulerAngle pose;
    private boolean cancelled;

    public PlayerEditArmorStandPoseEvent(Session session, BoneType bone, Matrix3x3 rotation, EulerAngle pose) {
        super(session);
        this.bone = bone;
        this.rotation = rotation;
        this.pose = pose;
    }

    public BoneType getBone() {
        return bone;
    }

    public Matrix3x3 getRotation() {
        return rotation;
    }

    public EulerAngle getPose() {
        return pose;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
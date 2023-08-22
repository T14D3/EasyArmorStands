package me.m56738.easyarmorstands.node;

import me.m56738.easyarmorstands.api.Axis;
import me.m56738.easyarmorstands.api.editor.Session;
import me.m56738.easyarmorstands.api.editor.bone.PositionAndYawBone;
import me.m56738.easyarmorstands.api.editor.context.EnterContext;
import me.m56738.easyarmorstands.api.particle.ParticleColor;
import net.kyori.adventure.text.Component;

public class YawBoneNode extends RotationNode {
    private final PositionAndYawBone bone;
    private final Component name;
    private float initialYaw;

    public YawBoneNode(Session session, Component name, ParticleColor color, double radius, PositionAndYawBone bone) {
        super(session, color, bone.getPosition(), Axis.Y, radius);
        this.name = name;
        this.bone = bone;
    }

    @Override
    public void onEnter(EnterContext context) {
        super.onEnter(context);
        initialYaw = bone.getYaw();
    }

    @Override
    protected void abort() {
        bone.setYaw(initialYaw);
    }

    @Override
    protected void refresh() {
        getAnchor().set(bone.getPosition());
    }

    @Override
    protected void apply(double angle, double degrees) {
        bone.setYaw(initialYaw - (float) degrees);
    }

    @Override
    protected void commit() {
        bone.commit();
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public boolean isValid() {
        return bone.isValid();
    }
}
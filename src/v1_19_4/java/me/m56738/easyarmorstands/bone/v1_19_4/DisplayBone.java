package me.m56738.easyarmorstands.bone.v1_19_4;

import me.m56738.easyarmorstands.bone.EntityLocationBone;
import me.m56738.easyarmorstands.bone.RotationBone;
import me.m56738.easyarmorstands.bone.ScaleBone;
import me.m56738.easyarmorstands.property.Property;
import me.m56738.easyarmorstands.property.PropertyContainer;
import me.m56738.easyarmorstands.property.PropertyType;
import me.m56738.easyarmorstands.property.v1_19_4.display.DisplayScaleProperty;
import me.m56738.easyarmorstands.property.v1_19_4.display.DisplayTranslationProperty;
import org.joml.Math;
import org.joml.*;

public class DisplayBone extends EntityLocationBone implements RotationBone, ScaleBone {
    private final Property<Vector3fc> translationProperty;
    private final Property<Quaternionfc> rotationProperty;
    private final Property<Vector3fc> scaleProperty;

    public DisplayBone(PropertyContainer container, PropertyType<Quaternionfc> rotationType) {
        super(container);
        this.translationProperty = container.get(DisplayTranslationProperty.TYPE);
        this.rotationProperty = container.get(rotationType);
        this.scaleProperty = container.get(DisplayScaleProperty.TYPE);
    }

    @Override
    public Vector3dc getOffset() {
        return new Vector3d(translationProperty.getValue())
                .rotateY(-Math.toRadians(getYaw()));
    }

    @Override
    public Vector3dc getAnchor() {
        return getPosition();
    }

    @Override
    public Vector3dc getOrigin() {
        return getPosition();
    }

    @Override
    public Quaterniondc getRotation() {
        return new Quaterniond(rotationProperty.getValue())
                .rotateLocalY(-Math.toRadians(getYaw()));
    }

    @Override
    public void setRotation(Quaterniondc rotation) {
        rotationProperty.setValue(new Quaternionf(rotation)
                .rotateLocalY(Math.toRadians(getYaw())));
    }

    @Override
    public Vector3dc getScale() {
        return new Vector3d(scaleProperty.getValue());
    }

    @Override
    public void setScale(Vector3dc scale) {
        scaleProperty.setValue(scale.get(new Vector3f()));
    }
}

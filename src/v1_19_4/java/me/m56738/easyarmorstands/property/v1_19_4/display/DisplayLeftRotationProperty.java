package me.m56738.easyarmorstands.property.v1_19_4.display;

import me.m56738.easyarmorstands.property.Property;
import me.m56738.easyarmorstands.property.type.PropertyType;
import me.m56738.easyarmorstands.util.v1_19_4.JOMLMapper;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionfc;

public class DisplayLeftRotationProperty implements Property<Quaternionfc> {
    private final Display entity;
    private final JOMLMapper mapper;

    public DisplayLeftRotationProperty(Display entity, JOMLMapper mapper) {
        this.entity = entity;
        this.mapper = mapper;
    }

    @Override
    public PropertyType<Quaternionfc> getType() {
        return DisplayPropertyTypes.DISPLAY_LEFT_ROTATION;
    }

    @Override
    public Quaternionfc getValue() {
        return mapper.getLeftRotation(entity.getTransformation());
    }

    @Override
    public boolean setValue(Quaternionfc value) {
        Transformation transformation = entity.getTransformation();
        entity.setTransformation(mapper.getTransformation(
                mapper.getTranslation(transformation),
                value,
                mapper.getScale(transformation),
                mapper.getRightRotation(transformation)));
        return true;
    }

    // TODO /eas reset
//        @Override
//        public Quaternionfc getResetValue() {
//            return new Quaternionf();
//        }
}

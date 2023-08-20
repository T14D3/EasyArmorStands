package me.m56738.easyarmorstands.property.armorstand;

import me.m56738.easyarmorstands.property.Property;
import me.m56738.easyarmorstands.property.type.PropertyType;
import me.m56738.easyarmorstands.property.type.PropertyTypes;
import org.bukkit.entity.ArmorStand;

public class ArmorStandArmsProperty implements Property<Boolean> {
    private final ArmorStand entity;

    public ArmorStandArmsProperty(ArmorStand entity) {
        this.entity = entity;
    }

    @Override
    public PropertyType<Boolean> getType() {
        return PropertyTypes.ARMOR_STAND_ARMS;
    }

    @Override
    public Boolean getValue() {
        return entity.hasArms();
    }

    @Override
    public boolean setValue(Boolean value) {
        entity.setArms(value);
        return true;
    }
}

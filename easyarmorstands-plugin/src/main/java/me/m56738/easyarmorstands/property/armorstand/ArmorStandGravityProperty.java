package me.m56738.easyarmorstands.property.armorstand;

import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import me.m56738.easyarmorstands.api.property.type.PropertyType;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;

public class ArmorStandGravityProperty implements Property<Boolean> {
    private final ArmorStand entity;

    public ArmorStandGravityProperty(ArmorStand entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull PropertyType<Boolean> getType() {
        return ArmorStandPropertyTypes.GRAVITY;
    }

    @Override
    public Boolean getValue() {
        return entity.hasGravity();
    }

    @Override
    public boolean setValue(Boolean value) {
        entity.setGravity(value);
        return true;
    }
}

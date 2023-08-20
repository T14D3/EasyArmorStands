package me.m56738.easyarmorstands.property.entity;

import me.m56738.easyarmorstands.capability.equipment.EquipmentCapability;
import me.m56738.easyarmorstands.property.Property;
import me.m56738.easyarmorstands.property.type.PropertyType;
import me.m56738.easyarmorstands.property.type.PropertyTypes;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class EntityEquipmentProperty implements Property<ItemStack> {
    private final LivingEntity entity;
    private final EquipmentSlot slot;
    private final PropertyType<ItemStack> type;
    private final EquipmentCapability equipmentCapability;

    public EntityEquipmentProperty(LivingEntity entity, EquipmentSlot slot, EquipmentCapability equipmentCapability) {
        this.entity = entity;
        this.slot = slot;
        this.type = PropertyTypes.ENTITY_EQUIPMENT.get(slot);
        this.equipmentCapability = equipmentCapability;
    }

    @Override
    public PropertyType<ItemStack> getType() {
        return type;
    }

    @Override
    public ItemStack getValue() {
        return equipmentCapability.getItem(entity.getEquipment(), slot);
    }

    @Override
    public boolean setValue(ItemStack value) {
        equipmentCapability.setItem(entity.getEquipment(), slot, value);
        return true;
    }
}

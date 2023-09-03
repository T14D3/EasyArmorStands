package me.m56738.easyarmorstands.menu.slot;

import me.m56738.easyarmorstands.api.menu.MenuSlotFactory;
import me.m56738.easyarmorstands.api.menu.MenuSlotType;
import me.m56738.easyarmorstands.element.ArmorStandElementType;
import me.m56738.easyarmorstands.item.ItemTemplate;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class ArmorStandSpawnSlotType implements MenuSlotType {
    public static final Key KEY = Key.key("easyarmorstands", "spawn/armor_stand");
    private final ArmorStandElementType type;

    public ArmorStandSpawnSlotType(ArmorStandElementType type) {
        this.type = type;
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }

    @Override
    public @NotNull MenuSlotFactory load(ConfigurationNode node) throws SerializationException {
        return new SpawnSlotFactory(type, node.node("item").get(ItemTemplate.class));
    }
}

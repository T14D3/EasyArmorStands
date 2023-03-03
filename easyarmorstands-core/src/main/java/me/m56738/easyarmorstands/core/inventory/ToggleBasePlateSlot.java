package me.m56738.easyarmorstands.core.inventory;

import me.m56738.easyarmorstands.core.platform.EasArmorStand;
import me.m56738.easyarmorstands.core.platform.EasMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;

public class ToggleBasePlateSlot extends ToggleSlot {
    private final SessionMenu menu;

    public ToggleBasePlateSlot(SessionMenu menu) {
        super(
                menu,
                EasMaterial.STONE_SLAB,
                Component.text("Toggle base plate", NamedTextColor.BLUE),
                Arrays.asList(
                        Component.text("Changes whether the base", NamedTextColor.GRAY),
                        Component.text("plate of the armor stand", NamedTextColor.GRAY),
                        Component.text("is visible.", NamedTextColor.GRAY)
                )
        );
        this.menu = menu;
    }

    @Override
    protected Component getValue() {
        return menu.getSession().getEntity().hasBasePlate()
                ? Component.text("has a base plate", NamedTextColor.GREEN)
                : Component.text("has no base plate", NamedTextColor.RED);
    }

    @Override
    protected void onClick() {
        EasArmorStand entity = menu.getSession().getEntity();
        entity.setBasePlate(!entity.hasBasePlate());
    }
}
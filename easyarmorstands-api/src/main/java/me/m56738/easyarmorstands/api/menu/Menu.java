package me.m56738.easyarmorstands.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@ApiStatus.NonExtendable
public interface Menu extends InventoryHolder {
    static int index(int row, int column) {
        return 9 * row + column;
    }

    @Contract(pure = true)
    int getSize();

    @Contract(pure = true)
    @Nullable MenuSlot getSlot(int index);

    void close(@NotNull Player player);

    void updateItem(int index);

    void updateItem(@NotNull MenuSlot slot);

    void updateItems(Predicate<MenuSlot> predicate);

    void queueTask(@NotNull Runnable task);

    void interceptNextClick(@NotNull MenuClickInterceptor interceptor);

    void onClick(@NotNull MenuClick click);
}

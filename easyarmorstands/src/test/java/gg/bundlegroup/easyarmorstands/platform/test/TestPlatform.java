package gg.bundlegroup.easyarmorstands.platform.test;

import cloud.commandframework.CommandManager;
import gg.bundlegroup.easyarmorstands.platform.EasArmorEntity;
import gg.bundlegroup.easyarmorstands.platform.EasCommandSender;
import gg.bundlegroup.easyarmorstands.platform.EasInventory;
import gg.bundlegroup.easyarmorstands.platform.EasInventoryListener;
import gg.bundlegroup.easyarmorstands.platform.EasItem;
import gg.bundlegroup.easyarmorstands.platform.EasListener;
import gg.bundlegroup.easyarmorstands.platform.EasPlatform;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestPlatform implements EasPlatform {
    private final TestCommandManager commandManager = new TestCommandManager();
    private final Set<TestPlayer> players = new HashSet<>();

    public TestWorld createWorld() {
        return new TestWorld(this);
    }

    public TestItem createItem(boolean tool) {
        return new TestItem(this, tool);
    }

    @Override
    public CommandManager<EasCommandSender> commandManager() {
        return commandManager;
    }

    @Override
    public boolean canHideEntities() {
        return true;
    }

    @Override
    public boolean canSetEntityPersistence() {
        return true;
    }

    @Override
    public boolean canSetEntityGlowing() {
        return true;
    }

    @Override
    public boolean canSpawnParticles() {
        return false;
    }

    @Override
    public boolean hasSlot(EasArmorEntity.Slot slot) {
        return true;
    }

    @Override
    public Collection<TestPlayer> getPlayers() {
        return players;
    }

    @Override
    public EasInventory createInventory(Component title, int width, int height, EasInventoryListener listener) {
        return new TestInventory(this);
    }

    @Override
    public EasItem createPlaceholderItem() {
        return createItem(false);
    }

    @Override
    public void registerListener(EasListener listener) {
    }

    @Override
    public void registerTickTask(Runnable task) {
    }
}

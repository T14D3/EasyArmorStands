package gg.bundlegroup.easyarmorstands.common.platform;

public interface EasListener {
    boolean onLeftClick(EasPlayer player, EasItem item);

    boolean onLeftClickArmorStand(EasPlayer player, EasArmorStand armorStand, EasItem item, boolean cancelled);

    boolean onRightClick(EasPlayer player, EasItem item);

    boolean onRightClickArmorStand(EasPlayer player, EasArmorStand armorStand, EasItem item, boolean cancelled);

    void onScroll(EasPlayer player, int from, int to);

    void onLogin(EasPlayer player);

    void onJoin(EasPlayer player);

    void onQuit(EasPlayer player);
}
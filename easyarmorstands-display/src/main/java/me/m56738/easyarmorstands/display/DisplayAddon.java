package me.m56738.easyarmorstands.display;

import cloud.commandframework.brigadier.CloudBrigadierManager;
import io.leangen.geantyref.TypeToken;
import me.m56738.easyarmorstands.EasyArmorStands;
import me.m56738.easyarmorstands.addon.Addon;
import me.m56738.easyarmorstands.capability.entitytype.EntityTypeCapability;
import me.m56738.easyarmorstands.command.sender.EasCommandSender;
import me.m56738.easyarmorstands.display.command.BlockDataArgumentParser;
import me.m56738.easyarmorstands.display.command.DisplayCommands;
import me.m56738.easyarmorstands.display.editor.DisplaySessionListener;
import me.m56738.easyarmorstands.display.editor.node.DisplayRootNode;
import me.m56738.easyarmorstands.display.element.DisplayElementProvider;
import me.m56738.easyarmorstands.display.element.DisplayElementType;
import me.m56738.easyarmorstands.display.element.TextDisplayElementType;
import me.m56738.easyarmorstands.display.property.display.DefaultDisplayPropertyTypes;
import me.m56738.easyarmorstands.api.element.EntityElementProviderRegistry;
import me.m56738.easyarmorstands.item.ItemTemplate;
import me.m56738.easyarmorstands.util.ConfigUtil;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;

public class DisplayAddon implements Addon {
    private JOMLMapper mapper;
    private DisplayElementType<ItemDisplay> itemDisplayType;
    private DisplayElementType<BlockDisplay> blockDisplayType;
    private DisplayElementType<TextDisplay> textDisplayType;
    private ItemTemplate displayBoxButtonTemplate;

    public DisplayAddon() {
        try {
            mapper = new JOMLMapper();
        } catch (Throwable ignored) {
        }
    }

    @Override
    public boolean isSupported() {
        return mapper != null;
    }

    @Override
    public String getName() {
        return "display entity";
    }

    @Override
    public void enable(EasyArmorStands plugin) {
        new DefaultDisplayPropertyTypes(plugin.getPropertyTypeRegistry());

        DisplaySessionListener listener = new DisplaySessionListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        plugin.getServer().getPluginManager().registerEvents(new DisplayListener(mapper), plugin);

        itemDisplayType = new DisplayElementType<>(this, ItemDisplay.class, plugin.getCapability(EntityTypeCapability.class).getName(EntityType.ITEM_DISPLAY), DisplayRootNode::new);
        blockDisplayType = new DisplayElementType<>(this, BlockDisplay.class, plugin.getCapability(EntityTypeCapability.class).getName(EntityType.BLOCK_DISPLAY), DisplayRootNode::new);
        textDisplayType = new TextDisplayElementType(this, DisplayRootNode::new);

        EntityElementProviderRegistry registry = plugin.getEntityElementProviderRegistry();
        registry.register(new DisplayElementProvider<>(itemDisplayType));
        registry.register(new DisplayElementProvider<>(blockDisplayType));
        registry.register(new DisplayElementProvider<>(textDisplayType));

        load(plugin.getConfig());

        plugin.getCommandManager().parserRegistry().registerParserSupplier(TypeToken.get(BlockData.class),
                p -> new BlockDataArgumentParser<>());

        CloudBrigadierManager<EasCommandSender, ?> brigadierManager = plugin.getCommandManager().brigadierManager();
        if (brigadierManager != null) {
            try {
                BlockDataArgumentParser.registerBrigadier(brigadierManager);
            } catch (Throwable e) {
                plugin.getLogger().warning("Failed to register Brigadier mappings for block data arguments");
            }
        }

        plugin.getAnnotationParser().parse(new DisplayCommands(this));
    }

    @Override
    public void reload(EasyArmorStands plugin) {
        load(plugin.getConfig());
    }

    private void load(ConfigurationSection config) {
        itemDisplayType.load(config, "menu.spawn.buttons.item-display");
        blockDisplayType.load(config, "menu.spawn.buttons.block-display");
        textDisplayType.load(config, "menu.spawn.buttons.text-display");
        displayBoxButtonTemplate = ConfigUtil.getButton(config, "menu.element.buttons.display-bone.bounding-box");
    }

    public JOMLMapper getMapper() {
        return mapper;
    }

    public DisplayElementType<ItemDisplay> getItemDisplayType() {
        return itemDisplayType;
    }

    public DisplayElementType<BlockDisplay> getBlockDisplayType() {
        return blockDisplayType;
    }

    public DisplayElementType<TextDisplay> getTextDisplayType() {
        return textDisplayType;
    }

    public ItemTemplate getDisplayBoxButtonTemplate() {
        return displayBoxButtonTemplate;
    }
}

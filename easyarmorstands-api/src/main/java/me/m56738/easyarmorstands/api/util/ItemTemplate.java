package me.m56738.easyarmorstands.api.util;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public interface ItemTemplate {
    ItemStack render(Locale locale);

    ItemStack render(Locale locale, TagResolver resolver);
}

package me.m56738.easyarmorstands.command;

import me.m56738.easyarmorstands.command.sender.EasPlayer;
import me.m56738.easyarmorstands.history.History;
import me.m56738.easyarmorstands.history.action.Action;
import me.m56738.easyarmorstands.message.Message;
import me.m56738.easyarmorstands.permission.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.annotation.specifier.Range;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Default;
import org.incendo.cloud.annotations.Permission;

import java.util.Iterator;

@Command("eas")
public class HistoryCommands {
    @Command("history")
    @Permission(Permissions.HISTORY)
    @CommandDescription("easyarmorstands.command.description.history")
    public void history(EasPlayer sender) {
        History history = sender.history();
        if (history.getPast().isEmpty()) {
            sender.sendMessage(Message.warning("easyarmorstands.warning.history-empty"));
            return;
        }
        sender.sendMessage(Message.title("easyarmorstands.title.history"));
        for (Iterator<Action> it = history.getPast().descendingIterator(); it.hasNext(); ) {
            Action action = it.next();
            sender.sendMessage(Component.text()
                    .content("* ")
                    .color(NamedTextColor.GRAY)
                    .append(action.describe()));
        }
    }

    @Command("redo [count]")
    @Permission(Permissions.REDO)
    @CommandDescription("easyarmorstands.command.description.redo")
    public void redo(EasPlayer sender,
                     @Range(min = "1", max = "10") @Argument(value = "count") @Default("1") int count) {
        History history = sender.history();
        for (int i = 0; i < count; i++) {
            Action action = history.takeRedoAction();
            if (action != null) {
                if (!action.execute(sender)) {
                    sender.sendMessage(Message.error("easyarmorstands.error.cannot-redo", action.describe()));
                    break;
                }
                sender.sendMessage(Message.success("easyarmorstands.success.redone-change", action.describe()));
            } else {
                sender.sendMessage(Message.error("easyarmorstands.error.nothing-to-redo"));
                break;
            }
        }
    }

    @Command("undo [count]")
    @Permission(Permissions.UNDO)
    @CommandDescription("easyarmorstands.command.description.undo")
    public void undo(EasPlayer sender,
                     @Range(min = "1", max = "10") @Argument(value = "count") @Default("1") int count) {
        History history = sender.history();
        for (int i = 0; i < count; i++) {
            Action action = history.takeUndoAction();
            if (action != null) {
                if (!action.undo(sender)) {
                    sender.sendMessage(Message.error("easyarmorstands.error.cannot-undo", action.describe()));
                    break;
                }
                sender.sendMessage(Message.success("easyarmorstands.success.undone-change", action.describe()));
            } else {
                sender.sendMessage(Message.error("easyarmorstands.error.nothing-to-undo"));
                break;
            }
        }
    }
}

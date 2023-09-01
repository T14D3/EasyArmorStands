package me.m56738.easyarmorstands.editor.node;

import me.m56738.easyarmorstands.api.editor.Session;
import me.m56738.easyarmorstands.api.editor.context.ClickContext;
import me.m56738.easyarmorstands.api.editor.context.ExitContext;
import me.m56738.easyarmorstands.api.editor.node.Node;
import me.m56738.easyarmorstands.api.editor.tool.ToolSession;
import org.jetbrains.annotations.NotNull;

public abstract class ToolNode implements Node {
    private final Session session;
    private final ToolSession toolSession;

    public ToolNode(Session session, ToolSession toolSession) {
        this.session = session;
        this.toolSession = toolSession;
    }

    @Override
    public void onExit(@NotNull ExitContext context) {
        toolSession.commit();
    }

    @Override
    public boolean onClick(@NotNull ClickContext context) {
        if (context.type() == ClickContext.Type.LEFT_CLICK) {
            toolSession.revert();
            session.popNode();
            return true;
        }
        if (context.type() == ClickContext.Type.RIGHT_CLICK) {
            session.popNode();
            return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        return toolSession.isValid();
    }
}
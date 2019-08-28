package kr.neko.sokcuri.naraechat.Keyboard;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.TickEvent;

import java.awt.*;

public interface KeyboardLayout {
    String getName();
    String getIndicatorText();
    Color getIndicatorColor();
    String getLayoutString();

    void onCharTyped(GuiScreenEvent.KeyboardCharTypedEvent.Pre event);
    void onKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event);
    void renderTick(TickEvent.RenderTickEvent event);

    void cleanUp();
}

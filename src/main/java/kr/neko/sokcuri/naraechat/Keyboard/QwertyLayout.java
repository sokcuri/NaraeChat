package kr.neko.sokcuri.naraechat.Keyboard;

import kr.neko.sokcuri.naraechat.IMEIndicator;
import kr.neko.sokcuri.naraechat.NaraeUtils;
import kr.neko.sokcuri.naraechat.Wrapper.TextComponentWrapper;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;

import java.awt.*;

public class QwertyLayout implements KeyboardLayout {
    private final String layout = "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";

    private static KeyboardLayout instance = new QwertyLayout();
    public static KeyboardLayout getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "영문 쿼티";
    }

    @Override
    public String getIndicatorText() {
        return "영문";
    }

    @Override
    public Color getIndicatorColor() {
        return Color.YELLOW;
    }

    @Override
    public String getLayoutString() {
        return layout;
    }

    @Override
    public void onCharTyped(ScreenEvent.KeyboardCharTypedEvent.Pre event) { }

    @Override
    public void onKeyPressed(ScreenEvent.KeyboardKeyPressedEvent.Pre event) { }

    @Override
    public void renderTick(TickEvent.RenderTickEvent event) {
        TextComponentWrapper comp = NaraeUtils.getTextComponent();
        if (comp == null) return;

        IMEIndicator.Instance.drawIMEIndicator(this);

        // Minecraft mc = Minecraft.getInstance();
        // mc.fontRenderer.drawString(String.format("사용중인 자판 : %s", this.getName()), 176, 166, 16479127);
    }

    @Override
    public void cleanUp() { }
}

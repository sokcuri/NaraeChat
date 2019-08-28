package kr.neko.sokcuri.naraechat.Keyboard;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.TickEvent;

import java.awt.*;

public class Hangul_Set_3_90_Layout implements KeyboardLayout {
    private final String layout = "`ㅎㅆㅂㅛㅠㅑㅖㅢㅜㅋ-=~ㅈ@#$%^&*()_+ㅅㄹㅕㅐㅓㄹㄷㅁㅊㅍ[]\\ㅍㅌㅋㅒ;<789>{}|ㅇㄴㅣㅏㅡㄴㅇㄱㅈㅂㅌㄷㄶㄺㄲ/'456:\"ㅁㄱㅔㅗㅜㅅㅎ,.ㅗㅊㅄㄻㅀ!0123?";

    @Override
    public String getName() {
        return "한글 3벌식 3-90";
    }

    @Override
    public String getLayoutString() {
        return layout;
    }

    @Override
    public String getIndicatorText() {
        return "한글";
    }

    @Override
    public Color getIndicatorColor() {
        return new Color(0x6C, 0x35, 0x8B);
    }

    @Override
    public void onCharTyped(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {

    }

    @Override
    public void onKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {

    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent event) {

    }

    @Override
    public void cleanUp() {

    }
}

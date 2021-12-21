package kr.neko.sokcuri.naraechat.Keyboard;

import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;

import java.awt.*;

public class Hangul_Set_3_91_Layout implements KeyboardLayout {
    private final String layout = "*ㅎㅆㅂㅛㅠㅑㅖㅢㅜㅋ)>※ㄲㄺㅈㄿㄾ=“”'~;+ㅅㄹㅕㅐㅓㄹㄷㅁㅊㅍ(<:ㅍㅌㄵㅀㄽ56789%/\\ㅇㄴㅣㅏㅡㄴㅇㄱㅈㅂㅌㄷㄶㄼㄻㅒ01234·ㅁㄱㅔㅗㅜㅅㅎ,.ㅗㅊㅄㅋㄳ?-\",.!";

    @Override
    public String getName() {
        return "한글 3벌식 최종";
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
        return new Color(0x26, 0x68, 0x9A);
    }

    @Override
    public void onCharTyped(ScreenEvent.KeyboardCharTypedEvent.Pre event) {

    }

    @Override
    public void onKeyPressed(ScreenEvent.KeyboardKeyPressedEvent.Pre event) {

    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent event) {

    }

    @Override
    public void cleanUp() {

    }
}

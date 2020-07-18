package kr.neko.sokcuri.naraechat.Keyboard;

import kr.neko.sokcuri.naraechat.HangulProcessor;
import kr.neko.sokcuri.naraechat.IMEIndicator;
import kr.neko.sokcuri.naraechat.NaraeUtils;
import kr.neko.sokcuri.naraechat.Obfuscated.*;
import kr.neko.sokcuri.naraechat.Wrapper.TextComponentWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextFieldWidgetWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.TickEvent;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Splitter;
import com.mojang.blaze3d.platform.GlStateManager;

import java.awt.*;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Hangul_Set_2_Layout implements KeyboardLayout {
    private final String layout = "`1234567890-=~!@#$%^&*()_+ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ[]\\ㅃㅉㄸㄲㅆㅛㅕㅑㅒㅖ{}|ㅁㄴㅇㄹㅎㅗㅓㅏㅣ;'ㅁㄴㅇㄹㅎㅗㅓㅏㅣ:\"ㅋㅌㅊㅍㅠㅜㅡ,./ㅋㅌㅊㅍㅠㅜㅡ<>?";
    private final String chosung_table = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
    private final String jungsung_table = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
    private final String jongsung_table = "\u0000ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ";
    private final List<String> jungsung_ref_table = Splitter.on(",").splitToList(",,,,,,,,,ㅗㅏ,ㅗㅐ,ㅗㅣ,,,ㅜㅓ,ㅜㅔ,ㅜㅣ,,,ㅡㅣ,ㅣ");
    private final List<String> jongsung_ref_table = Splitter.on(",").splitToList(",,,ㄱㅅ,,ㄴㅈ,ㄴㅎ,,,ㄹㄱ,ㄹㅁ,ㄹㅂ,ㄹㅅ,ㄹㅌ,ㄹㅍ,ㄹㅎ,,,ㅂㅅ,,,,,,,,,");
    private int assemblePosition = -1;

    private static KeyboardLayout instance = new Hangul_Set_2_Layout();
    public static KeyboardLayout getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "한글 2벌식";
    }

    @Override
    public String getIndicatorText() {
        return "한글";
    }

    @Override
    public Color getIndicatorColor() {
        return new Color(0xFF, 0x7F, 0x00);
    }

    @Override
    public String getLayoutString() {
        return layout;
    }

    private int getQwertyIndexCodePoint(char ch) {
        return QwertyLayout.getInstance().getLayoutString().indexOf(ch);
    }

    boolean onBackspaceKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        TextComponentWrapper comp = NaraeUtils.getTextComponent();
        if (comp == null) return false;

        int cursorPosition = comp.getCursorPosition();
        if (cursorPosition == 0 || cursorPosition != assemblePosition) return false;

        String text = comp.getText();

        char ch = text.toCharArray()[cursorPosition - 1];

        if (HangulProcessor.isHangulSyllables(ch)) {
            int code = ch - 0xAC00;
            int cho = code / (21 * 28);
            int jung = (code % (21 * 28)) / 28;
            int jong = (code % (21 * 28)) % 28;

            if (jong != 0) {
                char[] ch_arr = jongsung_ref_table.get(jong).toCharArray();
                if (ch_arr.length == 2) {
                    jong = jongsung_table.indexOf(ch_arr[0]);
                } else {
                    jong = 0;
                }
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                comp.modifyText(c);
                return true;
            } else {
                char[] ch_arr = jungsung_ref_table.get(jung).toCharArray();
                if (ch_arr.length == 2) {
                    jung = jungsung_table.indexOf(ch_arr[0]);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    comp.modifyText(c);
                    return true;
                } else {
                    char c = chosung_table.charAt(cho);
                    comp.modifyText(c);
                    return true;
                }
            }
        } else if (HangulProcessor.isHangulCharacter(ch)) {
            assemblePosition = -1;
            return false;
        }
        return false;
    }

    boolean onHangulCharTyped(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
        TextComponentWrapper comp = NaraeUtils.getTextComponent();
        if (comp == null) return false;

        int codePoint = event.getCodePoint();

        int idx = QwertyLayout.getInstance().getLayoutString().indexOf(codePoint);
        // System.out.println(String.format("idx: %d", idx));
        if (idx == -1) {
            assemblePosition = -1;
            return false;
        }

        int cursorPosition = comp.getCursorPosition();
        String text = comp.getText();

        char prev = text.toCharArray()[cursorPosition - 1];
        char curr = layout.toCharArray()[idx];

        if (cursorPosition == 0) {
            if (!HangulProcessor.isHangulCharacter(curr)) return false;

            comp.writeText(String.valueOf(curr));
            assemblePosition = comp.getCursorPosition();
        }
        else if (cursorPosition == assemblePosition) {

            // 자음 + 모음
            if (HangulProcessor.isJaeum(prev) && HangulProcessor.isMoeum(curr)) {
                int cho = chosung_table.indexOf(prev);
                int jung = jungsung_table.indexOf(curr);
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                comp.modifyText(c);
                assemblePosition = comp.getCursorPosition();
                return true;
            }

            if (HangulProcessor.isHangulSyllables(prev)) {
                int code = prev - 0xAC00;
                int cho = code / (21 * 28);
                int jung = (code % (21 * 28)) / 28;
                int jong = (code % (21 * 28)) % 28;

                // 중성 합성 (ㅘ, ㅙ)..
                if (jong == 0 && HangulProcessor.isJungsung(prev, curr)) {
                    jung = HangulProcessor.getJungsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    comp.modifyText(c);
                    assemblePosition = comp.getCursorPosition();
                    return true;
                }

                // 종성 추가
                if (jong == 0 && HangulProcessor.isJongsung(curr)) {
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, HangulProcessor.getJongsung(curr));
                    comp.modifyText(c);
                    assemblePosition = comp.getCursorPosition();
                    return true;
                }

                // 종성 받침 추가
                if (jong != 0 && HangulProcessor.isJongsung(prev, curr)) {
                    jong = HangulProcessor.getJongsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    comp.modifyText(c);
                    assemblePosition = comp.getCursorPosition();
                    return true;
                }

                // 종성에서 받침 하나 빼고 글자 만들기
                if (jong != 0 && HangulProcessor.isJungsung(curr)) {
                    char[] tbl = jongsung_ref_table.get(jong).toCharArray();
                    int newCho = 0;
                    if (tbl.length == 2) {
                        newCho = chosung_table.indexOf(tbl[1]);
                        jong = jongsung_table.indexOf(tbl[0]);
                    } else {
                        newCho = chosung_table.indexOf(jongsung_table.charAt(jong));
                        jong = 0;
                    }

                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    comp.modifyText(c);

                    cho = newCho;
                    jung = jungsung_table.indexOf(curr);
                    code = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    comp.writeText(String.valueOf(Character.toChars(code)));
                    assemblePosition = comp.getCursorPosition();
                    return true;
                }
            }
        }

        comp.writeText(String.valueOf(curr));
        assemblePosition = comp.getCursorPosition();
        return true;
    }

    public void typedTextField(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
        TextComponentWrapper comp = NaraeUtils.getTextComponent();
        if (comp == null) return;

        char qwertyChar = event.getCodePoint();
        int qwertyIndex = getQwertyIndexCodePoint(qwertyChar);
        if (qwertyIndex == -1) {
            assemblePosition = -1;
            return;
        }

        event.setCanceled(true);

        char curr = layout.toCharArray()[qwertyIndex];
        int cursorPosition = comp.getCursorPosition();

        if (cursorPosition == 0 || !HangulProcessor.isHangulCharacter(curr) || !onHangulCharTyped(event)) {
            comp.writeText(String.valueOf(curr));
            assemblePosition = HangulProcessor.isHangulCharacter((curr)) ? comp.getCursorPosition() : -1;
        }
    }

    public void typedTextInput(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
    }

    @Override
    public void onCharTyped(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
        typedTextField(event);
        typedTextInput(event);
    }

    @Override
    public void onKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        boolean isCanceled = false;

        TextComponentWrapper comp = NaraeUtils.getTextComponent();
        if (comp == null) return;

        switch(event.getKeyCode()) {
            case GLFW_KEY_BACKSPACE:
                isCanceled = onBackspaceKeyPressed(event);
                break;
        }

        event.setCanceled(isCanceled);
    }


    void drawAssembleCharBox(int startX, int startY, int endX, int endY, int x, int width) {
        if (startX < endX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY) {
            int j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > x + width) {
            endX = x + width;
        }

        if (startX > x + width) {
            startX = x + width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.polygonMode(GL_FRONT_AND_BACK, GL_FILL);
        GlStateManager.color4f(255.0f, 255.0f, 255.0f, 0.3f);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.disableTexture();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(startX, endY, 0.0D).endVertex();
        bufferbuilder.pos(endX, endY, 0.0D).endVertex();
        bufferbuilder.pos(endX, startY, 0.0D).endVertex();
        bufferbuilder.pos(startX, startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            renderEndPhaseTick(event);
        }
    }

    @Override
    public void cleanUp() {
        assemblePosition = -1;
    }

    private void drawCharAssembleBox(TextComponentWrapper comp) {
        if (!(comp instanceof TextFieldWidgetWrapper)) {
            return;
        }

        TextFieldWidgetWrapper wrapper = (TextFieldWidgetWrapper)comp;
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

        boolean enableBackgroundDrawing = wrapper.getEnableBackgroundDrawing();
        boolean isEnabled = wrapper.isEnabled();
        int adjustedWidth = wrapper.getAdjustedWidth();
        int cursorPosition = wrapper.getCursorPosition();
        int lineScrollOffset = wrapper.getLineScrollOffset();
        int selectionEnd = wrapper.getSelectionEnd();

        int width = wrapper.getWidth();
        int height = wrapper.getHeight();

        String trimStr = wrapper.getText().substring(lineScrollOffset);

        int x = enableBackgroundDrawing ? wrapper.getX() + 4 : wrapper.getX();
        int y = enableBackgroundDrawing ? wrapper.getY() + (height - 8) / 2 : wrapper.getY();
        int specifiedOffset = selectionEnd - lineScrollOffset;

        if (!isEnabled || !wrapper.isFocused() || assemblePosition != cursorPosition) {
            assemblePosition = -1;
        }

        if (assemblePosition == -1) return;
        if (trimStr.isEmpty()) return;
        if (cursorPosition == 0) return;
        if (trimStr.length() == 0) return;
        if (specifiedOffset == 0 || specifiedOffset - 1 >= trimStr.length()) return;
        int startX = x + ObfuscatedMethod.$FontRenderer.getStringWidth.invoke(fontRenderer, trimStr.substring(0, specifiedOffset - 1));
        int startY = y - 1;
        int endX = x + ObfuscatedMethod.$FontRenderer.getStringWidth.invoke(fontRenderer, trimStr.substring(0, specifiedOffset)) - 1;
        int endY = y + 1 + 9;
        drawAssembleCharBox(startX, startY, endX, endY, x, width);
    }

    private void renderEndPhaseTick(TickEvent.RenderTickEvent event) {
        TextComponentWrapper comp = NaraeUtils.getTextComponent();
        if (comp == null) return;

        drawCharAssembleBox(comp);
        IMEIndicator.Instance.drawIMEIndicator(this);
    }
}

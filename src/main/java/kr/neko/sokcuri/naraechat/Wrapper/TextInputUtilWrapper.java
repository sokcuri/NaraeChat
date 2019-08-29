package kr.neko.sokcuri.naraechat.Wrapper;

import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.TextInputUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextInputUtilWrapper implements TextComponentWrapper {
    private final TextInputUtil base;

    public TextInputUtilWrapper(TextInputUtil inputUtil) {
        this.base = inputUtil;
    }

    public Minecraft getMinecraftInstance() {
        return ObfuscatedField.$TextInputUtil.minecraft.get(base);
    }

    public FontRenderer getFontRenderer() {
        return ObfuscatedField.$TextInputUtil.fontRenderer.get(base);
    }

    public Supplier<String> getSupplier() {
        return ObfuscatedField.$TextInputUtil.textSupplier.get(base);
    }

    public Consumer<String> getConsumer() {
        return ObfuscatedField.$TextInputUtil.textConsumer.get(base);
    }

    public int getMaxStringLength() {
        return ObfuscatedField.$TextInputUtil.maxStringLength.get(base);
    }

    @Override
    public Object getBaseComponent() {
        return base;
    }

    @Override
    public int getCursorPosition() {
        return ObfuscatedField.$TextInputUtil.cursorPosition.get(base);
    }

    public void setCursorPosition(int pos) {
        ObfuscatedField.$TextInputUtil.cursorPosition.set(base, pos);
    }

    @Override
    public void deleteFromCursor(int num) {
        String text = getText();
        if (!text.isEmpty()) {
            int cursorPosition = getCursorPosition();
            boolean flag = num < 0;
            int i = flag ? cursorPosition + num : cursorPosition;
            int j = flag ? cursorPosition : cursorPosition + num;
            String s = "";
            if (i >= 0) {
                s = text.substring(0, i);
            }

            if (j < text.length()) {
                s = s + text.substring(j);
            }

            setText(s);
        }
    }

    private int getCursorPosition2() {
        return ObfuscatedField.$TextInputUtil.cursorPosition2.get(base);
    }

    private void setCursorPosition2(int pos) {
        ObfuscatedField.$TextInputUtil.cursorPosition2.set(base, pos);
    }

    @Override
    public String getText() {
        return this.getSupplier().get();
    }

    @Override
    public boolean setText(String str) {
        if (getFontRenderer().getStringWidth(str) <= getMaxStringLength()) {
            getConsumer().accept(str);
            return true;
        }
        return false;
    }

    @Override
    public void writeText(String str) {
        int cursorPosition = getCursorPosition();
        String s = getText();
        String res;
        if (cursorPosition > 0) {
            res = s.substring(0, cursorPosition) + str + s.substring(cursorPosition);
        } else {
            res = str + s;
        }
        if (setText(res) && getText().length() == res.length()) {
            setCursorPosition(cursorPosition + 1);
            setCursorPosition2(cursorPosition + 1);
        }
    }

    @Override
    public void modifyText(char ch) {
        int cursorPosition = getCursorPosition();
        char arr[] = getText().toCharArray();
        if (cursorPosition > 0 && cursorPosition <= arr.length) {
            arr[cursorPosition - 1] = ch;
            setText(String.valueOf(arr));
        }
    }
}


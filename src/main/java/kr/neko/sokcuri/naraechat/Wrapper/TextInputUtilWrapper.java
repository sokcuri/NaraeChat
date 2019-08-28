package kr.neko.sokcuri.naraechat.Wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextInputUtilWrapper implements TextComponentWrapper {
    private final TextInputUtil base;
    private static ArrayList<String> clsFieldNames = new ArrayList<>();
    private static boolean isInitialized = false;

    public TextInputUtilWrapper(TextInputUtil inputUtil) {
        this.base = inputUtil;
        if (!isInitialized) {
            for (Field field : inputUtil.getClass().getDeclaredFields()) {
                clsFieldNames.add(field.getName());
            }
            if (clsFieldNames.size() != 7) {
                System.out.println("clsFieldNames size is not equals 7");
            }
        }
        isInitialized = true;
    }

    public Minecraft getMinecraftInstance() {
        return (Minecraft) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(0));
    }

    public FontRenderer getFontRenderer() {
        return (FontRenderer) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(1));
    }

    public Supplier<String> getSupplier() {
        return (Supplier<String>) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(2));
    }

    public Consumer<String> getConsumer() {
        return (Consumer<String>) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(3));
    }

    public int getTextMaxLength() {
        return (int) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(4));
    }

    @Override
    public Object getBaseComponent() {
        return base;
    }

    @Override
    public int getCursorPosition() {
        return (int) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(5));
    }

    public void setCursorPosition(int pos) {
        ObfuscationReflectionHelper.setPrivateValue(TextInputUtil.class, base, pos, clsFieldNames.get(5));
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
        return (int) ObfuscationReflectionHelper.getPrivateValue(TextInputUtil.class, base, clsFieldNames.get(6));
    }

    private void setCursorPosition2(int n) {
        ObfuscationReflectionHelper.setPrivateValue(TextInputUtil.class, base, n, clsFieldNames.get(6));
    }

    @Override
    public String getText() {
        return this.getSupplier().get();
    }

    @Override
    public boolean setText(String str) {
        if (getFontRenderer().getStringWidth(str) <= getTextMaxLength()) {
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
        if (setText(res)) {
            setCursorPosition(cursorPosition + 1);
            setCursorPosition2(cursorPosition + 1);
        }
    }

    @Override
    public void modifyText(String str) {
        int cursorPosition = getCursorPosition();
        setCursorPosition(cursorPosition - 1);
        deleteFromCursor(1);
        writeText(str);
    }

    @Override
    public void modifyText(char ch) {
        modifyText(String.valueOf(Character.toChars(ch)));
    }
}


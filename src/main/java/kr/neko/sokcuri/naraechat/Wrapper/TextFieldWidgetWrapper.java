package kr.neko.sokcuri.naraechat.Wrapper;

import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedField;
import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

import java.util.function.Consumer;

public class TextFieldWidgetWrapper implements TextComponentWrapper {
    private final EditBox base;

    public TextFieldWidgetWrapper(EditBox widget) {
        this.base = widget;
    }

    private void sendTextChanged(String str) {
        if (getGuiResponder() != null) {
            getGuiResponder().accept(str);
        }
    }

    private void updateScreen() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) return;

        if (mc.screen instanceof CreativeModeInventoryScreen) {
            CreativeModeInventoryScreen creativeScreen = (CreativeModeInventoryScreen)mc.screen;
            ObfuscatedMethod.$CreativeScreen.updateCreativeSearch.invoke(creativeScreen);
        }
    }

    public int getSelectionEnd() {
        return ObfuscatedField.$TextFieldWidget.selectionEnd.get(base);
    }

    public int getLineScrollOffset() {
        return ObfuscatedField.$TextFieldWidget.lineScrollOffset.get(base);
    }

    public boolean isEnabled() {
        return ObfuscatedField.$TextFieldWidget.isEnabled.get(base);
    }

    public boolean getEnableBackgroundDrawing() {
        return ObfuscatedField.$TextFieldWidget.enableBackgroundDrawing.get(base);
    }

    public Consumer<String> getGuiResponder() {
        return ObfuscatedField.$TextFieldWidget.guiResponder.get(base);
    }

    public int getWidth() {
        return base.getWidth();
    }

    public boolean isFocused() {
        return base.isFocused();
    }

    public int getX() {
        return base.x;
    }

    public int getY() {
        return base.y;
    }

    public int getHeight() {
        return base.getHeight();
    }

    public int getAdjustedWidth() {
        return base.getInnerWidth();
    }

    public int getCursorPosition() {
        return base.getCursorPosition();
    }

    @Override
    public void setCursorPosition(int pos) {
        base.setCursorPosition(pos);
    }

    public int getMaxStringLength() {
        return ObfuscatedField.$TextFieldWidget.maxStringLength.get(base);
    }

    public int getNthWordFromCursor(int numWords) {
        return base.getWordPosition(numWords);
    }

    public String getSelectedText() {
        return base.getHighlighted();
    }

    public boolean getCanLoseFocus() {
        return ObfuscatedField.$TextFieldWidget.canLoseFocus.get(base);
    }
    public void setCanLoseFocus(boolean b) {
        base.setCanLoseFocus(b);
    }

    public String getText() {
        return base.getValue();
    }

    public boolean setText(String str) {
        base.setValue(str);
        sendTextChanged(str);
        updateScreen();
        return true;
    }

    public void deleteFromCursor(int num) {
        base.deleteChars(num);
    }

    public boolean getVisible() {
        return base.isVisible();
    }

    @Override
    public Object getBaseComponent() {
        return base;
    }

    @Override
    public void writeText(String str) {
        base.insertText(str);
        sendTextChanged(str);
        updateScreen();
    }

    @Override
    public void modifyText(char ch) {
        int cursorPosition = getCursorPosition();
        setCursorPosition(cursorPosition - 1);
        deleteFromCursor(1);
        writeText(String.valueOf(Character.toChars(ch)));
    }
}

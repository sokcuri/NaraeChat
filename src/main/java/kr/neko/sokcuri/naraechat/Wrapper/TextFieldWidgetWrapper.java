package kr.neko.sokcuri.naraechat.Wrapper;

import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedField;
import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.function.Consumer;

public class TextFieldWidgetWrapper implements TextComponentWrapper {
    private final TextFieldWidget base;

    public TextFieldWidgetWrapper(TextFieldWidget widget) {
        this.base = widget;
    }

    private void sendTextChanged(String str) {
        if (getGuiResponder() != null) {
            getGuiResponder().accept(str);
        }
    }

    private void updateScreen() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.currentScreen == null) return;

        if (mc.currentScreen instanceof CreativeScreen) {
            CreativeScreen creativeScreen = (CreativeScreen)mc.currentScreen;
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
        return base.getHeightRealms();
    }

    public int getAdjustedWidth() {
        return base.getAdjustedWidth();
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
        return base.getNthWordFromCursor(numWords);
    }

    public String getSelectedText() {
        return base.getSelectedText();
    }

    public boolean getCanLoseFocus() {
        return ObfuscatedField.$TextFieldWidget.canLoseFocus.get(base);
    }
    public void setCanLoseFocus(boolean b) {
        base.setCanLoseFocus(b);
    }

    public String getText() {
        return base.getText();
    }

    public boolean setText(String str) {
        base.setText(str);
        sendTextChanged(str);
        updateScreen();
        return true;
    }

    public void deleteFromCursor(int num) {
        base.deleteFromCursor(num);
    }

    public boolean getVisible() {
        return base.getVisible();
    }

    @Override
    public Object getBaseComponent() {
        return base;
    }

    @Override
    public void writeText(String str) {
        base.writeText(str);
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

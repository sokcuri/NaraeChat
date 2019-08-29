package kr.neko.sokcuri.naraechat;

import kr.neko.sokcuri.naraechat.Obfuscated.ReflectionFieldInfo;
import kr.neko.sokcuri.naraechat.Obfuscated.ReflectionFieldMap;
import kr.neko.sokcuri.naraechat.Wrapper.TextComponentWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextFieldWidgetWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextInputUtilWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaraeUtils {

    private static ReflectionFieldMap<TextFieldWidget> textFieldWidgetRefMap = new ReflectionFieldMap(TextFieldWidget.class);
    private static ReflectionFieldMap<TextInputUtil> textInputUtilRefMap = new ReflectionFieldMap(TextInputUtil.class);

    public static TextComponentWrapper getTextComponent() {
        TextFieldWidget widget = getWidget();
        TextInputUtil inputUtil = getTextInput();

        if (widget != null) {
            return new TextFieldWidgetWrapper(widget);
        }

        if (inputUtil != null) {
            return new TextInputUtilWrapper(inputUtil);
        }
        return null;
    }

    private static TextInputUtil getTextInput() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.currentScreen == null) return null;

        return textInputUtilRefMap.findField(mc.currentScreen);
    }

    private static TextFieldWidget getWidget() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.currentScreen == null) return null;

        return textFieldWidgetRefMap.findField(mc.currentScreen);
    }
}

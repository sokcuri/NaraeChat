package kr.neko.sokcuri.naraechat;

import kr.neko.sokcuri.naraechat.Obfuscated.ReflectionFieldInfo;
import kr.neko.sokcuri.naraechat.Obfuscated.ReflectionFieldMap;
import kr.neko.sokcuri.naraechat.Wrapper.TextComponentWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextFieldWidgetWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextInputUtilWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaraeUtils {

    private static ReflectionFieldMap<EditBox> textFieldWidgetRefMap = new ReflectionFieldMap(EditBox.class);
    private static ReflectionFieldMap<TextFieldHelper> textInputUtilRefMap = new ReflectionFieldMap(TextFieldHelper.class);

    public static TextComponentWrapper getTextComponent() {
        EditBox widget = getWidget();
        TextFieldHelper inputUtil = getTextInput();

        if (widget != null) {
            return new TextFieldWidgetWrapper(widget);
        }

        if (inputUtil != null) {
            return new TextInputUtilWrapper(inputUtil);
        }
        return null;
    }

    private static TextFieldHelper getTextInput() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) return null;

        return textInputUtilRefMap.findField(mc.screen);
    }

    private static EditBox getWidget() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) return null;

        return textFieldWidgetRefMap.findField(mc.screen);
    }
}

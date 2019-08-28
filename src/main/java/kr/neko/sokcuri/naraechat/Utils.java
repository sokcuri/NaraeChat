package kr.neko.sokcuri.naraechat;

import kr.neko.sokcuri.naraechat.Wrapper.TextComponentWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextFieldWidgetWrapper;
import kr.neko.sokcuri.naraechat.Wrapper.TextInputUtilWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
    public static HashMap<String, ArrayList<String>> inputUtilFieldMap = new HashMap<>();
    public static HashMap<String, ArrayList<String>> textFieldWidgetFieldMap = new HashMap<>();

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

        Object o = mc.currentScreen;
        String className = o.getClass().getName();

        if (!inputUtilFieldMap.containsKey(o.getClass().getName())) {
            ArrayList<String> textInputUtilFields = new ArrayList<>();
            for (Field field : o.getClass().getDeclaredFields()) {
                if (field.getType().getName() == "net.minecraft.client.gui.fonts.TextInputUtil") {
                    textInputUtilFields.add(field.getName());
                }
            }
            inputUtilFieldMap.put(className, textInputUtilFields);
        }

        if (inputUtilFieldMap.containsKey(className)) {
            for (String fieldName : inputUtilFieldMap.get(className)) {
                TextInputUtil inputUtil = (TextInputUtil) ObfuscationReflectionHelper.getPrivateValue((Class)o.getClass(), mc.currentScreen, fieldName);
                return inputUtil;
            }
        }
        return null;
    }

    private static TextFieldWidget getWidget() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.currentScreen == null) return null;

        Object o = mc.currentScreen;
        String className = o.getClass().getName();

        if (!textFieldWidgetFieldMap.containsKey(o.getClass().getName())) {
            ArrayList<String> textFieldWidgetFields = new ArrayList<>();
            for (Field field : o.getClass().getDeclaredFields()) {
                if (field.getType().getName() == "net.minecraft.client.gui.widget.TextFieldWidget") {
                    textFieldWidgetFields.add(field.getName());
                }
            }
            textFieldWidgetFieldMap.put(className, textFieldWidgetFields);
        }

        if (textFieldWidgetFieldMap.containsKey(className)) {
            for (String fieldName : textFieldWidgetFieldMap.get(className)) {
                TextFieldWidget widget = (TextFieldWidget) ObfuscationReflectionHelper.getPrivateValue((Class)o.getClass(), mc.currentScreen, fieldName);
                if (widget.isFocused()) {
                    return widget;
                }
            }
        }
        return null;
//
//
//
//
//        IGuiEventListener focusedScreen = mc.currentScreen.getFocused();
//        if (focusedScreen != null) {
//            if (focusedScreen.getClass().getName() == "net.minecraft.client.gui.widget.TextFieldWidget") {
//                return (TextFieldWidget) focusedScreen;
//            }
//        }
//
//        Object o = mc.currentScreen;
//        for (Field field : o.getClass().getDeclaredFields()) {
//            if (field.getType().getName() == "net.minecraft.client.gui.widget.TextFieldWidget") {
//
//                TextFieldWidget widget = null;
//                try {
//                    widget = (TextFieldWidget) FieldUtils.readField(o, field.getName(), true);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//                if (widget != null) {
//                    TextFieldWidgetWrapper wrapper = new TextFieldWidgetWrapper(widget);
//                    if (wrapper.getCanLoseFocus() != false) {
//                        wrapper.setFocused(true);
//                    }
//                }
//                break;
//            }
//        }
//
//        return null;
    }
}

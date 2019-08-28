package kr.neko.sokcuri.naraechat;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.function.Consumer;

public final class ObfuscatedField<O, T> {

    private static boolean isDeobf = false;

    private final String obfName;
    private final String deobfName;
    private final Class<O> owner;
    private final Class<T> retClass;

    public static class $TextFieldWidget {
        public static final ObfuscatedField<TextFieldWidget, Boolean> canLoseFocus;
        public static final ObfuscatedField<TextFieldWidget, Boolean> enableBackgroundDrawing;
        public static final ObfuscatedField<TextFieldWidget, Consumer<String>> guiResponder;
        public static final ObfuscatedField<TextFieldWidget, Boolean> isEnabled;
        public static final ObfuscatedField<TextFieldWidget, Integer> lineScrollOffset;
        public static final ObfuscatedField<TextFieldWidget, Integer> maxStringLength;
        public static final ObfuscatedField<TextFieldWidget, Integer> selectionEnd;

        static {
            canLoseFocus                = new ObfuscatedField("canLoseFocus", "field_146212_n", TextFieldWidget.class, boolean.class);
            enableBackgroundDrawing     = new ObfuscatedField("enableBackgroundDrawing", "field_146215_m", TextFieldWidget.class, boolean.class);
            guiResponder                = new ObfuscatedField("guiResponder", "field_175210_x", TextFieldWidget.class, Consumer.class);
            isEnabled                   = new ObfuscatedField("isEnabled", "field_146226_p", TextFieldWidget.class, boolean.class);
            lineScrollOffset            = new ObfuscatedField("lineScrollOffset", "field_146225_q", TextFieldWidget.class, int.class);
            maxStringLength             = new ObfuscatedField("maxStringLength", "field_146217_k", TextFieldWidget.class, int.class);
            selectionEnd                = new ObfuscatedField("selectionEnd", "field_146223_s", TextFieldWidget.class, int.class);
        }
    }

    public ObfuscatedField(String deobfName, String obfName, Class<O> owner, Class<T> retClass) {
        this.obfName = obfName;
        this.deobfName = deobfName;
        this.owner = owner;
        this.retClass = retClass;
    }

    public String getDeobfName() {
        return this.deobfName;
    }

    public String getObjName() {
        return this.obfName;
    }

    public Class<O> getOwnerClass() {
        return this.owner;
    }

    public Class<T> getTypeClass() {
        return this.retClass;
    }

    public T get(O obj) {
        return (T)ObfuscationReflectionHelper.getPrivateValue((Class<O>)obj.getClass(), obj, isDeobf ? deobfName : obfName);
    }

    public void set(O obj, T value) {
        ObfuscationReflectionHelper.setPrivateValue((Class<O>)obj.getClass(), obj, value, isDeobf ? deobfName : obfName);
    }
}

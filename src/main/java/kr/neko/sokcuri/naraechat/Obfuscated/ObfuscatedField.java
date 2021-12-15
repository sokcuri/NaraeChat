package kr.neko.sokcuri.naraechat.Obfuscated;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    public static class $TextInputUtil {
        public static final ObfuscatedField<TextInputUtil, Supplier<String>> textSupplier;
        public static final ObfuscatedField<TextInputUtil, Consumer<String>> textConsumer;
        public static final ObfuscatedField<TextInputUtil, Predicate<String>> textPredicate;
        public static final ObfuscatedField<TextInputUtil, Integer> maxStringLength;
        public static final ObfuscatedField<TextInputUtil, Integer> cursorPosition;
        public static final ObfuscatedField<TextInputUtil, Integer> cursorPosition2;

        static {
            textSupplier                = new ObfuscatedField("textSupplier", "field_216902_c", TextInputUtil.class, Supplier.class);
            textConsumer                = new ObfuscatedField("textConsumer", "field_216903_d", TextInputUtil.class, Consumer.class);
            textPredicate               = new ObfuscatedField("textPredicate", "field_238566_e_", TextInputUtil.class, Consumer.class);
            maxStringLength             = new ObfuscatedField("field_216906_g", "field_216906_g", TextInputUtil.class, int.class);
            cursorPosition              = new ObfuscatedField("endIndex", "field_216905_f", TextInputUtil.class, int.class);
            cursorPosition2             = new ObfuscatedField("startIndex", "field_216906_g", TextInputUtil.class, int.class);
        }
    }

    public static class $Widget {
        public static final ObfuscatedField<Widget, Boolean> focused;

        static {
            focused                     = new ObfuscatedField("focused", "field_230686_c_", Widget.class, boolean.class);
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
        return (T)ObfuscationReflectionHelper.getPrivateValue(owner, obj, isDeobf ? deobfName : obfName);
    }

    public void set(O obj, T value) {
        ObfuscationReflectionHelper.setPrivateValue(owner, obj, value, isDeobf ? deobfName : obfName);
    }
}

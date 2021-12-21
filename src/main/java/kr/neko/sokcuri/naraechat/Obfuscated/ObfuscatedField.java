package kr.neko.sokcuri.naraechat.Obfuscated;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

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
        public static final ObfuscatedField<EditBox, Boolean> canLoseFocus;
        public static final ObfuscatedField<EditBox, Boolean> enableBackgroundDrawing;
        public static final ObfuscatedField<EditBox, Consumer<String>> guiResponder;
        public static final ObfuscatedField<EditBox, Boolean> isEnabled;
        public static final ObfuscatedField<EditBox, Integer> lineScrollOffset;
        public static final ObfuscatedField<EditBox, Integer> maxStringLength;
        public static final ObfuscatedField<EditBox, Integer> selectionEnd;

        static {
            canLoseFocus                = new ObfuscatedField("canLoseFocus", "f_94097_", EditBox.class, boolean.class);
            enableBackgroundDrawing     = new ObfuscatedField("bordered", "f_94096_", EditBox.class, boolean.class);
            guiResponder                = new ObfuscatedField("responder", "f_94089_", EditBox.class, Consumer.class);
            isEnabled                   = new ObfuscatedField("isEditable", "f_94098_", EditBox.class, boolean.class);
            lineScrollOffset            = new ObfuscatedField("displayPos", "f_94100_", EditBox.class, int.class);
            maxStringLength             = new ObfuscatedField("maxLength", "f_94094_", EditBox.class, int.class);
            selectionEnd                = new ObfuscatedField("highlightPos", "f_94102_", EditBox.class, int.class);
        }
    }

    public static class $TextInputUtil {
        public static final ObfuscatedField<TextFieldHelper, Supplier<String>> textSupplier;
        public static final ObfuscatedField<TextFieldHelper, Consumer<String>> textConsumer;
        public static final ObfuscatedField<TextFieldHelper, Predicate<String>> textPredicate;
        public static final ObfuscatedField<TextFieldHelper, Integer> maxStringLength;
        public static final ObfuscatedField<TextFieldHelper, Integer> cursorPosition;
        public static final ObfuscatedField<TextFieldHelper, Integer> cursorPosition2;

        static {
            //
            textSupplier                = new ObfuscatedField("getMessageFn", "f_95129_", TextFieldHelper.class, Supplier.class);
            textConsumer                = new ObfuscatedField("setMessageFn", "f_95130_", TextFieldHelper.class, Consumer.class);
            textPredicate               = new ObfuscatedField("stringValidator", "f_95133_", TextFieldHelper.class, Predicate.class);
            maxStringLength             = new ObfuscatedField("field_216906_g", "field_216906_g", TextFieldHelper.class, int.class);
            cursorPosition              = new ObfuscatedField("cursorPos", "f_95134_", TextFieldHelper.class, int.class);
            cursorPosition2             = new ObfuscatedField("selectionPos", "f_95135_", TextFieldHelper.class, int.class);
        }
    }

    public static class $Widget {
        public static final ObfuscatedField<AbstractWidget, Boolean> focused;

        static {
            focused                     = new ObfuscatedField("focused", "f_93616_", AbstractWidget.class, boolean.class);
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
        return (T) ObfuscationReflectionHelper.getPrivateValue(owner, obj, isDeobf ? deobfName : obfName);
    }

    public void set(O obj, T value) {
        ObfuscationReflectionHelper.setPrivateValue(owner, obj, value, isDeobf ? deobfName : obfName);
    }
}

package kr.neko.sokcuri.naraechat.Obfuscated;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public final class ObfuscatedMethod<O, R> {

    private static boolean isDeobf = false;

    private final String deobfName;
    private final String obfName;
    private final Class<O> owner;
    private final Class<R> retClass;
    private final Class<?>[] parameters;

    public static class $CreativeScreen {
        public static final ObfuscatedMethod<CreativeScreen, Void> updateCreativeSearch;

        static {
            updateCreativeSearch        = new ObfuscatedMethod("updateCreativeSearch", "func_147053_i", CreativeScreen.class, void.class);
        }
    }

    public static class $Widget {
        public static final ObfuscatedMethod<Widget, Integer> getWidth;
        public static final ObfuscatedMethod<Widget, Boolean> isFocused;

        static {
            getWidth        = new ObfuscatedMethod("getWidth", "func_230998_h_", Widget.class, Integer.class);
            isFocused       = new ObfuscatedMethod("isFocused", "func_230999_j_", Widget.class, Boolean.class);
        }
    }

    public static class $FontRenderer {
        public static final ObfuscatedMethod<FontRenderer, Integer> getStringWidth;

        static {
            getStringWidth        = new ObfuscatedMethod("getStringWidth", "func_78256_a", FontRenderer.class, Integer.class, new Class<?>[] { String.class });
        }
    }

    public ObfuscatedMethod(String deobfName, String obfName, Class<O> owner, Class<R> retClass, Class<?>... parameters) {
        this.deobfName = deobfName;
        this.obfName = obfName;
        this.owner = owner;
        this.retClass = retClass;
        this.parameters = parameters;
    }

    public String getDeobfName() {
        return this.deobfName;
    }

    public String getObfName() {
        return this.obfName;
    }

    public Class<O> getOwnerClass() {
        return this.owner;
    }

    public Class<R> getReturnClass() {
        return this.retClass;
    }

    public Class<?>[] getParameters() {
        return this.parameters;
    }

    public R invoke(O obj, Object... args) {
        try {
            return (R)ObfuscationReflectionHelper.findMethod(owner, isDeobf ? deobfName : obfName, parameters).invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

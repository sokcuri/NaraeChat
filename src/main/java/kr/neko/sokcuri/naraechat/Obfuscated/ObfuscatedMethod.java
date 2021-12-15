package kr.neko.sokcuri.naraechat.Obfuscated;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

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
        public static final ObfuscatedMethod<CreativeModeInventoryScreen, Void> updateCreativeSearch;

        static {
            updateCreativeSearch        = new ObfuscatedMethod("updateCreativeSearch", "func_147053_i", CreativeModeInventoryScreen.class, void.class);
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
            return (R) ObfuscationReflectionHelper.findMethod(owner, isDeobf ? deobfName : obfName, parameters).invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

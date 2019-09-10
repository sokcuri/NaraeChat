package kr.neko.sokcuri.naraechat.Obfuscated;

import com.sun.javafx.font.FontResource;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
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

    public static class $FontResourceManager {
        public static final ObfuscatedMethod<FontResourceManager, Void> apply;
        static {
            apply                       = new ObfuscatedMethod("apply", "func_212853_a_", FontResourceManager.class, void.class, Map.class, IResourceManager.class, IProfiler.class);
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
            return (R)ObfuscationReflectionHelper.findMethod(obj.getClass(), isDeobf ? deobfName : obfName).invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package kr.neko.sokcuri.naraechat.Obfuscated;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
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
        public static final ObfuscatedField<TextInputUtil, Minecraft> minecraft;
        public static final ObfuscatedField<TextInputUtil, FontRenderer> fontRenderer;
        public static final ObfuscatedField<TextInputUtil, Supplier<String>> textSupplier;
        public static final ObfuscatedField<TextInputUtil, Consumer<String>> textConsumer;
        public static final ObfuscatedField<TextInputUtil, Integer> maxStringLength;
        public static final ObfuscatedField<TextInputUtil, Integer> cursorPosition;
        public static final ObfuscatedField<TextInputUtil, Integer> cursorPosition2;

        static {
            minecraft                   = new ObfuscatedField("field_216900_a", "field_216900_a", TextInputUtil.class, Minecraft.class);
            fontRenderer                = new ObfuscatedField("field_216901_b", "field_216901_b", TextInputUtil.class, FontRenderer.class);
            textSupplier                = new ObfuscatedField("field_216902_c", "field_216902_c", TextInputUtil.class, Supplier.class);
            textConsumer                = new ObfuscatedField("field_216903_d", "field_216903_d", TextInputUtil.class, Consumer.class);
            maxStringLength             = new ObfuscatedField("field_216904_e", "field_216904_e", TextInputUtil.class, int.class);
            cursorPosition              = new ObfuscatedField("field_216905_f", "field_216905_f", TextInputUtil.class, int.class);
            cursorPosition2             = new ObfuscatedField("field_216906_g", "field_216906_g", TextInputUtil.class, int.class);
        }
    }

    public static class $FontResourceManager {
        public static final ObfuscatedField<FontResourceManager, Boolean> forceUnicodeFont;
        public static final ObfuscatedField<FontResourceManager, Map<ResourceLocation, FontRenderer>> fontRenderers;
        public static final ObfuscatedField<FontResourceManager, TextureManager> textureManager;
        public static final ObfuscatedField<FontResourceManager, Set> glyphProviders;

        static {
            forceUnicodeFont            = new ObfuscatedField("forceUnicodeFont", "field_211826_d", FontResourceManager.class, Boolean.class);
            fontRenderers               = new ObfuscatedField("fontRenderers", "field_211510_b", FontResourceManager.class, Map.class);
            textureManager              = new ObfuscatedField("textureManager", "field_211511_c", FontResourceManager.class, TextureManager.class);
            glyphProviders              = new ObfuscatedField("field_216888_c", "field_216888_c", FontResourceManager.class, Set.class);
        }
    }

    public static class $FontRenderer {
        public static final ObfuscatedField<FontRenderer, Boolean> forceUnicodeFont;
        public static final ObfuscatedField<FontRenderer, TextureManager> textureManager;
        public static final ObfuscatedField<FontRenderer, Font> font;

        static {
            font                        = new ObfuscatedField("font", "field_211127_e", FontRenderer.class, Font.class);
            forceUnicodeFont            = new ObfuscatedField("forceUnicodeFont", "field_211826_d", FontRenderer.class, Boolean.class);
            textureManager              = new ObfuscatedField("textureManager", "field_211511_c", FontRenderer.class, TextureManager.class);
        }
    }

    public static class $LanguageScreen {
        public static final ObfuscatedField<LanguageScreen, Screen> parentScreen;
        public static final ObfuscatedField<LanguageScreen, GameSettings> game_settings_3;
        public static final ObfuscatedField<LanguageScreen, LanguageManager> languageManager;

        static {
            parentScreen                = new ObfuscatedField("parentScreen", "field_146453_a", LanguageScreen.class, Screen.class);
            game_settings_3             = new ObfuscatedField("game_settings_3", "field_146451_g", LanguageScreen.class, GameSettings.class);
            languageManager             = new ObfuscatedField("languageManager", "field_146454_h", LanguageScreen.class, LanguageManager.class);
        }
    }

    public static class $Font {
        public static final ObfuscatedField<Font, List<IGlyphProvider>> glyphProviders;

        static {
            glyphProviders              = new ObfuscatedField("glyphProviders", "field_211194_f", Font.class, List.class);
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

package kr.neko.sokcuri.naraechat.Fonts;

import kr.neko.sokcuri.naraechat.Config.AbstractNaraeOption;
import kr.neko.sokcuri.naraechat.Config.ConfigHelper;
import kr.neko.sokcuri.naraechat.NaraeChat;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class NaraeFontSettings extends Screen {
    protected final Screen parentScreen;
    private CustomList list;
    private final GameSettings game_settings_3;
    private final LanguageManager languageManager;
    private OptionButton field_211832_i;
    private Button confirmSettingsBtn;
    private SliderPercentageOption fontSizeSlider;
    private static String selectedFontPath = "";
    private static double scrollAmount;
    private static final AbstractOption[] FONT_OPTIONS = new AbstractOption[]{AbstractNaraeOption.FONT_SIZE, AbstractNaraeOption.OVER_SAMPLE, AbstractNaraeOption.SHIFT_X, AbstractNaraeOption.SHIFT_Y};
    public static double scrollbarPosition;

    public NaraeFontSettings(Screen screen, GameSettings gameSettingsObj, LanguageManager manager) {
        super(new TranslationTextComponent("options.naraechat.font"));
        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }

    private void refreshFontData() {
        String naraeFontName = FontManager.instance.fontFamily;
        Map<String, String> fontMap = FontManager.instance.getSystemFontMap();
        String naraeFontFileName = fontMap.get(naraeFontName);
        if (naraeFontFileName == null) return;

        String chars = "";
        NaraeChat.naraeFont.setFontData(naraeFontName, naraeFontFileName, FontManager.instance.fontSize, FontManager.instance.overSample, FontManager.instance.shiftX, FontManager.instance.shiftY, chars);
        NaraeChat.naraeFont.setGlyphProvider(naraeFontName);
        ConfigHelper.setFontFamily(naraeFontName);
        this.minecraft.updateWindowSize();
    }

    protected void init() {
        this.list = new CustomList(this.minecraft);
        this.children.add(this.list);
        this.confirmSettingsBtn = this.addButton(new Button(this.width / 2 - 155 + 80, this.height - 28, 150, 20, I18n.format("gui.done"), (p_213036_1_) -> {
            refreshFontData();
            this.minecraft.displayGuiScreen(this.parentScreen);
        }));

        int i = 0;
        for(AbstractOption abstractoption : FONT_OPTIONS) {
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height - 38 - 44 + 24 * (i >> 1);
            this.addButton(abstractoption.createWidget(this.minecraft.gameSettings, j, k, 150));
            ++i;
        }

        super.init();
    }

    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.list.render(p_render_1_, p_render_2_, p_render_3_);
        this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 16, 16777215);
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }

    @OnlyIn(Dist.CLIENT)
    class CustomList extends ExtendedList<CustomList.FontEntry> {
        public CustomList(Minecraft mcIn) {
            super(mcIn, NaraeFontSettings.this.width, NaraeFontSettings.this.height, 32, NaraeFontSettings.this.height - 95 + 4, 18);

            Map<String, String> fontMap = FontManager.instance.getSystemFontMap();
            java.util.List<String> sortedKeys = new ArrayList(fontMap.keySet());
            Collections.sort(sortedKeys);

            FontEntry targetEntry = null;
            for (String name : sortedKeys) {
                String path = fontMap.get(name);
                NaraeTTF naraeTTF = new NaraeTTF(name, path);
                FontEntry entry = new FontEntry(naraeTTF);
                this.addEntry(entry);

                if (ConfigHelper.getFontFamily().equals(name)) {
                    targetEntry = entry;
                }
            }
            super.setSelected(targetEntry);

            if (scrollAmount <= Math.pow(10, -3)) {
                this.centerScrollOn(super.getSelected());
            } else {
                super.setScrollAmount(scrollAmount);
            }
        }

        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 20;
        }

        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        public void setSelected(@Nullable FontEntry entry) {
            ConfigHelper.setFontFamily(entry.font.getName());
            FontManager.instance.fontFamily = entry.font.getName();
            selectedFontPath = entry.font.getPath();
            scrollAmount = getScrollAmount();
            scrollbarPosition = super.getScrollbarPosition();
            refreshFontData();
        }

        protected void renderBackground() {
            NaraeFontSettings.this.renderBackground();
        }

        protected boolean isFocused() {
            return NaraeFontSettings.this.getFocused() == this;
        }

        @OnlyIn(Dist.CLIENT)
        public class FontEntry extends ExtendedList.AbstractListEntry<FontEntry> {
            private final NaraeTTF font;
            public FontEntry(NaraeTTF font) {
                this.font = font;
            }

            public void render(int p_render_1_, int p_render_2_, int p_render_3_, int p_render_4_, int p_render_5_, int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_) {
                CustomList.this.drawCenteredString(NaraeFontSettings.this.font, this.font.getName(), CustomList.this.width / 2, p_render_2_ + 1, 16777215);
            }

            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
                if (mouseButton == 0) {
                    this.select();
                    return true;
                } else {
                    return false;
                }
            }

            private void select() {
                CustomList.this.setSelected(this);
            }
        }
    }
}
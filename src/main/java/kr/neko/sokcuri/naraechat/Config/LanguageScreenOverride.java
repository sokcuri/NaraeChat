package kr.neko.sokcuri.naraechat.Config;

import kr.neko.sokcuri.naraechat.Fonts.NaraeFontSettings;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;

public class LanguageScreenOverride extends LanguageScreen {
    private final Screen parentScreen;
    private final GameSettings game_settings_3;
    private final LanguageManager languageManager;

    public LanguageScreenOverride(Screen screen, GameSettings gameSettingsObj, LanguageManager manager) {
        super(screen, gameSettingsObj, manager);

        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }

    @Override
    protected void init() {
        super.init();

        Widget fontConfBtn = new Button(this.width / 2 - 155, this.height - 38, 150, 20, I18n.format("options.naraechat.font"), (p_213037_1_) -> {
            NaraeFontSettings.scrollbarPosition = 0.0f;
            this.minecraft.displayGuiScreen(new NaraeFontSettings(this, this.game_settings_3, languageManager));
        });

        for (Widget button : this.buttons) {
            if (button instanceof OptionButton) {
                this.buttons.add(this.buttons.indexOf(button), fontConfBtn);
                this.children.add(this.children.indexOf(button), fontConfBtn);
                this.buttons.remove(button);
                this.children.remove(button);
                break;
            }
        }
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}

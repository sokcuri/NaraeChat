package kr.neko.sokcuri.naraechat.Config;

import kr.neko.sokcuri.naraechat.Config.ConfigHelper;
import kr.neko.sokcuri.naraechat.Fonts.FontManager;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.client.settings.SliderPercentageOption;

public abstract class AbstractNaraeOption extends AbstractOption {
    public AbstractNaraeOption(String translationKeyIn) {
        super(translationKeyIn);
    }

    public static final SliderPercentageOption FONT_SIZE = new SliderPercentageOption("options.naraechat.fontsize", 8.0D, 15.0D, 1.0F, (gameSettings) -> {
        return (double) FontManager.instance.fontSize;
    }, (gameSettings, value) -> {
        FontManager.instance.fontSize = value.floatValue();
        ConfigHelper.setFontSize(value.floatValue());
    }, (gameSettings, sliderPercentageOption) -> {
        double d0 = sliderPercentageOption.get(gameSettings);
        return "Font Size: " + String.format("%.0f", d0) + "pt";
    });

    public static final SliderPercentageOption OVER_SAMPLE = new SliderPercentageOption("options.naraechat.oversample", 0.0D, 8.0D, 0.1F, (gameSettings) -> {
        return (double) FontManager.instance.overSample;
    }, (gameSettings, value) -> {
        FontManager.instance.overSample = value.floatValue();
        ConfigHelper.setOversample(value.floatValue());
    }, (gameSettings, sliderPercentageOption) -> {
        double d0 = sliderPercentageOption.get(gameSettings);
        return "Over Sample : " + String.format("%.1f", d0) + "pt";
    });

    public static final SliderPercentageOption SHIFT_X = new SliderPercentageOption("options.naraechat.shiftx", -5.0D, 5.0D, 0.1F, (gameSettings) -> {
        return (double) FontManager.instance.shiftX;
    }, (gameSettings, value) -> {
        FontManager.instance.shiftX = value.floatValue();
        ConfigHelper.setShiftX(value.floatValue());
    }, (gameSettings, sliderPercentageOption) -> {
        double d0 = sliderPercentageOption.get(gameSettings);
        return "ShiftX : " + String.format("%.1f", d0) + "px";
    });

    public static final SliderPercentageOption SHIFT_Y = new SliderPercentageOption("options.naraechat.shifty", -5.0D, 5.0D, 0.1F, (gameSettings) -> {
        return (double) FontManager.instance.shiftY;
    }, (gameSettings, value) -> {
        FontManager.instance.shiftY = value.floatValue();
        ConfigHelper.setShiftY(value.floatValue());
    }, (gameSettings, sliderPercentageOption) -> {
        double d0 = sliderPercentageOption.get(gameSettings);
        return "ShiftY : " + String.format("%.1f", d0) + "px";
    });
}

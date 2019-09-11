package kr.neko.sokcuri.naraechat.Config;

import net.minecraftforge.fml.config.ModConfig;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public final class ConfigHelper {
    private static Wini ini;

    static {
        try {
            File file = new File("naraechat.ini");
            if (!file.exists()) {
                file.createNewFile();
                ini = new Wini(file);
                setFontFamily("맑은 고딕");
                setFontSize(12.0f);
                setOversample(4.0f);
                setShiftX(-0.3f);
                setShiftY(0.3f);
                ini.load(file);
            } else {
                ini = new Wini(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig clientConfig;

    public static void bakeClient(final ModConfig config) {
        clientConfig = config;
    }

    public static String getFontFamily() {
        String fontFamily = ini.get("font", "fontfamily", String.class);
        return fontFamily;
    }

    public static void setFontFamily(final String value) {
        ini.put("font", "fontfamily", value);
        try {
            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float getFontSize() {
        float size = ini.get("font", "fontsize", float.class);
        if (size == 0) size = 12.0f;
        return size;
    }

    public static void setFontSize(final float value) {
        ini.put("font", "fontsize", value);
        try {
            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float getOversample() {
        float oversample = ini.get("font", "oversample", float.class);
        if (oversample == 0) oversample = 4.0f;
        return oversample;
    }

    public static void setOversample(final float value) {
        ini.put("font", "oversample", value);
        try {
            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float getShiftX() {
        return ini.get("font", "shiftx", float.class);
    }

    public static void setShiftX(final float value) {
        ini.put("font", "shiftx", value);
        try {
            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float getShiftY() {
        return ini.get("font", "shifty", float.class);
    }

    public static void setShiftY(final float value) {
        ini.put("font", "shifty", value);
        try {
            ini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
        modConfig.getConfigData().set(path, newValue);
        modConfig.save();
    }
}

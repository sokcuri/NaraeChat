package kr.neko.sokcuri.naraechat.Fonts;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.neko.sokcuri.naraechat.Config.ConfigHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.fontbox.ttf.NameRecord;
import org.apache.fontbox.ttf.TrueTypeCollection;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

public class FontManager {

    public static FontManager instance = new FontManager();
    public String fontFamily;
    public float fontSize;
    public float overSample;
    public float shiftX;
    public float shiftY;
    public boolean isInitial;

    private static final String TAG = FontManager.class.getCanonicalName();
    private HashMap<String, String> systemFontMap = new HashMap<>();

    public FontManager() {
        preCacheSystemFontsMap();

        fontFamily = ConfigHelper.getFontFamily();
        fontSize = ConfigHelper.getFontSize();
        overSample = ConfigHelper.getOversample();
        shiftX = ConfigHelper.getShiftX();
        shiftY = ConfigHelper.getShiftY();

        isInitial = true;
    }

    public HashMap<String, String> getSystemFontMap() {
        return systemFontMap;
    }

    public String[] getSystemFontNames() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    public String[] getSystemFontsPaths() {
        String[] result;
        if (SystemUtils.IS_OS_WINDOWS) {
            result = new String[1];
            String path = System.getenv("WINDIR");
            result[0] = path + "\\" + "Fonts";
            return result;
        } else if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC) {
            result = new String[3];
            result[0] = System.getProperty("user.home") + File.separator + "Library/Fonts";
            result[1] = "/Library/Fonts";
            result[2] = "/System/Library/Fonts";
            return result;
        } else if (SystemUtils.IS_OS_LINUX) {
            String[] pathsToCheck = {
                    System.getProperty("user.home") + File.separator + ".fonts",
                    "/usr/share/fonts/truetype",
                    "/usr/share/fonts/TTF"
            };
            ArrayList<String> resultList = new ArrayList<>();

            for (int i = pathsToCheck.length - 1; i >= 0; i--) {
                String path = pathsToCheck[i];
                File tmp = new File(path);
                if (tmp.exists() && tmp.isDirectory() && tmp.canRead()) {
                    resultList.add(path);
                }
            }

            if (resultList.isEmpty()) {
                // TODO: show user warning, TextTool will be crash editor, because system font directories not found
                result = new String[0];
            }
            else {
                result = new String[resultList.size()];
                result = resultList.toArray(result);
            }

            return result;
        }

        return null;
    }

    public List<File> getSystemFontFiles() {
        // only retrieving ttf files
        String[] extensions = new String[]{"ttf", "TTF", "ttc", "TTC"};
        String[] paths = getSystemFontsPaths();

        ArrayList<File> files = new ArrayList<>();

        for (int i = 0; i < paths.length; i++) {
            File fontDirectory = new File(paths[i]);
            if (!fontDirectory.exists()) break;
            files.addAll(FileUtils.listFiles(fontDirectory, extensions, true));
        }

        return files;
    }

    public class TTFProcessor implements TrueTypeCollection.TrueTypeFontProcessor {

        public List<String> fontList = new ArrayList();
        @Override
        public void process(TrueTypeFont ttf) throws IOException {
            String name = ttf.getNaming().getName(
                    NameRecord.NAME_FONT_FAMILY_NAME,
                    NameRecord.PLATFORM_WINDOWS,
                    NameRecord.ENCODING_WINDOWS_UNICODE_BMP,
                    0x0412);

            if (name == null) {
                name = ttf.getName();
            }
            fontList.add(name);
        }
    }

    public int getFontIndex(String fontName, String fileName) {
        FileInputStream fis;

        File file = new File(fileName);
        try {
            fis = new FileInputStream(file);
            if (file.getAbsolutePath().toLowerCase().endsWith(".ttc")) {
                TTFProcessor Processor = new TTFProcessor();
                FileChannel fc = fis.getChannel();

                TrueTypeCollection truetypeCollection = new TrueTypeCollection(fis);
                truetypeCollection.processAllFonts(Processor);

                for (int i = 0; i < Processor.fontList.size(); i++) {
                    String fontString = Processor.fontList.get(i);
                    if (fontString.equals(fontName)) {
                        return i;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void preCacheSystemFontsMap() {
        List<File> fontFiles = getSystemFontFiles();

        for (File file : fontFiles) {
            Font f;
            try {
                //if (!systemFontMap.containsValue(file.getAbsolutePath())) {

                    ByteBuffer buffer;
                    FileInputStream fis;

                    fis = new FileInputStream(file);
                    if (file.getAbsolutePath().toLowerCase().endsWith(".ttc")) {
                        TTFProcessor Processor = new TTFProcessor();
                        FileChannel fc = fis.getChannel();
                        buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                        // fc.close();

                        TrueTypeCollection truetypeCollection = new TrueTypeCollection(fis);
                        truetypeCollection.processAllFonts(Processor);
                        // truetypeCollection.close();

                        int number = STBTruetype.stbtt_GetNumberOfFonts(buffer);

                        fis.close();

                        for (int i = 0; i < Processor.fontList.size(); i++) {
                            String name = Processor.fontList.get(i);
                            STBTTFontinfo info = STBTTFontinfo.create();
                            if (!STBTruetype.stbtt_InitFont(info, buffer, STBTruetype.stbtt_GetFontOffsetForIndex(buffer, i)))
                                throw new IOException("Invalid ttf");

                                if (!systemFontMap.containsKey(name))
                                    systemFontMap.put(name, file.getAbsolutePath());
                            }
                        } else if (file.getAbsolutePath().toLowerCase().endsWith(".ttf")){
                            f = Font.createFont(Font.TRUETYPE_FONT, fis);
                            String name = f.getFontName();
                            FileChannel fc = fis.getChannel();

                            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                            STBTTFontinfo info = STBTTFontinfo.create();
                            if (!STBTruetype.stbtt_InitFont(info, buffer)) {
                                fc.close();
                                throw new IOException("Invalid ttf");
                            }

                            systemFontMap.put(name, file.getAbsolutePath());
                            fc.close();
                        }
                    } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }
        }
    }

}
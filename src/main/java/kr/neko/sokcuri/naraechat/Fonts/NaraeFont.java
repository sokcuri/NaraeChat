package kr.neko.sokcuri.naraechat.Fonts;

import com.google.common.collect.Lists;
import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraft.client.gui.fonts.providers.TrueTypeGlyphProvider;
import net.minecraft.client.gui.fonts.providers.UnicodeTextureGlyphProvider;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.List;

public class NaraeFont {
    public class FontData {
        String fileName;
        ByteBuffer buffer;
        float size;
        float overSample;
        float shiftX;
        float shiftY;
        String chars;
        int index;
    }

    private static Map<String, FontData> fontDataMap = new HashMap();
    private static Map<String, ByteBuffer> fontBufferMap = new HashMap();

    public boolean setFontData(String fontName, String fileName, float size, float overSample, float shiftX, float shiftY, String chars) {
        FontData fontData = new FontData();
        ByteBuffer buffer = null;
        File file = new File(fileName);
        if (!file.isFile())
            return false;

        FileInputStream fis = null;
        fontData.fileName = fileName;
        try {
            fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (buffer == null) {
            System.out.println("font not loaded");
            return false;
        }

        fontData.buffer = buffer;
        fontData.size = size;
        fontData.overSample = overSample;
        fontData.shiftX = shiftX;
        fontData.shiftY = shiftY;
        fontData.chars = chars;

        fontData.index = FontManager.instance.getFontIndex(fontName, fileName);

        if (fontDataMap.containsKey(fontName)) {
            fontDataMap.replace(fontName, fontData);
        } else {
            fontDataMap.put(fontName, fontData);
        }
        return true;
    }

    public void setGlyphProvider(String fontName) {
        FontData fontData = fontDataMap.get(fontName);
        if (fontData == null) return;

        STBTTFontinfo info = STBTTFontinfo.create();
        if (!STBTruetype.stbtt_InitFont(info, fontData.buffer, STBTruetype.stbtt_GetFontOffsetForIndex(fontData.buffer, fontData.index))) {
            try {
                throw new IOException("Invalid ttf");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        TrueTypeGlyphProvider glyphProvider = new TrueTypeGlyphProvider(info, fontData.size, fontData.overSample, fontData.shiftX, fontData.shiftY, fontData.chars);


        Minecraft mc = Minecraft.getInstance();
        FontResourceManager fontResourceManager = mc.getFontResourceManager();
        FontRenderer fontRenderer = mc.fontRenderer;

        Map<ResourceLocation, FontRenderer> fontRenderers = ObfuscatedField.$FontResourceManager.fontRenderers.get(fontResourceManager);
        TextureManager textureManager = ObfuscatedField.$FontResourceManager.textureManager.get(fontResourceManager);

        ResourceLocation naraeResLoc = new ResourceLocation("narae");
        Map<ResourceLocation, List<IGlyphProvider>> splashList = new HashMap();

        List<IGlyphProvider> providers = new ArrayList();
        providers.add(0, glyphProvider);

        IGlyphProvider unicodeGlyphProvider = new UnicodeTextureGlyphProvider.Factory(new ResourceLocation("minecraft:font/glyph_sizes.bin"), "minecraft:font/unicode_page_%s.png").create(mc.getResourceManager());
        providers.add(unicodeGlyphProvider);

        FontRenderer newRenderer = new FontRenderer(textureManager, new Font(textureManager, naraeResLoc));
        newRenderer.setGlyphProviders(providers);

        if (fontRenderers.containsKey(naraeResLoc)) {
            fontRenderers.replace(naraeResLoc, newRenderer);
        } else {
            fontRenderers.put(naraeResLoc, newRenderer);
        }

        mc.fontRenderer = mc.getFontResourceManager().getFontRenderer(new ResourceLocation("narae"));

        for(char c0 = 0; c0 < '\uffff'; ++c0) {
            if (c0 != ' ') {
                for(IGlyphProvider iglyphprovider : Lists.reverse(providers)) {
                    if (iglyphprovider.func_212248_a(c0) != null) {
                        break;
                    }
                }
            }
        }
    }

    public void renderTick(TickEvent.RenderTickEvent event) {
        if (FontManager.instance.isInitial == false) return;

        Minecraft mc = Minecraft.getInstance();
        FontResourceManager fontResourceManager = mc.getFontResourceManager();
        ResourceLocation resLoc = new ResourceLocation("narae");

        Map<String, String> fontMap = FontManager.instance.getSystemFontMap();
        String fontName = FontManager.instance.fontFamily;
        String fontFileName = fontMap.get(fontName);
        if (fontDataMap.size() == 0 && fontFileName != null) {
            setFontData(fontName, fontFileName, FontManager.instance.fontSize, FontManager.instance.overSample, FontManager.instance.shiftX, FontManager.instance.shiftY, "");
            setGlyphProvider(fontName);
        }

        // 현재 인게임 font Renderer가 narae font renderer로 사용중일때 처리
        if (mc.fontRenderer == mc.getFontResourceManager().getFontRenderer(resLoc)) {
            Font font = ObfuscatedField.$FontRenderer.font.get(mc.fontRenderer);

            List<IGlyphProvider> glyphProviders = ObfuscatedField.$Font.glyphProviders.get(font);
            if (glyphProviders.size() <= 1) {
                setGlyphProvider(fontName);
            }
        }
    }
}

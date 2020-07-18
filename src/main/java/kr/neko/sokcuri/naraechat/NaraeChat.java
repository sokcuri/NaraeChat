package kr.neko.sokcuri.naraechat;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;

import kr.neko.sokcuri.naraechat.Config.LanguageScreenOverride;
import kr.neko.sokcuri.naraechat.Fonts.NaraeFont;
import kr.neko.sokcuri.naraechat.Keyboard.*;

import kr.neko.sokcuri.naraechat.Obfuscated.ObfuscatedField;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NaraeChat.MODID)
@OnlyIn(Dist.CLIENT)
public final class NaraeChat
{
    public static final String MODID = "naraechat";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static KeyboardLayout keyboard = Hangul_Set_2_Layout.getInstance();
    private static List<KeyboardLayout> keyboardArray = new ArrayList<>();
    public static NaraeFont naraeFont = new NaraeFont();
    public static KeyBinding[] keyBindings;

    private int getBindingKeyCode(int n) {
        return keyBindings[n].getKey().getKeyCode();
    }

    private void switchKeyboardLayout() {
        for (int i = 0; i < keyboardArray.size(); i++) {
            if (keyboard == keyboardArray.get(i)) {
                int n = (i + 1) % keyboardArray.size();
                keyboard.cleanUp();
                keyboard = keyboardArray.get(n);
                break;
            }
        }
    }

    public NaraeChat() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);



        keyboardArray.add(QwertyLayout.getInstance());
        keyboardArray.add(Hangul_Set_2_Layout.getInstance());
    }

    public interface Imm32 extends StdCallLibrary {
        Imm32 INSTANCE = Native.loadLibrary("Imm32", Imm32.class);

        boolean ImmDisableIME(int ThreadID);
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (url == null)
            throw new IOException("Classpath resource not found: " + resource);
        File file = new File(url.getFile());
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        } else {
            buffer = BufferUtils.createByteBuffer(bufferSize);
            InputStream source = url.openStream();
            if (source == null)
                throw new FileNotFoundException(resource);
            try {
                byte[] buf = new byte[8192];
                while (true) {
                    int bytes = source.read(buf, 0, buf.length);
                    if (bytes == -1)
                        break;
                    if (buffer.remaining() < bytes)
                        buffer = resizeBuffer(buffer, Math.max(buffer.capacity() * 2, buffer.capacity() - buffer.remaining() + bytes));
                    buffer.put(buf, 0, bytes);
                }
                buffer.flip();
            } finally {
                source.close();
            }
        }
        return buffer;
    }

    @SubscribeEvent
    public void onSpecialKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {

        int keyCode = event.getKeyCode();
        int scanCode = event.getScanCode();

        Minecraft mc = Minecraft.getInstance();


        KeyModifier activeModifier = KeyModifier.getActiveModifier();

        int glfwModifier = 0;
        if (activeModifier == KeyModifier.SHIFT) {
            glfwModifier = GLFW_MOD_SHIFT;
        } else if (activeModifier == KeyModifier.CONTROL) {
            glfwModifier = GLFW_MOD_CONTROL;
        } else if (activeModifier == KeyModifier.ALT) {
            glfwModifier = GLFW_MOD_ALT;
        }

        // 103 키보드 문제 수정. 한글(0x1F2)/한자(0x1F1) 키를 강재로 리매핑한다
        // Windows 화상 키보드 한영키(0xF2)/한자키(0xF1) 반영
        if (keyCode == -1 && scanCode == 0x1F2 || keyCode == -1 && scanCode == 0x1F1 || keyCode == -1 && scanCode == 0xF2 || keyCode == -1 && scanCode == 0xF1) {
            if (scanCode == 0x1F2 || scanCode == 0xF2) {
                keyCode = GLFW_KEY_RIGHT_ALT;
                scanCode = glfwGetKeyScancode(keyCode);
            } else if (scanCode == 0x1F1 || scanCode == 0xF1) {
                keyCode = GLFW_KEY_RIGHT_CONTROL;
                scanCode = glfwGetKeyScancode(keyCode);
            }

            event.setCanceled(true);

            int glfwModifier = 0;
            if (KeyModifier.getActiveModifier() == KeyModifier.SHIFT) {
                glfwModifier = GLFW_MOD_SHIFT;
            } else if (KeyModifier.getActiveModifier() == KeyModifier.CONTROL) {
                glfwModifier = GLFW_MOD_CONTROL;
            } else if (KeyModifier.getActiveModifier() == KeyModifier.ALT) {
                glfwModifier = GLFW_MOD_ALT;
            }

            Minecraft mc = Minecraft.getInstance();
            mc.currentScreen.keyPressed(keyCode, scanCode, glfwModifier);
            mc.currentScreen.keyReleased(keyCode, scanCode, glfwModifier);
        }

        KeyModifier modifier = KeyModifier.getActiveModifier();
        if (keyCode == GLFW_KEY_LEFT_CONTROL || keyCode == GLFW_KEY_RIGHT_CONTROL) {
            modifier = KeyModifier.NONE;
        } else if (keyCode == GLFW_KEY_LEFT_ALT || keyCode == GLFW_KEY_RIGHT_ALT) {
            modifier = KeyModifier.NONE;
        }

        if (keyBindings[0].matchesKey(keyCode, scanCode) && keyBindings[0].getKeyModifier() == modifier) {
            switchKeyboardLayout();
        }

        if (keyBindings[1].matchesKey(keyCode, scanCode) && keyBindings[1].getKeyModifier() == modifier) {
            // hanja
        }
    }

    @SubscribeEvent
    public void onKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        keyboard.onKeyPressed(event);
    }

    @SubscribeEvent
    public void onCharTyped(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
        keyboard.onCharTyped(event);
    }

    @SubscribeEvent
    public void guiOpened(GuiOpenEvent event) {

        if (event.getGui() instanceof LanguageScreenOverride) {

        }
        else if (event.getGui() instanceof LanguageScreen) {
            LanguageScreen langaugeScreen = (LanguageScreen)event.getGui();
            Screen parentScreen = ObfuscatedField.$LanguageScreen.parentScreen.get(langaugeScreen);
            GameSettings game_settings_3 = ObfuscatedField.$LanguageScreen.game_settings_3.get(langaugeScreen);
            LanguageManager languageManager = ObfuscatedField.$LanguageScreen.languageManager.get(langaugeScreen);
            event.setGui(new LanguageScreenOverride(parentScreen, game_settings_3, languageManager));
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        naraeFont.renderTick(event);
        keyboard.renderTick(event);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        if (Platform.isWindows()) {
            Imm32.INSTANCE.ImmDisableIME(-1);
        }
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // declare an array of key bindings
        keyBindings = new KeyBinding[2];

        // instantiate the key bindings
        keyBindings[0] = new KeyBinding("key.naraechat.ime_switch.desc", GLFW_KEY_RIGHT_ALT, "key.naraechat.category");
        keyBindings[1] = new KeyBinding("key.naraechat.hanja.desc", GLFW_KEY_RIGHT_CONTROL, "key.naraechat.category");

        if (Platform.isWindows()) {
            keyBindings[0].setKeyModifierAndCode(KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW_KEY_RIGHT_ALT));
            keyBindings[1].setKeyModifierAndCode(KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW_KEY_RIGHT_CONTROL));
        }
        else if (Platform.isMac()) {
            keyBindings[0].setKeyModifierAndCode(KeyModifier.CONTROL, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW_KEY_SPACE));
            keyBindings[1].setKeyModifierAndCode(KeyModifier.SHIFT, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW_KEY_ENTER));
        } else {
            keyBindings[0].setKeyModifierAndCode(KeyModifier.CONTROL, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW_KEY_SPACE));
            keyBindings[1].setKeyModifierAndCode(KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW_KEY_F9));
        }

        // register all the key bindings
        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }

        // changeFont();
    }
}

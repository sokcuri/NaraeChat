package kr.neko.sokcuri.naraechat;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;

import kr.neko.sokcuri.naraechat.Keyboard.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("naraechat")
@OnlyIn(Dist.CLIENT)
public final class NaraeChat
{
    private static KeyboardLayout keyboard = Hangul_Set_2_Layout.getInstance();
    private static List<KeyboardLayout> keyboardArray = new ArrayList<>();
    public static KeyBinding[] keyBindings;

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

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

    @SubscribeEvent
    public void proxyHangulSpecificKey(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {

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

        // 102 키보드 문제 수정. 한글/한자 키를 강재로 리매핑한다
        if (keyCode == -1 && scanCode == 0x1F2 || keyCode == -1 && scanCode == 0x1F1) {
            if (scanCode == 0x1F2) {
                keyCode = GLFW_KEY_RIGHT_ALT;
                scanCode = glfwGetKeyScancode(keyCode);
            } else if (scanCode == 0x1F1) {
                keyCode = GLFW_KEY_RIGHT_CONTROL;
                scanCode = glfwGetKeyScancode(keyCode);
            }

            event.setCanceled(true);
        }

        // 키 바인딩 설정창일 때 우측 CONTROL이나 ALT가 단독으로만 동작하게 만들기
        if (mc.currentScreen instanceof ControlsScreen) {
            ControlsScreen controlsScreen = (ControlsScreen)mc.currentScreen;
            if (keyCode == GLFW_KEY_RIGHT_CONTROL || keyCode == GLFW_KEY_RIGHT_ALT) {
                controlsScreen.keyPressed(keyCode, scanCode, glfwModifier);
                controlsScreen.buttonId = null;
                event.setCanceled(true);
                return;
            }
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
    public void renderTick(TickEvent.RenderTickEvent event) {
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
    }
}
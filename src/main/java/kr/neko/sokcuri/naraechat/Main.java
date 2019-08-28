package kr.neko.sokcuri.naraechat;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;

import kr.neko.sokcuri.naraechat.Keyboard.*;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
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
public class Main
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

    public Main() {
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
    public void onSpecialKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        if (event.getKeyCode() == getBindingKeyCode(0)) {
            switchKeyboardLayout();
        }

        else if (event.getKeyCode() == getBindingKeyCode(1)) {
            // hanja key
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
            // pass
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

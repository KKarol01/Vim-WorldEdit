package org.kkarol01.vimworldedit.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.Stack;

public class VimworldeditClient implements ClientModInitializer {

    @Override
    public void onInitializeClient()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            initialize_commands();
            old_callback = GLFW.glfwSetKeyCallback(MinecraftClient.getInstance().getWindow().getHandle(), VimworldeditClient::handle_keys);
        });
    }

    private static void initialize_commands()
    {
        int mod_shift = GLFW.GLFW_MOD_SHIFT;
        int mod_ctrl = GLFW.GLFW_MOD_CONTROL;

        //commands
        actions.add(new Action("//expand",      KeyCategory.COMMAND,    GLFW.GLFW_KEY_E,        0));
        actions.add(new Action("//contract",    KeyCategory.COMMAND,    GLFW.GLFW_KEY_C,        0));
        actions.add(new Action("//move",        KeyCategory.COMMAND,    GLFW.GLFW_KEY_M,        0));
        actions.add(new Action("//stack",       KeyCategory.COMMAND,    GLFW.GLFW_KEY_S,        0));
        actions.add(new Action("//copy",        KeyCategory.COMMAND,    GLFW.GLFW_KEY_Y,        0));
        actions.add(new Action("//paste",       KeyCategory.COMMAND,    GLFW.GLFW_KEY_P,        0));
        actions.add(new Action("//flip",        KeyCategory.COMMAND,    GLFW.GLFW_KEY_F,        0));
        actions.add(new Action("//undo",        KeyCategory.COMMAND,    GLFW.GLFW_KEY_U,        0));
        actions.add(new Action("//redo",        KeyCategory.COMMAND,    GLFW.GLFW_KEY_U,        mod_shift));
        actions.add(new Action("//set 0",       KeyCategory.COMMAND,    GLFW.GLFW_KEY_D,        0));
        actions.add(new Action("//rotate",      KeyCategory.COMMAND,    GLFW.GLFW_KEY_R,        0));

        //movement
        actions.add(new Action("/tpr",          KeyCategory.COMMAND,    GLFW.GLFW_KEY_RIGHT,    0));                // move_right
        actions.add(new Action("/tpl",          KeyCategory.COMMAND,    GLFW.GLFW_KEY_LEFT,     0));                // move_left
        actions.add(new Action("/tpd",          KeyCategory.COMMAND,    GLFW.GLFW_KEY_DOWN,     mod_shift));        // move_down
        actions.add(new Action("/tpu",          KeyCategory.COMMAND,    GLFW.GLFW_KEY_UP,       mod_shift));        // move_up
        actions.add(new Action("/tpb",          KeyCategory.COMMAND,    GLFW.GLFW_KEY_DOWN,     0));                // move_backward
        actions.add(new Action("/tpf",          KeyCategory.COMMAND,    GLFW.GLFW_KEY_UP,       0));                // move_forward

        //directions
        actions.add(new Action("l",             KeyCategory.DIRECTION,  GLFW.GLFW_KEY_H,        0));                // direction_left
        actions.add(new Action("d",             KeyCategory.DIRECTION,  GLFW.GLFW_KEY_J,        0));                // direction_down
        actions.add(new Action("u",             KeyCategory.DIRECTION,  GLFW.GLFW_KEY_K,        0));                // direction_up
        actions.add(new Action("r",             KeyCategory.DIRECTION,  GLFW.GLFW_KEY_L,        0));                // direction_right
        actions.add(new Action("b",             KeyCategory.DIRECTION,  GLFW.GLFW_KEY_J,        mod_shift));        // direction_back
        actions.add(new Action("f",             KeyCategory.DIRECTION,  GLFW.GLFW_KEY_K,        mod_shift));        // direction_forwards

        //flags
        actions.add(new Action("-e",            KeyCategory.FLAG,       GLFW.GLFW_KEY_E,        mod_ctrl));         // include_entities_flag
        actions.add(new Action("-s",            KeyCategory.FLAG,       GLFW.GLFW_KEY_S,        mod_ctrl));         // move_selection_flag
        actions.add(new Action("-a",            KeyCategory.FLAG,       GLFW.GLFW_KEY_A,        mod_ctrl));         // ignore_air_flag
        actions.add(new Action("-m",            KeyCategory.FLAG,       GLFW.GLFW_KEY_M,        mod_ctrl));         // use_mask_flag
    }


    private static void handle_keys(long window, int key, int scancode, int action, int modifier)
    {
        if (key == KEY_TOGGLE_VIM && action == GLFW.GLFW_PRESS) { toggle_command_mode(); }
        if (!command_mode_enabled) { old_callback.invoke(window, key, scancode, action, modifier); return; }
        if (action != GLFW.GLFW_PRESS) { return; }

        if (key == GLFW.GLFW_KEY_PERIOD)
        {
            int cmds_to_repeat = 1;
            Stack<Command> repeat_stack = new Stack<>();
            if (!command.number.isEmpty())
            {
                cmds_to_repeat = Integer.parseInt(command.number);
                cmds_to_repeat = Math.min(previous_commands.size(), cmds_to_repeat);
            }
            for (int i = 0; i < cmds_to_repeat; ++i)
            {
                repeat_stack.add(previous_commands.peek().clone());
                previous_commands.pop();
            }
            for (int i = 0; i < cmds_to_repeat; ++i)
            {
                command = repeat_stack.peek();
                repeat_stack.pop();
                execute_command();
            }
            return;
        }
        else if (key == GLFW.GLFW_KEY_APOSTROPHE)
        {
            ClientPlayerEntity player = MinecraftClient.getInstance() != null ? MinecraftClient.getInstance().player : null;
            if (player == null) { return; }
            Vec3d player_pos = player.getPos();
            if (modifier == GLFW.GLFW_MOD_SHIFT)
            {
                last_used_position = new SavedPosition(player_pos);
                return;
            }
            if(last_used_position == null)
            {
                send_message("No position saved; cannot teleport.");
                return;
            }
            command.clear();
            command.command = "/tp";
            command.number = last_used_position.toString();
            last_used_position = new SavedPosition(player_pos);
            execute_command();
            return;
        }
        else if (key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_W)
        {
            if (command.toString().trim().isEmpty())
            {
                toggle_command_mode();
                if (key == GLFW.GLFW_KEY_W) { old_callback.invoke(window, key, scancode, GLFW.GLFW_KEY_DOWN, modifier); }
                return;
            }
            command.clear();
            return;
        }
        update_command(key, modifier);
    }

    private static void update_command(int key, int mod)
    {
        if (is_number_key(key))
        {
            final int number = key - KEY_ZERO;
            if (number == 0 && command.number.isEmpty()) { return; }
            command.number += Integer.toString(number);
            return;
        }

//        if (is_function_key(key))
//        {
//            if (mod == 0) { command.mask = masks.get(key);}
//            else if (mod == GLFW.GLFW_MOD_ALT) { command.mask = "!" + masks.get(key); }
//            else
//            {
//                var client = MinecraftClient.getInstance();
//                if(client == null || client.player == null) { return; }
//                var player = client.player;
//
//                if (mod == (GLFW.GLFW_MOD_CONTROL | GLFW.GLFW_MOD_SHIFT)) { send_message(masks.get(key)); return; }
//
//                HitResult hitResult = player.raycast(30.0, 0.f, false);
//                if (hitResult.getType() != HitResult.Type.BLOCK) { send_message("No block in sight!"); return; }
//
//                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
//                ClientPlayNetworkHandler nhandler = client.getNetworkHandler();
//                if (nhandler == null) { send_message("Could not obtain network handler."); return; }
//                else if (nhandler.getWorld() != null)
//                {
//                    Block block = nhandler.getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock();
//                    var name = block.getName().toString();
//                    var last_dot_idx = name.lastIndexOf('.');
//                    name = name.substring(last_dot_idx + 1, name.indexOf('\'', last_dot_idx));
//
//                    if (mod == GLFW.GLFW_MOD_CONTROL) {
//                        masks.put(key, name);
//                    } else if (mod == GLFW.GLFW_MOD_SHIFT) {
//                        masks.put(key, masks.get(key) + "," + name);
//                    }
//
//                }
//            }
//            return;
//        }

        Action action = get_action(key, mod);
        if (action == null || action.modifierKey != mod || action.keyBinding != key) { return; }

        switch (action.category)
        {
            case COMMAND ->
            {
                command.command = action.command;
                patch_command();
                execute_command();
            }
            case DIRECTION ->
            {
                final String delim = command.directions.isEmpty() ? "" : ",";
                command.directions += delim + action.command;
            }
            case FLAG ->
            {
                final String delim = command.flags.isEmpty() ? "" : " ";
                String flag = action.command;

                // a mask flag should be at the end of flags section.
                if (flag.equals("-m")) { command.flags = command.flags + delim + flag; }
                else { command.flags = flag + delim + command.flags; }

            }
        }
    }

    private static void toggle_command_mode()
    {
        command_mode_enabled = !command_mode_enabled;
        send_message(command_mode_enabled ? "Vimworldedit: on" : "Vimworldedit: off");
    }

    private static Action get_action(int key, int mod)
    {
        for (Action action : actions)
        {
            if (action.keyBinding != key) { continue; }
            if (action.modifierKey != mod) { continue; }
            return action;
        }
        return null;
    }

    /* Tries to add missing parameters */
    private static void patch_command()
    {
        if (command.command.equals("//expand") && command.number.isEmpty()) { command.number = "1"; }
        else if (command.command.equals("//contract") && command.number.isEmpty()) { command.number = "1"; }
        else if (command.command.startsWith("/tp"))
        {
            if (MinecraftClient.getInstance() == null || MinecraftClient.getInstance().player == null) { command.command = ""; return; }
            if (command.number.isEmpty()) { command.number = "1"; }

            Vec3d rot = MinecraftClient.getInstance().player.getRotationVector();
            rot = rot.multiply(new Vec3d(1.0, 0.0, 1.0));
            rot = rot.normalize();

            Vec3d[] dirs =
            {
                    new Vec3d(1.0, 0.0, 0.0),
                    new Vec3d(0.0, 0.0, 1.0),
                    new Vec3d(-1.0, 0.0, 0.0),
                    new Vec3d(0.0, 0.0, -1.0),
            };

            float[] dot_products =
            {
                    (float) (rot.x * dirs[0].x + rot.z * dirs[0].z),
                    (float) (rot.x * dirs[1].x + rot.z * dirs[1].z),
                    (float) (rot.x * dirs[2].x + rot.z * dirs[2].z),
                    (float) (rot.x * dirs[3].x + rot.z * dirs[3].z),
            };

            Vec3d forward_dir = dirs[0];
            float max_dot_product = dot_products[0];
            for (int i = 1; i < 4; ++i)
            {
                if (max_dot_product < dot_products[i]) // find most similar vector from dirs to player's rotation vector.
                {
                    max_dot_product = dot_products[i];
                    forward_dir = dirs[i];
                }
            }

            Vec3d right_dir = forward_dir.crossProduct(new Vec3d(0.0, 1.0, 0.0));
            Vec3d player_pos = MinecraftClient.getInstance().player.getPos();
            Vec3d original_pos = player_pos;

            final int command_number = Integer.parseInt(command.number);
            forward_dir = forward_dir.multiply(command_number);
            right_dir = right_dir.multiply(command_number);

            if (command.command.startsWith("/tpr")) { player_pos = player_pos.add(right_dir); }
            else if (command.command.startsWith("/tpl")) { player_pos = player_pos.add(right_dir.multiply(new Vec3d(-1.0, 0.0, -1.0))); }
            else if (command.command.startsWith("/tpd")) { player_pos = player_pos.add(new Vec3d(0.0, -command_number, 0.0)); }
            else if (command.command.startsWith("/tpu")) { player_pos = player_pos.add(new Vec3d(0.0, command_number, 0.0)); }
            else if (command.command.startsWith("/tpf")) { player_pos = player_pos.add(forward_dir); }
            else if (command.command.startsWith("/tpb")) { player_pos = player_pos.add(forward_dir.multiply(new Vec3d(-1.0, 0.0, -1.0))); }

            final Vec3d delta_pos = player_pos.subtract(original_pos);
            command.command = "/tp";
            command.number = String.format("~%f ~%f ~%f", delta_pos.x, delta_pos.y, delta_pos.z);
        }
    }

    private static void execute_command()
    {
        if (MinecraftClient.getInstance().getNetworkHandler() == null)
        {
            send_message("You are not on a server with WorldEdit plugin installed!");
            return;
        }

        if (!command.command.equals("//undo") && !command.command.equals("//redo"))
        {
            previous_commands.add(command.clone());
            if (previous_commands.size() > 128) { previous_commands.removeFirst(); }
        }

        String cmd = command.toString();
        command.clear();
        if (cmd.endsWith("-m"))
        {
            final long window = MinecraftClient.getInstance().getWindow().getHandle();
            GLFW.glfwSetClipboardString(window, cmd + " ");
            old_callback.invoke(window, GLFW.GLFW_KEY_T, GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_T), GLFW.GLFW_PRESS, 0);     // open chat
            old_callback.invoke(window, GLFW.GLFW_KEY_T, GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_T), GLFW.GLFW_RELEASE, 0);   // open chat
            toggle_command_mode();
            send_message("Paste the command in the chat now.");
            return;
        }
        cmd = cmd.substring(1); // remove first slash, as sendChatCommand adds one.
        cmd = cmd.trim();
        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand(cmd);
    }

    private static void send_message(String msg) { if (MinecraftClient.getInstance().player != null) { MinecraftClient.getInstance().player.sendMessage(Text.of(msg), true); } }

    private static boolean is_number_key(int key) { return KEY_ZERO <= key && key <= KEY_NINE; }
    private static boolean is_function_key(int key) { return KEY_F1 <= key && key <= KEY_F12; }

    private static final ArrayList<Action> actions = new ArrayList<>();

    private static boolean command_mode_enabled = false;
    private static GLFWKeyCallback old_callback = null;
    private static Command command = new Command();
    private static final Stack<Command> previous_commands = new Stack<>();
    private static SavedPosition last_used_position = null;

    private static final int KEY_TOGGLE_VIM = GLFW.GLFW_KEY_GRAVE_ACCENT;
    private static final int KEY_ZERO = GLFW.GLFW_KEY_0;
    private static final int KEY_NINE = GLFW.GLFW_KEY_9;
    private static final int KEY_F1 = GLFW.GLFW_KEY_F1;
    private static final int KEY_F12 = GLFW.GLFW_KEY_F12;
}

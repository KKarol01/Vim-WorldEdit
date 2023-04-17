# VimWorldEdit
![Project's logo](/readme_images/logo_100px.png)


## Why?

This:
```
//copy
//flip r
//paste
```
Or:
```
ylfp
```

This:
```
//move 1 u -s -m stone,glowstone,glass
//move 1 u -s -m stone,glowstone,glass
//move 1 u -s -m stone,glowstone,glass
//move 1 u -s -m stone,glowstone,glass
//move 1 u -s -m stone,glowstone,glass
```
Or[^1]:
```
k Cs Cm F1 m ....
```

I think the provided above examples are sufficient without any further commentary.

## How to install
You need Minecraft 1.19.3 (as of writing this), [fabric](https://fabricmc.net/use/installer/) (and fabric api), and this mod, which you can get `here` and put it inside the `mods` folder.
You also need a server with WorldEdit plugin (not included here) and permissions to use the commands it provides (obviously).

## How to use
First, you need to enter the *command mode* by pressing backtick.
An in-game message should appear: ![Screenshot of command mode being turned on](/readme_images/mode_on.png).

Now you are in *command mode*. To exit, you can press escape[^2] or backtick again.
In *command mode* you cannot move and you are accountable for every letter you press (like in real vim).

Now the only thing left to do is to compose a command.
Further in this readme, there is a keymap list, which you should refer to and memorize. I tried to make it fairly easy, as some concepts are borrowed directly from the vim keymapping, and the command keys translate to the first letter of a command they execute (most of the time).
Pressing any other key not included in this list does nothing.

### Simple use
Just press any of the command keys and it will execute immediately.

### Advanced use
Before pressing command keys, you can accumulate modifiers, flags, etc. To move something 10 blocks up, you should input:
`10km` or `k10m`. Notice that the modifier input order does not matter: the only constraint is that the last key must be a command (in this case it's `m`).

You can also add flags by holding `Ctrl` and pressing the existing flag key in WorldEdit (currently there are implemented only `-m`, `-s`, `-a` and `-e`). 
Note that while you need to add `-m` flag when using masks, if you add `-m` without the mask and later execute the command of your choosing, the command will not execute, rather the chat screen will open and the created command will await manual pasting (Ctrl+V). This is because method that was working in the development environment rendered not functional after I exported the mod.

So, if you want to move your selection by one block multiple times upwards, you can do:
`Ctrl+s k m` or `k Ctrl+s m`. Again, the modifier order does not matter. Notice that it runs only once. To run it multiple times, you can just press `.` (the dot key), as it repeats the last issued command.

### Canceling modifiers
If you wanted to type a command that flips the clipboard to the right side, but you accidentaly pressed up/down (so the typed keys are `k` or `j`). The only thing you can do, is to press `Escape` and type your command anew.

### Using masks
Currently, to create a mask, you need to be looking at the block you wish to add to the mask, and press Ctrl+F1-F12. You can can have 12 masks at the same time, hence F1-F12. By pressing functional key with a ctrl, you will erase what was there before, and add a new block.
If you are not looking at any block or the nearest block isn't in the 30 block radius, a message saying that "There is no block in sight" will appear and nothing will happen. To only append the block to the mask, you should hold `Shift` instead of `Ctrl`.
Also, liquids are skipped and will not be added to the mask.

If you feel the need to remind yourself what blocks the mask contains, press `Ctrl+Shift+F1-F12` to print on the screen the contents of the mask.

Now, onto the real usage. To stack 10 times forwards only the glass in your selection, you should first look upon the glass, enter the *command mode*, press `Ctrl+F1-F12` (let's assume the functional key pressed here was F1) to clear the F1 mask and add the glass block to it.
Then: `10 Shift+k Ctrl+m F1 s` (or you can omit the direction, because `//stack` command will stack in the direction the player is looking if the direction is omitted).

If you want to move everything *except* the glass, leave your mask be (the one with only glass block) and type: `10 Shift+k Ctrl+m Alt+F1 s`.
By pressing alt, the negated version of your mask will be pasted.

If you want to stack glass *and glowstone*, point your camera to the glowstone, press `Shift+F1`, to append glowstone to the mask under `F1` (which, in this example already has glass added to it), and then type: `10 Shift+k Ctrl+m F1 s`.

### Keymap
Command keys:
- `E` - `//expand`
- `C` - `//contract`
- `M` - `//move`
- `S` - `//stack`
- `Y` - `//copy`
- `P` - `//paste`
- `F` - `//flip`
- `U` - `//undo`
- `Shift+U` - `//redo`
- `D` - `//set 0`
- `R` - `//rotate`

Direction keys:
- `H` - `left`
- `J` - `down`
- `K` - `up`
- `L` - `right`
- `Shift+J` - `back`
- `Shift+K` - `forward`

Number Keys:
- `0 - 9` - `Number keys from the keyboard's number row.`

Flags:
- `Ctrl+E` - `-e`
- `Ctrl+A` - `-a`
- `Ctrl+S` - `-s`
- `Ctrl+M` - `-m`

Masks[^3]: 
- `F1-F12` - `Include the mask in the command`
- `Alt+F1-F12` - `Include negated version of the mask in the command`
- `Ctrl+F1-F12` - `Clear mask and add a block that you're looking at`
- `Shift+F1-F12` - `Aggregate a block that you're looking at to the mask`
- `Ctrl+Shift+F1-F12` - `Print the content of the mask`

Special keys:
- `.` - `Repeat the last command (executed by the mod, not the last one typed by hand in the chat). Can be prefixed with a number (from 1 to 10) to repeat the last x number of commands in order.`
- `Backtick` - `Toggles the command mode`
- `Escape` - `Cancels pending command or, if there is no such command (no accumulated flags, modifiers), exits the command mode`

[^1]: Where "C" means holding control key before pressing the button to the right, "F1" is a functional key, and you have saved "stone,glowstone,glass" to the "F1" mask. Spaces included for readability.

[^2]: If you are halfway through writing command, ie. pressed some buttons like flags but didn't press a button that is responsible for actual command, the first "escape" press will cancel the command, and only the next one exits.

[^3]: Where F1-F12 means any functional key ranging from F1 to F12 (inclusive).


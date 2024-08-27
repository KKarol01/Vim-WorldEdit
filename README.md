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
This: (if you don't know the exact number - also useful for little moves to adjust something)
```
//stack 1 -s
//stack 1 -s
//stack 1 -s
//stack 1 -s
```
Or:
```
Cs s . . .
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
This mod works with minecraft versions 1.19.3-1.20.1, [fabric](https://fabricmc.net/use/installer/) (and fabric api), and this mod, which you can get `here` and put it inside the `mods` folder.
You also need a server with WorldEdit plugin (not included here) and permissions to use the commands it provides (obviously).

## How to use
First, you need to enter the *command mode* by pressing backtick.
An in-game message should appear: ![Screenshot of command mode being turned on](/readme_images/mode_on.png).

Now you are in *command mode*. To exit, you can press escape or "w"[^2], or backtick again.
In *command mode* you cannot move (with WSAD, but Arrow keys work) and you are accountable for every letter you press (like in real vim).

Now the only thing left to do is to compose a command.
Further in this readme, there is a [keymap list](https://github.com/ThinCan/Vim-WorldEdit/blob/main/README.md#keymap), which you should refer to and memorize. I tried to make it fairly easy, as some concepts are borrowed directly from the vim keymapping, and the command keys translate to the first letter of a command they execute (most of the time).
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
If you wanted to type a command that flips the clipboard to the right side, but you accidentaly pressed up/down (so the typed keys are `k` or `j`). The only thing you can do, is to press `Escape` or `W` and type your command anew.

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

### Movement
Starting from version `1.0.2`, you can move without exiting the command mode (for example to copy, flip and paste selection that does not have a single center and requires you to strafe one block to the left or right).
To do that, you can now press the arrow keys. Arrow keys can be repeated with "`.`" command and combined with number keys to move farther than one block. For example: `Arrow up` teleports you forward, `Arrow right` teleports you to the right, relative to the current looking direction. `Shift + Arrow Up` and `Shift + Arrow down` moves up and down.

### Saving positions and teleporting (bookmarks)
Starting from version `1.0.2`, you can save locations to teleport to them later (kinda like sethome).
To save a location, you need to position yourself at a place of interest, and then type a number: it can be 1, 2, 3, etc. up to 2147483647; then you need to press `Shift + '` (that's an apostrophe).
If you do it again, you will overwrite this location with your current one.
To teleport to that location, you enter the corresponding number and press `'`.

If you don't type the number, and just enter sole `'` your current position will be saved, and you will be teleported to the previously saved position.
So you can first enter a number to teleport to the saved position, then you can just press `'` to go back to where you were before teleporting.
For `'` to work without entering bookmark number, you need to have teleported to the bookmarked location at least once. 
Keep in mind that pressing `'` overwrites the last position, so if you used apostrophe to go back to the last bookmarked location, and moved somewhere, then pressed it again to go back to where you were, the next `'` press will not teleport you to the bookmarked place as it did the first time (that's what `'` with entered location does), but to the place you went to, before going back to where you were.
That means you can have two moving locations, that you can teleport to between, and which will update to your last position before teleporting to the other.

I know it's kinda complicated at first reading, but it's super simple and useful. In essence: you have two, seperate, moving-with-you locations, that you can jump to with each press of the `'`.

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
- `Shift + U` - `//redo`
- `D` - `//set 0`
- `R` - `//rotate`
- `Arrow Up` - `Moves the player one block forward`
- `Arrow Right` - `Moves the player one block right`
- `Arrow Down` - `Moves the player one block back`
- `Arrow Left` - `Moves the player one block left`
- `Shift + Arrow Up` - `Moves the player one block up`
- `Shift + Arrow Down` - `Moves the player one block down`
- `'` - `Teleport to saved position`
- `Shift + '` - `Save position`

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
- `W` - `Cancels pending command or, if there is no such command (no accumulated flags, modifiers), exits the command mode, **and**, upon exiting the command mode, makes Steve immediately walk forward`[^4]

## Where is the sauce?
The main code is in the location: [VimWorldEditClient.java](https://github.com/ThinCan/Vim-WorldEdit/blob/main/src/client/java/com/vimworldedit/VimWorldEditClient.java)

[^1]: Where "C" means holding control key before pressing the button to the right, "F1" is a functional key, and you have saved "stone,glowstone,glass" to the "F1" mask. Spaces included for readability.

[^2]: First escape or w cancel whatever command you were writing, and only if the command is empty (nothing there or was just cleared), will those keys exit from editing mode.

[^3]: Where F1-F12 means any functional key ranging from F1 to F12 (inclusive).

[^4]: I found out that I sometimes forget that I am in command mode and get irritated that in my mind I was already walking forward, but in reality was actually hindered by forgetting to exit the mode first. This is super QoL change.

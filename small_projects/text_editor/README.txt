Socket Canvas
-------------
A simple, Tkinter-based implementation of a canvas that receives commands
and sends events over a plain TCP socket. All commands and events are
comma-delimited, terminated with a newline character.

Prerequistes
------------
Python 2.7

Running
-------
python socket_canvas.py

Listens on port 5005.
Opens a window on the host OS when a connection is established.
Closes window when the connection is closed.

Commands
--------
Drawing a red square (rect,x,y,width,height,color):
rect,0,0,100,100,#ff0000

Drawing "Hello, world!" (text,x,y,color,string):
text,0,0,#000000,Hello world!

NOTE: Characters are rendering using a monospace font, measuring 8x14 per character

Events
------
Window resize (resize,width,height):
resize,1024,768

Mouse events (mouseevent,x,y):
mousedown,100,100
mouseup,100,100
mousemove,100,100

Keyboard events (keyevent,key_char_or_code):
keydown,a
keyup,a

Special key codes
------------------
For some keys, a special string is in the "key_char_or_code" field:

Return
Tab
space
Up
Down
Left
Right
BackSpace
Escape
LeftShift
LeftControl
LeftAlt
LeftCommand
RightCommand
RightAlt
RightControl
RightShift


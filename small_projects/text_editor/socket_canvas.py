from Tkinter import *
import signal
import socket
import struct
import sys
import Queue
import threading

root = Tk()

# Tkinter canvas seems to clips the left side of some text if we draw to 0,0
# so we offset all x coordinates by LEFT_PAD
LEFT_PAD = 3
TOP_PAD = 3

# Keycodes for modifier keys - might be OS specific
MODIFIER_KEYS = {
  '131074': 'LeftShift',
  '262145': 'LeftControl',
  '524320': 'LeftAlt',
  '1048584': 'LeftCommand',
  '1048592': 'RightCommand',
  '524352': 'RightAlt',
  '270336': 'RightControl',
  '131076': 'RightShift'
};

def handle_conn(conn):
  def send_event(*args):
    conn.send(','.join(map(str, args)) + '\n')

  # Queue for sending commands received on the socket to the canvas
  queue = Queue.Queue()
  window = Toplevel()
  window.wm_title("Hello")

  canvas_width = 800
  canvas_height = 600
  w = Canvas(window,
             width=canvas_width - LEFT_PAD,
             height=canvas_height - TOP_PAD)
  w.pack(expand=YES, fill=BOTH)

  def on_mousedown(event):
    w.focus_set()
    print "mousedown: %d, %d" % (event.x, event.y)
    send_event('mousedown', event.x - LEFT_PAD, event.y - TOP_PAD)

  def on_mouseup(event):
    w.focus_set()
    print "mouseup: %d, %d" % (event.x, event.y)
    send_event('mouseup', event.x - LEFT_PAD, event.y - TOP_PAD)

  def on_drag(event):
    print "mousemove: %d, %d" % (event.x, event.y)
    send_event('mousemove', event.x - LEFT_PAD, event.y - TOP_PAD)

  def resolve_keysym(event):
    if len(event.keysym) == 2 and str(event.keycode) in MODIFIER_KEYS:
      event.keysym = MODIFIER_KEYS[str(event.keycode)]

  def on_keydown(event):
    resolve_keysym(event)
    print 'keydown: %s' % event.keysym
    send_event('keydown', event.keysym)

  def on_keyup(event):
    resolve_keysym(event)
    print 'keyup: %s' % event.keysym
    send_event('keyup', event.keysym)

  def on_configure(event):
    print "resize: %d, %d" % (event.width, event.height);
    send_event('resize', event.width - LEFT_PAD, event.height - TOP_PAD)

  def on_quit(event):
    window.destroy()

  def process_commands():
    while queue.qsize():
      command = queue.get(0).strip()
      print 'Received command: %s' % command

      command, comma, remaining = command.partition(',')
      if command == 'quit':
        on_quit(None)
        return

      print 'Actual command: "%s"' % command

      x, comma, remaining = remaining.partition(',')
      x = int(x) + LEFT_PAD

      y, comma, remaining = remaining.partition(',')
      y = int(y) + TOP_PAD

      if command == 'rect':
        width, comma, remaining = remaining.partition(',')
        height, comma, color = remaining.partition(',')
        width = int(width)
        height = int(height)
        print 'color:' + color
        w.create_rectangle(x, y, x + width, y + height, fill=color, width=0)

      if command == 'text':
        color, comma, text = remaining.partition(',')
        w.create_text(x, y, text=text, anchor=NW, fill=color, font='Courier')

    w.after(10, process_commands)

  w.bind("<Button-1>", on_mousedown)
  w.bind("<ButtonRelease-1>", on_mouseup)
  w.bind("<Motion>", on_drag)
  w.bind("<Key>", on_keydown)
  w.bind("<KeyRelease>", on_keyup)
  w.bind("<Control-q>", on_quit)
  w.bind("<Configure>", on_configure)

  def read_commands():
    buffer = ''
    while True:
      data = conn.recv(100)
      if not data:
        break

      buffer = buffer + data
      index = buffer.find('\n')
      while index != -1:
        command = buffer[0:index]
        if len(command) > 0:
          queue.put(command)
        buffer = buffer[index + 1:]
        index = buffer.find('\n')

    queue.put('quit');

  thread = threading.Thread(target=read_commands)
  thread.daemon = True
  thread.start()

  process_commands()

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

print 'Listening on 5005...'
sock.bind(('127.0.0.1', 5005))
sock.listen(1)

def listen():
  while True:
    conn, addr = sock.accept()
    print 'Connection accepted...'

    handle_conn(conn)

listen_thread = threading.Thread(target=listen)
listen_thread.daemon = True
listen_thread.start()

def handle_close(event):
  root.destroy()

root.bind('WM_DELETE_WINDOW', handle_close);
root.mainloop()

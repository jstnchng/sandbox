" Use Vim settings, rather then Vi settings (much better!).
" This must be first, because it changes other options as a side effect.
set nocompatible
" Adjust esckeys option timeout length
set timeoutlen=300
syn on
filetype off                  " required

" Plugins
" Vundle allows easy installation of vim plugins
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
Plugin 'VundleVim/Vundle.vim'
Plugin 'tpope/vim-fugitive'
" plugin from http://vim-scripts.org/vim/scripts.html
Plugin 'L9'
" Git plugin not hosted on GitHub
Plugin 'git://git.wincent.com/command-t.git'
" The sparkup vim script is in a subdirectory of this repo called vim.
Plugin 'rstacruz/sparkup', {'rtp': 'vim/'}
" NERDtree lets you view the file system in a tree structure
Plugin 'scrooloose/nerdtree'
" easy commenting
Plugin 'scrooloose/nerdcommenter'
" faster motion
Plugin 'easymotion/vim-easymotion'
Plugin 'kien/rainbow_parentheses.vim'
Plugin 'junegunn/seoul256.vim'
" Airline status bar
Plugin 'bling/vim-airline'
Plugin 'jeetsukumaran/vim-buffergator'
call vundle#end()            " required
filetype plugin indent on    " required

set history=1000                "Store lots of :cmdline history
set showcmd                     "Show incomplete cmds down the bottom
set showmode                    "Show current mode down the bottom
set gcr=a:blinkon0              "Disable cursor blink
set visualbell                  "No sounds
set autoread                    "Reload files changed outside vim
set backspace=indent,eol,start  "Make backspace work properly
set laststatus=2                "Turn on status line
" set exrc                        "Enable per-directory .vimrc files
set secure                      "Disable unsafe commands in local .vimrc files
set incsearch                   "Search incrementally instead of after I press enter
set number                      "Show line numbers
" This makes vim act like all other editors, buffers can
" exist in the background without being in a window.
set hidden
" turn on syntax highlighting
syntax enable
" Automatically remove trailing whitespace
autocmd BufWritePre * :%s/\s\+$//e

set expandtab
set tabstop=4
set shiftwidth=4
set selection=exclusive
" makes yank/delete operations copy to clipboard
set clipboard=unnamed
" folds by indents, folds automatically disabled
set foldmethod=indent
set nofoldenable

" ================ Turn Off Swap Files ==============
set noswapfile
set nobackup
set nowb

" Mappings
let mapleader = "\<Space>"
map <leader>k 10k
map <leader>j 10j
map <leader>w 5w
map <leader>b 5b
nnoremap <leader>O O<ESC>O
nnoremap <leader>o o<cr>
nnoremap <silent><leader>\ :set relativenumber!<cr>

" Settings for plugins
" add spaces after comment delimiters by default
let g:NERDSpaceDelims = 1
" Allow commenting and inverting empty lines (useful when commenting a region)
let g:NERDCommentEmptyLines = 1
" Enable trimming of trailing whitespace when uncommenting
let g:NERDTrimTrailingWhitespace = 1
" Enable the list of buffers
let g:airline#extensions#tabline#enabled = 1
" Show just the filename
let g:airline#extensions#tabline#fnamemod = ':t'
let g:rbpt_colorpairs = [
    \ ['brown',       'RoyalBlue3'],
    \ ['Darkblue',    'SeaGreen3'],
    \ ['darkgray',    'DarkOrchid3'],
    \ ['darkgreen',   'firebrick3'],
    \ ['darkcyan',    'RoyalBlue3'],
    \ ['darkred',     'SeaGreen3'],
    \ ['darkmagenta', 'DarkOrchid3'],
    \ ['brown',       'firebrick3'],
    \ ['gray',        'RoyalBlue3'],
    \ ['darkmagenta', 'DarkOrchid3'],
    \ ['Darkblue',    'firebrick3'],
    \ ['darkgreen',   'RoyalBlue3'],
    \ ['darkcyan',    'SeaGreen3'],
    \ ['darkred',     'DarkOrchid3'],
    \ ['red',         'firebrick3'],
    \ ]
let g:rbpt_max = 15
let g:rbpt_loadcmd_toggle = 0
let g:seoul256_background = 233
color seoul256

" autocommands
autocmd StdinReadPre * let s:std_in=1
autocmd VimEnter * if argc() == 0 && !exists("s:std_in") | NERDTree | endif
autocmd VimEnter * RainbowParenthesesToggle
au Syntax * RainbowParenthesesLoadRound
au Syntax * RainbowParenthesesLoadSquare
au Syntax * RainbowParenthesesLoadBraces
au BufRead,BufNewFile *.md setlocal textwidth=80
au BufRead,BufNewFile *.txt setlocal textwidth=80
au FileType javascript setlocal ts=2 sw=2


#!/usr/bin/env bash

PROJ_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

function ensure_symlink () {
  local src="$1"
  local dst="$2"
  local dstlnk="$(readlink -f $2)"


  if [ "$src" = "$dstlnk" ]; then
    echo "OK: $src already exists and points to $dst so we're good"
    return 0
  fi

  if [ -e "$dst" ]; then
    local bak="$dst.$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
    echo "$dst already exists, but is not $src, moving it aside to: $bak"
    mv $dst $bak
  fi

  echo "linking $src to $dst"
  ln -s "$src" "$dst"
}

ensure_symlink $PROJ_ROOT/.zshrc $HOME/.zshrc
ensure_symlink $PROJ_ROOT/.gitconfig $HOME/.gitconfig


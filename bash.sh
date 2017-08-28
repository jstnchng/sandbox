if ! grep -q /usr/local/bin/bash /etc/shells; then
   echo \"/usr/local/bin/bash\" | sudo tee -a /etc/shells
fi
    sudo chsh -s  /usr/local/bin/bash $LOGNAME

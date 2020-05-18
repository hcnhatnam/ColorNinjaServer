mvn clean compile assembly:single
sshpass -p "v7h3tVdpBtkbmkv" rsync -rauhv "$PWD" -e "ssh -i /home/namhcn/Documents/zalo/vngsave/key/keycmc" root@119.82.135.105:~/
sshpass -p "v7h3tVdpBtkbmkv" ssh -i /home/namhcn/Documents/zalo/vngsave/key/keycmc root@119.82.135.105 sh /root/ColorNinjaServer/runservice


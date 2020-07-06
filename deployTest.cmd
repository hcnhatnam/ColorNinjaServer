mvn clean compile assembly:single
sshpass -p "grnt2mba3AzcQue" rsync -rauhv "$PWD" -e "ssh -i /home/namhcn/Documents/zalo/vngsave/key/keycmc" root@119.82.135.105:~/test
sshpass -p "grnt2mba3AzcQue" ssh -i /home/namhcn/Documents/zalo/vngsave/key/keycmc root@119.82.135.105 sh /root/test/ColorNinjaServer/runservicetest


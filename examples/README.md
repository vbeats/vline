# example

## [vline-ex-jtds](vline-ex-jtds)

```bash

sql server 2000

cache: sqlite

db已读数据可以缓存到sqlite ( 具体业务处理)

```

## [vline-ex-sp](vline-ex-sp)

串口间数据转发

```bash

serial port 1 ---|---- serial port a
                 |
serial port 2 ---|---- serial port b
                 |
serial port x ---|---- serial port y
                
# socat创建虚拟串口

socat -d -d pty,raw,echo=0 pty,raw,echo=0

# demo环境

# vmware 虚拟机添加2个pipe serial port

\\.\pipe\com_1  -- /dev/ttyS0
\\.\pipe\com_2  -- /dev/ttyS1

# 虚拟机内socat socat创建2个虚拟串口对

/dev/pts/2 --- /dev/pts/3
/dev/pts/4 --- /dev/pts/5


# 虚拟机外 putty 连接串口 收发数据
# 虚拟机内 minicom 连接串口 收发数据
```

## [vline-ex-sp2](vline-ex-sp2)

读串口数据 转发到其它node

```bash

serial port 1 ---|
                 |
serial port 2 ---|---- node a
                 |
serial port x ---|

# 共享串口

# com1 com2 com3 任意com口收发数据 其它com口都可以收到数据
```
help:
    @just --list

run:
    mvn clean
    mvn package -DskipTests
    mvn dependency:copy-dependencies
    clear
    @java -p target/dependency:target/classes --enable-native-access=dev.mccue.boba -m dev.mccue.boba/dev.mccue.boba.simpletea.Main
run-tct:
    mvn clean
    mvn package -DskipTests
    mvn dependency:copy-dependencies
    clear
    @java -p target/dependency:target/classes --enable-native-access=dev.mccue.boba -m dev.mccue.boba/dev.mccue.boba.tictactoe.Main

extract_mac:
    jextract \
        --output src/main/java \
        --target-package dev.mccue.boba.c.mac \
        --include-struct termios \
        --include-struct winsize \
        --include-function ioctl \
        --include-function tcgetattr \
        --include-function tcsetattr \
        --include-constant EBADF \
        --include-constant EFAULT \
        --include-constant EINVAL \
        --include-constant ENOTTY \
        --include-constant VINTR \
        --include-constant VQUIT \
        --include-constant VERASE \
        --include-constant VEOF \
        --include-constant VEOL \
        --include-constant VEOL2 \
        --include-constant VSTART \
        --include-constant VSTOP \
        --include-constant VSUSP \
        --include-constant VWERASE \
        --include-constant VREPRINT \
        --include-constant VLNEXT \
        --include-constant VDISCARD \
        --include-constant IGNPAR \
        --include-constant PARMRK \
        --include-constant INPCK \
        --include-constant ISTRIP \
        --include-constant INLCR \
        --include-constant IGNCR \
        --include-constant ICRNL \
        --include-constant IXON \
        --include-constant IXANY \
        --include-constant IXOFF \
        --include-constant IMAXBEL \
        --include-constant OPOST \
        --include-constant ONLCR \
        --include-constant OCRNL \
        --include-constant ONOCR \
        --include-constant ONLRET \
        --include-constant CS7 \
        --include-constant CS8 \
        --include-constant PARENB \
        --include-constant PARODD \
        --include-constant ISIG \
        --include-constant ICANON \
        --include-constant ECHO \
        --include-constant ECHOE \
        --include-constant ECHOK \
        --include-constant ECHONL \
        --include-constant NOFLSH \
        --include-constant TOSTOP \
        --include-constant IEXTEN \
        --include-constant ECHOCTL \
        --include-constant ECHOKE \
        --include-constant PENDIN \
        --include-constant TIOCGETA \
        --include-constant TIOCSETA \
        --include-constant TIOCGWINSZ \
        --include-constant TCGETS \
        --include-constant TCSETS \
        --include-constant IGNBRK \
        --include-constant BRKINT \
        --include-constant PARMRK \
        --include-constant ISTRIP \
        --include-constant INLCR \
        --include-constant IGNCR \
        --include-constant ICRNL \
        --include-constant IXON \
        --include-constant OPOST \
      	--include-constant ECHO \
      	--include-constant ECHONL \
      	--include-constant ICANON \
      	--include-constant ISIG \
      	--include-constant IEXTEN \
      	--include-constant CSIZE \
       	--include-constant PARENB \
      	--include-constant CS8 \
      	--include-constant VMIN \
      	--include-constant VTIME \
        ioctl.h


extract_linux:
    jextract \
        --output src/main/java \
        --target-package dev.mccue.boba.c.linux \
        --include-struct termios \
        --include-function ioctl \
        --include-function tcgetattr \
        --include-function tcsetattr \
        --include-constant EBADF \
        --include-constant EFAULT \
        --include-constant EINVAL \
        --include-constant ENOTTY \
        --include-constant VINTR \
        --include-constant VQUIT \
        --include-constant VERASE \
        --include-constant VEOF \
        --include-constant VEOL \
        --include-constant VEOL2 \
        --include-constant VSTART \
        --include-constant VSTOP \
        --include-constant VSUSP \
        --include-constant VWERASE \
        --include-constant VREPRINT \
        --include-constant VLNEXT \
        --include-constant VDISCARD \
        --include-constant IGNPAR \
        --include-constant PARMRK \
        --include-constant INPCK \
        --include-constant ISTRIP \
        --include-constant INLCR \
        --include-constant IGNCR \
        --include-constant ICRNL \
        --include-constant IXON \
        --include-constant IXANY \
        --include-constant IXOFF \
        --include-constant IMAXBEL \
        --include-constant OPOST \
        --include-constant ONLCR \
        --include-constant OCRNL \
        --include-constant ONOCR \
        --include-constant ONLRET \
        --include-constant CS7 \
        --include-constant CS8 \
        --include-constant PARENB \
        --include-constant PARODD \
        --include-constant ISIG \
        --include-constant ICANON \
        --include-constant ECHO \
        --include-constant ECHOE \
        --include-constant ECHOK \
        --include-constant ECHONL \
        --include-constant NOFLSH \
        --include-constant TOSTOP \
        --include-constant IEXTEN \
        --include-constant ECHOCTL \
        --include-constant ECHOKE \
        --include-constant PENDIN \
        --include-constant TCGETS \
        --include-constant TCSETS \
        --include-constant IGNBRK \
        --include-constant BRKINT \
        --include-constant PARMRK \
        --include-constant ISTRIP \
        --include-constant INLCR \
        --include-constant IGNCR \
        --include-constant ICRNL \
        --include-constant IXON \
        --include-constant OPOST \
      	--include-constant ECHO \
      	--include-constant ECHONL \
      	--include-constant ICANON \
      	--include-constant ISIG \
      	--include-constant IEXTEN \
      	--include-constant CSIZE \
       	--include-constant PARENB \
      	--include-constant CS8 \
      	--include-constant VMIN \
      	--include-constant VTIME \
      	--include-constant TCGETS \
      	--include-constant TCSETS \
        ioctl.h

extract_windows:
    jextract \
        --output src/main/java \
        --target-package dev.mccue.boba.c.windows \
        --include-function GetConsoleMode \
        --include-function SetConsoleMode \
        --include-constant ENABLE_ECHO_INPUT \
        --include-constant ENABLE_PROCESSED_INPUT \
        --include-constant ENABLE_LINE_INPUT \
        --include-constant ENABLE_PROCESSED_OUTPUT \
        --include-typedef HANDLE \
        --include-function GetConsoleScreenBufferInfo \
        --include-function GetCurrentProcess \
        --include-function DuplicateHandle \
        --include-constant DUPLICATE_SAME_ACCESS \
        --include-function GetStdHandle \
        --include-constant STD_OUTPUT_HANDLE \
        windows.h

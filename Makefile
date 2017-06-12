.PHONY: all

BUKKIT=lib/bukkit-1.6.2-R0.1.jar
SRCS=src/com/scott_weldon/xp_to_emerald/*.java
BINS=bin/com/scott_weldon/xp_to_emerald/*.class
VER=2.0-dev

all: $(SRCS)
	javac -d bin -classpath $(BUKKIT) $(SRCS)
	jar -cf jars/XPtoEmerald-$(VER).jar $(BINS)

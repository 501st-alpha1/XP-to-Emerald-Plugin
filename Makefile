.PHONY: all clean

BUKKIT=lib/bukkit-1.6.2-R0.1.jar
BUKKIT_VERSIONS=1.6.2-R0.1
JARS=$(BUKKIT_VERSIONS:%=jars/XPtoEmerald-$(VER)-%.jar)
SRCS=src/com/scott_weldon/xp_to_emerald/*.java
BINS=bin/com/scott_weldon/xp_to_emerald/*.class
VER=2.0-dev

all: $(JARS)

clean:
	rm $(JARS)

jars/XPtoEmerald-$(VER)-%.jar: lib/bukkit-%.jar $(SRCS)
	javac -d bin -classpath $< $(SRCS)
	jar -cf $@ $(BINS)

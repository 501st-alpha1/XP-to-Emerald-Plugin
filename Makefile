.PHONY: all clean

BUKKIT=lib/bukkit-1.6.2-R0.1.jar
BUKKIT_VERSIONS=1.6.2-R0.1 1.6.2-R1.0
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

# FIXME: Figure out way to use Git features, e.g. submodules, to do this.
bukkit:
	git clone "https://hub.spigotmc.org/stash/scm/spigot/bukkit.git"

bukkit-%: bukkit
	cd bukkit; \
	git remote update; \
	git checkout $*

.PHONY: all clean

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

bukkit-build:
	cd bukkit; \
	mvn -DskipTests package

bukkit-cp-%:
	cp bukkit/target/$* lib/

bukkit-cp-snap-%:
	cp bukkit/target/$(*:%.jar=%-SNAPSHOT.jar) lib/$*

bukkit-checkout-%: bukkit
	cd bukkit; \
	git remote update; \
	git checkout $*

lib/bukkit-1.7.10-R0.1.jar: bukkit-checkout-a329bc5 bukkit-build
	make bukkit-cp-snap-$(@F)

BUKKIT=/mnt/data/games/Minecraft/Bukkit/API/bukkit-1.6.2-R0.1.jar

all: XPtoEmerald.class XPtoEmeraldListener.class

%.class: src/com/scott_weldon/xp_to_emerald/%.java
	javac -d bin -classpath $(BUKKIT) $<

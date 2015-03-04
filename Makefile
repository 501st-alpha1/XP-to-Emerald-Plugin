BUKKIT=/mnt/data/games/Minecraft/Bukkit/API/bukkit-1.6.2-R0.1.jar
SRCS=src/com/scott_weldon/xp_to_emerald/*.java

all: $(SRCS)
	javac -d bin -classpath $(BUKKIT) $(SRCS)

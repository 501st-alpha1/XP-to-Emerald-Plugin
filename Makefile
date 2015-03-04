all: XPtoEmerald.class XPtoEmeraldListener.class

%.class: src/com/scott_weldon/xp_to_emerald/%.java
	javac -d bin $<

package kr.neko.sokcuri.naraechat.Obfuscated;

public class ReflectionFieldInfo<O, T> {
    static public <O, T> ReflectionFieldInfo create(String name, Class<T> type, Class<O> owner, int depth) {
        return new ReflectionFieldInfo(name, type, owner, depth);
    }

    ReflectionFieldInfo(String name, Class<T> type, Class<O> owner, int depth) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.depth = depth;
    }

    private String name;
    private Class<T> type;
    private Class<O> owner;
    private int depth;

    public String getName() {
        return this.name;
    }

    public Class<T> getTypeClass() {
        return this.type;
    }

    public Class<O> getOwnerClass() {
        return this.owner;
    }

    public int getDepth() {
        return this.depth;
    }
}
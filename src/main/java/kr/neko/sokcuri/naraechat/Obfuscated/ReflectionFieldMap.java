package kr.neko.sokcuri.naraechat.Obfuscated;

import net.minecraft.client.gui.components.EditBox;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReflectionFieldMap<T> {
    private HashMap<String, List<ReflectionFieldInfo>> map = new HashMap<>();
    private Class<T> target;

    public ReflectionFieldMap(Class<T> target) {
        this.target = target;
    }

    public List<ReflectionFieldInfo> getReflectionArray(List<ReflectionFieldInfo> array, Class<?> owner, int depth) {
        for (Field field : owner.getDeclaredFields()) {
            array.add(ReflectionFieldInfo.create(field.getName(), field.getType(), owner, depth));
        }

        if (owner.getSuperclass() != null) {
            getReflectionArray(array, owner.getSuperclass(), depth + 1);
        }
        return array;
    }

    public T findField(Object owner) {
        String className = owner.getClass().getName();
        if (!map.containsKey(className)) {
            List<ReflectionFieldInfo> fieldInfoArray = getReflectionArray(new ArrayList(), owner.getClass(), 0);
            List<ReflectionFieldInfo> savedInfo = new ArrayList<>();

            for (ReflectionFieldInfo fieldInfo : fieldInfoArray) {
                if (fieldInfo.getTypeClass() == target) {
                    savedInfo.add(fieldInfo);
                }
            }

            map.put(className, savedInfo);
        }

        if (map.containsKey(className)) {
            for (ReflectionFieldInfo fieldInfo : map.get(className)) {
                Object obj = ObfuscationReflectionHelper.getPrivateValue(fieldInfo.getOwnerClass(), owner, fieldInfo.getName());
                if (obj instanceof EditBox) {
                    if (((EditBox)obj).isFocused()) {
                        return (T)obj;
                    }
                } else {
                    return (T)obj;
                }
            }
        }
        return null;
    }
}

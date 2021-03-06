package pt.andre.projecto.Model.Utils;


import java.util.HashMap;

/**
 * A implementation of a HashMap, that has a default value, in case of the key not being valid
 */
public class DefaultHashMap<T, T1> extends HashMap<T, T1>{
    private final T1 defaultValue;

    public DefaultHashMap(T1 defaultValue){
        this.defaultValue = defaultValue;
    }

    @Override
    public T1 get(Object key) {
        return containsKey(key) ? super.get(key) : defaultValue;
    }
}

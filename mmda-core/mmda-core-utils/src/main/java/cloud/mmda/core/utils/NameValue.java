package cloud.mmda.core.utils;

public class NameValue<K,V> {

    private final K name;
    public K getName(){return name;}

    private final V value;
    public V getValue(){return value;}

    public NameValue(K name, V value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof NameValue) {
            NameValue pair = (NameValue) o;
            if (name != null ? !name.equals(pair.name) : pair.name != null) return false;
            if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
            return true;
        }
        return false;
    }
}

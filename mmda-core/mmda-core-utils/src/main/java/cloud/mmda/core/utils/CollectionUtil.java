/*
 *  Copyright (c) 2022-2025, Mybatis-Flex (fuhai999@gmail.com).
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cloud.mmda.core.utils;

import java.util.*;
import java.util.function.Function;

public final class CollectionUtil {

    private CollectionUtil() {
    }


    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean hasAny(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }


    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean hasAny(Map<?, ?> map) {
        return !isNullOrEmpty(map);
    }

    /**
     * 合并 list
     */
    public static <T> List<T> merge(List<T> list, List<T> other) {
        if (list == null && other == null) {
            return new ArrayList<>();
        } else if (isNullOrEmpty(other) && list != null) {
            return list;
        } else if (isNullOrEmpty(list)) {
            return other;
        }
        List<T> newList = new ArrayList<>(list);
        newList.addAll(other);
        return newList;
    }


    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 主要是用于修复 concurrentHashMap 在 jdk1.8 下的死循环问题
     *
     * @see <a href="https://bugs.openjdk.org/browse/JDK-8161372">https://bugs.openjdk.org/browse/JDK-8161372</a>
     */
    public static <K, V> V computeIfAbsent(Map<K, V> concurrentHashMap, K key, Function<? super K, ? extends V> mappingFunction) {
        V v = concurrentHashMap.get(key);
        if (v != null) {
            return v;
        }
        return concurrentHashMap.computeIfAbsent(key, mappingFunction);
    }


    public static <T> List<T> toList(Collection<T> collection) {
        if (collection instanceof List) {
            return (List<T>) collection;
        } else {
            return new ArrayList<>(collection);
        }
    }

    public static String[] toArrayString(Collection<?> collection) {
        if (isNullOrEmpty(collection)) {
            return new String[0];
        }
        String[] results = new String[collection.size()];
        int index = 0;
        for (Object o : collection) {
            results[index++] = String.valueOf(o);
        }
        return results;
    }

//    @SuppressWarnings("all")
//    public static <E extends CloneSupport<E>> List<E> cloneArrayList(List<E> list) {
//        if (list == null) {
//            return null;
//        }
//        List<E> arrayList = new ArrayList<>(list.size());
//        for (E e : list) {
//            arrayList.add(e.clone());
//        }
//        return arrayList;
//    }


    public static <T> Set<T> newHashSet(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }


    public static <T> List<T> newArrayList(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }


    public static <E> ArrayList<E> newArrayList(Collection<E> collection) {
        if (isNullOrEmpty(collection)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(collection);
    }


    public static <K, V> HashMap<K, V> newHashMap(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return new HashMap<>();
        }
        return new HashMap<>(map);
    }



    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @param <T>   数组类型
     * @return {@code true} 数组为空，{@code false} 数组不为空
     */
    public static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }


    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @param <T>   数组类型
     * @return {@code true} 数组不为空，{@code false} 数组为空
     */
    public static <T> boolean hasAny(T[] array) {
        return !isNullOrEmpty(array);
    }


    /**
     * 合并两个数组为一个新的数组
     *
     * @param first  第一个数组
     * @param second 第二个数组
     * @param <T>
     * @return 新的数组
     */
    public static <T> T[] concat(T[] first, T[] second) {
        if (first == null && second == null) {
            throw new IllegalArgumentException("not allow first and second are null.");
        } else if (isNullOrEmpty(first) && second != null) {
            return second;
        } else if (isNullOrEmpty(second)) {
            return first;
        } else {
            T[] result = Arrays.copyOf(first, first.length + second.length);
            System.arraycopy(second, 0, result, first.length, second.length);
            return result;
        }
    }


    public static <T> T[] concat(T[] first, T[] second, T[] third, T[]... others) {
        T[] results = concat(first, second);
        results = concat(results, third);

        if (others != null && others.length > 0) {
            for (T[] other : others) {
                results = concat(results, other);
            }
        }
        return results;
    }


    /**
     * 可变长参形式数组
     *
     * @param first  第一个数组
     * @param second 第二个数组
     * @param <T>
     * @return 新的数组
     */
    @SafeVarargs
    public static <T> T[] append(T[] first, T... second) {
        if (first == null && second == null) {
            throw new IllegalArgumentException("not allow first and second are null.");
        } else if (isNullOrEmpty(first) && second != null) {
            return second;
        } else if (isNullOrEmpty(second)) {
            return first;
        } else {
            T[] result = Arrays.copyOf(first, first.length + second.length);
            System.arraycopy(second, 0, result, first.length, second.length);
            return result;
        }
    }


    /**
     * 查看数组中是否包含某一个值
     *
     * @param arrays 数组
     * @param object 用于检测的值
     * @param <T>
     * @return true 包含
     */
    public static <T> boolean contains(T[] arrays, T object) {
        if (isNullOrEmpty(arrays)) {
            return false;
        }
        for (T array : arrays) {
            if (Objects.equals(array, object)) {
                return true;
            }
        }
        return false;
    }
}

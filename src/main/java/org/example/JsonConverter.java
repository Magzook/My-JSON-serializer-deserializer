package org.example;

import java.lang.reflect.*;
import java.util.*;

public class JsonConverter {
    // Объект: {"поле1":значение1,"поле2":значение2,"полеN":значениеN}
    // Массив: [значение1,значение2,значениеN]
    // Поля private, protected, transient, static игнорируются.
    private final HashMap<Class<?>, Class<?>> classMap = new HashMap<>(); // Используется для нужд jsonToValue()
    public JsonConverter(){
        classMap.put(byte.class, Byte.class);
        classMap.put(short.class, Short.class);
        classMap.put(int.class, Integer.class);
        classMap.put(long.class, Long.class);
        classMap.put(float.class, Float.class);
        classMap.put(double.class, Double.class);
        classMap.put(boolean.class, Boolean.class);
        classMap.put(char.class, Character.class);
    }
    public String serialize(Object obj) throws IllegalAccessException {
        ArrayList<Field> fields = getNecessaryFields(obj.getClass()); // Получить поля соответствующего класса
        return objectToJson(obj, fields);
    }
    public String serialize(Object[] obj) throws IllegalAccessException {
        return arrayToJson(obj);
    }
    public Object deserialize(String json, Class<?> cl) throws ReflectiveOperationException {
        if (cl.isArray()) {
            return jsonToArray(json, cl);
        }
        else {
            ArrayList<Field> fieldsList = getNecessaryFields(cl);
            return jsonToObject(json, cl, fieldsList);
        }
    }


    private String objectToJson(Object obj, ArrayList<Field> fields) throws IllegalAccessException {
        // Перевести объект в JSON
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field field : fields) {
            String fieldName = field.getName(); // Имя поля
            Object value = field.get(obj); // Значение поля (как объект)
            String fieldValue = valueToJson(value); // Строковый вид значения поля
            sb.append("\"").append(fieldName).append("\":").append(fieldValue).append(",");
        }
        sb.deleteCharAt(sb.length() - 1); // Удалить последнюю запятую
        sb.append("}");
        return sb.toString();
    }
    private String arrayToJson(Object array) throws IllegalAccessException {
        // Перевести массив в JSON
        if (array == null) return "null";
        StringBuilder sb = new StringBuilder();
        int length = Array.getLength(array);
        Class<?> arrayType = array.getClass().getComponentType();
        if (arrayType == byte.class) {
            sb.append("\"");
            sb.append(Base64.getEncoder().encodeToString((byte[]) array));
            sb.append("\"");
        }
        else if (arrayType == char.class) {
            sb.append("\"");
            for (int i = 0; i < length; i++) {
                sb.append(Array.get(array, i));
            }
            sb.append("\"");
        }
        else {
            sb.append("[");
            for (int i = 0; i < length; i++) {
                String json = valueToJson(Array.get(array, i));
                sb.append(json).append(",");
            }
            sb.deleteCharAt(sb.length() - 1); // Удалить последнюю запятую
            sb.append("]");
        }
        return sb.toString();
    }
    private String valueToJson(Object value) throws IllegalAccessException {
        // Перевести значение в JSON
        if (value == null) return "null";
        String json;
        Class<?> valueType = value.getClass();
        if (valueType.isArray()) {
            json = arrayToJson(value);
        }
        else if (valueType.getSuperclass() == Number.class || valueType == Boolean.class) {
            json = value.toString();
        }
        else if (valueType == Character.class || valueType == String.class || valueType.isEnum()) {
            json = "\"" + value + "\""; // неявный вызов Object.toString() у переменной value
        }
        else {
            ArrayList<Field> fields = getNecessaryFields(valueType);
            json = objectToJson(value, fields);
        }
        return json;
    }


    private Object jsonToObject(String json, Class<?> cl, ArrayList<Field> fieldsList) throws ReflectiveOperationException {
        // Перевести фрагмент JSON в объект.
        Object object = cl.getDeclaredConstructor().newInstance();  // Создать объект по классу
        ArrayList<String> values = splitJson(json);                 // Извлечь значения из строки json
        // Циклом каждому полю присвоить значение. Поля в fieldsList идут в том же порядке, что в json.
        for (int i = 0; i < fieldsList.size(); i++) {
            String valueStr = values.get(i);                    // Взять строку со значением из objectData
            Class<?> fieldType = fieldsList.get(i).getType();   // Определить тип поля
            Object value = jsonToValue(valueStr, fieldType);    // Перевести строку в значение
            fieldsList.get(i).set(object, value);               // Установить полученное значение для поля
        }
        return object;
    }
    private Object jsonToArray(String json, Class<?> fieldType) throws ReflectiveOperationException {
        // Перевести фрагмент JSON в массив
        Class<?> componentType = fieldType.getComponentType();
        if (componentType == byte.class) {
            json = json.substring(1, json.length() - 1);
            return Base64.getDecoder().decode(json);
        }
        else if (componentType == char.class) {
            json = json.substring(1, json.length() - 1);
            char[] arr = new char[json.length()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = json.charAt(i);
            }
            return arr;
        }
        else {
            ArrayList<String> jsons = splitJson(json);
            int length = jsons.size();
            Object arr = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                String element = jsons.get(i);
                Object value = jsonToValue(element, componentType);
                Array.set(arr, i, value);
            }
            return arr;
        }
    }
    private Object jsonToValue(String json, Class<?> fieldType) throws ReflectiveOperationException {
        // Перевести фрагмент JSON в одно значение
        if (Objects.equals(json, "null")) return null;
        if (fieldType.isPrimitive()) fieldType = classMap.get(fieldType);
        if (fieldType.getSuperclass() == Number.class || fieldType == Boolean.class || fieldType == Character.class) {
            switch (fieldType.getSimpleName()) {
                case "Byte":
                    return Byte.parseByte(json);
                case "Short":
                    return Short.parseShort(json);
                case "Integer":
                    return Integer.parseInt(json);
                case "Long":
                    return Long.parseLong(json);
                case "Float":
                    return Float.parseFloat(json);
                case "Double":
                    return Double.parseDouble(json);
                case "Boolean":
                    return Boolean.parseBoolean(json);
                case "Character":
                    return json.charAt(1);
            }
        }
        else if (fieldType.isArray()) {
            return jsonToArray(json, fieldType);
        }
        else if (fieldType.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>)fieldType, json.substring(1, json.length() - 1));
        }
        else if (fieldType == String.class) {
            return json.substring(1, json.length() - 1);
        }
        else {
            return jsonToObject(json, fieldType, getNecessaryFields(fieldType));
        }
        return null;
    }


    private ArrayList<String> splitJson(String json) {
        // Разделить фрагмент JSON по запятым, игнорируя запятые внутри скобок и кавычек
        // Метод применим к классу и массиву.
        ArrayList<String> list = new ArrayList<>();
        boolean isObject = false;
        if (json.charAt(0) == '{') isObject = true;
        json = json.substring(1, json.length() - 1);
        int length = json.length();
        int openedBrackets = 0, previousComaIndex = 0;
        boolean quotationMarksAreOpened = false;
        for (int i = 0; i < length; i++) {
            char ch = json.charAt(i);
            if (ch == ',' && openedBrackets == 0 && !quotationMarksAreOpened) {
                list.add(json.substring(previousComaIndex, i));
                previousComaIndex = i + 1;
            }
            else if (ch == '[' || ch == '{')
                openedBrackets++;
            else if (ch == ']' || ch == '}')
                openedBrackets--;
            else if (ch == '"') {
                quotationMarksAreOpened = !quotationMarksAreOpened;
            }
            if (i == length - 1)
                list.add(json.substring(previousComaIndex, i + 1));
        }
        if (isObject) {
            // Оставить только значения
            for (int i = 0; i < list.size(); i++) {
                String nameAndValue = list.get(i);
                int colonIndex = nameAndValue.indexOf(':');
                list.set(i, nameAndValue.substring(colonIndex + 1));
            }
        }
        return list;
    }
    private ArrayList<Field> getNecessaryFields(Class<?> cl) {
        // Получить коллекцию полей класса
        // Поля с модификаторами private, protected, static, transient игнорируются.
        ArrayList<Field> fieldsList = new ArrayList<>(List.of(cl.getFields()));
        int length = fieldsList.size(), i = 0;
        while (i < length) {
            Field field = fieldsList.get(i);
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
                fieldsList.remove(i);
                length--;
                i--;
            }
            i++;
        }
        return fieldsList;
    }
}

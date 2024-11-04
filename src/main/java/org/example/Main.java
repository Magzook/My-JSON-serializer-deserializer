package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testclasses.MyClass;
import org.testclasses.MyClassCompact;

import java.io.IOException;
import java.io.StringWriter;

public class Main {
    public static void main(String[] args) throws IOException, ReflectiveOperationException {
        JsonConverter jsonConverter = new JsonConverter();

        // Сериализовать один объект
        MyClass object1 = new MyClass();
        String json_obj = jsonConverter.serialize(object1); // Наш сериализатор
        System.out.println("JsonConverter:\n" + json_obj);
        compareSerializationWithObjectMapper(object1, json_obj); // Сериализатор от Jackson

        // Сериализовать массив
        int length = 4;
        MyClassCompact[] array1 = new MyClassCompact[length];
        for (int i = 0; i < length; i++) array1[i] = new MyClassCompact();
        String json_arr = jsonConverter.serialize(array1); // Наш сериализатор
        System.out.println("JsonConverter:\n" + json_arr);
        compareSerializationWithObjectMapper(array1, json_arr); // Сериализатор от Jackson

        // Десериализовать один объект
        MyClass obj1 = (MyClass) jsonConverter.deserialize(json_obj, MyClass.class);
        // Десериализовать, используя ObjectMapper, сравнить
        compareDeserializationWithObjectMapper(obj1, json_obj);

        // Десериализовать массив
        MyClassCompact[] array2 = (MyClassCompact[]) jsonConverter.deserialize(json_arr, MyClassCompact[].class);
        // Десериализовать, используя ObjectMapper, сравнить
        compareDeserializationWithObjectMapper(array2, json_arr);

    }
    private static void compareSerializationWithObjectMapper(Object obj, String json) throws IOException {
        // Сравнить сериализацию с той, которая у ObjectMapper
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, obj);
        System.out.println("ObjectMapper:");
        System.out.println(writer);

        boolean match = true;
        int mismatchIndex = 0;
        String writerStr = writer.toString();
        int maxLength = Math.max(writerStr.length(), json.length());
        for (int i = 0; i < maxLength; i++) {
            if (writerStr.charAt(i) != json.charAt(i)) {
                match = false;
                mismatchIndex = i;
                break;
            }
        }
        if (match)
            System.out.println("Perfect match!\n");
        else
            System.out.println("Mismatch at " + mismatchIndex + '\n');
    }
    private static void compareDeserializationWithObjectMapper(Object obj1, String json) throws IOException {
        // Десериализовать тот же объект через ObjectMapper,
        // затем оба объекта сериализовать обратно в JSON
        // через ObjectMapper, сравнить строки.
        // Так мы можем понять, были ли равны по полям исходные объекты.
        ObjectMapper mapper = new ObjectMapper();
        Object obj2 = mapper.readValue(json, obj1.getClass());
        StringWriter writer1 = new StringWriter();
        StringWriter writer2 = new StringWriter();
        mapper.writeValue(writer1, obj1);
        mapper.writeValue(writer2, obj2);
        System.out.println((writer1.toString()).equals(writer2.toString()));
    }
}
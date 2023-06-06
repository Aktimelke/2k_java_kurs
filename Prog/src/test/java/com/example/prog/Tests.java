package com.example.prog;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

class Tests {

    private String removeCharFromString(String input) {
        char charToRemove = '$';
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != charToRemove) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Test
    void testRemoveCharFromString() {
        // Тестирование на обычной строке без символа '$'
        String input1 = "Hello, World!";
        String expected1 = "Hello, World!";
        assertEquals(expected1, removeCharFromString(input1));

        // Тестирование на строке с символом '$'
        String input2 = "He$llo, $Wor$ld$!";
        String expected2 = "Hello, World!";
        assertEquals(expected2, removeCharFromString(input2));

        // Тестирование на пустой строке
        String input3 = "";
        String expected3 = "";
        assertEquals(expected3, removeCharFromString(input3));

        // Тестирование на строке, состоящей только из символа '$'
        String input4 = "$$$$";
        String expected4 = "";
        assertEquals(expected4, removeCharFromString(input4));
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private boolean checkDate(String unChangedDate) {
        int splitIndex = 12;
        String secondPart = unChangedDate.substring(splitIndex);

        LocalDate date = LocalDate.parse(secondPart, formatter);
        LocalDate today = LocalDate.now();
        boolean belongsToToday = date.isEqual(today);

        return belongsToToday;
    }

    @Test
    void testCheckDate() {
        // Тестирование на дате, соответствующей текущей дате
        String input1 = "08:27:01 PM 06-06-2023";
        assertTrue(checkDate(input1));

        // Тестирование на дате, не соответствующей текущей дате
        String input2 = "08:27:01 PM 07-06-2023";
        assertFalse(checkDate(input2));

    }


    @Test
    void testInput_SingleDot() {
        // Создание строки для тестирования
        String input = "3.14";

        // Вызов метода input() для тестирования
        boolean result = input(input);

        // Проверка, что результат равен true
        assertTrue(result);
    }

    @Test
    void testInput_MultipleDots() {
        // Создание строки для тестирования
        String input = "1.2.3";

        // Вызов метода input() для тестирования
        boolean result = input(input);

        // Проверка, что результат равен false
        assertFalse(result);
    }

    @Test
    void testCountOccurrences_SingleOccurrence() {
        // Вызов метода countOccurrences() для тестирования
        int count = countOccurrences("Hello", 'l');

        // Проверка, что количество вхождений равно 2
        assertEquals(2, count);
    }

    @Test
    void testCountOccurrences_NoOccurrence() {
        // Вызов метода countOccurrences() для тестирования
        int count = countOccurrences("Hello", 'z');

        // Проверка, что количество вхождений равно 0
        assertEquals(0, count);
    }

    boolean input(String input) {
        char i = '.';
        if (countOccurrences(input, i) > 1) {
            return false;
        } else {
            return true;
        }
    }

    int countOccurrences(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }


    @Test
    void testEnterName_EmptyName() {
        // Создание строки для тестирования
        String name = "";

        // Вызов метода enterName() для тестирования
        boolean result = enterName(name);

        // Проверка, что результат равен false
        assertFalse(result);
    }

    @Test
    void testEnterName_NonEmptyName() {
        // Создание строки для тестирования
        String name = "John";

        // Вызов метода enterName() для тестирования
        boolean result = enterName(name);

        // Проверка, что результат равен true
        assertTrue(result);
    }

    // Аналогично для остальных тестовых методов enterUsername(), enterPassword() и enterConPass()

    boolean enterName(String name) {
        if (name.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


}
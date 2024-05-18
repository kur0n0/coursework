package ru.volsu.course.enums;

public enum AnswerMappingEnum {
    STRING("Строка"),
    LONG("Целое число");

    private String text;

    AnswerMappingEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

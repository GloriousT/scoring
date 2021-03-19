package com.myproject.model;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class CalculatedValue<T> {
    private T value;
    private String detailOfPossibleError;

    public static <T> CalculatedValue<T> error(String error) {
        return new CalculatedValue<T>(null, error);
    }

    public static <T> CalculatedValue<T> present(T value) {
        return new CalculatedValue<T>(value, null);
    }

    public static <T> CalculatedValue<T> suspicious(T value, String error) {
        return new CalculatedValue<T>(value, error);
    }

    public String getValue() {
        if (null != value && null != detailOfPossibleError) {
            return "" + value.toString() + " BUT " + getDetailOfPossibleError();
        } else if (null != value) {
            return value.toString();
        } else if (null != detailOfPossibleError) {
            return detailOfPossibleError;
        } else {
            return null;
        }
    }

    //we need this to make sure no extra commas will be passed to Spreadsheet
    private String getDetailOfPossibleError() {
        return detailOfPossibleError.replaceAll(",", "");
    }
}

package com.cozyhome.onlineshop.productservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum MaxLoadEnum {

    UP_TO_100("до 100 кг", (short) 1, (short) 100 ),
    FROM_101_TO_200("100-200 кг", (short) 101, (short) 200),
    OVER_201("від 200 кг", (short) 201, (short) 300);

    private final String description;
    private final short minValue;
    private final short maxValue;

    MaxLoadEnum(String description, short minValue, short maxValue) {
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static List<String> findDescription(short minLoad, short maxLoad) {
        List<String> strings = new ArrayList<>();
        if (maxLoad <= UP_TO_100.maxValue) {
            strings.add(UP_TO_100.description);
        } else if (minLoad >= OVER_201.minValue) {
            strings.add(OVER_201.description);
        } else if (minLoad <= UP_TO_100.maxValue && maxLoad <= FROM_101_TO_200.maxValue) {
            strings.add(UP_TO_100.description);
            strings.add(FROM_101_TO_200.description);
        } else if (minLoad <= UP_TO_100.maxValue && maxLoad >= OVER_201.minValue) {
            strings.add(UP_TO_100.description);
            strings.add(FROM_101_TO_200.description);
            strings.add(OVER_201.description);
        } else if (minLoad >= FROM_101_TO_200.minValue && maxLoad >= OVER_201.minValue) {
            strings.add(FROM_101_TO_200.description);
            strings.add(OVER_201.description);
        } else {
            strings.add(FROM_101_TO_200.description);
        }

        return strings;
    }

    public static MaxLoad findValues(List<String> strings) {
        MaxLoad maxLoad = new MaxLoad();
        if (strings.size() == 1) {
            getMapFromString(strings.stream().findFirst().get(), maxLoad);
        } else if (strings.size() == 2) {
            getMapFromList(strings, maxLoad);
        } else {
            maxLoad.setLoadMin(UP_TO_100.minValue);
            maxLoad.setLoadMax(OVER_201.maxValue);
        }
        return maxLoad;
    }

    private static void getMapFromString(String string, MaxLoad maxLoad) {
        if (string.equals(UP_TO_100.description)) {
            maxLoad.setLoadMin(UP_TO_100.minValue);
            maxLoad.setLoadMax(UP_TO_100.maxValue);
        } else if (string.equals(FROM_101_TO_200.description)) {
            maxLoad.setLoadMin(FROM_101_TO_200.minValue);
            maxLoad.setLoadMax(FROM_101_TO_200.maxValue);
        } else {
            maxLoad.setLoadMin(OVER_201.minValue);
            maxLoad.setLoadMax(OVER_201.maxValue);
        }
    }

    private static void getMapFromList(List<String> strings, MaxLoad maxLoad) {
        if (strings.contains(UP_TO_100.description) && strings.contains(FROM_101_TO_200.description)) {
            maxLoad.setLoadMin(UP_TO_100.minValue);
            maxLoad.setLoadMax(FROM_101_TO_200.maxValue);
        } else if (strings.contains(FROM_101_TO_200.description) && strings.contains(OVER_201.description)) {
            maxLoad.setLoadMin(FROM_101_TO_200.minValue);
            maxLoad.setLoadMax(OVER_201.maxValue);
        } else {
            maxLoad.setLess(UP_TO_100.maxValue);
            maxLoad.setMore(OVER_201.minValue);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaxLoad {
        private short loadMin;
        private short loadMax;
        private short less;
        private short more;
    }
}

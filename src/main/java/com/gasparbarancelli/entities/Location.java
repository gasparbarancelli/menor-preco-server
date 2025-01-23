package com.gasparbarancelli.entities;

import java.util.Arrays;
import java.util.List;

public enum Location {

    PATO_BRANCO("6g6dc3jcw"),
    CLEVELANDIA("6g6cb9qch"),
    VITORINO("6g66wfwkw"),
    CORONEL_VIVIDA("6g6s5y7j2"),
    FRANCISCO_BELTRAO("6g678srwp");

    private String code;

    Location(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static List<String> getCodes() {
        return Arrays.stream(Location.values()).map(Location::getCode).toList();
    }

}

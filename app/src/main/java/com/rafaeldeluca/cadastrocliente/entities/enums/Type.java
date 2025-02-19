package com.rafaeldeluca.cadastrocliente.entities.enums;

public enum Type {

    NOVO("Cliente novo"),
    REATIVADO("Cliente reativado"),
    RECORRENTE("Cliente compra com recorrência");

    private final String value;

    Type(final String value) {
        this.value= value;
    }

    @Override
    public String toString() {
        return value;
    }




}

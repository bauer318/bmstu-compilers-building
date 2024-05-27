package ru.bmstu.kibamba.models;

import java.util.Objects;

public class GrammarSymbol implements Cloneable {
    private String name;
    private String attribute;

    private String value;

    private GrammarSymbol(String name) {
        this.name = name;
    }

    public GrammarSymbol(String name, String attribute) {
        this(name);
        this.attribute = attribute;
        this.value = name;

    }

    public GrammarSymbol(String name, String attribute, String value) {
        this(name, attribute);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarSymbol symbol = (GrammarSymbol) o;
        return Objects.equals(name, symbol.name) &&
                Objects.equals(attribute, symbol.attribute) &&
                Objects.equals(value, symbol.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, attribute);
    }

    @Override
    public GrammarSymbol clone() {
        try {
            GrammarSymbol clone = (GrammarSymbol) super.clone();
            clone.setName(this.getName());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

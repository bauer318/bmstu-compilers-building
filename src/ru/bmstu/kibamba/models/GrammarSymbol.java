package ru.bmstu.kibamba.models;

import java.util.Objects;

public class GrammarSymbol implements Cloneable {
    private String name;

    public GrammarSymbol(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarSymbol that = (GrammarSymbol) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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

package ru.bmstu.kibamba.models;

import java.util.Objects;

public class Terminal extends GrammarSymbol {
    private String spell;

    public Terminal(String name, String spell) {
        super(name);
        this.spell = spell;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Terminal terminal = (Terminal) o;
        return Objects.equals(spell, terminal.spell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), spell);
    }
}

package ru.bmstu.kibamba;

public class TransitionFunction {
    //f(from,with)=to
    private final State from;
    private final State to;
    private final char with;

    public TransitionFunction(State from, State to, char with) {
        this.from = from;
        this.to = to;
        this.with = with;
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    public char getWith() {
        return with;
    }

    @Override
    public String toString() {
        String lastStateIndicator = to.isAcceptState() ? " last " : "";
        return "f{" + from.getId() + "," + with + "}=" + to.getId() + lastStateIndicator;
    }

}

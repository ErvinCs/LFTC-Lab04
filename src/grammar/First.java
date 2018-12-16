package grammar;

import java.util.*;

public class First {
    private Grammar grammar;
    private Map<String,Set<String>> firsts;

    public First(Grammar grammar){
        this.grammar = grammar;
        this.firsts = new HashMap<>();
    }

    public  Map<String,Set<String>> getFirsts() {return this.firsts;}

    public void BuildFirst(){
        grammar.getTerminals().forEach(elem -> {
            Set<String> strs = new HashSet<>();
            strs.add(elem);
            firsts.put(elem , strs);
        });

        grammar.getNonTerminals().forEach(elem -> {
            Set<String> first = ComputeFirst(elem);
            firsts.put(elem , first);
        });
    }

    private Set<String> ComputeFirst(String symbol) {
        Set<String> first = new HashSet<>();

        try {
            grammar.nonTerminalProductions(symbol).forEach(elem ->
                first.addAll(ProductionFirst(elem.getTo()))
            );
        } catch (Exception e) { System.out.println("lemnes");}

        return first;
    }

    private  Set<String> ProductionFirst(List<String> elems){
        Set<String> first = new HashSet<>();

        for (String elem : elems) {
            if (grammar.getTerminals().contains(elem))
                return  firsts.get(elem);

            if (elem.equals("Eps")) {
                first.add(elem);
                return first;
            }

            if (grammar.getNonTerminals().contains(elem)){
                first.addAll(ComputeFirst(elem));
                if (!first.contains("Eps"))
                    return first;
                first.remove("Eps");
            }
        }

        first.add("Eps");
        return first;
    }

    @Override
    public String toString(){
        StringBuilder strs = new StringBuilder();
        firsts.forEach((k, v) -> strs.append("F(").append(k).append(")= ").append(v.toString()).append("\n"));
        return strs.toString();
    }
}

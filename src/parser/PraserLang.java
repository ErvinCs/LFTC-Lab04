package parser;

import grammar.First;
import grammar.Follow;
import grammar.Grammar;
import grammar.Table;
import scanner.ProgramInternalForm;
import scanner.SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PraserLang {
    private First first;
    private Follow follow;
    private Table table;
    private Grammar grammar;

    private LinkedList<String> alpha;
    private LinkedList<String> beta;
    private List<Integer> pi;
    private String result;

    private File file;

    public PraserLang(Grammar grammar, ProgramInternalForm pif, SymbolTable st)
    {
        this.grammar = grammar;

        first = new First(grammar);
        first.BuildFirst();
        follow = new Follow(grammar, first);
        follow.BuildFollow();
        table = new Table(first, follow, grammar);

        alpha = new LinkedList<>();
        beta = new LinkedList<>();
        pi = new ArrayList<>();

        file = new File("res/input/grammar/inputGrammarShort");
        this.InitAlpha();
        this.InitBeta();
    }

    public void Parse() {

    }

    private void InitAlpha() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while(!(line.isEmpty())) {
                String[] tokens = line.split(" ");
                for(String token : tokens)
                    alpha.addLast(token);
            }
            alpha.addLast("$");
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private void InitBeta() {
        beta.addLast(grammar.getStartingSymbol());
        beta.addLast("$");
    }

    public void ShowResult(){
        if (result.equals("acc")){
            System.out.println("Sequence accepted!");
            pi.forEach(elem -> System.out.print(elem + " "));
            System.out.println();
        }else
            System.out.println("Sequence not accepted!");
    }

    private void ShowStepByStep(){
        this.alpha.forEach(elem -> System.out.print(elem + " "));
        System.out.println();
        this.beta.forEach(elem -> System.out.print(elem + " "));
        System.out.println();
        this.pi.forEach(elem -> System.out.print(elem + " "));
        System.out.println();
    }

    private void Push(List<String> items){
        for (int i = items.size() - 1; i >= 0; i--)
            beta.addFirst(items.get(i));
    }

    public LinkedList<String> getAlpha() {return alpha;}
    public LinkedList<String> getBeta() {return beta;}
    public List<Integer> getPi() {return pi;}
}

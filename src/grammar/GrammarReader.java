package grammar;

import exceptions.InvalidGrammarException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GrammarReader {
    private Grammar grammar;
    private Scanner reader;
    private File file;


    public GrammarReader() throws FileNotFoundException{
        this.file = new File("res/input/inputRG.txt");
        this.grammar = new Grammar();
        this.reader = new Scanner(System.in);
    }

    public void setFilePath(String path) {
        this.file = new File(path);
    }

    public Grammar getGrammar() {
        return grammar;
    }

    private String grammarActionsMenu() {
        String menu = "Menu:\n\t0.Exit\n";
        menu += "\t1.Set of non-terminals\n";
        menu += "\t2.Set of terminals\n";
        menu += "\t3.Set of productions\n";
        menu += "\t4.The productions of a given non-terminal\n";
        menu += "\t5.Check if the grammar is regular\n";
        menu += "\t6.Read a grammar\n";
        menu += "\t7.Load a grammar\n";
        return menu;
    }

    private void printNonTerminals() {
        System.out.println("Non-terminals: ");
        for(String nt : this.grammar.getNonTerminals())
            System.out.println(nt + " ");
    }

    private void printTerminals() {
        System.out.println("Terminals: ");
        for(String t : this.grammar.getTerminals())
            System.out.println(t + " ");
    }

    private void printProductions() {
        System.out.println("Productions: ");
        for(Production p : this.grammar.getProductions())
            System.out.println(p.toString());
    }

    private void printProductionsFor(String terminal) {
        System.out.println("Productions<" + terminal + ">: ");
        for(Production p : this.grammar.getProductions())
            if (p.getFrom().equals(terminal))
                System.out.print(p.toString());
    }


    private boolean checkInputGrammar(Grammar grammar) throws InvalidGrammarException {
        if (grammar.getNonTerminals().isEmpty())
            throw new InvalidGrammarException("Empty set of non-terminals!");
        else if (grammar.getProductions().isEmpty())
            throw new InvalidGrammarException("Empty set of productions!");
        else if(grammar.getTerminals().isEmpty())
            throw new InvalidGrammarException("Empty set of terminals!");
        else if (grammar.getStartingSymbol().equals(""))
            throw new InvalidGrammarException("No starting symbol specified!");
        else
            return true;
    }

    private void readNonTerminals() {
        System.out.println("Enter number of non-terminals: ");
        int number = reader.nextInt();
        for(int i = 0; i < number; i++) {
            System.out.println("Enter non-temrinal #" + i);
            String nonTerm = reader.next();
            grammar.addNonTerminal(nonTerm);
        }
    }

    private void readTerminals() {
        System.out.println("Enter number of terminals: ");
        int number = reader.nextInt();
        for(int i = 0; i < number; i++) {
            System.out.println("Enter temrinal #" + i);
            String term = reader.next();
            grammar.addTerminal(term);
        }
    }

    private void readProductions() throws InvalidGrammarException {
        System.out.println("Enter number of productions: ");
        int number = reader.nextInt();
        for(int i = 0; i < number; i++) {
            System.out.println("Non-terminal: ");
            String nonTerm = reader.next();
            System.out.print("->");
            reader.nextLine();
            String termLine = reader.nextLine();
            String[] auxTerms = termLine.trim().split(" ");
            List<String> terms = new ArrayList<>();
            for (String t : auxTerms)
                terms.add(t);

            for (String t : terms) {
                if (!(this.grammar.getTerminals().contains(t) || this.grammar.getNonTerminals().contains(t) || t.equals("Eps"))) {
                    throw new InvalidGrammarException("No such term!");
                }
                this.grammar.addProduction(new Production(nonTerm, terms));
            }
        }
    }

    private void readStartingSYmbol() throws InvalidGrammarException {
        System.out.println("Enter starting symbol: ");
        String startingSymbol = reader.next();
        grammar.setStartingSymbol(startingSymbol);
        if (!(grammar.getNonTerminals().contains(startingSymbol)))
            throw new InvalidGrammarException("No such symbol!");
    }

    public void run() throws InvalidGrammarException, IOException {
        int option;
        boolean loop = true;
        while(loop) {
            System.out.println(grammarActionsMenu());
            System.out.println("Enter option: ");
            option = reader.nextInt();
            switch (option) {
                case 0:
                    loop = false;
                    break;
                case 1:
                    checkInputGrammar(grammar);
                    printNonTerminals();
                    break;
                case 2:
                    checkInputGrammar(grammar);
                    printTerminals();
                    break;
                case 3:
                    checkInputGrammar(grammar);
                    printProductions();
                    break;
                case 4:
                    checkInputGrammar(grammar);
                    System.out.println("Give Non-terminal: ");
                    String nonTerminal = reader.next();
                    if (!(grammar.getNonTerminals().contains(nonTerminal)))
                        throw new InvalidGrammarException("No such non-terminal!");
                    printProductionsFor(nonTerminal);
                    break;
                case 5:
                    checkInputGrammar(grammar);
                    System.out.println("The grammar is regular - " + grammar.isRegular());
                    break;
                case 6:
                    grammar = new Grammar();
                    readGrammar();
                    System.out.println(this.grammar.toString());
                    break;
                case 7:
                    grammar = new Grammar();
                    loadGrammar();
                    System.out.println(this.grammar.toString());
                    break;
            }
        }
    }

    private void loadGrammar() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        br.readLine();  //read N:
        String[] tokens = br.readLine().split(" ");
        for(String t : tokens)
            this.grammar.addNonTerminal(t);

        br.readLine();  //read E:
        tokens = br.readLine().split(" ");
        for(String t : tokens)
            this.grammar.addTerminal(t);

        br.readLine();  //read P:
        String line = br.readLine();
        while (!(line.equals("S:"))) {
            tokens = line.split(" ");
            String from = tokens[0];
            List<String> to = new ArrayList<>();
            for(int i = 2; i < tokens.length; i++) {
                to.add(tokens[i]);
            }
            this.grammar.addProduction(new Production(from, to));
            line = br.readLine();
        }

        this.grammar.setStartingSymbol(br.readLine().trim());
    }

    private void readGrammar() throws InvalidGrammarException {
        readTerminals();
        readNonTerminals();
        readProductions();
        readStartingSYmbol();
    }
}

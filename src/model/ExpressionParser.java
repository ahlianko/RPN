package model;

import utils.Aggregate;
import utils.Iterator;
import view.View;

import java.util.*;

public class ExpressionParser implements Aggregate{
    private List<String> expressions;
    private List<String> postfixExpressions;
    private static String operators = "+-*/^";
    private static String delimiters = "() " + operators;

    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isOperator(String token) {
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }


    private static int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        if (token.equals("^")) return 4;
        return 5;
    }

    public String parse(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String curr;
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                View.printMessage(View.INCORRECT_EXPR);
                return View.ERROR;
            }
            if (curr.equals(" ")) continue;
            if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            View.printMessage(View.BRACKETS_ERR);
                            return View.ERROR;
                        }
                    }
                    stack.pop();
                    if (!stack.isEmpty() && isOperator(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                }
                else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }
                        stack.push(curr);
                }
            }
            else {
                postfix.add(curr);
            }
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) postfix.add(stack.pop());
            else {
                View.printMessage(View.BRACKETS_ERR);
                return View.ERROR;
            }
        }
        return converListToString(postfix);
    }

    public void parseAll(){
        Iterator it = new InfixExpressionIterator();
        while (it.hasNext()) {
            String temp = parse((String)it.next());
            postfixExpressions.add(temp);
        }
    }

    public void printPostfixExpressions(){
        Iterator pit = new PostfixExpressionIterator();
        while (pit.hasNext()){
            String temp = (String) pit.next();
            if(!temp.equals(View.ERROR))
           View.printMessage(temp);
        }
    }

    public String converListToString(List<String> postfix){
        StringBuilder res = new StringBuilder("");
        for(String s : postfix){
            res.append(s);
        }
        return res.toString();
    }

    public ExpressionParser(){
        this.expressions = new ArrayList<>();
        this.postfixExpressions = new ArrayList<>();
    }

    public void addExpressionToList(String exp){
        expressions.add(exp);
    }
    public List<String> getListExpressions(){
        return expressions;
    }

    public String getExpressionByIndex(int index){
        return expressions.get(index);
    }

    @Override
    public Iterator getInfixIterator() {
        return new InfixExpressionIterator();
    }
    @Override
    public Iterator getPostfixIterator() {
        return new PostfixExpressionIterator();
    }
    private class InfixExpressionIterator implements Iterator{
        int index = 0;

        @Override
        public boolean hasNext() {
            if (index<expressions.size()) {
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            return expressions.get(index++);
        }
    }

    private class PostfixExpressionIterator implements Iterator{
        int index = 0;

        @Override
        public boolean hasNext() {
            if (index<postfixExpressions.size()) {
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            return postfixExpressions.get(index++);
        }
    }
}

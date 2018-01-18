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


    /**
     * This method checks whether char is a delimiter
     *
     * @param token                 one char in expression(current character)
     * @return return result of checking if this character is a Delimiter.
     *
     */
    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    /**
     * This method checks whether char is an operator
     *
     * @param token                 one char in expression(current character)
     * @return return result of checking if this character is a Operator.
     *
     */
    private static boolean isOperator(String token) {
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }
    /**
     * This method defines a priority of operation
     *
     * @param token                 one char in expression(current character)
     * @return return priority of current operation, bigger priority - more important operation.
     *
     */
    private static int priority(String token) {
        if (token.equals("(") || token.equals(")")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        if (token.equals("^")) return 4;
        return 5;
    }
    /**
     * This main method converts a expression from infix notation to RPN (postfix notation)
     *
     * @param infix                 one char in expression(current character)
     * @return return result of converting expression from infix notation to RPN.
     *
     */
    public String parse(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String curr;
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                return View.ERROR;
            }
            if (curr.equals(" ")) continue;
            if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
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
                return View.ERROR;
            }
        }
        return converListToString(postfix);
    }

    /**
     * This method converts all expressions in the list
     *
     * */
    public void parseAll(){
        Iterator it = new InfixExpressionIterator();
        while (it.hasNext()) {
            String temp = parse((String)it.next());
            postfixExpressions.add(temp);
        }
    }
    /**
     * This method prints all of converted exprssions
     */
    public void printPostfixExpressions(){
        Iterator pit = new PostfixExpressionIterator();
        while (pit.hasNext()){
            String temp = (String) pit.next();
            if(!temp.equals(View.ERROR))
           View.printMessage(temp);
        }
    }

    /**
     * This util method converts a builded List<String> to a String
     *
     * @param postfix            list of postfix expression
     * @return expression in one String
     *
     */
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

    @Override
    public Iterator getInfixIterator() {
        return new InfixExpressionIterator();
    }
    @Override
    public Iterator getPostfixIterator() {
        return new PostfixExpressionIterator();
    }
    /**
     * This is an implementation of a design pattern Iterator

     */
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

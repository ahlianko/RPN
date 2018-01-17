package controller;

import model.Model;
import view.View;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Controller {
    Model model;
    View view;
    public Controller(Model m, View v){
        this.model = m;
        this.view = v;
    }



    public void processUser() {
        loadData();
        model.expressionParser.parseAll();
        model.expressionParser.printPostfixExpressions();
    }

    private void loadData() {
        view.printMessage(View.INPUT_FILE);
        Scanner sc = new Scanner(System.in);
        String file = sc.nextLine();


        try (BufferedReader fr = new BufferedReader(new FileReader(file))) {
            int expressionNumber = Integer.parseInt(fr.readLine());
            String temp;
            if (expressionNumber<=100 && expressionNumber>0) {
                for (int i = 0; i < expressionNumber; i++) {
                    temp = fr.readLine();
                    if (temp.length() <= 400)
                        model.expressionParser.addExpressionToList(temp);
                }
            }
        } catch (IOException e) {
            View.printError(View.FILE_NOT_FOUND);
        } catch (NumberFormatException e){
            View.printMessage(View.STRUCTURE_ERR);
        }
    }
}

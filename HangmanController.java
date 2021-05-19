package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.security.SecureRandom;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class HangmanController {

    @FXML
    private ImageView currentStep;

    @FXML
    private TextField guess;

    @FXML
    private Button enter;

    @FXML
    private Label word;

    @FXML
    private Label hint;

    private ArrayList<Character> wordPicked = new ArrayList<>();
    private ArrayList<Integer> indexGuessed = new ArrayList<>();

    private int count = 0;

    @FXML
    void enterButtonPressed(ActionEvent event) {
        String wordGuessed = guess.getText();
        boolean didGuess = false;
        boolean didWin = false;

        if (wordGuessed.length() < 1){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input!", ButtonType.OK);
            alert.showAndWait();
            didGuess = false;
        }
        else if (wordGuessed.length() == 1){
            if (wordPicked.contains(wordGuessed.charAt(0))){ // check if the word contains that letter
                for (int i = 0; i < wordPicked.size(); i++){ // loop through the word to get index of that letter
                    if (wordPicked.get(i) == wordGuessed.charAt(0) && !(indexGuessed.contains(i))){ // if the word contains guessed letter AND
                        indexGuessed.add(i); // the user hasn't already guessed it (if the index isn't already in indexGuessed)
                        didGuess = true;
                        break;
                    }
                }
            }
        }
        else {
            String strWord = "";
            for (char c : wordPicked){
                strWord += String.valueOf(c);
            }

            if (strWord.equals(wordGuessed)){
                didGuess = true;
                didWin = true;
                for (int i = 0; i < wordPicked.size(); i++){
                    indexGuessed.add(i);
                }
            }
        }

        if (!didGuess){
            count++;
        }

        if (!(count == 0) && (!didWin)){
            currentStep.setImage(new Image("file:src/com/company/images/Step " + (count) + ".png"));
        }

        if ((count == 3 || count == 6 || count == 9) && (!didGuess && !(wordPicked.size() < 4))) {
            int guessToShow = 0;
            while (indexGuessed.contains(guessToShow)) {
                guessToShow++;
            }
            if (guessToShow == 0) {
                hint.setText("Hint: 1st letter is " + wordPicked.get(0));
                indexGuessed.add(0);
            } else if (guessToShow == 1) {
                hint.setText("Hint: 2nd letter is " + wordPicked.get(1));
                indexGuessed.add(1);
            } else if (guessToShow == 2) {
                hint.setText("Hint: 3rd letter is " + wordPicked.get(2));
                indexGuessed.add(2);
            } else {
                hint.setText("Hint: " + (guessToShow + 1) + "th letter is " + wordPicked.get(0));
                indexGuessed.add(guessToShow);
            }
        }
        if (count == 9){
            String strWord = "";
            for (char c : wordPicked){
                strWord += String.valueOf(c);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You Lost! Word was " + strWord, ButtonType.OK);
            alert.showAndWait();
            System.exit(0);
        }

        String wordToShow = "";
        for (int i = 0; i < wordPicked.size(); i++) {
            if (indexGuessed.contains(i)) {
                wordToShow += wordPicked.get(i);
            } else {
                wordToShow += "_";
            }
        }

        word.setText(wordToShow);

        if (indexGuessed.size() == wordPicked.size()){
            didWin = true;
        }

        if (didWin){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You Won!", ButtonType.OK);
            alert.showAndWait();
            System.exit(0);
        }

        guess.setText("");
    }

    public void initialize() throws IOException {
        Scanner out = new Scanner(Paths.get("words.txt")); // Add absolute path here
        SecureRandom randNum = new SecureRandom();

        for (int i = 0; i < randNum.nextInt(200) + 1; i++){
            out.next();
        }

        char[] wordArr = out.next().toCharArray();

        for (char c : wordArr){
            wordPicked.add(c);
        }

        System.out.println(wordPicked);

        String under = "";
        for (int i = 0; i < wordPicked.size(); i++){
            under += "_";
        }

        word.setText(under);

        guess.textProperty().addListener((ov, oldVal, newVal) -> {
            enter.setDisable(newVal.isBlank());
        });

        enter.setDefaultButton(true);
    }
}

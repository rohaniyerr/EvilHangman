package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class RandomHangmanChooser implements IHangmanChooser {
  private static final Random rand = new Random();
  private static final String filename = "data/scrabble.txt";
  private String secretWord;
  private int remGuesses;
  private String history;


  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    this.remGuesses = maxGuesses;
    this.history = "";
    SortedSet<String> words = new TreeSet<>();
    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException("Word length or Max guess less than 1.");
    }
    Scanner s = new Scanner(new File(filename));
    while (s.hasNextLine()) {
      String line = s.nextLine();
      if(line.length() == wordLength){
        words.add(line);
      }
    }
    if(words.size() == 0){
      throw new IllegalStateException("No words of wordLength found.");
    }
    int randint = rand.nextInt(words.size());
    int i = 0;
    for(String word : words)
    {
      if (i == randint){
        this.secretWord = word;
      }
      i++;
    }
  }

  @Override
  public int makeGuess(char letter) {
    int count = 0;
    for(int i = 0; i < this.secretWord.length(); i++){
      if(this.secretWord.charAt(i) == (letter)){
        count++;
      }
    }
    if(this.remGuesses < 1){
      throw new IllegalStateException("no more guesses left");
    }
    else if (letter < 'a' || letter > 'z'){
      throw new IllegalArgumentException("guess made was not lowercase");
    }
    else if(this.history.contains(String.valueOf(letter))){
      throw new IllegalArgumentException("letter already guessed");
    }
    if (!this.secretWord.contains(String.valueOf(letter))){
      this.remGuesses--;
    }
    this.history += "" + letter;
    return count;
  }

  @Override
  public boolean isGameOver() {
    System.out.println(getPattern());
    return this.remGuesses == 0 || getPattern().equals(this.secretWord);
  }

  @Override
  public String getPattern() {
    char[] patArr = new char[this.secretWord.length()];
    for (int i = 0; i < patArr.length; i++) {
      patArr[i] = '-';
    }
    for (int i = 0; i < this.history.length(); i++) {
      char c = this.history.charAt(i);
      if (this.secretWord.contains("" + c)) {
        int index = this.secretWord.indexOf(c);
        while (index >= 0) {
          patArr[index] = ("" + c).charAt(0);
          index = this.secretWord.indexOf(c, index + 1);
        }
      }
    }
    return new String(patArr);
  }

  @Override
  public SortedSet<Character> getGuesses() {
    SortedSet<Character> guesses = new TreeSet<>();
    for(int i = 0; i < this.history.length(); i++){
      char c = this.history.charAt(i);
      guesses.add(c);
    }
    return guesses;
  }

  @Override
  public int getGuessesRemaining() {
    return this.remGuesses;
  }

  @Override
  public String getWord() {
    this.remGuesses = 0;
    return this.secretWord;
  }
}
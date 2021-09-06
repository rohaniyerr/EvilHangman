package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {
  private String patternShown;
  private Set<String> family;
  private int remGuesses;
  private SortedSet<Character> lettersGuessed;

  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    if (wordLength < 1 || maxGuesses < 0) {
      throw new IllegalArgumentException("Word length or Max guess less than 1.");
    }
    this.remGuesses = maxGuesses;
    this.family = new TreeSet<>();
    this.lettersGuessed = new TreeSet<>();
    Scanner s = new Scanner(new File("data/scrabble.txt"));
    while (s.hasNextLine()) {
      String word = s.nextLine();
      if(word.length() == wordLength){
        this.family.add(word);
      }
    }
    if(this.family.isEmpty()){
      throw new IllegalStateException("No words of wordLength found.");
    }
    patternShown = "";
    for (int i = 0; i < wordLength; i++) {
      patternShown += "-";
    }
  }

  @Override
  public int makeGuess(char letter) {
    if(this.remGuesses < 1){
      throw new IllegalStateException("no more guesses left");
    }
    else if (letter < 'a' || letter > 'z'){
      throw new IllegalArgumentException("guess made was not lowercase");
    }
    else if(this.lettersGuessed.contains(letter)){
      throw new IllegalArgumentException("letter already guessed");
    }
    this.lettersGuessed.add(letter);
    Map<String, Set<String>> findFamily = new TreeMap<>();
    for(String word : this.family){
      String pattern = newPattern(letter, word);
      Set<String> setOfWords = new TreeSet<>();
      if (!findFamily.containsKey(pattern)) {
        findFamily.put(pattern, setOfWords);
      }
      findFamily.get(pattern).add(word);
    }
    int maximumSize = 0;
    if (!findFamily.isEmpty()) {
      for (String currentPattern : findFamily.keySet()) {
        if (findFamily.get(currentPattern).size() > maximumSize) {
          this.family.clear();
          this.family.addAll(findFamily.get(currentPattern));
          this.patternShown = currentPattern;
          maximumSize = findFamily.get(currentPattern).size();
        }
      }
    }
    return decision(letter);
  }

  @Override
  public boolean isGameOver() {
    return this.remGuesses == 0 || !this.patternShown.contains("" + '-');
  }

  public String newPattern(char letter, String word){
    String pattern = "";
    int index = 0;
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) != letter) {
        pattern += this.patternShown.substring(index, index + 1);
      } else {
        pattern += letter + "";
      }
      index += 1;
    }
    return pattern;
  }

  @Override
  public String getPattern() {
    return this.patternShown;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.lettersGuessed;
  }
  @Override
  public int getGuessesRemaining() {
    return this.remGuesses;
  }

  @Override
  public String getWord() {
    this.remGuesses = 0;
    String finalWord = null;
    int size = this.family.size();
    int item = new Random().nextInt(size);
    int i = 0;
    for(String word : this.family)
    {
      if (i == item)
        finalWord = word;
      i++;
    }
    return finalWord;
  }

  private int decision(char letter) {
    int maxOccurence = 0;
    for (int i = 0; i < this.patternShown.length(); i++) {
      if (this.patternShown.charAt(i) == letter) {
        maxOccurence++;
      }
    }
    lettersGuessed.add(letter);
    if (maxOccurence == 0) {
      this.remGuesses--;
    }
    return maxOccurence;
  }
}
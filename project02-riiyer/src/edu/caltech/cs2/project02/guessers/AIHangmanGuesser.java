package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static final String filename = "data/scrabble.txt";

  @Override
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {
    Set<String> words = new TreeSet<>();
    Scanner s = new Scanner(new File(filename));
    while (s.hasNextLine()) {
      String line = s.nextLine();
      if (line.length() == pattern.length()) {
        String copy = "";
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          if(!(guesses.contains(c) && !pattern.contains(""+c)))
            if (pattern.contains(""+c)) {
              copy += "" + c;
            } else {
              copy += "" + '-';
            }
            if (pattern.equals(copy)) {
              words.add(line);
          }
        }
      }
    }
    Map<Character, Integer> letterCount = new HashMap<>();
    for (char ch = 'a'; ch <= 'z'; ch++) {
      letterCount.put(ch, 0);
    }
    for (String word : words) {
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        if (!guesses.contains(c)) {
          letterCount.put(c, letterCount.get(c) + 1);
        }
      }
    }
    char maxChar = ' ';
    int maxValueInMap = (Collections.max(letterCount.values()));
    for (Map.Entry<Character, Integer> entry : letterCount.entrySet()) {
      if (entry.getValue() == maxValueInMap) {
        maxChar = entry.getKey();
        break;
      }
    }


    return maxChar;
  }
}

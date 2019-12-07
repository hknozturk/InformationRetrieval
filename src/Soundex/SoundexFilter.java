package Soundex;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.HashMap;

/**
 * This implementation is referenced. The Soundex algorithm is the one we learned in the lecture.
 * It converts letters to their respective digits and it reduces each converted token into 4-character.
 * ****************************************************************************************************
 * How algorithm works:
 * Retain the first letter of the term.
 * Change all occurrences of the following letters to corresponding digits:
 ** A, E, I, O, U, H, W, Y => 0
 ** B, F, P, V => 1
 ** C, G, J, K, Q, S, X, Z => 2
 ** D, T => 3
 ** L => 4
 ** M, N to 5
 ** R => 6
 * Repeatedly remove one out of each pair of consecutive identical digits.
 * Remove all zeros from the resulting string;
 * pad the resulting string with trailing zeros and return the first four positions,
 * which will consist of a letter followed by three digits.
 * ****************************************************************************************************
 * E.g. Soundex of HERMAN
 * Remain H
 * ERMAN -> 0RM0N
 * ORM0N -> 06505
 * 06505 -> 655
 * Return H655
**/

public class SoundexFilter extends TokenFilter {
    private final HashMap<Character, Character> letterMap = new HashMap<Character, Character>();
    private TermAttribute termAtt;

    public SoundexFilter(TokenStream in) {
        super(in);

        letterMap.put('a', '0');
        letterMap.put('e', '0');
        letterMap.put('i', '0');
        letterMap.put('o', '0');
        letterMap.put('u', '0');
        letterMap.put('h', '0');
        letterMap.put('w', '0');
        letterMap.put('y', '0');
        letterMap.put('b', '1');
        letterMap.put('f', '1');
        letterMap.put('p', '1');
        letterMap.put('v', '1');
        letterMap.put('c', '2');
        letterMap.put('g', '2');
        letterMap.put('j', '2');
        letterMap.put('k', '2');
        letterMap.put('q', '2');
        letterMap.put('s', '2');
        letterMap.put('x', '2');
        letterMap.put('z', '2');
        letterMap.put('d', '3');
        letterMap.put('t', '3');
        letterMap.put('l', '4');
        letterMap.put('m', '5');
        letterMap.put('n', '5');
        letterMap.put('r', '6');

        termAtt = addAttribute(TermAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            final char[] buffer = termAtt.termBuffer();
            final int length = termAtt.termLength();

            for (int t = 0; t < length; t++) {
                if (Character.isDigit(buffer[t])) return true;
            }
            // max size can be length of token + 3 0's?
            StringBuilder temp = new StringBuilder(length+3);
            // Add first letter of token to temp. We keep first letter of term.
            temp.append(buffer[0]);

            for (int i = 1; i < length; i++) {
                if (letterMap.get(buffer[i]) != null) {
                    // change following letters with their corresponding digits.
                    temp.append(letterMap.get(buffer[i]));
                }
                else {
                    // default to 0 for unrecognized characters?
                    temp.append('0');
                }
            }

            if (temp.length() > 2) {
                // remove all consecutive identical digits.
                int k = 2;

                while (k < temp.length()) {
                    if (temp.charAt(k) == temp.charAt(k - 1)) {
                        temp.deleteCharAt(k);
                    }
                    else {
                        k++;
                    }
                }

                // Remove all instances of 0 from string
                k = 1;

                while (k < temp.length()) {
                    if (temp.charAt(k) == '0') {
                        temp.deleteCharAt(k);
                    } else {
                        k++;
                    }
                }
            }

            // pad with 0 just in case string is not long enough
            temp.append('0');
            temp.append('0');
            temp.append('0');

            // Update buffer with new term, truncate string to only first 4 chars
            termAtt.setTermBuffer(temp.substring(0, 4));

            return true;
        } else {
            return false;
        }
    }
}

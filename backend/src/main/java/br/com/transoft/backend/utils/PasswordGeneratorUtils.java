package br.com.transoft.backend.utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.Rule;

import java.util.List;

public class PasswordGeneratorUtils {

    public static Integer DEFAULT_SIZE = 6;

    public static List<Rule> getDefaultRules() {
        return List.of(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)
        );
    }

}

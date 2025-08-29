package br.com.transoft.backend.utils;

import lombok.experimental.UtilityClass;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.passay.Rule;

import java.util.List;

@UtilityClass
public class PasswordGeneratorUtils {

    private static final Integer DEFAULT_SIZE = 6;

    public static String generatePassword() {
        return new PasswordGenerator().generatePassword(DEFAULT_SIZE, getDefaultRules());
    }

    private static List<Rule> getDefaultRules() {
        return List.of(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)
        );
    }

}

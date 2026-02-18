package dk.easv.eventtickets.GUI.Utils;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.util.function.UnaryOperator;
import io.github.palexdev.materialfx.controls.MFXTextField;

public class Validator {

    public static void numeric(MFXTextField textField) {
        numeric(textField, null, Integer.MAX_VALUE);
    }

    public static void numeric(MFXTextField textField, Integer defaultValue, int maxDigits) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= maxDigits) {
                return change;
            }
            return null;
        };

        StringConverter<Integer> converter = new IntegerStringConverter();
        TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, defaultValue, filter);
        textField.delegateSetTextFormatter(textFormatter);
    }

    public static void nonNumeric(MFXTextField textField) {
        nonNumeric(textField, null, Integer.MAX_VALUE);
    }

    public static void nonNumeric(MFXTextField textField, String defaultValue, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (!newText.matches(".*\\d.*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };

        StringConverter<String> converter = new StringConverter<>() {
            @Override
            public String fromString(String s) { return s; }
            @Override
            public String toString(String s) { return s; }
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(converter, defaultValue, filter);
        textField.delegateSetTextFormatter(textFormatter);
    }
}

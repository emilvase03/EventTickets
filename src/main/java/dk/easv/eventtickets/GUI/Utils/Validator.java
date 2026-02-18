package dk.easv.eventtickets.GUI.Utils;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.StringConverter;
import java.util.function.UnaryOperator;
import io.github.palexdev.materialfx.controls.MFXTextField;

public class Validator {

    public static void apply(MFXTextField textField) {
        apply(textField, null, 3); // default max 3 digits
    }

    public static void apply(MFXTextField textField, Integer defaultValue, int maxDigits) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= maxDigits) {
                return change;
            }
            return null;
        };

        StringConverter<Integer> converter = new IntegerStringConverter();
        TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, defaultValue, integerFilter);
        textField.delegateSetTextFormatter(textFormatter);
    }
}

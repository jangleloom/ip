package mrducky.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import mrducky.exception.MrDuckyException;

/**
 * Tests parsing of user inputs.
 */
public class ParserTest {

    @Test
    public void parseDeadline_validInput_returnsDescriptionAndBy() throws MrDuckyException {
        String input = "deadline return book /by 2/12/2019 1800";
        String[] result = Parser.parseDeadline(input);
        assertArrayEquals(new String[]{"return book", "2/12/2019 1800"}, result);
    }

    @Test
    public void parseDeadline_missingBy_throwsMrDuckyException() {
        String input = "deadline return book";
        assertThrows(MrDuckyException.class, () -> Parser.parseDeadline(input));
    }

    @Test
    public void parseDeadline_missingDescription_throwsMrDuckyException() {
        String input = "deadline /by 2/12/2019 1800";
        assertThrows(MrDuckyException.class, () -> Parser.parseDeadline(input));
    }

    @Test
    public void parseTodo_emptyDescription_throwsMrDuckyException() {
        String input = "todo";
        assertThrows(MrDuckyException.class, () -> Parser.parseTodo(input));
    }

    @Test
    public void parseEvent_missingFrom_throwsMrDuckyException() {
        String input = "event meeting /to 2/12/2019 1800";
        assertThrows(MrDuckyException.class, () -> Parser.parseEvent(input));
    }

    @Test
    public void parseEvent_missingTo_throwsMrDuckyException() {
        String input = "event meeting /from 2/12/2019 1800";
        assertThrows(MrDuckyException.class, () -> Parser.parseEvent(input));
    }

    @Test
    public void parseIndex_missingIndex_throwsMrDuckyException() {
        String input = "mark";
        assertThrows(MrDuckyException.class, () -> Parser.parseIndex(input, "mark"));
    }

    @Test
    public void parseIndex_nonNumeric_throwsMrDuckyException() {
        String input = "mark abc";
        assertThrows(MrDuckyException.class, () -> Parser.parseIndex(input, "mark"));
    }
}


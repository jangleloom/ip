package mrducky.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import mrducky.exception.DukeException;

/**
 * Tests parsing of user inputs.
 */
public class ParserTest {

    @Test
    public void parseDeadline_validInput_returnsDescriptionAndBy() throws DukeException {
        String input = "deadline return book /by 2/12/2019 1800";
        String[] result = Parser.parseDeadline(input);
        assertArrayEquals(new String[]{"return book", "2/12/2019 1800"}, result);
    }

    @Test
    public void parseDeadline_missingBy_throwsDukeException() {
        String input = "deadline return book";
        assertThrows(DukeException.class, () -> Parser.parseDeadline(input));
    }

    @Test
    public void parseDeadline_missingDescription_throwsDukeException() {
        String input = "deadline /by 2/12/2019 1800";
        assertThrows(DukeException.class, () -> Parser.parseDeadline(input));
    }

    @Test
    public void parseTodo_emptyDescription_throwsDukeException() {
        String input = "todo";
        assertThrows(DukeException.class, () -> Parser.parseTodo(input));
    }

    @Test
    public void parseEvent_missingFrom_throwsDukeException() {
        String input = "event meeting /to 2/12/2019 1800";
        assertThrows(DukeException.class, () -> Parser.parseEvent(input));
    }

    @Test
    public void parseEvent_missingTo_throwsDukeException() {
        String input = "event meeting /from 2/12/2019 1800";
        assertThrows(DukeException.class, () -> Parser.parseEvent(input));
    }

    @Test
    public void parseIndex_missingIndex_throwsDukeException() {
        String input = "mark";
        assertThrows(DukeException.class, () -> Parser.parseIndex(input, "mark"));
    }

    @Test
    public void parseIndex_nonNumeric_throwsDukeException() {
        String input = "mark abc";
        assertThrows(DukeException.class, () -> Parser.parseIndex(input, "mark"));
    }
}

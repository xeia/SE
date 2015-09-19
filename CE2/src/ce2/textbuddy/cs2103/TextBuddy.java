package ce2.textbuddy.cs2103;

/**
 * CE2: TextBuddy++
 *
 * This class is based on the sample execution and program behavior found within
 * the module handbook. It is used to manipulate text in a file via various
 * commands that is supported and the manipulations are all saved to the output
 * document after each command operation.
 *
 * The commands that this program support and their forms are as follow:
 *     add      < text to be added >
 *     delete   < line number of entry displayed by 'display' >
 *     search   < search term >
 *     sort
 *     display
 *     clear
 *     exit
 *
 * Some assumptions that this program makes:
 * - The amount of content kept within the text file output by this program is reasonable
 * - The text file used by the program when it is running is not being directly
 *     modified outside of the program
 *     i.e. all modifications to the text file outside of this program is
 *     made while the program is not running
 * - The input file name might refer to an existing file and is assumed to be
 *     a text file that is manipulatable.
 * - Commands are entered exactly as specified and with the correct number of parameters.
 *  - File name inputs are given correctly
 *
 * Most of the work is done in the Logic component which this class calls.
 * The interaction would be similar to:
 *
 * TextBuddy -> Logic -> DataEngine -> Storage
 *                  |--> UI ~^
 *
 * @author Tan Xue Si
 *
 */
public class TextBuddy {

    public static void main(String[] args) {
        Logic logic = new Logic();
        logic.initialize(args);
    }
}

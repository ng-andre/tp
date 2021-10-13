package seedu.parser;

import seedu.command.AddContactCommand;
import seedu.command.EditContactCommand;
import seedu.command.DeleteContactCommand;
import seedu.command.Command;
import seedu.command.HelpCommand;
import seedu.command.ViewContactCommand;
import seedu.command.FailedCommand;
import seedu.command.ExitCommand;
import seedu.command.ListContactsCommand;

import seedu.contact.DetailType;
import seedu.exception.ForbiddenDetailException;
import seedu.exception.InvalidEmailException;
import seedu.exception.InvalidFlagException;
import seedu.exception.InvalidGithubUsernameException;
import seedu.exception.InvalidLinkedinUsernameException;
import seedu.exception.InvalidNameException;
import seedu.exception.InvalidTelegramUsernameException;
import seedu.exception.InvalidTwitterUsernameException;
import seedu.exception.MissingArgException;
import seedu.exception.MissingDetailException;
import seedu.exception.MissingNameException;

import static seedu.parser.ContactParser.NUMBER_OF_FIELDS;

public class MainParser {
    private static final String ADD_CONTACT_COMD = "add";
    private static final String EDIT_CONTACT_COMD = "edit";
    private static final String DELETE_CONTACT_COMD = "rm";
    private static final String VIEW_CONTACT_COMD = "view";
    private static final String EXIT_COMD = "exit";
    private static final String LIST_COMD = "list";
    private static final String HELP_COMD = "help";

    private static final int COMD_WORD_INDEX = 0;
    private static final int ISOLATE_COMD_WORD = 2;
    public static final int NAME_INDEX = 0;

    private ContactParser contactParser;
    private AddContactParser addContactParser = new AddContactParser();
    private EditContactParser editContactParser = new EditContactParser();

    public Command parseCommand(String userInput) {
        String commandType = getCommandWord(userInput);
        Command command;
        switch (commandType) {
        case ADD_CONTACT_COMD:
            command = parseAddContact(userInput);
            break;
        case EDIT_CONTACT_COMD:
            command = parseEditContact(userInput);
            break;
        case DELETE_CONTACT_COMD:
            command = parseDeleteContact(userInput);
            break;
        case VIEW_CONTACT_COMD:
            command = parseViewContact(userInput);
            break;
        case EXIT_COMD:
            command = new ExitCommand();
            break;
        case LIST_COMD:
            command = new ListContactsCommand();
            break;
        default:
            command = new FailedCommand(FailedCommandType.GENERAL);
        }
        return command;
    }

    public String getCommandWord(String userInput) {
        String[] destructuredInputs = userInput.split(" ", ISOLATE_COMD_WORD);
        return destructuredInputs[COMD_WORD_INDEX];
    }

    private Command parseAddContact(String userInput) {
        contactParser = addContactParser;
        try {
            String[] details = contactParser.parseContactDetails(userInput);
            //check if name is specified in input
            if (details[NAME_INDEX] == null) {
                throw new MissingNameException();
            }
            assert details.length == NUMBER_OF_FIELDS;
            String name = details[DetailType.NAME.getIndex()];
            String github = details[DetailType.GITHUB.getIndex()];
            String linkedin = details[DetailType.LINKEDIN.getIndex()];
            String telegram = details[DetailType.TELEGRAM.getIndex()];
            String twitter = details[DetailType.TWITTER.getIndex()];
            String email = details[DetailType.EMAIL.getIndex()];
            return new AddContactCommand(name, github, linkedin, telegram, twitter, email);
        } catch (InvalidFlagException e) {
            return new FailedCommand(FailedCommandType.INVALID_FLAG);
        } catch (MissingArgException e) {
            return new FailedCommand(FailedCommandType.MISSING_ARG);
        } catch (MissingNameException e) {
            return new FailedCommand(FailedCommandType.MISSING_NAME);
        } catch (MissingDetailException e) {
            return new FailedCommand(FailedCommandType.MISSING_DETAIL);
        } catch (ForbiddenDetailException e) {
            return new FailedCommand(FailedCommandType.FORBIDDEN_DETAIL);
        } catch (InvalidNameException e) {
            return new FailedCommand(FailedCommandType.INVALID_NAME);
        } catch (InvalidGithubUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_GITHUB_USERNAME);
        } catch (InvalidEmailException e) {
            return new FailedCommand(FailedCommandType.INVALID_MAIL);
        } catch (InvalidTelegramUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_TELEGRAM);
        } catch (InvalidTwitterUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_TWITTER);
        } catch (InvalidLinkedinUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_LINKEDIN);
        }
    }

    private Command parseEditContact(String userInput) { // userInput is raw user input
        contactParser = editContactParser;
        try {
            String[] details = editContactParser.parseContactDetails(userInput);
            //throws InvalidFlagException, MissingDetailException, MissingArgException
            int userIndex = IndexParser.getIndexFromInput(userInput, EDIT_CONTACT_COMD); //throws MissingArgException
            return new EditContactCommand(details, userIndex);
        } catch (InvalidFlagException e) {
            return new FailedCommand(FailedCommandType.INVALID_FLAG);
        } catch (MissingArgException e) {
            return new FailedCommand(FailedCommandType.MISSING_ARG);
        } catch (NumberFormatException e) {
            return new FailedCommand(FailedCommandType.INVALID_INDEX);
        } catch (MissingDetailException e) {
            return new FailedCommand(FailedCommandType.MISSING_DETAIL);
        } catch (ForbiddenDetailException e) {
            return new FailedCommand(FailedCommandType.FORBIDDEN_DETAIL);
        } catch (InvalidNameException e) {
            return new FailedCommand(FailedCommandType.INVALID_NAME);
        } catch (InvalidGithubUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_GITHUB_USERNAME);
        } catch (InvalidEmailException e) {
            return new FailedCommand(FailedCommandType.INVALID_MAIL);
        } catch (InvalidTelegramUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_TELEGRAM);
        } catch (InvalidTwitterUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_TWITTER);
        } catch (InvalidLinkedinUsernameException e) {
            return new FailedCommand(FailedCommandType.INVALID_LINKEDIN);
        }
    }

    private Command parseViewContact(String userInput) {
        try {
            int viewedIndex = IndexParser.getIndexFromInput(userInput, VIEW_CONTACT_COMD);
            return new ViewContactCommand(viewedIndex);
        } catch (MissingArgException e) {
            return new FailedCommand(FailedCommandType.MISSING_ARG);
        } catch (NumberFormatException e) {
            return new FailedCommand(FailedCommandType.INVALID_INDEX);
        }
    }

    private Command parseDeleteContact(String userInput) {
        try {
            int deletedIndex = IndexParser.getIndexFromInput(userInput, DELETE_CONTACT_COMD);
            return new DeleteContactCommand(deletedIndex);
        } catch (MissingArgException e) {
            return new FailedCommand(FailedCommandType.MISSING_ARG);
        } catch (NumberFormatException e) {
            return new FailedCommand(FailedCommandType.INVALID_INDEX);
        }
    }
}

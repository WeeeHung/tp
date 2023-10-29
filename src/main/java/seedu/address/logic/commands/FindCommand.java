package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.IdContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String COMMAND_WORD_ALIAS = "f";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " or " + COMMAND_WORD_ALIAS
            + ": Finds all Patients whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example 1: " + COMMAND_WORD + " n/alice bob charlie \n"
            + "Example 2: " + COMMAND_WORD + " id/S872D \n"
            + "Example 3: " + COMMAND_WORD_ALIAS + " id/S872D";

    private final Predicate<Person> predicate;

    /**
     * Creates a {@code FindCommand} to find persons by name with the specified namePredicate.
     *
     * @param namePredicate The predicate to match persons by name.
     * @throws NullPointerException if {@code namePredicate} is null.
     */
    public FindCommand(NameContainsKeywordsPredicate namePredicate) {
        this.predicate = namePredicate;
    }

    public FindCommand(IdContainsKeywordsPredicate idPredicate) {
        this.predicate = idPredicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}

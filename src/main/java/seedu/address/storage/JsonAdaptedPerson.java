package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.Id;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.enums.InputSource;
import seedu.address.model.person.exceptions.BadAppointmentFormatException;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String id;
    private final String phone;
    private final String email;
    private final String address;
    private final String appointment;
    private final List<JsonAdaptedMedicalHistory> medicalHistories = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("id") String id,
                             @JsonProperty("phone") String phone, @JsonProperty("email") String email,
                             @JsonProperty("address") String address, @JsonProperty("appointment") String appointment,
                             @JsonProperty("medicalHistories") List<JsonAdaptedMedicalHistory> medicalHistories) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.appointment = appointment;
        if (medicalHistories != null) {
            this.medicalHistories.addAll(medicalHistories);
        }
    }

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    public JsonAdaptedPerson(String name,
                             String phone,
                             String email,
                             String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.id = null;
        this.appointment = null;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        id = source.getId() != null
            ? source.getId().value
            : null;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        appointment = source.getAppointment().isPresent()
            ? source.getAppointment().get().toSaveString()
            : null;
        medicalHistories.addAll(source.getMedicalHistories().stream()
                .map(JsonAdaptedMedicalHistory::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<MedicalHistory> personMedicalHistory = new ArrayList<>();
        for (JsonAdaptedMedicalHistory medicalHistory : medicalHistories) {
            personMedicalHistory.add(medicalHistory.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Id.class.getSimpleName()));
        }
        if (!Id.isValidId(id)) {
            throw new IllegalValueException(Id.MESSAGE_CONSTRAINTS);
        }
        final Id modelId = new Id(id);

        final Set<MedicalHistory> modelMedicalHistories = new HashSet<>(personMedicalHistory);

        if (appointment != null && !Appointment.isValidAppointmentDelimit(appointment, InputSource.STORAGE)) {
            throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
        }

        final Appointment modelAppointment;
        try {
            modelAppointment = appointment == null ? null : Appointment.of(appointment, InputSource.STORAGE);
        } catch (BadAppointmentFormatException e) {
            throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
        }

        return new Person(modelName, modelId, modelPhone, modelEmail, modelAddress, modelAppointment,
                modelMedicalHistories);
    }

}

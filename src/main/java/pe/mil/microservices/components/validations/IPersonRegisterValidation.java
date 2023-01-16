package pe.mil.microservices.components.validations;

import pe.mil.microservices.components.enums.PersonValidationResult;
import pe.mil.microservices.dto.requests.RegisterPersonRequest;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface IPersonRegisterValidation
    extends Function<RegisterPersonRequest, PersonValidationResult> {

    static IPersonRegisterValidation isPersonNameValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getName() != null
                && !registerCustomerRequest.getName().isEmpty()
                && !registerCustomerRequest.getName().isBlank()

                ? PersonValidationResult.PERSON_VALID
                : PersonValidationResult.INVALID_PERSON_NAME;
    }

    static IPersonRegisterValidation isPersonIdValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getPersonId() != null
                ? PersonValidationResult.PERSON_VALID
                : PersonValidationResult.INVALID_PERSON_ID;
    }

    static IPersonRegisterValidation isPersonLastNameValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getLastName() != null
                && !registerCustomerRequest.getLastName().isBlank()
                && !registerCustomerRequest.getLastName().isEmpty()
                ? PersonValidationResult.PERSON_VALID
                : PersonValidationResult.INVALID_PERSON_LASTNAME;
    }

    static IPersonRegisterValidation isPersonDocumentNumberValidation() {
        return registerCustomerRequest ->
            registerCustomerRequest.getDni() != null
                && !registerCustomerRequest.getDni().isBlank()
                && !registerCustomerRequest.getDni().isEmpty()
                ? PersonValidationResult.PERSON_VALID
                : PersonValidationResult.INVALID_PERSON_DOCUMENT_NUMBER;
    }

    static IPersonRegisterValidation customValidation(Predicate<RegisterPersonRequest> validate) {
        return registerCustomerRequest -> validate.test(registerCustomerRequest)
            ? PersonValidationResult.PERSON_VALID
            : PersonValidationResult.INVALID_PERSON_NAME;
    }

    default IPersonRegisterValidation and(IPersonRegisterValidation andValidation) {
        return registerCustomerRequest -> {
            PersonValidationResult validation = this.apply(registerCustomerRequest);
            return validation.equals(PersonValidationResult.PERSON_VALID)
                ? andValidation.apply(registerCustomerRequest)
                : validation;
        };
    }

    default IPersonRegisterValidation or(IPersonRegisterValidation orValidation) {
        return registerCustomerRequest -> {
            PersonValidationResult validation = this.apply(registerCustomerRequest);
            return validation.equals(PersonValidationResult.PERSON_VALID)
                ? orValidation.apply(registerCustomerRequest)
                : validation;
        };
    }
}

package pe.mil.microservices.services.implementations;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mil.microservices.components.enums.PersonValidationResult;
import pe.mil.microservices.components.mappers.contracts.IPersonMapperByMapstruct;
import pe.mil.microservices.components.validations.IPersonRegisterValidation;
import pe.mil.microservices.dto.Person;
import pe.mil.microservices.dto.requests.RegisterPersonRequest;
import pe.mil.microservices.dto.responses.RegisterPersonResponse;
import pe.mil.microservices.repositories.contracts.IPersonRepository;
import pe.mil.microservices.repositories.entities.PersonEntity;
import pe.mil.microservices.services.contracts.IPersonServices;
import pe.mil.microservices.utils.components.enums.ResponseCode;
import pe.mil.microservices.utils.components.exceptions.CommonBusinessProcessException;
import pe.mil.microservices.utils.components.helpers.ObjectMapperHelper;
import pe.mil.microservices.utils.dtos.base.GenericBusinessResponse;
import pe.mil.microservices.utils.dtos.process.BusinessProcessResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class PersonService implements IPersonServices {

    private final IPersonRepository personRepository;
    private final String personServiceId;


    @Autowired
    public PersonService(
        final IPersonRepository personRepository
    ) {
        this.personRepository = personRepository;
        personServiceId = UUID.randomUUID().toString();
        log.debug("personServiceId {}", personServiceId);
        log.debug("PersonService loaded successfully");
    }

    @Override
    public Mono<BusinessProcessResponse> getById(Long id) {

        log.info("this is in services getById method");
        log.debug("personServiceId {}", personServiceId);

        GenericBusinessResponse<Person> genericMessagesBusinessResponse = new GenericBusinessResponse<>();

        return Mono
            .just(genericMessagesBusinessResponse)
            .flatMap(generic -> {
                final Optional<PersonEntity> entity = this.personRepository.findById(id);
                if (entity.isEmpty()) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                } else {
                    return Mono.just(entity.get());
                }
            })
            .flatMap(entity -> {
                final Person target = ObjectMapperHelper.map(entity, Person.class);
                log.info("person {} ", target.toString());
                GenericBusinessResponse<Person> data = new GenericBusinessResponse<>(target);
                return Mono.just(data);
            })
            .flatMap(response -> {
                return Mono.just(BusinessProcessResponse.setEntitySuccessfullyResponse(response));
            })
            .doOnSuccess(success ->
                log.info("finish process getById, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Mono<BusinessProcessResponse> getAll() {

        log.info("this is in services getAll method");
        log.debug("personServiceId {}", personServiceId);
        GenericBusinessResponse<List<Person>> genericMessagesBusinessResponse = new GenericBusinessResponse<>();

        return Mono.just(genericMessagesBusinessResponse)
            .flatMap(generic -> {
                generic.setData(ObjectMapperHelper.mapAll(Lists.newArrayList(this.personRepository.findAll()), Person.class));
                return Mono.just(generic);
            })
            .flatMap(response -> {
                log.info("response {} ", response.getData().toString());
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(response));
            }).flatMap(process -> Mono.just(BusinessProcessResponse.setEntitySuccessfullyResponse(process.getBusinessResponse())))
            .doOnSuccess(success ->
                log.info("finish process getById, success: {}", success.toString())
            )
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Mono<BusinessProcessResponse> save(Mono<RegisterPersonRequest> entity) {

        log.info("this is in services save method");
        log.debug("personServiceId {}", personServiceId);

        return entity
            .flatMap(create -> {
                log.debug("this is in services save demo method");
                final PersonValidationResult result =
                    IPersonRegisterValidation
                        .isPersonIdValidation()
                        .and(IPersonRegisterValidation.isPersonNameValidation())
                        .and(IPersonRegisterValidation.isPersonLastNameValidation())
                        .apply(create);
                log.info("result {} ", result);
                if (!PersonValidationResult.PERSON_VALID.equals(result)) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(create);
            })
            .flatMap(request -> {
                log.debug("log in flatMap context #2");
                final PersonEntity save =
                    IPersonMapperByMapstruct.PERSON_MAPPER.mapPersonEntityByRegisterPersonRequest(request);
                log.info("person entity {} ", save);
                boolean exists = this.personRepository.existsByPersonId(save.getPersonId());
                log.info("person exists entity {} ", exists);
                if (exists) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA_EXISTS));
                }
                final PersonEntity saved = this.personRepository.save(save);
                log.info("person saved entity {} ", saved);

                if (Objects.isNull(saved.getPersonId())) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(saved);
            })
            .flatMap(person -> {
                log.info("person person entity {} ", person);
                final RegisterPersonResponse response = ObjectMapperHelper
                    .map(person, RegisterPersonResponse.class);
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(new GenericBusinessResponse<>(response)));
            })
            .doOnSuccess(success ->
                log.info("finish process save, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Boolean delete(Person entity) {

        log.debug("this is in services delete method");
        log.debug("personServiceId {}", personServiceId);

        final Optional<PersonEntity> result = this.personRepository.findById(entity.getPersonId());

        if (result.isEmpty()) {
            return false;
        }

        this.personRepository.delete(result.get());

        return true;
    }

    @Override
    public Mono<BusinessProcessResponse> update(Mono<RegisterPersonRequest> entity) {

        log.info("this is in services update method");
        log.debug("personServiceId {}", personServiceId);

        return entity.
            flatMap(update -> {
                log.debug("this is in services update demo method");
                final PersonValidationResult result = IPersonRegisterValidation
                    .isPersonNameValidation().apply(update);
                if (!PersonValidationResult.PERSON_VALID.equals(result)) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(update);
            })
            .flatMap(request -> {
                log.debug("log in flatMap context #2");
                final PersonEntity update = IPersonMapperByMapstruct
                    .PERSON_MAPPER
                    .mapPersonEntityByRegisterPersonRequest(request);
                final PersonEntity updated = this.personRepository.save(update);

                if (Objects.isNull(updated.getPersonId())) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(updated);

            })
            .flatMap(person -> {
                final RegisterPersonResponse response = ObjectMapperHelper
                    .map(person, RegisterPersonResponse.class);
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(new GenericBusinessResponse<>(response)));
            }).doOnSuccess(success ->
                log.info("finish process save, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }
}

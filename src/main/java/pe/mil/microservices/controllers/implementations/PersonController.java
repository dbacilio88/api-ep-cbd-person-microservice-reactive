package pe.mil.microservices.controllers.implementations;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import pe.mil.microservices.controllers.contracts.IPersonController;
import pe.mil.microservices.dto.requests.RegisterPersonRequest;
import pe.mil.microservices.services.contracts.IPersonServices;
import pe.mil.microservices.utils.components.exceptions.CommonBusinessProcessException;
import pe.mil.microservices.utils.constants.LoggerConstants;
import pe.mil.microservices.utils.dtos.base.BusinessResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static pe.mil.microservices.constants.ProcessConstants.*;

@Log4j2
@RestController
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PersonController implements IPersonController {
    private final IPersonServices personServices;
    private final BusinessResponse businessResponse;
    private final String personControllerId;

    public PersonController(
        IPersonServices personServices,
        BusinessResponse businessResponse
    ) {
        this.personServices = personServices;
        this.businessResponse = businessResponse;
        this.personControllerId = UUID.randomUUID().toString();
        log.debug("personControllerId {}", personControllerId);
        log.debug("PersonController loaded successfully");
    }

    @Override
    @GetMapping(path = GET_PERSON_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> findAll() {

        log.debug("method findAll initialized successfully");
        log.debug("personControllerId {}", personControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, FIND_ALL_PERSON_LOG_METHOD);

        return this.personServices
            .getAll()
            .flatMap(processResponse -> {

                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }

                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            })
            .doOnSuccess(success ->
                log.info("finish process findAll")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)

            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process findAll, error: {}", throwable.getMessage())
            );
    }

    @Override
    @GetMapping(path = GET_PERSON_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> getById(Long customerId) {

        log.debug("method getById initialized successfully");
        log.debug("personControllerId {}", personControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, FIND_BY_ID_PERSON_LOG_METHOD);

        return this.personServices.getById(customerId)
            .flatMap(processResponse -> {
                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }
                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            }).doOnSuccess(success ->
                log.info("finish process getById")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)
            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    @PostMapping(value = SAVE_PERSON_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> save(Mono<RegisterPersonRequest> request) {

        log.debug("method save initialized successfully");
        log.debug("personControllerId {}", personControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, SAVE_PERSON_LOG_METHOD);

        return this.personServices.save(request)
            .flatMap(processResponse -> {
                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }
                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            }).doOnSuccess(success ->
                log.info("finish process save")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)
            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }

    @Override
    @PutMapping(value = MICROSERVICE_PERSON_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> update(Mono<RegisterPersonRequest> request) {
        log.debug("method update initialized successfully");
        log.debug("personControllerId {}", personControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, UPDATE_PERSON_LOG_METHOD);

        return this.personServices.update(request)
            .flatMap(processResponse -> {
                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }
                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            }).doOnSuccess(success ->
                log.info("finish process update")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)
            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process update, error: {}", throwable.getMessage())
            );
    }
}

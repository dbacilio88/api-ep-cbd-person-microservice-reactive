package pe.mil.microservices.services.contracts;

import pe.mil.microservices.dto.Person;
import pe.mil.microservices.dto.requests.RegisterPersonRequest;
import pe.mil.microservices.utils.dtos.process.BusinessProcessResponse;
import pe.mil.microservices.utils.service.interfaces.*;
import reactor.core.publisher.Mono;

public interface IPersonServices
    extends
    IGetDomainEntityById<Mono<BusinessProcessResponse>, Long>,
    IGetAllDomainEntity<Mono<BusinessProcessResponse>>,
    ISaveDomainEntity<Mono<BusinessProcessResponse>, Mono<RegisterPersonRequest>>,
    IUpdateDomainEntity<Mono<BusinessProcessResponse>, Mono<RegisterPersonRequest>>,
    IDeleteDomainEntity<Person>{
}

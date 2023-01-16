package pe.mil.microservices.components.mappers.contracts;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pe.mil.microservices.dto.requests.RegisterPersonRequest;
import pe.mil.microservices.repositories.entities.PersonEntity;

import static pe.mil.microservices.components.mappers.contracts.IPersonMapperByMapstruct.CustomerFields.*;

@Mapper
public interface IPersonMapperByMapstruct {

    IPersonMapperByMapstruct PERSON_MAPPER = Mappers
        .getMapper(IPersonMapperByMapstruct.class);

    @Mapping(source = FIELD_PERSON_ID, target = FIELD_PERSON_ID)
    @Mapping(source = FIELD_PERSON_NAME, target = FIELD_PERSON_NAME)
    @Mapping(source = FIELD_CUSTOMER_LASTNAME, target = FIELD_CUSTOMER_LASTNAME)
    @Mapping(source = FIELD_CUSTOMER_DOCUMENT_NUMBER, target = FIELD_CUSTOMER_DOCUMENT_NUMBER)
    PersonEntity mapPersonEntityByRegisterPersonRequest(RegisterPersonRequest source);


    class CustomerFields {
        public static final String FIELD_PERSON_ID = "personId";
        public static final String FIELD_PERSON_NAME = "name";
        public static final String FIELD_CUSTOMER_LASTNAME = "lastName";
        public static final String FIELD_CUSTOMER_DOCUMENT_NUMBER = "dni";
    }
}

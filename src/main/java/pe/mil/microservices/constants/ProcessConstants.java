package pe.mil.microservices.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProcessConstants {

    public static final String PROCESS_TYPE_STRING = "String";
    public static final String PARAM_COMPONENT_UUID = "String";
    public static final String PARAMETER_EMPTY_VALUE = "";
    public static final String PARAMETER_ACTUATOR_PATH_CONTAIN_VALUE = "actuator";
    public static final String MICROSERVICE_PATH_CONTEXT = "";
    public static final String MICROSERVICE_PERSON_PATH = MICROSERVICE_PATH_CONTEXT + "/persons";
    public static final String GET_PERSON_PATH = "";
    public static final String SAVE_PERSON_PATH = "";
    public static final String GET_PERSON_ID_PATH = "/{personId}";

    public static final String FIND_ALL_PERSON_LOG_METHOD = "find.customers.method";
    public static final String FIND_BY_ID_PERSON_LOG_METHOD = "findById.customers.method";
    public static final String SAVE_PERSON_LOG_METHOD = "save.customers.method";
    public static final String UPDATE_PERSON_LOG_METHOD = "update.customers.method";


    public static final String MAPSTRUCT_COMPONENT_MODEL_CONFIGURATION = "spring";

}

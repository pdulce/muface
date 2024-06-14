package com.mfc.infra.utils;

public final class ArqConstantMessages {

    // Greeting
    public static final String GREETING = "greeting";
    public static final String LOGOUT = "logout";

    public static final String NOT_EMPTY_LET = "not_empty_msg";
    public static final String MIN_VALUE_REQUIRED = "min_value_required_msg";

    public static final String DISTRIBUTED_INITIADED = "transacc_distruted_initiated";
    public static final String DISTRIBUTED_FIN_STATE_OK = "saga_fin_ok";
    public static final String DISTRIBUTED_RUNNING = "saga_fin_running";
    public static final String DISTRIBUTED_FIN_STATE_KO = "saga_fin_ko";

    // Informational 1xx
    public static final String INFO_CONTINUE = "info.continue";
    public static final String INFO_SWITCHING_PROTOCOLS = "info.switching_protocols";
    public static final String INFO_PROCESSING = "info.processing";
    public static final String INFO_EARLY_HINTS = "info.early_hints";

    // Successful 2xx
    public static final String SUCCESS_OK = "success.ok";
    public static final String SUCCESS_CREATED = "success.created";
    public static final String SUCCESS_ACCEPTED = "success.accepted";
    public static final String SUCCESS_NON_AUTHORITATIVE_INFORMATION = "success.non_authoritative_information";
    public static final String SUCCESS_NO_CONTENT = "success.no_content";
    public static final String SUCCESS_RESET_CONTENT = "success.reset_content";
    public static final String SUCCESS_PARTIAL_CONTENT = "success.partial_content";
    public static final String SUCCESS_MULTI_STATUS = "success.multi_status";
    public static final String SUCCESS_ALREADY_REPORTED = "success.already_reported";
    public static final String SUCCESS_IM_USED = "success.im_used";

    // Redirection 3xx
    public static final String REDIRECT_MULTIPLE_CHOICES = "redirect.multiple_choices";
    public static final String REDIRECT_MOVED_PERMANENTLY = "redirect.moved_permanently";
    public static final String REDIRECT_FOUND = "redirect.found";
    public static final String REDIRECT_SEE_OTHER = "redirect.see_other";
    public static final String REDIRECT_NOT_MODIFIED = "redirect.not_modified";
    public static final String REDIRECT_USE_PROXY = "redirect.use_proxy";
    public static final String REDIRECT_TEMPORARY_REDIRECT = "redirect.temporary_redirect";
    public static final String REDIRECT_PERMANENT_REDIRECT = "redirect.permanent_redirect";

    // Client Error 4xx
    public static final String ERROR_BAD_REQUEST = "error.bad_request";
    public static final String ERROR_UNAUTHORIZED = "error.unauthorized";
    public static final String ERROR_PAYMENT_REQUIRED = "error.payment_required";
    public static final String ERROR_FORBIDDEN = "error.forbidden";
    public static final String ERROR_NOT_FOUND = "error.not_found";
    public static final String ERROR_METHOD_NOT_ALLOWED = "error.method_not_allowed";
    public static final String ERROR_NOT_ACCEPTABLE = "error.not_acceptable";
    public static final String ERROR_PROXY_AUTHENTICATION_REQUIRED = "error.proxy_authentication_required";
    public static final String ERROR_REQUEST_TIMEOUT = "error.request_timeout";
    public static final String ERROR_CONFLICT = "error.conflict";
    public static final String ERROR_GONE = "error.gone";
    public static final String ERROR_LENGTH_REQUIRED = "error.length_required";
    public static final String ERROR_PRECONDITION_FAILED = "error.precondition_failed";
    public static final String ERROR_PAYLOAD_TOO_LARGE = "error.payload_too_large";
    public static final String ERROR_URI_TOO_LONG = "error.uri_too_long";
    public static final String ERROR_UNSUPPORTED_MEDIA_TYPE = "error.unsupported_media_type";
    public static final String ERROR_RANGE_NOT_SATISFIABLE = "error.range_not_satisfiable";
    public static final String ERROR_EXPECTATION_FAILED = "error.expectation_failed";
    public static final String ERROR_IM_A_TEAPOT = "error.im_a_teapot";
    public static final String ERROR_MISDIRECTED_REQUEST = "error.misdirected_request";
    public static final String ERROR_UNPROCESSABLE_ENTITY = "error.unprocessable_entity";
    public static final String ERROR_LOCKED = "error.locked";
    public static final String ERROR_FAILED_DEPENDENCY = "error.failed_dependency";
    public static final String ERROR_TOO_EARLY = "error.too_early";
    public static final String ERROR_UPGRADE_REQUIRED = "error.upgrade_required";
    public static final String ERROR_PRECONDITION_REQUIRED = "error.precondition_required";
    public static final String ERROR_TOO_MANY_REQUESTS = "error.too_many_requests";
    public static final String ERROR_REQUEST_HEADER_FIELDS_TOO_LARGE = "error.request_header_fields_too_large";
    public static final String ERROR_UNAVAILABLE_FOR_LEGAL_REASONS = "error.unavailable_for_legal_reasons";

    // Server Error 5xx
    public static final String ERROR_INTERNAL_SERVER_ERROR = "error.internal_server_error";
    public static final String ERROR_NOT_IMPLEMENTED = "error.not_implemented";
    public static final String ERROR_BAD_GATEWAY = "error.bad_gateway";
    public static final String ERROR_SERVICE_UNAVAILABLE = "error.service_unavailable";
    public static final String ERROR_GATEWAY_TIMEOUT = "error.gateway_timeout";
    public static final String ERROR_HTTP_VERSION_NOT_SUPPORTED = "error.http_version_not_supported";
    public static final String ERROR_VARIANT_ALSO_NEGOTIATES = "error.variant_also_negotiates";
    public static final String ERROR_INSUFFICIENT_STORAGE = "error.insufficient_storage";
    public static final String ERROR_LOOP_DETECTED = "error.loop_detected";
    public static final String ERROR_NOT_EXTENDED = "error.not_extended";
    public static final String ERROR_NETWORK_AUTHENTICATION_REQUIRED = "error.network_authentication_required";

    private ArqConstantMessages() {
        // Evitar instanciación
    }
}




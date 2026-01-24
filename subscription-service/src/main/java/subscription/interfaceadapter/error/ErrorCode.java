package subscription.interfaceadapter.error;

public enum ErrorCode {

    // === GENERIC ===
    INTERNAL_ERROR("GEN-001", "Erro inesperado"),
    ENDPOINT_NOT_FOUND("GEN-404", "Endpoint não encontrado"),


    // === SUBSCRIPTION ===
    SUBSCRIPTION_NOT_FOUND("SUB-001", "Assinatura não encontrada"),
    SUBSCRIPTION_INVALID_STATE("SUB-002", "Estado inválido da assinatura"),
    SUBSCRIPTION_ALREADY_CANCELED("SUB-003", "Assinatura já cancelada"),

    // === VALIDATION ===
    INVALID_PAYLOAD("VAL-001", "Payload inválido");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String code() {
        return code;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
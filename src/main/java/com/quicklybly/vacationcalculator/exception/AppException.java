package com.quicklybly.vacationcalculator.exception;

public class AppException extends RuntimeException {
    protected final CODE code;

    protected AppException(CODE code, String msg) {
        this(code, null, msg);
    }

    protected AppException(CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

    public CODE getCode() {
        return code;
    }

    public enum CODE {
        INTEGRATION_RETRY_EXCEPTION("Integration exception, retrying"),
        INTEGRATION_FINAL_EXCEPTION("Integration failed"),
        ;
        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public String getCodeDescription() {
            return codeDescription;
        }

        public AppException get() {
            return new AppException(this, this.codeDescription);
        }

        public AppException get(String msg) {
            return new AppException(this, this.codeDescription + " : " + msg);
        }

        public AppException get(Throwable e) {
            return new AppException(this, e, this.codeDescription + " : " + e.getMessage());
        }

        public AppException get(Throwable e, String msg) {
            return new AppException(this, e, this.codeDescription + " : " + msg);
        }
    }
}

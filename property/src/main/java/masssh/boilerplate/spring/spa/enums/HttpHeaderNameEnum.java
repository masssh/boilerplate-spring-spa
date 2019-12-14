package masssh.boilerplate.spring.spa.enums;

import lombok.Getter;

@Getter
public enum HttpHeaderNameEnum {
    X_ACCESS_ID("X_ACCESS_ID"),
    X_ACCESS_TOKEN("X_ACCESS_TOKEN"),
    ;

    final String headerName;

    HttpHeaderNameEnum(final String headerName) {
        this.headerName = headerName;
    }
}

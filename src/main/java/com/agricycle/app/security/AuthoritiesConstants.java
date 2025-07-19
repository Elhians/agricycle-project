package com.agricycle.app.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String AGRICULTEUR = "ROLE_AGRICULTEUR";
    public static final String CONSOMMATEUR = "ROLE_CONSOMMATEUR";
    public static final String COMMERCANT = "ROLE_COMMERCANT";
    public static final String ENTREPRISE = "ROLE_ENTREPRISE";
    public static final String ORGANISATION = "ROLE_ORGANISATION";
    public static final String TRANSPORTEUR = "ROLE_TRANSPORTEUR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}

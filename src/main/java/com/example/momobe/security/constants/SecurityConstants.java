package com.example.momobe.security.constants;

public class SecurityConstants {
    public static final String JWT_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String JWT_PREFIX = "Bearer ";

    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 60000L * 30; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

    public final static String ROLES = "roles";
    public final static String ID = "id";
    public final static String EMAIL = "email";
    public static final String NICKNAME = "nickname";

    public final static String REDIRECT_URL_SERVER = "https://www.momo-deploy.site/oauth/login";
    public final static String REDIRECT_URL_LOCAL = "http://localhost:3000/oauth/login";

    public final static String SERVER_URL = "www.momo-deploy.site";
    public final static String LOCAL_URL = "localhost:3000";

    public final static String ACCESS_TOKEN = "AccessToken";

    public final static Integer TEMPORARY_PASSWORD_LENGTH = 15;
}

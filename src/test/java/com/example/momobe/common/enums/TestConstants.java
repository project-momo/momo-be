package com.example.momobe.common.enums;

import java.time.LocalDateTime;
import java.util.List;

public class TestConstants {
    public final static String EMAIL1 = "user@test.com";
    public final static String EMAIL2 = "user2@test.com";

    public final static String PASSWORD1 = "passWORD123!";
    public final static String PASSWORD2 = "passWORD321!";
    public final static Long ID1 = 1L;
    public final static Long ID2 = 2L;
    public final static String NICKNAME1 = "test1";
    public final static String NICKNAME2 = "test2";

    public final static String NAME1 = "김민수";
    public final static String NAME2 = "임지민";

    public final static String ROLE_USER = "ROLE_USER";
    public final static String ROLE_MANAGER = "ROLE_MANAGER";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    public final static String TITLE1 = "제목1";
    public final static String TITLE2 = "제목2";

    public final static String CONTENT1 = "내용1";
    public final static String CONTENT2 = "내용2";
    public final static String CONTENT3 = "내용3";

    public final static String ADDRESS1 = "주소1";
    public final static String ADDRESS2 = "주소2";

    public final static LocalDateTime NOW_TIME = LocalDateTime.now();

    public final static String GITHUB_URL = "https://github.com/";
    public final static String TISTORY_URL = "https://7357.tistory.com/";
    public final static String REMOTE_PATH = "https://7357.tistory.com/";

    public final static String JWT_HEADER = "Authorization";
    public final static String INVALID_JWT_PREFIX = "fake ";
    public final static String JWT_PREFIX = "Bearer ";
    public final static String BEARER_ACCESS_TOKEN = "Bearer AccessToken";
    public final static String INVALID_BEARER_ACCESS_TOKEN = "Bearer FakeToken";
    public final static String BEARER_REFRESH_TOKEN = "Bearer RefreshToken";
    public final static String INVALID_BEARER_REFRESH_TOKEN = "Bearer FakeToken";
    public final static String ACCESS_TOKEN = "AccessToken";
    public final static String REFRESH_TOKEN = "RefreshToken";
    public final static String INVALID_REFRESH_TOKEN = "Bearer FakeToken";

    public final static String INVALID_SECRET_KEY = "dlrjtdmswkfahtehlstlzmfltzldlqslek";
    public final static String SECRET_KEY = "dlrjtdmstlzmfltzlekrmfjsepTmfakfdldjqtek";
    public final static String REFRESH_KEY = "dlrjtdmsflvmfptlzlekrmfjsepTmfakfdldjqtek";

    public final static int ZERO = 0;
    public final static int ONE = 1;
    public final static int TWO = 2;
    public final static int THREE = 3;
    public final static int FOUR = 4;

    public final static String ROLES = "roles";
    public final static String ID = "id";

    public final static List<String> ROLE_USER_LIST = List.of(ROLE_USER);
    public final static List<String> ROLE_MANAGER_LIST = List.of(ROLE_MANAGER);
    public final static List<String> ROLE_ADMIN_LIST = List.of(ROLE_ADMIN);
    public final static List<String> ROLE_USER_ADMIN_LIST = List.of(ROLE_USER, ROLE_ADMIN);
    public final static List<String> ROLE_USER_MANAGER_LIST = List.of(ROLE_USER, ROLE_MANAGER);
    public final static List<String> ROLE_USER_MANAGER_ADMIN_LIST = List.of(ROLE_USER,ROLE_MANAGER,ROLE_ADMIN);

    public final static String AUTH_KEY = "11111111";
    public final static String INVALID_AUTH_KEY = "22222222";

    public final static String REDIS_AUTH_KEY_EMPTY = "emtpy key";
    public final static String REDIS_AUTH_KEY_NOT_EMPTY = "not empty key";

    public final static String REDIS_EMAIL_EMPTY = "empty email";
    public final static String REDIS_EMAIL_NOT_EMPTY = "not empty email";

    public final static String BEARER_REDIS_TOKEN_EMPTY = "Bearer emptyToken";
    public final static String BEARER_REDIS_TOKEN_NOT_EMPTY = "Bearer notEmptyToken";

    public final static String REDIS_TOKEN_EMPTY = "emptyToken";
    public final static String REDIS_TOKEN_NOT_EMPTY = "notEmptyToken";

    public final static String EMAIL = "email";
    public static final String NICKNAME = "nickname";
}

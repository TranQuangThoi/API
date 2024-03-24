package com.techmarket.api.constant;


public class UserBaseConstant {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";


    public static final Integer USER_KIND_MANAGER = 1;
    public static final Integer USER_KIND_EMPLOYEE = 2;
    public static final Integer USER_KIND_USER = 3;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer NATION_KIND_PROVINCE = 1;
    public static final Integer NATION_KIND_DISTRICT = 2;
    public static final Integer NATION_KIND_COMMUNE = 3;

    public static final Integer GROUP_KIND_EMPLOYEE = 1;
    public static final Integer GROUP_KIND_MANAGER = 2;
    public static final Integer GROUP_KIND_USER=3;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final int MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;

    public static final Integer CATEGORY_KIND_NEWS = 1;
    public static final Integer CATEGORY_KIND_PRODUCT = 2;


    public static final Integer GENDER_KIND_MALE = 0;
    public static final Integer GENDER_KIND_FEMALE = 1;
    public static final Integer GENDER_KIND_OTHER = 2;

    public static final Integer PAYMENT_KIND_CASH = 0;
    public static final Integer PAYMENT_KIND_BANK_TRANFER = 1;

    public static final Integer ORDER_STATE_PENDING_CONFIRMATION = 1;
    public static final Integer ORDER_STATE_CONFIRMED = 2;
    public static final Integer ORDER_STATE_CANCELED = 3;
    public static final Integer ORDER_STATE_COMPLETED = 4;

    public static final Integer REVIEW_ONE_STAR = 1;
    public static final Integer REVIEW_TWO_STAR = 2;
    public static final Integer REVIEW_THREE_STAR = 3;
    public static final Integer REVIEW_FOUR_STAR = 4;
    public static final Integer REVIEW_FIVE_STAR = 5;
    public static final Integer[] REVIEW_STARS = {
            REVIEW_ONE_STAR,
            REVIEW_TWO_STAR,
            REVIEW_THREE_STAR,
            REVIEW_FOUR_STAR,
            REVIEW_FIVE_STAR
    };

    public static final Integer VOUCHER_KIND_NEW_MEMBERSHIP = 0;
    public static final Integer VOUCHER_KIND_SILVER_MEMBERSHIP = 1;
    public static final Integer VOUCHER_KIND_GOLD_MEMBERSHIP = 2;
    public static final Integer VOUCHER_KIND_DIAMOND_MEMEBERSHIP = 3;
    public static final Integer VOUCHER_KIND_VIP_MEMEBERSHIP = 4;
    public static final Integer VOUCHER_KIND_ALL = 5;

    public static final Integer USER_KIND_NEW_MEMBERSHIP = 0;
    public static final Integer USER_KIND_SILVER_MEMBERSHIP = 1;
    public static final Integer USER_KIND_GOLD_MEMBERSHIP = 2;
    public static final Integer USER_KIND_DIAMOND_MEMEBERSHIP = 3;
    public static final Integer USER_KIND_VIP_MEMEBERSHIP = 4;

    public static final Integer NOTIFICATION_STATE_SENT = 0;
    public static final Integer NOTIFICATION_STATE_READ = 1;

    public static final Integer NOTIFICATION_KIND_NOTIFY_VOUCHER = 0;

    public static final Integer REFUNDS_KIND_NOT_YET_PROCCESS = 0;
    public static final Integer REFUNDS_KIND_PROCCESSED = 1;

    private UserBaseConstant(){
        throw new IllegalStateException("Utility class");
    }
}

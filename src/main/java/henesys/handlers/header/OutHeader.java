package henesys.handlers.header;

import java.util.List;

/**
 * Created on 15/12/2023
 */
public enum OutHeader {

    CHECK_PASSWORD_RESULT(0),
    GUEST_ID_LOGIN_RESULT(1),
    ACCOUNT_INFO_RESULT(2),
    CHECK_USER_LIMIT_RESULT(3),
    SET_ACCOUNT_RESULT(4),
    CONFIRM_EULA_RESULT(5),
    CHECK_PIN_CODE_RESULT(6),
    UPDATE_PIN_CODE_RESULT(7),
    VIEW_ALL_CHAR_RESULT(8),
    SELECT_CHARACTER_BY_VAC_RESULT(9),
    WORLD_INFORMATION(10),
    SELECT_WORLD_RESULT(11),
    SELECT_CHARACTER_RESULT(12),
    CHECK_DUPLICATED_ID_RESULT(13),
    CREATE_NEW_CHARACTER_RESULT(14),
    DELETE_CHARACTER_RESULT(15),
    MIGRATE_COMMAND(16),
    ALIVE_REQ(17),
    AUTHEN_CODE_CHANGED(18),
    AUTHEN_MESSAGE(19),
    SECURITY_PACKET(20),
    ENABLE_SPW_RESULT(21),
    DELETE_CHARACTER_OTP_REQUEST(22),
    CHECK_CRC_RESULT(23),
    LATEST_CONNECTED_WORLD(24),
    RECOMMENDED_WORLD_MESSAGE(25),
    CHECK_EXTRA_CHAR_INFO_RESULT(26),
    CHECK_SPW_RESULT(27),
    INVENTORY_OPERATION(28),
    INVENTORY_GROW(29),
    STAT_CHANGED(30),
    TEMPORARY_STAT_SET(31),
    TEMPORARY_STAT_RESET(32),
    FORCED_STAT_SET(33),
    FORCED_STAT_RESET(34),
    CHANGE_SKILL_RECORD_RESULT(35),
    SKILL_USE_RESULT(36),
    GIVE_POPULARITY_RESULT(37),
    MESSAGE(38),
    SEND_OPEN_FULL_CLIENT_LINK(39),
    MEMO_RESULT(40),
    MAP_TRANSFER_RESULT(41),
    ANTI_MACRO_RESULT(42),
    INITIAL_QUIZ_START(43),
    CLAIM_RESULT(44),
    SET_CLAIM_SVR_AVAILABLE_TIME(45),
    CLAIM_SVR_STATUS_CHANGED(46),
    SET_TAMING_MOB_INFO(47),
    QUEST_CLEAR(48),
    ENTRUSTED_SHOP_CHECK_RESULT(49),
    SKILL_LEARN_ITEM_RESULT(50),
    SKILL_RESET_ITEM_RESULT(51),
    GATHER_ITEM_RESULT(52),
    SORT_ITEM_RESULT(53),
    REMOTE_SHOP_OPEN_RESULT(54),
    SUE_CHARACTER_RESULT(55),
    MIGRATE_TO_CASH_SHOP_RESULT(56),
    TRADE_MONEY_LIMIT(57),
    SET_GENDER(58),
    GUILD_BBS(59),
    PET_DEAD_MESSAGE(60),
    CHARACTER_INFO(61),
    PARTY_RESULT(62),
    EXPEDITION_REQUEST(63),
    EXPEDITION_NOTI(64),
    FRIEND_RESULT(65),
    GUILD_REQUEST(66),
    GUILD_RESULT(67),
    ALLIANCE_RESULT(68),
    TOWN_PORTAL(69),
    OPEN_GATE(70),
    BROADCAST_MSG(71),
    INCUBATOR_RESULT(72),
    SHOP_SCANNER_RESULT(73),
    SHOP_LINK_RESULT(74),
    MARRIAGE_REQUEST(75),
    MARRIAGE_RESULT(76),
    WEDDING_GIFT_RESULT(77),
    MARRIED_PARTNER_MAP_TRANSFER(78),
    CASH_PET_FOOD_RESULT(79),
    SET_WEEK_EVENT_MESSAGE(80),
    SET_POTION_DISCOUNT_RATE(81),
    BRIDLE_MOB_CATCH_FAIL(82),
    IMITATED_NPC_RESULT(83),
    IMITATED_NPC_DATA(84),
    LIMITED_NPC_DISABLE_INFO(85),
    MONSTER_BOOK_SET_CARD(86),
    MONSTER_BOOK_SET_COVER(87),
    HOUR_CHANGED(88),
    MINI_MAP_ON_OFF(89),
    CONSULT_AUTHKEY_UPDATE(90),
    CLASS_COMPETITION_AUTHKEY_UPDATE(91),
    WEB_BOARD_AUTHKEY_UPDATE(92),
    SESSION_VALUE(93),
    PARTY_VALUE(94),
    FIELD_SET_VARIABLE(95),
    BONUS_EXP_RATE_CHANGED(96),
    POTION_DISCOUNT_RATE_CHANGED(97),
    FAMILY_CHART_RESULT(98),
    FAMILY_INFO_RESULT(99),
    FAMILY_RESULT(100),
    FAMILY_JOIN_REQUEST(101),
    FAMILY_JOIN_REQUEST_RESULT(102),
    FAMILY_JOIN_ACCEPTED(103),
    FAMILY_PRIVILEGE_LIST(104),
    FAMILY_FAMOUS_POINT_INC_RESULT(105),
    FAMILY_NOTIFY_LOGIN_OR_LOGOUT(106),
    FAMILY_SET_PRIVILEGE(107),
    FAMILY_SUMMON_REQUEST(108),
    NOTIFY_LEVEL_UP(109),
    NOTIFY_WEDDING(110),
    NOTIFY_JOB_CHANGE(111),
    INC_RATE_CHANGED(112),
    MAPLE_TV_USE_RES(113),
    AVATAR_MEGAPHONE_RES(114),
    AVATAR_MEGAPHONE_UPDATE_MESSAGE(115),
    AVATAR_MEGAPHONE_CLEAR_MESSAGE(116),
    CANCEL_NAME_CHANGE_RESULT(117),
    CANCEL_TRANSFER_WORLD_RESULT(118),
    DESTROY_SHOP_RESULT(119),
    FAKE_GM_NOTICE(120),
    SUCCESS_IN_USE_GACHAPON_BOX(121),
    NEW_YEAR_CARD_RES(122),
    RANDOM_MORPH_RES(123),
    CANCEL_NAME_CHANGE_BY_OTHER(124),
    SET_BUY_EQUIP_EXT(125),
    SET_PASSENGER_REQUEST(126),
    SCRIPT_PROGRESS_MESSAGE(127),
    DATA_CRC_CHECK_FAILED(128),
    CAKE_PIE_EVENT_RESULT(129),
    UPDATE_GM_BOARD(130),
    SHOW_SLOT_MESSAGE(131),
    WILD_HUNTER_INFO(132),
    ACCOUNT_MORE_INFO(133),
    FIND_FRIEND(134),
    STAGE_CHANGE(135),
    DRAGON_BALL_BOX(136),
    ASK_USER_WHETHER_USE_PAMS_SONG(137),
    TRANSFER_CHANNEL(138),
    DISALLOWED_DELIVERY_QUEST_LIST(139),
    MACRO_SYS_DATA_INIT(140),
    // end CharacterData
    SET_FIELD(141),
    SET_ITC(142),
    SET_CASH_SHOP(143),
    SET_BACKGROUND_EFFECT(144),
    SET_MAP_OBJECT_VISIBLE(145),
    CLEAR_BACKGROUND_EFFECT(146),

    TRANSFER_FIELD_REQ_IGNORED(147),
    TRANSFER_CHANNEL_REQ_IGNORED(148),
    FIELD_SPECIFIC_DATA(149),
    GROUP_MESSAGE(150),
    WHISPER(151),
    COUPLE_MESSAGE(152),
    MOB_SUMMON_ITEM_USE_RESULT(153),
    FIELD_EFFECT(154),
    FIELD_OBSTACLE_ON_OFF(155),
    FIELD_OBSTACLE_ON_OFF_STATUS(156),
    FIELD_OBSTACLE_ALL_RESET(157),
    BLOW_WEATHER(158),
    PLAY_JUKE_BOX(159),
    ADMIN_RESULT(160),
    QUIZ(161),
    DESC(162),
    CLOCK(163),
    CONTI_MOVE(164),
    CONTI_STATE(165),
    SET_QUEST_CLEAR(166),
    SET_QUEST_TIME(167),
    WARN(168),
    SET_OBJECT_STATE(169),
    DESTROY_CLOCK(170),
    SHOW_ARENA_RESULT(171),
    STALK_RESULT(172),
    MASSACRE_INC_GAUGE(173),
    MASSACRE_RESULT(174),
    QUICKSLOT_MAPPED_INIT(175),
    FOOT_HOLD_INFO(176),
    REQUEST_FOOT_HOLD_INFO(177),
    FIELD_KILL_COUNT(178),

    USER_ENTER_FIELD(179),
    USER_LEAVE_FIELD(180),
    USER_CHAT(181),
    USER_CHAT_NLCPQ(182),
    USER_AD_BOARD(183),
    USER_MINI_ROOM_BALLOON(184),
    USER_CONSUME_ITEM_EFFECT(185),
    USER_ITEM_UPGRADE_EFFECT(186),
    USER_ITEM_HYPER_UPGRADE_EFFECT(187),
    USER_ITEM_OPTION_UPGRADE_EFFECT(188),
    USER_ITEM_RELEASE_EFFECT(189),
    USER_ITEM_UNRELEASE_EFFECT(190),
    USER_HIT_BY_USER(191),
    USER_TESLA_TRIANGLE(192),
    USER_FOLLOW_CHARACTER(193),
    USER_SHOW_PQ_REWARD(194),
    USER_SET_PHASE(195),
    SET_PORTAL_USABLE(196),
    SHOW_PAMS_SONG_RESULT(197),
    BEGIN_PET(198),
    PET_ACTIVATED(198),
    PET_EVOL(199),
    PET_TRANSFER_FIELD(200),
    PET_MOVE(201),
    PET_ACTION(202),
    PET_NAME_CHANGED(203),
    PET_LOAD_EXCEPTION_LIST(204),
    PET_ACTION_COMMAND(205),
    END_PET(205),
    BEGIN_DRAGON(206),
    DRAGON_ENTER_FIELD(206),
    DRAGON_MOVE(207),
    DRAGON_LEAVE_FIELD(208),
    END_DRAGON(208),
    END_USERCOMMON(209),
    BEGIN_USERREMOTE(210),
    USER_MOVE(210),
    USER_MELEE_ATTACK(211),
    USER_SHOOT_ATTACK(212),
    USER_MAGIC_ATTACK(213),
    USER_BODY_ATTACK(214),
    USER_SKILL_PREPARE(215),
    USER_MOVING_SHOOT_ATTACK_PREPARE(216),
    USER_SKILL_CANCEL(217),
    USER_HIT(218),
    USER_EMOTION(219),
    USER_SET_ACTIVE_EFFECT_ITEM(220),
    USER_SHOW_UPGRADE_TOMB_EFFECT(221),
    USER_SET_ACTIVE_PORTABLE_CHAIR(222),
    USER_AVATAR_MODIFIED(223),
    USER_EFFECT_REMOTE(224),
    USER_TEMPORARY_STAT_SET(225),
    USER_TEMPORARY_STAT_RESET(226),
    USER_HP(227),
    USER_GUILD_NAME_CHANGED(228),
    USER_GUILD_MARK_CHANGED(229),
    USER_THROW_GRENADE(230),
    END_USERREMOTE(230),
    BEGIN_USERLOCAL(231),
    USER_SIT_RESULT(231),
    USER_EMOTION_LOCAL(232),
    USER_EFFECT_LOCAL(233),
    USER_TELEPORT(234),
    PREMIUM(235),
    MESO_GIVE_SUCCEEDED(236),
    MESO_GIVE_FAILED(237),
    RANDOM_MESOBAG_SUCCEED(238),
    RANDOM_MESOBAG_FAILED(239),
    FIELD_FADE_IN_OUT(240),
    FIELD_FADE_OUT_FORCE(241),
    USER_QUEST_RESULT(242),
    NOTIFY_HP_DEC_BY_FIELD(243),
    USER_PET_SKILL_CHANGED(244),
    USER_BALLOON_MSG(245),
    PLAY_EVENT_SOUND(246),
    PLAY_MINIGAME_SOUND(247),
    USER_MAKER_RESULT(248),
    USER_OPEN_CONSULT_BOARD(249),
    USER_OPEN_CLASS_COMPETITION_PAGE(250),
    USER_OPEN_UI(251),
    USER_OPEN_UI_WITH_OPTION(252),
    SET_DIRECTION_MODE(253),
    SET_STAND_ALONE_MODE(254),
    USER_HIRE_TUTOR(255),
    USER_TUTOR_MSG(256),
    INC_COMBO(257),
    USER_RANDOM_EMOTION(258),
    RESIGN_QUEST_RETURN(259),
    PASS_MATE_NAME(260),
    SET_RADIO_SCHEDULE(261),
    USER_OPEN_SKILL_GUIDE(262),
    USER_NOTICE_MSG(263),
    USER_CHAT_MSG(264),
    USER_BUFFZONE_EFFECT(265),
    USER_GO_TO_COMMODITY_SN(266),
    USER_DAMAGE_METER(267),
    USER_TIME_BOMB_ATTACK(268),
    USER_PASSIVE_MOVE(269),
    USER_FOLLOW_CHARACTER_FAILED(270),
    USER_REQUEST_VENGEANCE(271),
    USER_REQUEST_EX_JABLIN(272),
    USER_ASK_APSP_EVENT(273),
    QUEST_GUIDE_RESULT(274),
    USER_DELIVERY_QUEST(275),
    SKILL_COOLTIME_SET(276),
    END_USERLOCAL(276),
    END_USERPOOL(277),
    BEGIN_SUMMONED(278),
    SUMMONED_ENTER_FIELD(278),
    SUMMONED_LEAVE_FIELD(279),
    SUMMONED_MOVE(280),
    SUMMONED_ATTACK(281),
    SUMMONED_SKILL(282),
    SUMMONED_HIT(283),
    END_SUMMONED(283),
    BEGIN_MOBPOOL(284),
    MOB_ENTER_FIELD(284),
    MOB_LEAVE_FIELD(285),
    MOB_CHANGE_CONTROLLER(286),
    BEGIN_MOB(287),
    MOB_MOVE(287),
    MOB_CTRL_ACK(288),
    MOB_CTRL_HINT(289),
    MOB_STAT_SET(290),
    MOB_STAT_RESET(291),
    MOB_SUSPEND_RESET(292),
    MOB_AFFECTED(293),
    MOB_DAMAGED(294),
    MOB_SPECIAL_EFFECT_BY_SKILL(295),
    MOB_HP_CHANGE(296),
    MOB_CRC_KEY_CHANGED(297),
    MOB_HP_INDICATOR(298),
    MOB_CATCH_EFFECT(299),
    MOB_EFFECT_BY_ITEM(300),
    MOB_SPEAKING(301),
    MOB_CHARGE_COUNT(302),
    MOB_SKILL_DELAY(303),
    MOB_REQUEST_RESULT_ESCORT_INFO(304),
    MOB_ESCORT_STOP_END_PERMMISION(305),
    MOB_ESCORT_STOP_SAY(306),
    MOB_ESCORT_RETURN_BEFORE(307),
    MOB_NEXT_ATTACK(308),
    MOB_ATTACKED_BY_MOB(309),
    END_MOB(309),
    END_MOBPOOL(310),
    BEGIN_NPCPOOL(311),
    NPC_ENTER_FIELD(311),
    NPC_LEAVE_FIELD(312),
    NPC_CHANGE_CONTROLLER(313),
    BEGIN_NPC(314),
    NPC_MOVE(314),
    NPC_UPDATE_LIMITED_INFO(315),
    NPC_SPECIAL_ACTION(316),
    END_NPC(316),
    BEGIN_NPCTEMPLATE(317),
    NPC_SET_SCRIPT(317),
    END_NPCTEMPLATE(317),
    END_NPCPOOL(318),
    BEGIN_EMPLOYEEPOOL(319),
    EMPLOYEE_ENTER_FIELD(319),
    EMPLOYEE_LEAVE_FIELD(320),
    EMPLOYEE_MINI_ROOM_BALLOON(321),
    END_EMPLOYEEPOOL(321),
    BEGIN_DROPPOOL(322),
    DROP_ENTER_FIELD(322),
    DROP_RELEASE_ALL_FREEZE(323),
    DROP_LEAVE_FIELD(324),
    END_DROPPOOL(324),
    BEGIN_MESSAGEBOXPOOL(325),
    CREATE_MESSAGE_BOX_FAILED(325),
    MESSAGE_BOX_ENTER_FIELD(326),
    MESSAGE_BOX_LEAVE_FIELD(327),
    END_MESSAGEBOXPOOL(327),
    BEGIN_AFFECTEDAREAPOOL(328),
    AFFECTED_AREA_CREATED(328),
    AFFECTED_AREA_REMOVED(329),
    END_AFFECTEDAREAPOOL(329),
    BEGIN_TOWNPORTALPOOL(330),
    TOWN_PORTAL_CREATED(330),
    TOWN_PORTAL_REMOVED(331),
    END_TOWNPORTALPOOL(331),
    BEGIN_OPENGATEPOOL(332),
    OPEN_GATE_CREATED(332),
    OPEN_GATE_REMOVED(333),
    END_OPENGATEPOOL(333),
    BEGIN_REACTORPOOL(334),
    REACTOR_CHANGE_STATE(334),
    REACTOR_MOVE(335),
    REACTOR_ENTER_FIELD(336),
    REACTOR_LEAVE_FIELD(337),
    END_REACTORPOOL(337),
    BEGIN_ETCFIELDOBJ(338),
    SNOW_BALL_STATE(338),
    SNOW_BALL_HIT(339),
    SNOW_BALL_MSG(340),
    SNOW_BALL_TOUCH(341),
    COCONUT_HIT(342),
    COCONUT_SCORE(343),
    HEALER_MOVE(344),
    PULLEY_STATE_CHANGE(345),
    M_CARNIVAL_ENTER(346),
    M_CARNIVAL_PERSONAL_CP(347),
    M_CARNIVAL_TEAM_CP(348),
    M_CARNIVAL_RESULT_SUCCESS(349),
    M_CARNIVAL_RESULT_FAIL(350),
    M_CARNIVAL_DEATH(351),
    M_CARNIVAL_MEMBER_OUT(352),
    M_CARNIVAL_GAME_RESULT(353),
    ARENA_SCORE(354),
    BATTLEFIELD_ENTER(355),
    BATTLEFIELD_SCORE(356),
    BATTLEFIELD_TEAM_CHANGED(357),
    WITCHTOWER_SCORE(358),
    HONTALE_TIMER(359),
    CHAOS_ZAKUM_TIMER(360),
    HONTAIL_TIMER(361),
    ZAKUM_TIMER(362),
    END_ETCFIELDOBJ(362),
    BEGIN_SCRIPT(363),
    SCRIPT_MESSAGE(363),
    END_SCRIPT(363),
    BEGIN_SHOP(364),
    OPEN_SHOP_DLG(364),
    SHOP_RESULT(365),
    END_SHOP(365),
    BEGIN_ADMINSHOP(366),
    ADMIN_SHOP_RESULT(366),
    ADMIN_SHOP_COMMODITY(367),
    END_ADMINSHOP(367),
    TRUNK_RESULT(368),
    BEGIN_STOREBANK(369),
    STORE_BANK_GET_ALL_RESULT(369),
    STORE_BANK_RESULT(370),
    END_STOREBANK(370),
    RPS_GAME(371),
    MESSENGER(372),
    MINI_ROOM(373),
    BEGIN_TOURNAMENT(374),
    TOURNAMENT(374),
    TOURNAMENT_MATCH_TABLE(375),
    TOURNAMENT_SET_PRIZE(376),
    TOURNAMENT_NOTICE_UEW(377),
    TOURNAMENT_AVATAR_INFO(378),
    END_TOURNAMENT(378),
    BEGIN_WEDDING(379),
    WEDDING_PROGRESS(379),
    WEDDING_CERMONY_END(380),
    END_WEDDING(380),
    PARCEL(381),
    END_FIELD(381),
    CASH_SHOP_CHARGE_PARAM_RESULT(382),

    CASH_SHOP_QUERY_CASH_RESULT(383),
    CASH_SHOP_CASH_ITEM_RESULT(384),
    CASH_SHOP_PURCHASE_EXP_CHANGED(385),
    CASH_SHOP_GIFT_MATE_INFO_RESULT(386),
    CASH_SHOP_CHECK_DUPLICATED_ID_RESULT(387),
    CASH_SHOP_CHECK_NAME_CHANGE_POSSIBLE_RESULT(388),
    CASH_SHOP_REGISTER_NEW_CHARACTER_RESULT(389),
    CASH_SHOP_CHECK_TRANSFER_WORLD_POSSIBLE_RESULT(390),
    CASH_SHOP_GACHAPON_STAMP_ITEM_RESULT(391),
    CASH_SHOP_CASH_ITEM_GACHAPON_RESULT(392),
    CASH_SHOP_CASH_GACHAPON_OPEN_RESULT(393),
    CHANGE_MAPLE_POINT_RESULT(394),
    CASH_SHOP_ONE_A_DAY(395),
    CASH_SHOP_NOTICE_FREE_CASH_ITEM(396),
    CASH_SHOP_MEMBER_SHOP_RESULT(397),
    END_CASHSHOP(397),
    BEGIN_FUNCKEYMAPPED(398),
    FUNC_KEY_MAPPED_INIT(398),
    PET_CONSUME_ITEM_INIT(399),
    PET_CONSUME_MP_ITEM_INIT(400),
    END_FUNCKEYMAPPED(400),
    CHECK_SSN2_ON_CREATE_NEW_CHARACTER_RESULT(401),
    CHECK_SPW_ON_CREATE_NEW_CHARACTER_RESULT(402),
    FIRST_SSN_ON_CREATE_NEW_CHARACTER_RESULT(403),
    BEGIN_MAPLETV(404),
    MAPLE_TV_UPDATE_MESSAGE(405),
    MAPLE_TV_CLEAR_MESSAGE(406),
    MAPLE_TV_SEND_MESSAGE_RESULT(407),
    BROAD_SET_FLASH_CHANGE_EVENT(408),
    END_MAPLETV(409),
    BEGIN_ITC(410),
    ITC_CHARGE_PARAM_RESULT(410),
    ITC_QUERY_CASH_RESULT(411),
    ITC_NORMAL_ITEM_RESULT(412),
    END_ITC(412),
    BEGIN_CHARACTERSALE(413),
    CHECK_DUPLICATED_ID_RESULT_IN_CS(413),
    CREATE_NEW_CHARACTER_RESULT_IN_CS(414),
    CREATE_NEW_CHARACTER_FAIL_IN_CS(415),
    CHARACTER_SALE(416),
    END_CHARACTERSALE(416),
    BEGIN_GOLDHAMMER(417),
    GOLD_HAMMERE_S(417),
    GOLD_HAMMER_RESULT(418),
    GOLD_HAMMERE_E(419),
    END_GOLDHAMMER(419),
    BEGIN_BATTLERECORD(420),
    BATTLE_RECORD_S(420),
    BATTLE_RECORD_DOT_DAMAGE_INFO(421),
    BATTLE_RECORD_REQUEST_RESULT(422),
    BATTLE_RECORD_E(423),
    END_BATTLERECORD(423),
    BEGIN_ITEMUPGRADE(424),
    ITEM_UPGRADE_S(424),
    ITEM_UPGRADE_RESULT(425),
    ITEM_UPGRADE_FAIL(426),
    ITEM_UPGRADE_E(427),
    // End Item Upgrade
    // Begin Vega
    VEGA_S(428),
    VEGA_RESULT(429),
    VEGA_FAIL(430),
    VEGA_E(431),
    // End Vega
    LOGOUT_GIFT(432),
    NO(433);
    ;


    private static final List<OutHeader> spam = List.of(
            ALIVE_REQ
//            PRIVATE_SERVER_PACKET,
//            MOB_CONTROL_ACK,
//            CHAT_MSG,
//            MOB_HP_INDICATOR,
//            STAT_CHANGED,
//            MOB_CHANGE_CONTROLLER,
//            MOB_MOVE,
//            REMOTE_MOVE,
//            REMOTE_EMOTION,
//            EXCL_REQUEST,
//            NPC_MOVE,
//            DROP_LEAVE_FIELD,
//            TEMPORARY_STAT_RESET,
//            PET_MOVE,
//            NPC_ENTER_FIELD,
//            NPC_CHANGE_CONTROLLER,
//            MOB_ENTER_FIELD,
//            RESULT_INSTANCE_TABLE,
//            CREATE_OBTACLE,
//            DROP_ENTER_FIELD,
//            MOB_LEAVE_FIELD,
//            ANDROID_MOVE,
//            MESSAGE,
//            FOX_MAN_MOVE,
//            SKILL_PET_MOVE,
//            MOB_FORCE_CHASE,
//            AFFECTED_AREA_CREATED,
//            AFFECTED_AREA_REMOVED
    );

    private final short value;

    OutHeader(int value) {
        this.value = (short) value;
    }

    public short getValue() {
        return value;
    }

    public static OutHeader getOutHeaderByOp(int op) {
        for (OutHeader outHeader : OutHeader.values()) {
            if (outHeader.getValue() == op) {
                return outHeader;
            }
        }
        return null;
    }

    public static boolean isSpamHeader(OutHeader outHeader) {
        return spam.contains(outHeader);
    }

}
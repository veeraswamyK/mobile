package constants;

/**
 * ═══════════════════════════════════════════════════════════════
 *  AppDataKeys — Scenario Context Key Registry
 * ═══════════════════════════════════════════════════════════════
 *
 *  WHAT THIS CLASS IS:
 *  A registry of String constants used as keys in the test context
 *  (ContextManager / TextContext). Centralising keys here prevents
 *  typos from causing silent failures when sharing data between steps.
 *
 *  THE PROBLEM THIS SOLVES:
 *  Without this class, steps use raw strings like:
 *    ContextManager.getContext().set("orderNumber", value);  // step 1
 *    ContextManager.getContext().get("ordernumber", ...)     // step 2 — typo!
 *
 *  With this class:
 *    ContextManager.getContext().set(AppDataKeys.ORDER_NUMBER, value);
 *    ContextManager.getContext().get(AppDataKeys.ORDER_NUMBER, ...)  // safe
 *
 *  HOW TO USE:
 *  1. Add a new key constant below (prefix with the feature area).
 *  2. Use that constant in both the writing step and the reading step.
 *  3. Keys are scenario-scoped — they are cleared after each scenario
 *     by TestHooks.@After → ContextManager.unload().
 *
 *  NAMING CONVENTION:
 *  AREA_DESCRIPTION — e.g., USER_USERNAME, CART_ITEM_COUNT, ORDER_NUMBER
 *
 *  EXAMPLE USAGE IN STEP DEFINITIONS:
 *  ─────────────────────────────────────────────────────────────
 *  // Storing
 *  ContextManager.getContext().set(AppDataKeys.USER_USERNAME, username);
 *
 *  // Retrieving
 *  String username = ContextManager.getContext().get(AppDataKeys.USER_USERNAME, String.class);
 *  ─────────────────────────────────────────────────────────────
 */
public final class AppDataKeys {

    private AppDataKeys() {}

    // ── USER / AUTH ──────────────────────────────────────────────
    /** The username used to log in during this scenario. */
    public static final String USER_USERNAME = "user.username";

    /** The password used to log in during this scenario. */
    public static final String USER_PASSWORD = "user.password";

    /** The full display name shown on the profile/home screen after login. */
    public static final String USER_DISPLAY_NAME = "user.displayName";

    // ── CART ────────────────────────────────────────────────────
    /** The number of items currently in the cart (integer, stored as String). */
    public static final String CART_ITEM_COUNT = "cart.itemCount";

    /** The name of the last product added to the cart. */
    public static final String CART_LAST_ADDED_PRODUCT = "cart.lastAddedProduct";

    /** The total price shown in the cart (String, e.g., "$29.99"). */
    public static final String CART_TOTAL_PRICE = "cart.totalPrice";

    // ── ORDER / CHECKOUT ────────────────────────────────────────
    /** The order confirmation number shown on the success screen. */
    public static final String ORDER_NUMBER = "order.number";

    /** The total amount charged for the order. */
    public static final String ORDER_TOTAL = "order.total";

    // ── PRODUCT ────────────────────────────────────────────────
    /** The name of the product currently being viewed. */
    public static final String PRODUCT_NAME = "product.name";

    /** The price of the product currently being viewed. */
    public static final String PRODUCT_PRICE = "product.price";

    /** The description text of the product currently being viewed. */
    public static final String PRODUCT_DESCRIPTION = "product.description";

    // ── SEARCH ─────────────────────────────────────────────────
    /** The search query that was entered. */
    public static final String SEARCH_QUERY = "search.query";

    /** The number of search results returned. */
    public static final String SEARCH_RESULT_COUNT = "search.resultCount";

    // ── NAVIGATION ─────────────────────────────────────────────
    /** The name of the page/screen the user was last on (for navigation tracking). */
    public static final String CURRENT_SCREEN = "nav.currentScreen";

    // ── FORM DATA ──────────────────────────────────────────────
    /** First name entered in any form field. */
    public static final String FORM_FIRST_NAME = "form.firstName";

    /** Last name entered in any form field. */
    public static final String FORM_LAST_NAME = "form.lastName";

    /** Email entered in any form field. */
    public static final String FORM_EMAIL = "form.email";

    /** Postal/ZIP code entered in any form field. */
    public static final String FORM_ZIP_CODE = "form.zipCode";

    // ── GENERAL PURPOSE ────────────────────────────────────────
    /** Generic "value 1" slot for ad-hoc data sharing between steps. */
    public static final String TEMP_VALUE_1 = "temp.value1";

    /** Generic "value 2" slot for ad-hoc data sharing between steps. */
    public static final String TEMP_VALUE_2 = "temp.value2";

    /** Timestamp captured during a test (e.g., to verify time-based content). */
    public static final String CAPTURED_TIMESTAMP = "temp.timestamp";
}

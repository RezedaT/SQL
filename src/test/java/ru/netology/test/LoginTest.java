package ru.netology.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.*;

public class LoginTest {
    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999/", LoginPage.class);
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Пользователь незарегистрирован в базе")
    public void shouldBeError1() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.shouldBeErrorNotification();
    }

    @Test
    @DisplayName("Не заполнены логин и пароль")
    public void shouldNotAuthoriseWithoutLoginAndPassword() {
        var authInfo = DataHelper.getAuthInfo();
        loginPage.emptyAuthorisation(authInfo);
    }
//Баг-репорт. при трёхкратном неверном вводе пароля система должна блокироваться
    @Test
    @DisplayName("Трижды введен неверный пароль")
    public void shouldBlockedUser() {
        var authInfo = DataHelper.generateRandomPassworForUserVasya();
        loginPage.validLogin(authInfo);
        loginPage.passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        loginPage.validLoginWithRandomPassword(authInfo);
        loginPage.passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        loginPage.validLoginWithRandomPassword(authInfo);
        Assertions.assertEquals("blocked", getUserStatus());

    }
    @Test
    @DisplayName("Не заполнен код")
    public void shouldNotAuthoriseWithEmptyCode() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.codeVerify("");
        verificationPage.codeEmpty();
    }

    @Test
    @DisplayName("Неверный код")
    public void shouldNotAuthoriseWithIncorrectCode() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.codeVerify(verificationCode.getCode());
        verificationPage.incorrectCode();
    }

}
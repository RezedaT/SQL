package ru.netology.page;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class LoginPage {
    private SelenideElement heading = $(byText("Интернет Банк"));
    public SelenideElement loginField = $("[data-test-id = 'login'] input");
    public SelenideElement passwordField = $("[data-test-id = 'password'] input");
    private SelenideElement loginButton = $("[data-test-id = 'action-login']");
    private SelenideElement loginNotification = $("[data-test-id='login'] .input__sub");
    private SelenideElement passwordNotification = $("[data-test-id='password'] .input__sub");
    private SelenideElement errorNotification = $("[data-test-id = 'error-notification'] .notification__content");

    public LoginPage() {
        heading.shouldBe(Condition.visible);
    }

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
    public VerificationPage validLoginWithRandomPassword(DataHelper.AuthInfo authInfo) {
        passwordField.setValue(authInfo.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void shouldBeErrorNotification() {
        errorNotification.shouldBe(visible);
        errorNotification.shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль"));
    }

    public void emptyAuthorisation(DataHelper.AuthInfo authInfo) {
        loginButton.click();
        loginNotification.shouldBe(Condition.visible);
        passwordNotification.shouldBe(Condition.visible);
        loginNotification.shouldHave(Condition.text("Поле обязательно для заполнения"));
        passwordNotification.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }
}
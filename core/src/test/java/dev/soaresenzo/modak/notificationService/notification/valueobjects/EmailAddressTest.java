package dev.soaresenzo.modak.notificationService.notification.valueobjects;

import dev.soaresenzo.modak.notificationService.errors.EmailValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailAddressTest {

    @Test
    public void givenAValidEmail_whenCreateEmail_thenShouldReturnEmail() {
        final var anEmail = "test@gmail.com";

        final var actualEmail = EmailAddress.of(anEmail);

        Assertions.assertEquals(anEmail, actualEmail.getValue());
    }

    @Test
    public void givenAnInvalidEmail_whenCreateEmail_thenShouldThrowException() {
        final var anEmail = "testgmail.com";

        final var anException = Assertions.assertThrows(EmailValidationException.class, ()-> EmailAddress.of(anEmail));

        Assertions.assertEquals("The email does not meet the required format.", anException.getMessage());
    }

    @Test
    public void givenANullEmail_whenCreateEmail_thenShouldThrowException() {
        final String anEmail = null;

        final var anException = Assertions.assertThrows(EmailValidationException.class, ()-> EmailAddress.of(anEmail));

        Assertions.assertEquals("The email must not be null.", anException.getMessage());
    }

    @Test
    public void givenAnEmptyEmail_whenCreateEmail_thenShouldThrowException() {
        final var anEmail = "";

        final var anException = Assertions.assertThrows(EmailValidationException.class, ()-> EmailAddress.of(anEmail));

        Assertions.assertEquals("The email does not meet the required format.", anException.getMessage());
    }

    @Test
    public void givenAVeryLongEmail_whenCreateEmail_thenShouldThrowException() {
        final var anEmail = "testtestestestetstestestestestetesttestestestetstestestesteste@domaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomaindomain.com";

        final var anException = Assertions.assertThrows(EmailValidationException.class, ()-> EmailAddress.of(anEmail));

        Assertions.assertEquals("The email length must be 254 or lesser.", anException.getMessage());
    }

    @Test
    public void givenAVeryLongEmailUsername_whenCreateEmail_thenShouldThrowException() {
        final var anEmail = "usernameusernameusernameusernameusernameusernameusernameusernameu@domain.com";

        final var anException = Assertions.assertThrows(EmailValidationException.class, ()-> EmailAddress.of(anEmail));

        Assertions.assertEquals("The email does not meet the required format.", anException.getMessage());
    }
}
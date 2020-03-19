package masssh.boilerplate.spring.spa.dao.service;

import java.security.SecureRandom;

public class RandomService {
    private static SecureRandom random = new SecureRandom();
    private static int ZERO = 48; // numeral '0'
    private static int NINE = 57; // numeral '9'
    private static int A = 65; // letter 'A'
    private static int Z = 90; // letter 'Z'
    private static int a = 97; // letter 'a'
    private static int z = 122; // letter 'z'

    private RandomService() {
    }

    public static String randomAlphaNumeric(final int digit) {
        return new SecureRandom().ints(digit, ZERO, z + 1)
                .filter(i -> i <= NINE || a <= i || (A <= i && i <= Z))
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomLowerAlphaNumeric(final int digit) {
        return new SecureRandom().ints(digit, ZERO, z + 1)
                .filter(i -> i <= NINE || a <= i)
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomUpperAlphaNumeric(final int digit) {
        return new SecureRandom().ints(digit, ZERO, Z + 1)
                .filter(i -> i <= NINE || A <= i)
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomAlphabetic(final int digit) {
        return new SecureRandom().ints(digit, A, z + 1)
                .filter(i -> i <= Z || a <= i)
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomUpperAlphabetic(final int digit) {
        return new SecureRandom().ints(digit, A, Z + 1)
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomLowerAlphabetic(final int digit) {
        return new SecureRandom().ints(digit, a, z + 1)
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomNumeric(final int digit) {
        return new SecureRandom().ints(digit, ZERO, NINE + 1)
                .limit(digit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}

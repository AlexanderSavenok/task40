package dataProviders;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class LoginDataProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of("AlexanderSavenok", "Password1*", false),
                Arguments.of("AlexanderSavenok", "Password2*", false),
                Arguments.of("AlexanderSavenok", "Password3*", true)
        );
    }
}

package scouter.bytebuddy.dynamic.scaffold;

/**
 * <p>
 * If type validation is enabled, Byte Buddy performs several checks to ensure that a generated
 * class file is specified in a valid manner. This involves checks of the generated instrumented
 * type and checks of the generated byte code. Byte Buddy's {@link scouter.bytebuddy.implementation.Implementation}
 * instances perform their own checks, independently of any type validation.
 * </p>
 * <p>
 * The JVM's verifier performs its own checks; an illegal class file is never loaded. However, Byte Buddy's
 * checks might be more expressive in the context of using the library. Also, Byte Buddy emits exceptions
 * at class creation time while the JVM emits errors at class loading time.
 * </p>
 */
public enum TypeValidation {

    /**
     * Enables Byte Buddy's validation.
     */
    ENABLED(true),

    /**
     * Disables Byte Buddy's validation.
     */
    DISABLED(false);

    /**
     * {@code true} if type validation is enabled.
     */
    private final boolean enabled;

    /**
     * Creates a new type validation enumeration.
     *
     * @param enabled {@code true} if type validation is enabled.
     */
    TypeValidation(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns {@link TypeValidation#ENABLED} if the supplied argument is {@code true}.
     *
     * @param enabled {@code true} if type validation should be enabled.
     * @return A suitable type validation representation.
     */
    public static TypeValidation of(boolean enabled) {
        return enabled
                ? ENABLED
                : DISABLED;
    }

    /**
     * Returns {@code true} if type validation is enabled.
     *
     * @return {@code true} if type validation is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "TypeValidation." + name();
    }
}

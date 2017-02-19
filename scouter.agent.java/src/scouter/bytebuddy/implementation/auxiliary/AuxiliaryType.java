package scouter.bytebuddy.implementation.auxiliary;


import scouter.bytebuddy.ClassFileVersion;
import scouter.bytebuddy.description.modifier.ModifierContributor;
import scouter.bytebuddy.description.modifier.SyntheticState;
import scouter.bytebuddy.description.type.TypeDescription;
import scouter.bytebuddy.dynamic.DynamicType;
import scouter.bytebuddy.implementation.MethodAccessorFactory;
import scouter.bytebuddy.utility.RandomString;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An auxiliary type that provides services to the instrumentation of another type. Implementations should provide
 * meaningful {@code equals(Object)} and {@code hashCode()} implementations in order to avoid multiple creations
 * of this type.
 */
public interface AuxiliaryType {

    /**
     * The default type access of an auxiliary type. <b>This array must not be mutated</b>.
     */

    ModifierContributor.ForType[] DEFAULT_TYPE_MODIFIER = {SyntheticState.SYNTHETIC};

    /**
     * Creates a new auxiliary type.
     *
     * @param auxiliaryTypeName     The fully qualified binary name for this auxiliary type. The type should be in
     *                              the same package than the instrumented type this auxiliary type is providing services
     *                              to in order to allow package-private access.
     * @param classFileVersion      The class file version the auxiliary class should be written in.
     * @param methodAccessorFactory A factory for accessor methods.
     * @return A dynamically created type representing this auxiliary type.
     */
    DynamicType make(String auxiliaryTypeName, ClassFileVersion classFileVersion, MethodAccessorFactory methodAccessorFactory);

    /**
     * Representation of a naming strategy for an auxiliary type.
     */
    interface NamingStrategy {

        /**
         * Names an auxiliary type.
         *
         * @param instrumentedType The instrumented type for which an auxiliary type is registered.
         * @return The fully qualified name for the given auxiliary type.
         */
        String name(TypeDescription instrumentedType);

        /**
         * A naming strategy for an auxiliary type which returns the instrumented type's name with a fixed extension
         * and a random number as a suffix. All generated names will be in the same package as the instrumented type.
         */
        class SuffixingRandom implements NamingStrategy {

            /**
             * The suffix to append to the instrumented type for creating names for the auxiliary types.
             */
            private final String suffix;

            /**
             * An instance for creating random values.
             */
            private final RandomString randomString;

            /**
             * Creates a new suffixing random naming strategy.
             *
             * @param suffix The suffix to extend to the instrumented type.
             */
            public SuffixingRandom(String suffix) {
                this.suffix = suffix;
                randomString = new RandomString();
            }

            @Override
            public String name(TypeDescription instrumentedType) {
                return String.format("%s$%s$%s", instrumentedType.getName(), suffix, randomString.nextString());
            }

            @Override
            public boolean equals(Object other) {
                return this == other || !(other == null || getClass() != other.getClass())
                        && suffix.equals(((SuffixingRandom) other).suffix);
            }

            @Override
            public int hashCode() {
                return suffix.hashCode();
            }

            @Override
            public String toString() {
                return "Instrumentation.Context.Default.AuxiliaryTypeNamingStrategySuffixingRandom{suffix='" + suffix + '\'' + '}';
            }
        }
    }

    /**
     * A marker to indicate that an auxiliary type is part of the instrumented types signature. This information can be used to load a type before
     * the instrumented type such that reflection on the instrumented type does not cause a {@link NoClassDefFoundError}.
     */
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    @interface SignatureRelevant {
        /* empty */
    }
}

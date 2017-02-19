package scouter.bytebuddy.implementation.bytecode.constant;

import scouter.bytebuddy.description.field.FieldDescription;
import scouter.bytebuddy.description.method.MethodDescription;
import scouter.bytebuddy.description.type.TypeDescription;
import scouter.bytebuddy.implementation.Implementation;
import scouter.bytebuddy.implementation.bytecode.StackManipulation;
import scouter.bytebuddy.implementation.bytecode.member.FieldAccess;
import scouter.bytebuddy.implementation.bytecode.member.MethodInvocation;
import scouter.bytebuddy.jar.asm.MethodVisitor;

import java.lang.reflect.Field;

/**
 * Represents a {@link Field} constant for a given type.
 */
public class FieldConstant implements StackManipulation {

    /**
     * The field to be represent as a {@link Field}.
     */
    private final FieldDescription.InDefinedShape fieldDescription;

    /**
     * Creates a new field constant.
     *
     * @param fieldDescription The field to be represent as a {@link Field}.
     */
    public FieldConstant(FieldDescription.InDefinedShape fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    /**
     * Retruns a cached version of this field constant.
     *
     * @return A cached version of this field constant.
     */
    public StackManipulation cached() {
        return new Cached(this);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        try {
            return new Compound(
                    ClassConstant.of(fieldDescription.getDeclaringType()),
                    new TextConstant(fieldDescription.getInternalName()),
                    MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(Class.class.getMethod("getDeclaredField", String.class)))
            ).apply(methodVisitor, implementationContext);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Cannot locate Class::getDeclaredField", exception);
        }
    }

    @Override
    public boolean equals(Object other) {
        return this == other || !(other == null || getClass() != other.getClass())
                && fieldDescription.equals(((FieldConstant) other).fieldDescription);
    }

    @Override
    public int hashCode() {
        return fieldDescription.hashCode();
    }

    @Override
    public String toString() {
        return "FieldConstant{" +
                "fieldDescription=" + fieldDescription +
                '}';
    }

    /**
     * A cached version of a {@link FieldConstant}.
     */
    protected static class Cached implements StackManipulation {

        /**
         * The field constant stack manipulation.
         */
        private final StackManipulation fieldConstant;

        /**
         * Creates a new cached version of a field constant.
         *
         * @param fieldConstant The field constant stack manipulation.
         */
        public Cached(StackManipulation fieldConstant) {
            this.fieldConstant = fieldConstant;
        }

        @Override
        public boolean isValid() {
            return fieldConstant.isValid();
        }

        @Override
        public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return FieldAccess.forField(implementationContext.cache(fieldConstant, new TypeDescription.ForLoadedType(Field.class)))
                    .read()
                    .apply(methodVisitor, implementationContext);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || !(other == null || getClass() != other.getClass()) && fieldConstant.equals(((Cached) other).fieldConstant);
        }

        @Override
        public int hashCode() {
            return fieldConstant.hashCode();
        }

        @Override
        public String toString() {
            return "FieldConstant.Cached{" +
                    "fieldConstant=" + fieldConstant +
                    '}';
        }
    }
}

package scouter.bytebuddy.implementation.bytecode.assign;


import scouter.bytebuddy.description.type.TypeDefinition;
import scouter.bytebuddy.description.type.TypeDescription;
import scouter.bytebuddy.implementation.Implementation;
import scouter.bytebuddy.implementation.bytecode.StackManipulation;
import scouter.bytebuddy.implementation.bytecode.StackSize;
import scouter.bytebuddy.jar.asm.MethodVisitor;
import scouter.bytebuddy.jar.asm.Opcodes;

/**
 * A stack manipulation for a type down casting. Such castings are not implicit but must be performed explicitly.
 */
public class TypeCasting implements StackManipulation {

    /**
     * The type description to which a value should be casted.
     */
    private final TypeDescription typeDescription;

    /**
     * Creates a new type casting.
     *
     * @param typeDescription The type description to which a value should be casted.
     */
    protected TypeCasting(TypeDescription typeDescription) {
        this.typeDescription = typeDescription;
    }

    /**
     * Creates a casting to the given, non-primitive type.
     *
     * @param typeDefinition The type to which a value should be casted.
     * @return A stack manipulation that represents the casting.
     */
    public static StackManipulation to(TypeDefinition typeDefinition) {
        if (typeDefinition.isPrimitive()) {
            throw new IllegalArgumentException("Cannot cast to primitive type: " + typeDefinition);
        }
        return new TypeCasting(typeDefinition.asErasure());
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, typeDescription.getInternalName());
        return StackSize.ZERO.toIncreasingSize();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || !(other == null || getClass() != other.getClass())
                && typeDescription.equals(((TypeCasting) other).typeDescription);
    }

    @Override
    public int hashCode() {
        return typeDescription.hashCode();
    }

    @Override
    public String toString() {
        return "TypeCasting{typeDescription='" + typeDescription + '\'' + '}';
    }
}

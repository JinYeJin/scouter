package scouter.bytebuddy.implementation.bytecode.constant;

import scouter.bytebuddy.description.method.MethodDescription;
import scouter.bytebuddy.description.type.TypeDescription;
import scouter.bytebuddy.implementation.Implementation;
import scouter.bytebuddy.implementation.bytecode.Duplication;
import scouter.bytebuddy.implementation.bytecode.StackManipulation;
import scouter.bytebuddy.implementation.bytecode.TypeCreation;
import scouter.bytebuddy.implementation.bytecode.member.MethodInvocation;
import scouter.bytebuddy.jar.asm.MethodVisitor;

import java.io.*;

/**
 * A constant that represents a value in its serialized form.
 */
public class SerializedConstant implements StackManipulation {

    /**
     * A charset that does not change the supplied byte array upon encoding or decoding.
     */
    private static final String CHARSET = "ISO-8859-1";

    /**
     * The serialized value.
     */
    private final String serialization;

    /**
     * Creates a new constant for a serialized value.
     *
     * @param serialization The serialized value.
     */
    protected SerializedConstant(String serialization) {
        this.serialization = serialization;
    }

    /**
     * Creates a new stack manipulation to load the supplied value onto the stack.
     *
     * @param value The value to serialize.
     * @return A stack manipulation to serialize the supplied value.
     */
    public static StackManipulation of(Serializable value) {
        if (value == null) {
            return NullConstant.INSTANCE;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream.writeObject(value);
            } finally {
                objectOutputStream.close();
            }
            return new SerializedConstant(byteArrayOutputStream.toString(CHARSET));
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot serialize " + value, exception);
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        try {
            return new StackManipulation.Compound(
                    TypeCreation.of(new TypeDescription.ForLoadedType(ObjectInputStream.class)),
                    Duplication.SINGLE,
                    TypeCreation.of(new TypeDescription.ForLoadedType(ByteArrayInputStream.class)),
                    Duplication.SINGLE,
                    new TextConstant(serialization),
                    new TextConstant(CHARSET),
                    MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(String.class.getMethod("getBytes", String.class))),
                    MethodInvocation.invoke(new MethodDescription.ForLoadedConstructor(ByteArrayInputStream.class.getConstructor(byte[].class))),
                    MethodInvocation.invoke(new MethodDescription.ForLoadedConstructor(ObjectInputStream.class.getConstructor(InputStream.class))),
                    MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(ObjectInputStream.class.getMethod("readObject")))
            ).apply(methodVisitor, implementationContext);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Could not locate Java API method", exception);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SerializedConstant that = (SerializedConstant) object;
        return serialization.equals(that.serialization);
    }

    @Override
    public int hashCode() {
        return serialization.hashCode();
    }

    @Override
    public String toString() {
        return "SerializedConstant{" +
                "serialization='" + serialization + '\'' +
                '}';
    }
}

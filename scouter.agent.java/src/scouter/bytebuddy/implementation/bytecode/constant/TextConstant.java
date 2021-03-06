// Generated by delombok at Sun Feb 26 12:31:38 KST 2017
package scouter.bytebuddy.implementation.bytecode.constant;

import scouter.bytebuddy.implementation.Implementation;
import scouter.bytebuddy.implementation.bytecode.StackManipulation;
import scouter.bytebuddy.implementation.bytecode.StackSize;
import scouter.bytebuddy.jar.asm.MethodVisitor;

/**
 * Represents a {@link java.lang.String} value that is stored in a type's constant pool.
 */
public class TextConstant implements StackManipulation {
    /**
     * The text value to load onto the operand stack.
     */
    private final String text;

    /**
     * Creates a new stack manipulation to load a {@code String} constant onto the operand stack.
     *
     * @param text The value of the {@code String} to be loaded.
     */
    public TextConstant(String text) {
        this.text = text;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        methodVisitor.visitLdcInsn(text);
        return StackSize.SINGLE.toIncreasingSize();
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof TextConstant)) return false;
        final TextConstant other = (TextConstant) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$text = this.text;
        final java.lang.Object other$text = other.text;
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof TextConstant;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $text = this.text;
        result = result * PRIME + ($text == null ? 43 : $text.hashCode());
        return result;
    }
}

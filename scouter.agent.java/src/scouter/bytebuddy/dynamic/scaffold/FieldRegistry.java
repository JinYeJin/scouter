// Generated by delombok at Sun Feb 26 12:31:38 KST 2017
package scouter.bytebuddy.dynamic.scaffold;

import scouter.bytebuddy.description.field.FieldDescription;
import scouter.bytebuddy.description.type.TypeDescription;
import scouter.bytebuddy.dynamic.Transformer;
import scouter.bytebuddy.implementation.attribute.FieldAttributeAppender;
import scouter.bytebuddy.matcher.ElementMatcher;
import scouter.bytebuddy.matcher.LatentMatcher;

import java.util.*;

/**
 * A field registry represents an extendable collection of fields which are identified by their names that are mapped
 * to a given {@link FieldAttributeAppender}. Fields
 * can be uniquely identified by their name for a given type since fields are never inherited.
 * <p>&nbsp;</p>
 * This registry is the counterpart of a {@link MethodRegistry}.
 * However, a field registry is implemented simpler since it does not have to deal with complex signatures or
 * inheritance. For the sake of consistency, the field registry follows however a similar pattern without introducing
 * unnecessary complexity.
 */
public interface FieldRegistry {
    /**
     * Prepends the given field definition to this field registry, i.e. this configuration is applied first.
     *
     * @param matcher                       The matcher to identify any field that this definition concerns.
     * @param fieldAttributeAppenderFactory The field attribute appender factory to apply on any matched field.
     * @param defaultValue                  The default value to write to the field or {@code null} if no default value is to be set for the field.
     * @param transformer              The field transformer to apply to any matched field.
     * @return An adapted version of this method registry.
     */
    FieldRegistry prepend(LatentMatcher<? super FieldDescription> matcher, FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Object defaultValue, Transformer<FieldDescription> transformer);

    /**
     * Prepares the field registry for a given instrumented type.
     *
     * @param instrumentedType The instrumented type.
     * @return A prepared field registry.
     */
    Compiled compile(TypeDescription instrumentedType);


    /**
     * Represents a compiled field registry.
     */
    interface Compiled extends TypeWriter.FieldPool {

        /**
         * A no-op field registry that does not register annotations for any field.
         */
        enum NoOp implements Compiled {
            /**
             * The singleton instance.
             */
            INSTANCE;

            @Override
            public Record target(FieldDescription fieldDescription) {
                return new Record.ForImplicitField(fieldDescription);
            }
        }
    }


    /**
     * An immutable default implementation of a field registry.
     */
    class Default implements FieldRegistry {
        /**
         * This registries entries.
         */
        private final List<Entry> entries;

        /**
         * Creates a new empty default field registry.
         */
        public Default() {
            this(Collections.<Entry>emptyList());
        }

        /**
         * Creates a new default field registry.
         *
         * @param entries The entries of the field registry.
         */
        private Default(List<Entry> entries) {
            this.entries = entries;
        }

        @Override
        public FieldRegistry prepend(LatentMatcher<? super FieldDescription> matcher, FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Object defaultValue, Transformer<FieldDescription> transformer) {
            List<Entry> entries = new ArrayList<Entry>(this.entries.size() + 1);
            entries.add(new Entry(matcher, fieldAttributeAppenderFactory, defaultValue, transformer));
            entries.addAll(this.entries);
            return new Default(entries);
        }

        @Override
        public FieldRegistry.Compiled compile(TypeDescription instrumentedType) {
            List<Compiled.Entry> entries = new ArrayList<Compiled.Entry>(this.entries.size());
            Map<FieldAttributeAppender.Factory, FieldAttributeAppender> fieldAttributeAppenders = new HashMap<FieldAttributeAppender.Factory, FieldAttributeAppender>();
            for (Entry entry : this.entries) {
                FieldAttributeAppender fieldAttributeAppender = fieldAttributeAppenders.get(entry.getFieldAttributeAppenderFactory());
                if (fieldAttributeAppender == null) {
                    fieldAttributeAppender = entry.getFieldAttributeAppenderFactory().make(instrumentedType);
                    fieldAttributeAppenders.put(entry.getFieldAttributeAppenderFactory(), fieldAttributeAppender);
                }
                entries.add(new Compiled.Entry(entry.resolve(instrumentedType), fieldAttributeAppender, entry.getDefaultValue(), entry.getTransformer()));
            }
            return new Compiled(instrumentedType, entries);
        }

        /**
         * An entry of the default field registry.
         */
        protected static class Entry implements LatentMatcher<FieldDescription> {
            /**
             * The matcher to identify any field that this definition concerns.
             */
            private final LatentMatcher<? super FieldDescription> matcher;
            /**
             * The field attribute appender factory to apply on any matched field.
             */
            private final FieldAttributeAppender.Factory fieldAttributeAppenderFactory;
            /**
             * The default value to write to the field or {@code null} if no default value is to be set for the field.
             */
            private final Object defaultValue;
            /**
             * The field transformer to apply to any matched field.
             */
            private final Transformer<FieldDescription> transformer;

            /**
             * Creates a new entry.
             *
             * @param matcher                       The matcher to identify any field that this definition concerns.
             * @param fieldAttributeAppenderFactory The field attribute appender factory to apply on any matched field.
             * @param defaultValue                  The default value to write to the field or {@code null} if no default value is to be set for the field.
             * @param transformer              The field transformer to apply to any matched field.
             */
            protected Entry(LatentMatcher<? super FieldDescription> matcher, FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Object defaultValue, Transformer<FieldDescription> transformer) {
                this.matcher = matcher;
                this.fieldAttributeAppenderFactory = fieldAttributeAppenderFactory;
                this.defaultValue = defaultValue;
                this.transformer = transformer;
            }

            /**
             * Returns the field attribute appender factory to apply on any matched field.
             *
             * @return The field attribute appender factory to apply on any matched field.
             */
            protected FieldAttributeAppender.Factory getFieldAttributeAppenderFactory() {
                return fieldAttributeAppenderFactory;
            }

            /**
             * Returns the default value to write to the field or {@code null} if no default value is to be set for the field.
             *
             * @return The default value to write to the field or {@code null} if no default value is to be set for the field.
             */
            protected Object getDefaultValue() {
                return defaultValue;
            }

            /**
             * Returns the field transformer to apply to any matched field.
             *
             * @return The field transformer to apply to any matched field.
             */
            protected Transformer<FieldDescription> getTransformer() {
                return transformer;
            }

            @Override
            public ElementMatcher<? super FieldDescription> resolve(TypeDescription typeDescription) {
                return matcher.resolve(typeDescription);
            }

            @java.lang.Override
            @java.lang.SuppressWarnings("all")
            @javax.annotation.Generated("lombok")
            public boolean equals(final java.lang.Object o) {
                if (o == this) return true;
                if (!(o instanceof FieldRegistry.Default.Entry)) return false;
                final FieldRegistry.Default.Entry other = (FieldRegistry.Default.Entry) o;
                if (!other.canEqual((java.lang.Object) this)) return false;
                final java.lang.Object this$matcher = this.matcher;
                final java.lang.Object other$matcher = other.matcher;
                if (this$matcher == null ? other$matcher != null : !this$matcher.equals(other$matcher)) return false;
                final java.lang.Object this$fieldAttributeAppenderFactory = this.getFieldAttributeAppenderFactory();
                final java.lang.Object other$fieldAttributeAppenderFactory = other.getFieldAttributeAppenderFactory();
                if (this$fieldAttributeAppenderFactory == null ? other$fieldAttributeAppenderFactory != null : !this$fieldAttributeAppenderFactory.equals(other$fieldAttributeAppenderFactory)) return false;
                final java.lang.Object this$defaultValue = this.getDefaultValue();
                final java.lang.Object other$defaultValue = other.getDefaultValue();
                if (this$defaultValue == null ? other$defaultValue != null : !this$defaultValue.equals(other$defaultValue)) return false;
                final java.lang.Object this$transformer = this.getTransformer();
                final java.lang.Object other$transformer = other.getTransformer();
                if (this$transformer == null ? other$transformer != null : !this$transformer.equals(other$transformer)) return false;
                return true;
            }

            @java.lang.SuppressWarnings("all")
            @javax.annotation.Generated("lombok")
            protected boolean canEqual(final java.lang.Object other) {
                return other instanceof FieldRegistry.Default.Entry;
            }

            @java.lang.Override
            @java.lang.SuppressWarnings("all")
            @javax.annotation.Generated("lombok")
            public int hashCode() {
                final int PRIME = 59;
                int result = 1;
                final java.lang.Object $matcher = this.matcher;
                result = result * PRIME + ($matcher == null ? 43 : $matcher.hashCode());
                final java.lang.Object $fieldAttributeAppenderFactory = this.getFieldAttributeAppenderFactory();
                result = result * PRIME + ($fieldAttributeAppenderFactory == null ? 43 : $fieldAttributeAppenderFactory.hashCode());
                final java.lang.Object $defaultValue = this.getDefaultValue();
                result = result * PRIME + ($defaultValue == null ? 43 : $defaultValue.hashCode());
                final java.lang.Object $transformer = this.getTransformer();
                result = result * PRIME + ($transformer == null ? 43 : $transformer.hashCode());
                return result;
            }
        }

        /**
         * A compiled default field registry.
         */
        protected static class Compiled implements FieldRegistry.Compiled {
            /**
             * The instrumented type for which this registry was compiled for.
             */
            private final TypeDescription instrumentedType;
            /**
             * The entries of this compiled field registry.
             */
            private final List<Entry> entries;

            /**
             * Creates a new compiled field registry.
             *
             * @param instrumentedType The instrumented type for which this registry was compiled for.
             * @param entries          The entries of this compiled field registry.
             */
            protected Compiled(TypeDescription instrumentedType, List<Entry> entries) {
                this.instrumentedType = instrumentedType;
                this.entries = entries;
            }

            @Override
            public Record target(FieldDescription fieldDescription) {
                for (Entry entry : entries) {
                    if (entry.matches(fieldDescription)) {
                        return entry.bind(instrumentedType, fieldDescription);
                    }
                }
                return new Record.ForImplicitField(fieldDescription);
            }

            /**
             * An entry of a compiled field registry.
             */
            protected static class Entry implements ElementMatcher<FieldDescription> {
                /**
                 * The matcher to identify any field that this definition concerns.
                 */
                private final ElementMatcher<? super FieldDescription> matcher;
                /**
                 * The field attribute appender to apply on any matched field.
                 */
                private final FieldAttributeAppender fieldAttributeAppender;
                /**
                 * The default value to write to the field or {@code null} if no default value is to be set for the field.
                 */
                private final Object defaultValue;
                /**
                 * The field transformer to apply to any matched field.
                 */
                private final Transformer<FieldDescription> transformer;

                /**
                 * Creates a new entry.
                 *
                 * @param matcher                The matcher to identify any field that this definition concerns.
                 * @param fieldAttributeAppender The field attribute appender to apply on any matched field.
                 * @param defaultValue           The default value to write to the field or {@code null} if no default value is to be set for the field.
                 * @param transformer       The field transformer to apply to any matched field.
                 */
                protected Entry(ElementMatcher<? super FieldDescription> matcher, FieldAttributeAppender fieldAttributeAppender, Object defaultValue, Transformer<FieldDescription> transformer) {
                    this.matcher = matcher;
                    this.fieldAttributeAppender = fieldAttributeAppender;
                    this.defaultValue = defaultValue;
                    this.transformer = transformer;
                }

                /**
                 * Binds this entry to the provided field description.
                 *
                 * @param instrumentedType The instrumented type for which this entry applies.
                 * @param fieldDescription The field description to be bound to this entry.
                 * @return A record representing the binding of this entry to the provided field.
                 */
                protected Record bind(TypeDescription instrumentedType, FieldDescription fieldDescription) {
                    return new Record.ForExplicitField(fieldAttributeAppender, defaultValue, transformer.transform(instrumentedType, fieldDescription));
                }

                @Override
                public boolean matches(FieldDescription target) {
                    return matcher.matches(target);
                }

                @java.lang.Override
                @java.lang.SuppressWarnings("all")
                @javax.annotation.Generated("lombok")
                public boolean equals(final java.lang.Object o) {
                    if (o == this) return true;
                    if (!(o instanceof FieldRegistry.Default.Compiled.Entry)) return false;
                    final FieldRegistry.Default.Compiled.Entry other = (FieldRegistry.Default.Compiled.Entry) o;
                    if (!other.canEqual((java.lang.Object) this)) return false;
                    final java.lang.Object this$matcher = this.matcher;
                    final java.lang.Object other$matcher = other.matcher;
                    if (this$matcher == null ? other$matcher != null : !this$matcher.equals(other$matcher)) return false;
                    final java.lang.Object this$fieldAttributeAppender = this.fieldAttributeAppender;
                    final java.lang.Object other$fieldAttributeAppender = other.fieldAttributeAppender;
                    if (this$fieldAttributeAppender == null ? other$fieldAttributeAppender != null : !this$fieldAttributeAppender.equals(other$fieldAttributeAppender)) return false;
                    final java.lang.Object this$defaultValue = this.defaultValue;
                    final java.lang.Object other$defaultValue = other.defaultValue;
                    if (this$defaultValue == null ? other$defaultValue != null : !this$defaultValue.equals(other$defaultValue)) return false;
                    final java.lang.Object this$transformer = this.transformer;
                    final java.lang.Object other$transformer = other.transformer;
                    if (this$transformer == null ? other$transformer != null : !this$transformer.equals(other$transformer)) return false;
                    return true;
                }

                @java.lang.SuppressWarnings("all")
                @javax.annotation.Generated("lombok")
                protected boolean canEqual(final java.lang.Object other) {
                    return other instanceof FieldRegistry.Default.Compiled.Entry;
                }

                @java.lang.Override
                @java.lang.SuppressWarnings("all")
                @javax.annotation.Generated("lombok")
                public int hashCode() {
                    final int PRIME = 59;
                    int result = 1;
                    final java.lang.Object $matcher = this.matcher;
                    result = result * PRIME + ($matcher == null ? 43 : $matcher.hashCode());
                    final java.lang.Object $fieldAttributeAppender = this.fieldAttributeAppender;
                    result = result * PRIME + ($fieldAttributeAppender == null ? 43 : $fieldAttributeAppender.hashCode());
                    final java.lang.Object $defaultValue = this.defaultValue;
                    result = result * PRIME + ($defaultValue == null ? 43 : $defaultValue.hashCode());
                    final java.lang.Object $transformer = this.transformer;
                    result = result * PRIME + ($transformer == null ? 43 : $transformer.hashCode());
                    return result;
                }
            }

            @java.lang.Override
            @java.lang.SuppressWarnings("all")
            @javax.annotation.Generated("lombok")
            public boolean equals(final java.lang.Object o) {
                if (o == this) return true;
                if (!(o instanceof FieldRegistry.Default.Compiled)) return false;
                final FieldRegistry.Default.Compiled other = (FieldRegistry.Default.Compiled) o;
                if (!other.canEqual((java.lang.Object) this)) return false;
                final java.lang.Object this$instrumentedType = this.instrumentedType;
                final java.lang.Object other$instrumentedType = other.instrumentedType;
                if (this$instrumentedType == null ? other$instrumentedType != null : !this$instrumentedType.equals(other$instrumentedType)) return false;
                final java.lang.Object this$entries = this.entries;
                final java.lang.Object other$entries = other.entries;
                if (this$entries == null ? other$entries != null : !this$entries.equals(other$entries)) return false;
                return true;
            }

            @java.lang.SuppressWarnings("all")
            @javax.annotation.Generated("lombok")
            protected boolean canEqual(final java.lang.Object other) {
                return other instanceof FieldRegistry.Default.Compiled;
            }

            @java.lang.Override
            @java.lang.SuppressWarnings("all")
            @javax.annotation.Generated("lombok")
            public int hashCode() {
                final int PRIME = 59;
                int result = 1;
                final java.lang.Object $instrumentedType = this.instrumentedType;
                result = result * PRIME + ($instrumentedType == null ? 43 : $instrumentedType.hashCode());
                final java.lang.Object $entries = this.entries;
                result = result * PRIME + ($entries == null ? 43 : $entries.hashCode());
                return result;
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof FieldRegistry.Default)) return false;
            final FieldRegistry.Default other = (FieldRegistry.Default) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$entries = this.entries;
            final java.lang.Object other$entries = other.entries;
            if (this$entries == null ? other$entries != null : !this$entries.equals(other$entries)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof FieldRegistry.Default;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $entries = this.entries;
            result = result * PRIME + ($entries == null ? 43 : $entries.hashCode());
            return result;
        }
    }
}

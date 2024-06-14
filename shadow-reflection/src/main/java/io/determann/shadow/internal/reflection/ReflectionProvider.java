package io.determann.shadow.internal.reflection;

import io.determann.shadow.meta_meta.AbstractProvider;
import io.determann.shadow.meta_meta.MappingBuilder;

import static io.determann.shadow.api.reflection.ReflectionQueries.query;
import static io.determann.shadow.meta_meta.Operations.*;

public class ReflectionProvider extends AbstractProvider
{
   public static final String IMPLEMENTATION_NAME = "io.determann.shadow-reflection";

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   @Override
   protected void addMappings(MappingBuilder builder)
   {
      builder.with(NAMEABLE_NAME, nameable -> query(nameable).getName())
             .with(QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME, qualifiedNameable -> query(qualifiedNameable).getQualifiedName())
             .with(SHADOW_GET_KIND, shadow -> query(shadow).getKind())
             .with(SHADOW_REPRESENTS_SAME_TYPE, (shadow, shadow2) -> query(shadow).representsSameType(shadow2))
             .withOptional(WILDCARD_EXTENDS, wildcard -> query(wildcard).getExtends())
             .withOptional(WILDCARD_SUPER, wildcard -> query(wildcard).getSuper())
             .with(PRIMITIVE_AS_BOXED, primitive -> query(primitive).asBoxed())
             .with(PRIMITIVE_IS_ASSIGNABLE_FROM, (primitive, shadow) -> query(primitive).isAssignableFrom(shadow))
             .with(PRIMITIVE_IS_SUBTYPE_OF, (primitive, shadow) -> query(primitive).isSubtypeOf(shadow))
             .with(PACKAGE_GET_CONTENT, aPackage -> query(aPackage).getContent())
             .with(PACKAGE_IS_UNNAMED, aPackage -> query(aPackage).isUnnamed())
             .with(MODULE_ENCLOSED_GET_MODULE, moduleEnclosed -> query(moduleEnclosed).getModule())
             .with(DECLARED_IS_SUBTYPE_OF, (declared, shadow) -> query(declared).isSubtypeOf(shadow))
             .with(DECLARED_IS_SUBTYPE_OF, (declared, shadow) -> query(declared).isSubtypeOf(shadow))
             .with(DECLARED_GET_NESTING, declared -> query(declared).getNesting())
             .with(DECLARED_GET_FIELDS, declared -> query(declared).getFields())
             .with(DECLARED_GET_FIELD, (declared, s) -> query(declared).getFieldOrThrow(s))
             .with(DECLARED_GET_METHODS, declared -> query(declared).getMethods())
             .with(DECLARED_GET_METHOD, (declared, s) -> query(declared).getMethods(s))
             .with(DECLARED_GET_CONSTRUCTORS, declared -> query(declared).getConstructors())
             .with(DECLARED_GET_DIRECT_SUPER_TYPES, declared -> query(declared).getDirectSuperTypes())
             .with(DECLARED_GET_SUPER_TYPES, declared -> query(declared).getDirectSuperTypes())
             .with(DECLARED_GET_INTERFACES, declared -> query(declared).getInterfaces())
             .with(DECLARED_GET_INTERFACE, (declared, s) -> query(declared).getInterfaceOrThrow(s))
             .with(DECLARED_GET_DIRECT_INTERFACES, declared -> query(declared).getDirectInterfaces())
             .with(DECLARED_GET_DIRECT_INTERFACE, (declared, s) -> query(declared).getDirectInterfaceOrThrow(s))
             .with(DECLARED_GET_PACKAGE, declared -> query(declared).getPackage())
             .with(ENUM_GET_ENUM_CONSTANT, (anEnum, s) -> query(anEnum).getEnumConstantOrThrow(s))
             .with(ENUM_GET_EUM_CONSTANTS, anEnum -> query(anEnum).getEumConstants())
             .with(INTERFACE_IS_FUNCTIONAL, anInterface -> query(anInterface).isFunctional())
             .with(INTERFACE_GET_GENERIC_TYPES, anInterface -> query(anInterface).getGenericTypes())
             .with(INTERFACE_GET_GENERICS, anInterface -> query(anInterface).getGenerics())
             .with(RECORD_GET_RECORD_COMPONENTS, record -> query(record).getRecordComponents())
             .with(RECORD_GET_RECORD_COMPONENT, (record, s) -> query(record).getRecordComponentOrThrow(s))
             .with(RECORD_GET_GENERIC_TYPES, record -> query(record).getGenericTypes())
             .with(RECORD_GET_GENERICS, record -> query(record).getGenerics())
             .with(CLASS_GET_SUPER_CLASS, aClass -> query(aClass).getSuperClass())
             .with(CLASS_GET_PERMITTED_SUB_CLASSES, aClass -> query(aClass).getPermittedSubClasses())
             .with(CLASS_GET_PROPERTIES, aClass -> query(aClass).getMutableProperties())
             .with(CLASS_GET_MUTABLE_PROPERTIES, aClass -> query(aClass).getMutableProperties())
             .with(CLASS_GET_IMMUTABLE_PROPERTIES, aClass -> query(aClass).getImmutableProperties())
             .with(CLASS_IS_ASSIGNABLE_FROM, (aClass, shadow) -> query(aClass).isAssignableFrom(shadow))
             .withOptional(CLASS_GET_OUTER_TYPE, aClass -> query(aClass).getOuterType())
             .with(CLASS_GET_GENERIC_TYPES, aClass -> query(aClass).getGenericTypes())
             .with(CLASS_GET_GENERICS, aClass -> query(aClass).getGenerics())
             .with(CLASS_AS_UNBOXED, aClass -> query(aClass).asUnboxed())
             .with(ARRAY_IS_SUBTYPE_OF, (array, shadow) -> query(array).isSubtypeOf(shadow))
             .with(ARRAY_GET_COMPONENT_TYPE, array -> query(array).getComponentType())
             .with(ARRAY_GET_DIRECT_SUPER_TYPES, array -> query(array).getDirectSuperTypes());
   }
}

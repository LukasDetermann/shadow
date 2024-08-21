package io.determann.shadow.internal.reflection;


import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.AnnotationValue;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.implementation.support.api.provider.AbstractProvider;
import io.determann.shadow.implementation.support.api.provider.MappingBuilder;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.reflection.ReflectionQueries.query;

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
      builder.with(NAMEABLE_GET_NAME, nameable -> query(nameable).getName())
             .with(QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME, qualifiedNameable -> query(qualifiedNameable).getQualifiedName())
             .with(SHADOW_GET_KIND, shadow -> query(shadow).getKind())
             .with(SHADOW_REPRESENTS_SAME_TYPE, (shadow, shadow2) -> query(shadow).representsSameType(shadow2))
             .withOptional(WILDCARD_GET_EXTENDS, wildcard -> query(wildcard).getExtends())
             .withOptional(WILDCARD_GET_SUPER, wildcard -> query(wildcard).getSuper())
             .with(PRIMITIVE_AS_BOXED, primitive -> query(primitive).asBoxed())
             .with(PRIMITIVE_IS_ASSIGNABLE_FROM, (primitive, shadow) -> query(primitive).isAssignableFrom(shadow))
             .with(PRIMITIVE_IS_SUBTYPE_OF, (primitive, shadow) -> query(primitive).isSubtypeOf(shadow))
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
             .with(CLASS_GET_PROPERTIES, aClass -> query(aClass).getProperties())
             .with(CLASS_IS_ASSIGNABLE_FROM, (aClass, shadow) -> query(aClass).isAssignableFrom(shadow))
             .withOptional(CLASS_GET_OUTER_TYPE, aClass -> query(aClass).getOuterType())
             .with(CLASS_GET_GENERIC_TYPES, aClass -> query(aClass).getGenericTypes())
             .with(CLASS_GET_GENERICS, aClass -> query(aClass).getGenerics())
             .with(CLASS_AS_UNBOXED, aClass -> query(aClass).asUnboxed())
             .with(ARRAY_IS_SUBTYPE_OF, (array, shadow) -> query(array).isSubtypeOf(shadow))
             .with(ARRAY_GET_COMPONENT_TYPE, array -> query(array).getComponentType())
             .with(ARRAY_GET_DIRECT_SUPER_TYPES, array -> query(array).getDirectSuperTypes())
             .with(ENUM_CONSTANT_GET_SURROUNDING, enumConstant -> query(enumConstant).getSurrounding())
             .with(FIELD_IS_CONSTANT, field -> query(field).isConstant())
             .with(FIELD_GET_CONSTANT_VALUE, field -> query(field).getConstantValue())
             .with(FIELD_GET_SURROUNDING, field -> query(field).getSurrounding())
             .with(PARAMETER_IS_VAR_ARGS, parameter -> query(parameter).isVarArgs())
             .with(PARAMETER_GET_SURROUNDING, parameter -> query(parameter).getSurrounding())
             .with(VARIABLE_IS_SUBTYPE_OF, (variable, shadow) -> query(variable).isSubtypeOf(shadow))
             .with(VARIABLE_IS_ASSIGNABLE_FROM, (variable, shadow) -> query(variable).isAssignableFrom(shadow))
             .with(VARIABLE_GET_TYPE, variable -> query(variable).getType())
             .with(VARIABLE_GET_PACKAGE, variable -> query(variable).getPackage())
             .with(VARIABLE_GET_SURROUNDING, variable -> query(variable).getSurrounding())
             .with(EXECUTABLE_GET_PARAMETERS, executable -> query(executable).getParameters())
             .with(EXECUTABLE_GET_PARAMETER, (executable, s) -> query(executable).getParameterOrThrow(s))
             .with(EXECUTABLE_GET_RETURN, executable -> query(executable).getReturn())
             .with(EXECUTABLE_GET_RETURN_TYPE, executable -> query(executable).getReturnType())
             .with(EXECUTABLE_GET_PARAMETER_TYPES, executable -> query(executable).getParameterTypes())
             .with(EXECUTABLE_GET_THROWS, executable -> query(executable).getThrows())
             .with(EXECUTABLE_IS_VAR_ARGS, executable -> query(executable).isVarArgs())
             .with(EXECUTABLE_GET_SURROUNDING, executable -> query(executable).getSurrounding())
             .with(EXECUTABLE_GET_PACKAGE, executable -> query(executable).getPackage())
             .with(EXECUTABLE_GET_GENERICS, executable -> query(executable).getGenerics())
             .withOptional(EXECUTABLE_GET_RECEIVER_TYPE, executable -> query(executable).getReceiverType())
             .withOptional(EXECUTABLE_GET_RECEIVER, executable -> query(executable).getReceiver())
             .with(METHOD_OVERRIDES, (method, method2) -> query(method).overrides(method2))
             .with(METHOD_OVERWRITTEN_BY, (method, method2) -> query(method).overwrittenBy(method2))
             .with(METHOD_SAME_PARAMETER_TYPES, (method, method2) -> query(method).sameParameterTypes(method2))
             .with(METHOD_IS_BRIDGE, method -> query(method).isBridge())
             .with(RECORD_COMPONENT_IS_SUBTYPE_OF, (recordComponent, shadow) -> query(recordComponent).isSubtypeOf(shadow))
             .with(RECORD_COMPONENT_IS_ASSIGNABLE_FROM, (recordComponent, shadow) -> query(recordComponent).isAssignableFrom(shadow))
             .with(RECORD_COMPONENT_GET_RECORD, recordComponent -> query(recordComponent).getRecord())
             .with(RECORD_COMPONENT_GET_TYPE, recordComponent -> query(recordComponent).getType())
             .with(RECORD_COMPONENT_GET_GETTER, recordComponent -> query(recordComponent).getGetter())
             .with(RECORD_COMPONENT_GET_PACKAGE, recordComponent -> query(recordComponent).getPackage())
             .with(RETURN_GET_TYPE, aReturn -> query(aReturn).getType())
             .with(RECEIVER_GET_TYPE, receiver -> query(receiver).getType())
             .with(INTERSECTION_GET_BOUNDS, intersection -> query(intersection).getBounds())
             .with(GENERIC_GET_EXTENDS, generic -> query(generic).getExtends())
             .withOptional(GENERIC_GET_SUPER, generic -> query(generic).getSuper())
             .with(GENERIC_GET_ENCLOSING, generic -> query(generic).getEnclosing())
             .with(ANNOTATION_USAGE_GET_VALUES, annotationUsage -> query(annotationUsage).getValues())
             .withOptional(ANNOTATION_USAGE_GET_VALUE, (annotationUsage, s) -> query(annotationUsage).getValue(s).map(AnnotationValue.class::cast))
             .with(ANNOTATION_USAGE_GET_ANNOTATION, annotationUsage -> query(annotationUsage).getAnnotation())
             .with(MODULE_GET_PACKAGES, module -> query(module).getPackages())
             .with(MODULE_IS_OPEN, module -> query(module).isOpen())
             .with(MODULE_IS_UNNAMED, module -> query(module).isUnnamed())
             .with(MODULE_IS_AUTOMATIC, module -> query(module).isAutomatic())
             .with(MODULE_GET_DIRECTIVES, module -> query(module).getDirectives())
             .with(MODIFIABLE_GET_MODIFIERS, modifiable -> query(modifiable).getModifiers())
             .with(MODIFIABLE_HAS_MODIFIER, (modifiable, modifier) -> query(modifiable).hasModifier(modifier))
             .with(EXPORTS_GET_PACKAGE, exports -> query(exports).getPackage())
             .with(EXPORTS_GET_TARGET_MODULES, exports -> query(exports).getTargetModules())
             .with(EXPORTS_TO_ALL, exports -> query(exports).toAll())
             .with(OPENS_GET_PACKAGE, opens -> query(opens).getPackage())
             .with(OPENS_GET_TARGET_MODULES, opens -> query(opens).getTargetModules())
             .with(OPENS_TO_ALL, opens -> query(opens).toAll())
             .with(PROVIDES_GET_SERVICE, provides -> query(provides).getService())
             .with(PROVIDES_GET_IMPLEMENTATIONS, provides -> query(provides).getImplementations())
             .with(REQUIRES_IS_STATIC, requires -> query(requires).isStatic())
             .with(REQUIRES_IS_TRANSITIVE, requires -> query(requires).isTransitive())
             .with(REQUIRES_GET_DEPENDENCY, requires -> query(requires).getDependency())
             .with(USES_GET_SERVICE, uses -> query(uses).getService())
             .with(ANNOTATIONABLE_GET_ANNOTATION_USAGES, annotationable -> query(annotationable).getAnnotationUsages())
             .with(ANNOTATIONABLE_GET_USAGES_OF, (annotationable, annotation) -> query(annotationable).getUsagesOf(annotation))
             .withOptional(ANNOTATIONABLE_GET_USAGE_OF, (annotationable, annotation) -> query(annotationable).getUsageOf(annotation).map(AnnotationUsage.class::cast))
             .with(ANNOTATIONABLE_IS_ANNOTATED_WITH, (annotationable, annotation) -> query(annotationable).isAnnotatedWith(annotation))
             .with(ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES, annotationable -> query(annotationable).getDirectAnnotationUsages())
             .with(ANNOTATIONABLE_GET_DIRECT_USAGES_OF, (annotationable, annotation) -> query(annotationable).getDirectUsagesOf(annotation))
             .withOptional(ANNOTATIONABLE_GET_DIRECT_USAGE_OF, (annotationable, annotation) -> query(annotationable).getDirectUsageOf(annotation).map(AnnotationUsage.class::cast))
             .with(ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH, (annotationable, annotation) -> query(annotationable).isDirectlyAnnotatedWith(annotation))
             .with(ANNOTATION_VALUE_IS_DEFAULT, annotationValue -> query(annotationValue).isDefault())
             .with(ANNOTATION_VALUE_GET_VALUE, annotationValue -> query(annotationValue).getValue())
             .with(PROPERTY_GET_NAME, property -> query(property).getName())
             .with(PROPERTY_GET_TYPE, property -> query(property).getType())
             .withOptional(PROPERTY_GET_FIELD, property -> query(property).getField())
             .with(PROPERTY_GET_GETTER, property -> query(property).getGetter())
             .withOptional(PROPERTY_GET_SETTER, property -> query(property).getSetter())
             .with(PROPERTY_IS_MUTABLE, property -> query(property).isMutable())
             .withOptional(MODULE_GET_DECLARED, (module, s) -> query(module).getDeclared(s).map(Declared.class::cast))
             .with(MODULE_GET_DECLARED_LIST, module -> query(module).getDeclared())
             .withOptional(PACKAGE_GET_DECLARED, (aPackage, s) -> query(aPackage).getDeclared(s).map(Declared.class::cast))
             .with(PACKAGE_GET_DECLARED_LIST, aPackage -> query(aPackage).getDeclared());
   }
}

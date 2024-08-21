package io.determann.shadow.api;

import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.modifier.Modifiable;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Operations
{
   public static Operation0<Shadow, TypeKind> SHADOW_GET_KIND = new Operation0<>("shadow.getKind");

   public static Operation1<Shadow, Shadow, Boolean> SHADOW_REPRESENTS_SAME_TYPE = new Operation1<>("shadow.representsSameType");

   public static Operation0<Nameable, String> NAMEABLE_GET_NAME = new Operation0<>("nameable.getName");

   public static Operation0<QualifiedNameable, String> QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME = new Operation0<>("qualifiedNameable.getQualifiedName");

   public static Operation0<Wildcard, Shadow> WILDCARD_GET_EXTENDS = new Operation0<>("wildcard.getExtends");

   public static Operation0<Wildcard, Shadow> WILDCARD_GET_SUPER = new Operation0<>("wildcard.getSuper");

   public static Operation0<Primitive, Shadow> PRIMITIVE_AS_BOXED = new Operation0<>("primitive.asBoxed");

   public static Operation1<Primitive, Shadow, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new Operation1<>("primitive.isSubtypeOf");

   public static Operation1<Primitive, Shadow, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new Operation1<>("primitive.isAssignableFrom");

   public static Operation0<Package, Boolean> PACKAGE_IS_UNNAMED = new Operation0<>("package.isUnnamed");

   public static Operation1<Package, String, Declared> PACKAGE_GET_DECLARED = new Operation1<>("package.getDeclared");

   public static Operation0<Package, List<? extends Declared>> PACKAGE_GET_DECLARED_LIST = new Operation0<>("package.getDeclaredList");

   public static Operation0<ModuleEnclosed, Module> MODULE_ENCLOSED_GET_MODULE = new Operation0<>("moduleEnclosed.getModule");

   public static Operation1<Declared, Shadow, Boolean> DECLARED_IS_SUBTYPE_OF = new Operation1<>("declared.isSubtypeOf");

   public static Operation0<Declared, NestingKind> DECLARED_GET_NESTING = new Operation0<>("declared.getNesting");

   public static Operation0<Declared, List<? extends Field>> DECLARED_GET_FIELDS = new Operation0<>("declared.getFields");

   public static Operation1<Declared, String, Field> DECLARED_GET_FIELD = new Operation1<>("declared.getField");

   public static Operation0<Declared, List<? extends Method>> DECLARED_GET_METHODS = new Operation0<>("declared.getMethods");

   public static Operation1<Declared, String, List<? extends Method>> DECLARED_GET_METHOD = new Operation1<>("declared.getMethod");

   public static Operation0<Declared, List<? extends Constructor>> DECLARED_GET_CONSTRUCTORS = new Operation0<>("declared.getConstructor");

   public static Operation0<Declared, List<? extends Declared>> DECLARED_GET_DIRECT_SUPER_TYPES = new Operation0<>("declared.getDirectSuperTypes");

   public static Operation0<Declared, List<? extends Declared>> DECLARED_GET_SUPER_TYPES = new Operation0<>("declared.getSuperTypes");

   public static Operation0<Declared, List<? extends Interface>> DECLARED_GET_INTERFACES = new Operation0<>("declared.getInterfaces");

   public static Operation1<Declared, String, Interface> DECLARED_GET_INTERFACE = new Operation1<>("declared.getInterface");

   public static Operation0<Declared, List<? extends Interface>> DECLARED_GET_DIRECT_INTERFACES = new Operation0<>("declared.getDirectInterfaces");

   public static Operation1<Declared, String, Interface> DECLARED_GET_DIRECT_INTERFACE = new Operation1<>("declared.getDirectInterface");

   public static Operation0<Declared, Package> DECLARED_GET_PACKAGE = new Operation0<>("declared.getPackage");

   public static Operation0<Enum, List<? extends EnumConstant>> ENUM_GET_EUM_CONSTANTS = new Operation0<>("enum.getEumConstants");

   public static Operation1<Enum, String, EnumConstant> ENUM_GET_ENUM_CONSTANT = new Operation1<>("enum.getEnumConstant");

   public static Operation0<Interface, Boolean> INTERFACE_IS_FUNCTIONAL = new Operation0<>("Interface.isFunctional");

   public static Operation0<Interface, List<? extends Shadow>> INTERFACE_GET_GENERIC_TYPES = new Operation0<>("interface.getGenericTypes");

   public static Operation0<Interface, List<? extends Generic>> INTERFACE_GET_GENERICS = new Operation0<>("interface.getGenerics");

   public static Operation0<Record, List<? extends RecordComponent>> RECORD_GET_RECORD_COMPONENTS = new Operation0<>("record.getRecordComponents");

   public static Operation1<Record, String, RecordComponent> RECORD_GET_RECORD_COMPONENT = new Operation1<>("record.getRecordComponent");

   public static Operation0<Record, List<? extends Shadow>> RECORD_GET_GENERIC_TYPES = new Operation0<>("record.getGenericTypes");

   public static Operation0<Record, List<? extends Generic>> RECORD_GET_GENERICS = new Operation0<>("record.getGenerics");

   public static Operation0<Class, Class> CLASS_GET_SUPER_CLASS = new Operation0<>("class.getSuperClass");

   public static Operation0<Class, List<? extends Class>> CLASS_GET_PERMITTED_SUB_CLASSES = new Operation0<>("class.getPermittedSubClasses");

   public static Operation0<Class, List<? extends Property>> CLASS_GET_PROPERTIES = new Operation0<>("class.getProperties");

   public static Operation1<Class, Shadow, Boolean> CLASS_IS_ASSIGNABLE_FROM = new Operation1<>("class.isAssignableFrom");

   public static Operation0<Class, Declared> CLASS_GET_OUTER_TYPE = new Operation0<>("class.getOuterType");

   public static Operation0<Class, List<? extends Shadow>> CLASS_GET_GENERIC_TYPES = new Operation0<>("class.getGenericTypes");

   public static Operation0<Class, List<? extends Generic>> CLASS_GET_GENERICS = new Operation0<>("class.getGenerics");

   public static Operation0<Class, Primitive> CLASS_AS_UNBOXED = new Operation0<>("class.asUnboxed");

   public static Operation1<Array, Shadow, Boolean> ARRAY_IS_SUBTYPE_OF = new Operation1<>("array.isSubtypeOf");

   public static Operation0<Array, Shadow> ARRAY_GET_COMPONENT_TYPE = new Operation0<>("array.getComponentType");

   public static Operation0<Array, List<? extends Shadow>> ARRAY_GET_DIRECT_SUPER_TYPES = new Operation0<>("array.getDirectSuperTypes");

   public static Operation0<EnumConstant, Enum> ENUM_CONSTANT_GET_SURROUNDING = new Operation0<>("enumConstant.getSurrounding");

   public static Operation0<Field, Boolean> FIELD_IS_CONSTANT = new Operation0<>("field.isConstant");

   public static Operation0<Field, Object> FIELD_GET_CONSTANT_VALUE = new Operation0<>("field.getConstantValue");

   public static Operation0<Field, Declared> FIELD_GET_SURROUNDING = new Operation0<>("field.getSurrounding");

   public static Operation0<Parameter, Boolean> PARAMETER_IS_VAR_ARGS = new Operation0<>("parameter.isVarArgs");

   public static Operation0<Parameter, Executable> PARAMETER_GET_SURROUNDING = new Operation0<>("parameter.getSurrounding");

   public static Operation1<Variable, Shadow, Boolean> VARIABLE_IS_SUBTYPE_OF = new Operation1<>("variable.isSubtypeOf");

   public static Operation1<Variable, Shadow, Boolean> VARIABLE_IS_ASSIGNABLE_FROM = new Operation1<>("variable.isAssignableFrom");

   public static Operation0<Variable, Shadow> VARIABLE_GET_TYPE = new Operation0<>("variable.getType");

   public static Operation0<Variable, Package> VARIABLE_GET_PACKAGE = new Operation0<>("variable.getPackage");

   public static Operation0<Variable, Object> VARIABLE_GET_SURROUNDING = new Operation0<>("variable.getSurrounding");

   public static Operation0<Executable, List<? extends Parameter>> EXECUTABLE_GET_PARAMETERS = new Operation0<>("executable.getParameters");

   public static Operation1<Executable, String, Parameter> EXECUTABLE_GET_PARAMETER = new Operation1<>("executable.getParameter");

   public static Operation0<Executable, Return> EXECUTABLE_GET_RETURN = new Operation0<>("executable.getReturn");

   public static Operation0<Executable, Shadow> EXECUTABLE_GET_RETURN_TYPE = new Operation0<>("executable.getReturnType");

   public static Operation0<Executable, List<? extends Shadow>> EXECUTABLE_GET_PARAMETER_TYPES = new Operation0<>("executable.getParameterTypes");

   public static Operation0<Executable, List<? extends Class>> EXECUTABLE_GET_THROWS = new Operation0<>("executable.getThrows");

   public static Operation0<Executable, Boolean> EXECUTABLE_IS_VAR_ARGS = new Operation0<>("executable.isVarArgs");

   public static Operation0<Executable, Declared> EXECUTABLE_GET_SURROUNDING = new Operation0<>("executable.getSurrounding");

   public static Operation0<Executable, Package> EXECUTABLE_GET_PACKAGE = new Operation0<>("executable.getPackage");

   public static Operation0<Executable, List<? extends Generic>> EXECUTABLE_GET_GENERICS = new Operation0<>("executable.getGenerics");

   public static Operation0<Executable, Declared> EXECUTABLE_GET_RECEIVER_TYPE = new Operation0<>("executable.getReceiverType");

   public static Operation0<Executable, Receiver> EXECUTABLE_GET_RECEIVER = new Operation0<>("executable.getReceiver");

   public static Operation1<Method, Method, Boolean> METHOD_OVERRIDES = new Operation1<>("method.overrides");

   public static Operation1<Method, Method, Boolean> METHOD_OVERWRITTEN_BY = new Operation1<>("method.overwrittenBy");

   public static Operation1<Method, Method, Boolean> METHOD_SAME_PARAMETER_TYPES = new Operation1<>("method.sameParameterTypes");

   public static Operation0<Method, Boolean> METHOD_IS_BRIDGE = new Operation0<>("method.isBridge");

   public static Operation1<RecordComponent, Shadow, Boolean> RECORD_COMPONENT_IS_SUBTYPE_OF = new Operation1<>("recordComponent.isSubtypeOf");

   public static Operation1<RecordComponent, Shadow, Boolean> RECORD_COMPONENT_IS_ASSIGNABLE_FROM = new Operation1<>("recordComponent.isAssignableFrom");

   public static Operation0<RecordComponent, Record> RECORD_COMPONENT_GET_RECORD = new Operation0<>("RecordComponent.getRecord");

   public static Operation0<RecordComponent, Shadow> RECORD_COMPONENT_GET_TYPE = new Operation0<>("recordComponent.getType");

   public static Operation0<RecordComponent, Method> RECORD_COMPONENT_GET_GETTER = new Operation0<>("recordComponent.getGetter");

   public static Operation0<RecordComponent, Package> RECORD_COMPONENT_GET_PACKAGE = new Operation0<>("recordComponent.getPackage");

   public static Operation0<Return, Shadow> RETURN_GET_TYPE = new Operation0<>("return.getType");

   public static Operation0<Receiver, Shadow> RECEIVER_GET_TYPE = new Operation0<>("receiver.getType");

   public static Operation0<Intersection, List<? extends Shadow>> INTERSECTION_GET_BOUNDS = new Operation0<>("intersection.getBounds");

   public static Operation0<Generic, Shadow> GENERIC_GET_EXTENDS = new Operation0<>("generic.getExtends");

   public static Operation0<Generic, Shadow> GENERIC_GET_SUPER = new Operation0<>("generic.getSuper");

   public static Operation0<Generic, Object> GENERIC_GET_ENCLOSING = new Operation0<>("generic.getEnclosing");

   public static Operation0<AnnotationUsage, Map<? extends Method, ? extends AnnotationValue>> ANNOTATION_USAGE_GET_VALUES = new Operation0<>("annotationUsage.getValues");

   public static Operation1<AnnotationUsage, String, AnnotationValue> ANNOTATION_USAGE_GET_VALUE = new Operation1<>("annotationUsage.getValue");

   public static Operation0<AnnotationUsage, Annotation> ANNOTATION_USAGE_GET_ANNOTATION = new Operation0<>("annotationUsage.getAnnotation");

   public static Operation0<Module, List<? extends Package>> MODULE_GET_PACKAGES = new Operation0<>("module.getPackages");

   public static Operation0<Module, Boolean> MODULE_IS_OPEN = new Operation0<>("module.isOpen");

   public static Operation0<Module, Boolean> MODULE_IS_UNNAMED = new Operation0<>("module.isUnnamed");

   public static Operation0<Module, Boolean> MODULE_IS_AUTOMATIC = new Operation0<>("module.isAutomatic");

   public static Operation0<Module, List<? extends Directive>> MODULE_GET_DIRECTIVES = new Operation0<>("module.getDirectives");

   public static Operation1<Module, String, Declared> MODULE_GET_DECLARED = new Operation1<>("module.getDeclared");

   public static Operation0<Module, List<? extends Declared>> MODULE_GET_DECLARED_LIST = new Operation0<>("module.getDeclaredList");

   public static Operation0<Modifiable, Set<Modifier>> MODIFIABLE_GET_MODIFIERS = new Operation0<>("modifiable.getModifiers");

   public static Operation1<Modifiable, Modifier, Boolean> MODIFIABLE_HAS_MODIFIER = new Operation1<>("modifiable.hasModifier");

   public static Operation0<Exports, Package> EXPORTS_GET_PACKAGE = new Operation0<>("exports.getPackage");

   public static Operation0<Exports, List<? extends Module>> EXPORTS_GET_TARGET_MODULES = new Operation0<>("exports.getTargetModules");

   public static Operation0<Exports, Boolean> EXPORTS_TO_ALL = new Operation0<>("exports.toAll");

   public static Operation0<Opens, Package> OPENS_GET_PACKAGE = new Operation0<>("opens.getPackage");

   public static Operation0<Opens, List<? extends Module>> OPENS_GET_TARGET_MODULES = new Operation0<>("opens.getTargetModules");

   public static Operation0<Opens, Boolean> OPENS_TO_ALL = new Operation0<>("opens.toAll");

   public static Operation0<Provides, Declared> PROVIDES_GET_SERVICE = new Operation0<>("provides.getService");

   public static Operation0<Provides, List<? extends Declared>> PROVIDES_GET_IMPLEMENTATIONS = new Operation0<>("provides.getImplementations");

   public static Operation0<Requires, Boolean> REQUIRES_IS_STATIC = new Operation0<>("requires.isStatic");

   public static Operation0<Requires, Boolean> REQUIRES_IS_TRANSITIVE = new Operation0<>("requires.isTransitive");

   public static Operation0<Requires, Module> REQUIRES_GET_DEPENDENCY = new Operation0<>("requires.getDependency");

   public static Operation0<Uses, Declared> USES_GET_SERVICE = new Operation0<>("uses.getService");

   public static Operation0<Annotationable, List<? extends AnnotationUsage>> ANNOTATIONABLE_GET_ANNOTATION_USAGES = new Operation0<>("annotationable.getAnnotationUsages");

   public static Operation1<Annotationable, Annotation, List<? extends AnnotationUsage>> ANNOTATIONABLE_GET_USAGES_OF = new Operation1<>("annotationUsage.getUsagesOf");

   public static Operation1<Annotationable, Annotation, AnnotationUsage> ANNOTATIONABLE_GET_USAGE_OF = new Operation1<>("annotationUsage.getUsageOf");

   public static Operation1<Annotationable, Annotation, Boolean> ANNOTATIONABLE_IS_ANNOTATED_WITH = new Operation1<>("annotationable.isAnnotatedWith");

   public static Operation0<Annotationable, List<? extends AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES = new Operation0<>("annotationable.getDirectAnnotationUsages");

   public static Operation1<Annotationable, Annotation, List<? extends AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_USAGES_OF = new Operation1<>("annotationUsage.getDirectUsagesOf");

   public static Operation1<Annotationable, Annotation, AnnotationUsage> ANNOTATIONABLE_GET_DIRECT_USAGE_OF = new Operation1<>("annotationUsage.getDirectUsageOf");

   public static Operation1<Annotationable, Annotation, Boolean> ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH = new Operation1<>("annotationable.isDirectlyAnnotatedWith");

   public static Operation0<AnnotationValue, Boolean> ANNOTATION_VALUE_IS_DEFAULT = new Operation0<>("annotationValue.isDefault");

   public static Operation0<AnnotationValue, Object> ANNOTATION_VALUE_GET_VALUE = new Operation0<>("annotationValue.getValue");

   public static Operation0<Property, String> PROPERTY_GET_NAME = new Operation0<>("property.getName");

   public static Operation0<Property, Shadow> PROPERTY_GET_TYPE = new Operation0<>("property.getType");

   public static Operation0<Property, Field> PROPERTY_GET_FIELD = new Operation0<>("property.getField");

   public static Operation0<Property, Method> PROPERTY_GET_GETTER = new Operation0<>("property.getGetter");

   public static Operation0<Property, Method> PROPERTY_GET_SETTER = new Operation0<>("property.getSetter");

   public static Operation0<Property, Boolean> PROPERTY_IS_MUTABLE = new Operation0<>("property.isMutable");
}
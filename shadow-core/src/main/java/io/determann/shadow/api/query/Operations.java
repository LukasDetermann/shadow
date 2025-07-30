package io.determann.shadow.api.query;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.NestingKind;
import io.determann.shadow.api.query.operation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

//@formatter:off
public interface Operations
{
   //static
   ///ModuleName, PackageName
   StaticOperation2<String, String, C.Package> GET_PACKAGE_IN_MODULE = new StaticOperation2<>("getPackageInModule");

   StaticOperation1<String, List<? extends C.Package>> GET_PACKAGE = new StaticOperation1<>("getPackage");

   StaticOperation1<String, C.Declared> GET_DECLARED = new StaticOperation1<>("getDeclared");

   StaticOperation1<String, C.Annotation> GET_ANNOTATION = new StaticOperation1<>("getAnnotation");

   StaticOperation1<String, C.Class> GET_CLASS = new StaticOperation1<>("getClass");

   StaticOperation1<String, C.Enum> GET_ENUM = new StaticOperation1<>("getEnum");

   StaticOperation1<String, C.Interface> GET_INTERFACE = new StaticOperation1<>("getInterface");

   StaticOperation1<String, C.Record> GET_RECORD = new StaticOperation1<>("getRecord");

   StaticOperation1<String, C.Module> GET_MODULE = new StaticOperation1<>("getModule");

   StaticOperation0<C.Null> GET_NULL = new StaticOperation0<>("getNull");

   StaticOperation0<C.Void> GET_VOID = new StaticOperation0<>("getVoid");

   StaticOperation0<C.Primitive> GET_BOOLEAN = new StaticOperation0<>("getBoolean");

   StaticOperation0<C.Primitive> GET_BYTE = new StaticOperation0<>("getByte");

   StaticOperation0<C.Primitive> GET_SHORT = new StaticOperation0<>("getShort");

   StaticOperation0<C.Primitive> GET_INT = new StaticOperation0<>("getInt");

   StaticOperation0<C.Primitive> GET_LONG = new StaticOperation0<>("getLong");

   StaticOperation0<C.Primitive> GET_CHAR = new StaticOperation0<>("getChar");

   StaticOperation0<C.Primitive> GET_FLOAT = new StaticOperation0<>("getFloat");

   StaticOperation0<C.Primitive> GET_DOUBLE = new StaticOperation0<>("getDouble");

   //instance
   InstanceOperation1<C.Type, C.Type, Boolean> TYPE_REPRESENTS_SAME_TYPE = new InstanceOperation1<>("type.representsSameType");

   InstanceOperation0<C.Nameable, String> NAMEABLE_GET_NAME = new InstanceOperation0<>("nameable.getName");

   InstanceOperation0<C.QualifiedNameable, String> QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME = new InstanceOperation0<>("qualifiedNameable.getQualifiedName");

   InstanceOperation0<C.Wildcard, C.Type> WILDCARD_GET_EXTENDS = new InstanceOperation0<>("wildcard.getExtends");

   InstanceOperation0<C.Wildcard, C.Type> WILDCARD_GET_SUPER = new InstanceOperation0<>("wildcard.getSuper");

   InstanceOperation0<C.Primitive, C.Type> PRIMITIVE_AS_BOXED = new InstanceOperation0<>("primitive.asBoxed");

   InstanceOperation1<C.Primitive, C.Type, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new InstanceOperation1<>("primitive.isSubtypeOf");

   InstanceOperation1<C.Primitive, C.Type, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("primitive.isAssignableFrom");

   InstanceOperation0<C.Primitive, C.Array> PRIMITIVE_AS_ARRAY = new InstanceOperation0<>("primitive.asArray");

   InstanceOperation0<C.Package, Boolean> PACKAGE_IS_UNNAMED = new InstanceOperation0<>("package.isUnnamed");

   InstanceOperation1<C.Package, String, C.Declared> PACKAGE_GET_DECLARED = new InstanceOperation1<>("package.getDeclared");

   InstanceOperation0<C.Package, List<? extends C.Declared>> PACKAGE_GET_DECLARED_LIST = new InstanceOperation0<>("package.getDeclaredList");

   InstanceOperation0<C.ModuleEnclosed, C.Module> MODULE_ENCLOSED_GET_MODULE = new InstanceOperation0<>("moduleEnclosed.getModule");

   InstanceOperation1<C.Declared, C.Type, Boolean> DECLARED_IS_SUBTYPE_OF = new InstanceOperation1<>("declared.isSubtypeOf");

   InstanceOperation0<C.Declared, NestingKind> DECLARED_GET_NESTING = new InstanceOperation0<>("declared.getNesting");

   InstanceOperation0<C.Declared, List<? extends C.Field>> DECLARED_GET_FIELDS = new InstanceOperation0<>("declared.getFields");

   InstanceOperation1<C.Declared, String, C.Field> DECLARED_GET_FIELD = new InstanceOperation1<>("declared.getField");

   InstanceOperation0<C.Declared, List<? extends C.Method>> DECLARED_GET_METHODS = new InstanceOperation0<>("declared.getMethods");

   InstanceOperation1<C.Declared, String, List<? extends C.Method>> DECLARED_GET_METHOD = new InstanceOperation1<>("declared.getMethod");

   InstanceOperation0<C.Declared, List<? extends C.Declared>> DECLARED_GET_DIRECT_SUPER_TYPES = new InstanceOperation0<>("declared.getDirectSuperTypes");

   InstanceOperation0<C.Declared, Set<? extends C.Declared>> DECLARED_GET_SUPER_TYPES = new InstanceOperation0<>("declared.getSuperTypes");

   InstanceOperation0<C.Declared, List<? extends C.Interface>> DECLARED_GET_INTERFACES = new InstanceOperation0<>("declared.getInterfaces");

   InstanceOperation1<C.Declared, String, C.Interface> DECLARED_GET_INTERFACE = new InstanceOperation1<>("declared.getInterface");

   InstanceOperation0<C.Declared, List<? extends C.Interface>> DECLARED_GET_DIRECT_INTERFACES = new InstanceOperation0<>("declared.getDirectInterfaces");

   InstanceOperation1<C.Declared, String, C.Interface> DECLARED_GET_DIRECT_INTERFACE = new InstanceOperation1<>("declared.getDirectInterface");

   InstanceOperation0<C.Declared, C.Package> DECLARED_GET_PACKAGE = new InstanceOperation0<>("declared.getPackage");

   InstanceOperation0<C.Declared, String> DECLARED_GET_BINARY_NAME = new InstanceOperation0<>("declared.getBinaryName");

   InstanceOperation0<C.Declared, C.Array> DECLARED_AS_ARRAY = new InstanceOperation0<>("declared.asArray");

   InstanceOperation0<C.Enum, List<? extends C.Constructor>> ENUM_GET_CONSTRUCTORS = new InstanceOperation0<>("enum.getConstructors");

   InstanceOperation0<C.Enum, List<? extends C.EnumConstant>> ENUM_GET_ENUM_CONSTANTS = new InstanceOperation0<>("enum.getEumConstants");

   InstanceOperation1<C.Enum, String, C.EnumConstant> ENUM_GET_ENUM_CONSTANT = new InstanceOperation1<>("enum.getEnumConstant");

   InstanceOperation0<C.Interface, Boolean> INTERFACE_IS_FUNCTIONAL = new InstanceOperation0<>("Interface.isFunctional");

   InstanceOperation0<C.Interface, List<? extends C.Type>> INTERFACE_GET_GENERIC_TYPES = new InstanceOperation0<>("interface.getGenericTypes");

   InstanceOperation0<C.Interface, List<? extends C.Generic>> INTERFACE_GET_GENERICS = new InstanceOperation0<>("interface.getGenerics");

   InstanceOperation0<C.Interface, List<? extends C.Declared>> INTERFACE_GET_PERMITTED_SUB_TYPES = new InstanceOperation0<>("interface.getPermittedSubTypes");

   InstanceOperation0<C.Record, List<? extends C.Constructor>> RECORD_GET_CONSTRUCTORS = new InstanceOperation0<>("record.getConstructors");

   InstanceOperation0<C.Record, List<? extends C.RecordComponent>> RECORD_GET_RECORD_COMPONENTS = new InstanceOperation0<>("record.getRecordComponents");

   InstanceOperation1<C.Record, String, C.RecordComponent> RECORD_GET_RECORD_COMPONENT = new InstanceOperation1<>("record.getRecordComponent");

   InstanceOperation0<C.Record, List<? extends C.Type>> RECORD_GET_GENERIC_TYPES = new InstanceOperation0<>("record.getGenericTypes");

   InstanceOperation0<C.Record, List<? extends C.Generic>> RECORD_GET_GENERICS = new InstanceOperation0<>("record.getGenerics");

   InstanceOperation0<C.Class, List<? extends C.Constructor>> CLASS_GET_CONSTRUCTORS = new InstanceOperation0<>("class.getConstructors");

   InstanceOperation0<C.Class, C.Class> CLASS_GET_SUPER_CLASS = new InstanceOperation0<>("class.getSuperClass");

   InstanceOperation0<C.Class, List<? extends C.Class>> CLASS_GET_PERMITTED_SUB_CLASSES = new InstanceOperation0<>("class.getPermittedSubClasses");

   InstanceOperation0<C.Class, List<? extends C.Property>> CLASS_GET_PROPERTIES = new InstanceOperation0<>("class.getProperties");

   InstanceOperation1<C.Class, C.Type, Boolean> CLASS_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("class.isAssignableFrom");

   InstanceOperation0<C.Class, C.Declared> CLASS_GET_OUTER_TYPE = new InstanceOperation0<>("class.getOuterType");

   InstanceOperation0<C.Class, List<? extends C.Type>> CLASS_GET_GENERIC_TYPES = new InstanceOperation0<>("class.getGenericTypes");

   InstanceOperation0<C.Class, List<? extends C.Generic>> CLASS_GET_GENERICS = new InstanceOperation0<>("class.getGenerics");

   InstanceOperation0<C.Class, C.Primitive> CLASS_AS_UNBOXED = new InstanceOperation0<>("class.asUnboxed");

   InstanceOperation1<C.Array, C.Type, Boolean> ARRAY_IS_SUBTYPE_OF = new InstanceOperation1<>("array.isSubtypeOf");

   InstanceOperation0<C.Array, C.Type> ARRAY_GET_COMPONENT_TYPE = new InstanceOperation0<>("array.getComponentType");

   InstanceOperation0<C.Array, List<? extends C.Type>> ARRAY_GET_DIRECT_SUPER_TYPES = new InstanceOperation0<>("array.getDirectSuperTypes");

   InstanceOperation0<C.Array, C.Array> ARRAY_AS_ARRAY = new InstanceOperation0<>("array.asArray");

   InstanceOperation0<C.EnumConstant, C.Enum> ENUM_CONSTANT_GET_SURROUNDING = new InstanceOperation0<>("enumConstant.getSurrounding");

   InstanceOperation0<C.Field, Boolean> FIELD_IS_CONSTANT = new InstanceOperation0<>("field.isConstant");

   InstanceOperation0<C.Field, Object> FIELD_GET_CONSTANT_VALUE = new InstanceOperation0<>("field.getConstantValue");

   InstanceOperation0<C.Field, C.Declared> FIELD_GET_SURROUNDING = new InstanceOperation0<>("field.getSurrounding");

   InstanceOperation0<C.Parameter, Boolean> PARAMETER_IS_VAR_ARGS = new InstanceOperation0<>("parameter.isVarArgs");

   InstanceOperation0<C.Parameter, C.Executable> PARAMETER_GET_SURROUNDING = new InstanceOperation0<>("parameter.getSurrounding");

   InstanceOperation1<C.Variable, C.Type, Boolean> VARIABLE_IS_SUBTYPE_OF = new InstanceOperation1<>("variable.isSubtypeOf");

   InstanceOperation1<C.Variable, C.Type, Boolean> VARIABLE_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("variable.isAssignableFrom");

   InstanceOperation0<C.Variable, C.VariableType> VARIABLE_GET_TYPE = new InstanceOperation0<>("variable.getType");

   InstanceOperation0<C.Variable, Object> VARIABLE_GET_SURROUNDING = new InstanceOperation0<>("variable.getSurrounding");

   InstanceOperation0<C.Executable, List<? extends C.Parameter>> EXECUTABLE_GET_PARAMETERS = new InstanceOperation0<>("executable.getParameters");

   InstanceOperation1<C.Executable, String, C.Parameter> EXECUTABLE_GET_PARAMETER = new InstanceOperation1<>("executable.getParameter");

   InstanceOperation0<C.Executable, List<? extends C.Type>> EXECUTABLE_GET_PARAMETER_TYPES = new InstanceOperation0<>("executable.getParameterTypes");

   InstanceOperation0<C.Executable, List<? extends C.Class>> EXECUTABLE_GET_THROWS = new InstanceOperation0<>("executable.getThrows");

   InstanceOperation0<C.Executable, Boolean> EXECUTABLE_IS_VAR_ARGS = new InstanceOperation0<>("executable.isVarArgs");

   InstanceOperation0<C.Executable, C.Declared> EXECUTABLE_GET_SURROUNDING = new InstanceOperation0<>("executable.getSurrounding");

   InstanceOperation0<C.Executable, List<? extends C.Generic>> EXECUTABLE_GET_GENERICS = new InstanceOperation0<>("executable.getGenerics");

   InstanceOperation0<C.Executable, C.Declared> EXECUTABLE_GET_RECEIVER_TYPE = new InstanceOperation0<>("executable.getReceiverType");

   InstanceOperation0<C.Executable, C.Receiver> EXECUTABLE_GET_RECEIVER = new InstanceOperation0<>("executable.getReceiver");

   InstanceOperation0<C.Method, C.Type> METHOD_GET_RETURN_TYPE = new InstanceOperation0<>("method.getReturnType");

   InstanceOperation1<C.Method, C.Method, Boolean> METHOD_OVERRIDES = new InstanceOperation1<>("method.overrides");

   InstanceOperation1<C.Method, C.Method, Boolean> METHOD_OVERWRITTEN_BY = new InstanceOperation1<>("method.overwrittenBy");

   InstanceOperation1<C.Method, C.Method, Boolean> METHOD_SAME_PARAMETER_TYPES = new InstanceOperation1<>("method.sameParameterTypes");

   InstanceOperation0<C.Method, Boolean> METHOD_IS_BRIDGE = new InstanceOperation0<>("method.isBridge");

   InstanceOperation0<C.Method, C.Result> METHOD_GET_RESULT = new InstanceOperation0<>("method.getResult");

   InstanceOperation1<C.RecordComponent, C.Type, Boolean> RECORD_COMPONENT_IS_SUBTYPE_OF = new InstanceOperation1<>("recordComponent.isSubtypeOf");

   InstanceOperation1<C.RecordComponent, C.Type, Boolean> RECORD_COMPONENT_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("recordComponent.isAssignableFrom");

   InstanceOperation0<C.RecordComponent, C.Record> RECORD_COMPONENT_GET_RECORD = new InstanceOperation0<>("RecordComponent.getRecord");

   InstanceOperation0<C.RecordComponent, C.Type> RECORD_COMPONENT_GET_TYPE = new InstanceOperation0<>("recordComponent.getType");

   InstanceOperation0<C.RecordComponent, C.Method> RECORD_COMPONENT_GET_GETTER = new InstanceOperation0<>("recordComponent.getGetter");

   InstanceOperation0<C.Result, C.Type> RESULT_GET_TYPE = new InstanceOperation0<>("result.getType");

   InstanceOperation0<C.Receiver, C.Type> RECEIVER_GET_TYPE = new InstanceOperation0<>("receiver.getType");

   InstanceOperation0<C.Generic, C.Type> GENERIC_GET_BOUND = new InstanceOperation0<>("generic.getBound");

   InstanceOperation0<C.Generic, List<? extends C.Type>> GENERIC_GET_BOUNDS = new InstanceOperation0<>("generic.getBounds");

   InstanceOperation0<C.Generic, List<? extends C.Interface>> GENERIC_GET_ADDITIONAL_BOUNDS = new InstanceOperation0<>("generic.getAdditionalBounds");

   InstanceOperation0<C.Generic, Object> GENERIC_GET_ENCLOSING = new InstanceOperation0<>("generic.getEnclosing");

   InstanceOperation0<C.AnnotationUsage, Map<? extends C.Method, ? extends C.AnnotationValue>> ANNOTATION_USAGE_GET_VALUES = new InstanceOperation0<>("annotationUsage.getValues");

   InstanceOperation1<C.AnnotationUsage, String, C.AnnotationValue> ANNOTATION_USAGE_GET_VALUE = new InstanceOperation1<>("annotationUsage.getValue");

   InstanceOperation0<C.AnnotationUsage, C.Annotation> ANNOTATION_USAGE_GET_ANNOTATION = new InstanceOperation0<>("annotationUsage.getAnnotation");

   InstanceOperation0<C.Module, List<? extends C.Package>> MODULE_GET_PACKAGES = new InstanceOperation0<>("module.getPackages");

   InstanceOperation0<C.Module, Boolean> MODULE_IS_OPEN = new InstanceOperation0<>("module.isOpen");

   InstanceOperation0<C.Module, Boolean> MODULE_IS_UNNAMED = new InstanceOperation0<>("module.isUnnamed");

   InstanceOperation0<C.Module, Boolean> MODULE_IS_AUTOMATIC = new InstanceOperation0<>("module.isAutomatic");

   InstanceOperation0<C.Module, List<? extends C.Directive>> MODULE_GET_DIRECTIVES = new InstanceOperation0<>("module.getDirectives");

   InstanceOperation1<C.Module, String, C.Declared> MODULE_GET_DECLARED = new InstanceOperation1<>("module.getDeclared");

   InstanceOperation0<C.Module, List<? extends C.Declared>> MODULE_GET_DECLARED_LIST = new InstanceOperation0<>("module.getDeclaredList");

   InstanceOperation0<C.Modifiable, Set<Modifier>> MODIFIABLE_GET_MODIFIERS = new InstanceOperation0<>("modifiable.getModifiers");

   InstanceOperation1<C.Modifiable, Modifier, Boolean> MODIFIABLE_HAS_MODIFIER = new InstanceOperation1<>("modifiable.hasModifier");

   InstanceOperation0<C.Exports, C.Package> EXPORTS_GET_PACKAGE = new InstanceOperation0<>("exports.getPackage");

   InstanceOperation0<C.Exports, List<? extends C.Module>> EXPORTS_GET_TARGET_MODULES = new InstanceOperation0<>("exports.getTargetModules");

   InstanceOperation0<C.Exports, Boolean> EXPORTS_TO_ALL = new InstanceOperation0<>("exports.toAll");

   InstanceOperation0<C.Opens, C.Package> OPENS_GET_PACKAGE = new InstanceOperation0<>("opens.getPackage");

   InstanceOperation0<C.Opens, List<? extends C.Module>> OPENS_GET_TARGET_MODULES = new InstanceOperation0<>("opens.getTargetModules");

   InstanceOperation0<C.Opens, Boolean> OPENS_TO_ALL = new InstanceOperation0<>("opens.toAll");

   InstanceOperation0<C.Provides, C.Declared> PROVIDES_GET_SERVICE = new InstanceOperation0<>("provides.getService");

   InstanceOperation0<C.Provides, List<? extends C.Declared>> PROVIDES_GET_IMPLEMENTATIONS = new InstanceOperation0<>("provides.getImplementations");

   InstanceOperation0<C.Requires, Boolean> REQUIRES_IS_STATIC = new InstanceOperation0<>("requires.isStatic");

   InstanceOperation0<C.Requires, Boolean> REQUIRES_IS_TRANSITIVE = new InstanceOperation0<>("requires.isTransitive");

   InstanceOperation0<C.Requires, C.Module> REQUIRES_GET_DEPENDENCY = new InstanceOperation0<>("requires.getDependency");

   InstanceOperation0<C.Uses, C.Declared> USES_GET_SERVICE = new InstanceOperation0<>("uses.getService");

   InstanceOperation0<C.Annotationable, List<? extends C.AnnotationUsage>> ANNOTATIONABLE_GET_ANNOTATION_USAGES = new InstanceOperation0<>("annotationable.getAnnotationUsages");

   InstanceOperation1<C.Annotationable, C.Annotation, List<? extends C.AnnotationUsage>> ANNOTATIONABLE_GET_USAGES_OF = new InstanceOperation1<>("annotationUsage.getUsagesOf");

   InstanceOperation1<C.Annotationable, C.Annotation, C.AnnotationUsage> ANNOTATIONABLE_GET_USAGE_OF = new InstanceOperation1<>("annotationUsage.getUsageOf");

   InstanceOperation1<C.Annotationable, C.Annotation, Boolean> ANNOTATIONABLE_IS_ANNOTATED_WITH = new InstanceOperation1<>("annotationable.isAnnotatedWith");

   InstanceOperation0<C.Annotationable, List<? extends C.AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES = new InstanceOperation0<>("annotationable.getDirectAnnotationUsages");

   InstanceOperation1<C.Annotationable, C.Annotation, List<? extends C.AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_USAGES_OF = new InstanceOperation1<>("annotationUsage.getDirectUsagesOf");

   InstanceOperation1<C.Annotationable, C.Annotation, C.AnnotationUsage> ANNOTATIONABLE_GET_DIRECT_USAGE_OF = new InstanceOperation1<>("annotationUsage.getDirectUsageOf");

   InstanceOperation1<C.Annotationable, C.Annotation, Boolean>  ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH = new InstanceOperation1<>("annotationable.isDirectlyAnnotatedWith");

   InstanceOperation0<C.AnnotationValue, Boolean> ANNOTATION_VALUE_IS_DEFAULT = new InstanceOperation0<>("annotationValue.isDefault");

   InstanceOperation0<C.AnnotationValue, Object> ANNOTATION_VALUE_GET_VALUE = new InstanceOperation0<>("annotationValue.getValue");

   InstanceOperation0<C.Property, C.VariableType> PROPERTY_GET_TYPE = new InstanceOperation0<>("property.getType");

   InstanceOperation0<C.Property, C.Field> PROPERTY_GET_FIELD = new InstanceOperation0<>("property.getField");

   InstanceOperation0<C.Property, C.Method> PROPERTY_GET_GETTER = new InstanceOperation0<>("property.getGetter");

   InstanceOperation0<C.Property, C.Method> PROPERTY_GET_SETTER = new InstanceOperation0<>("property.getSetter");

   InstanceOperation0<C.Property, Boolean> PROPERTY_IS_MUTABLE = new InstanceOperation0<>("property.isMutable");

   InstanceOperation0<C.Erasable, C.Erasable> ERASABLE_ERASURE = new InstanceOperation0<>("erasable.erasure");
}
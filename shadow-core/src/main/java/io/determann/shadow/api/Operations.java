package io.determann.shadow.api;

import io.determann.shadow.api.operation.*;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.modifier.C_Modifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.List;
import java.util.Map;
import java.util.Set;

//@formatter:off
public interface Operations
{
   //static
   ///ModuleName, PackageName
   StaticOperation2<String, String, C_Package> GET_PACKAGE_IN_MODULE = new StaticOperation2<>("getPackageInModule");

   StaticOperation1<String, List<? extends C_Package>> GET_PACKAGE = new StaticOperation1<>("getPackage");

   StaticOperation1<String, C_Declared> GET_DECLARED = new StaticOperation1<>("getDeclared");

   StaticOperation1<String, C_Annotation> GET_ANNOTATION = new StaticOperation1<>("getAnnotation");

   StaticOperation1<String, C_Class> GET_CLASS = new StaticOperation1<>("getClass");

   StaticOperation1<String, C_Enum> GET_ENUM = new StaticOperation1<>("getEnum");

   StaticOperation1<String, C_Interface> GET_INTERFACE = new StaticOperation1<>("getInterface");

   StaticOperation1<String, C_Record> GET_RECORD = new StaticOperation1<>("getRecord");

   StaticOperation1<String, C_Module> GET_MODULE = new StaticOperation1<>("getModule");

   StaticOperation0<C_Null> GET_NULL = new StaticOperation0<>("getNull");

   StaticOperation0<C_Void> GET_VOID = new StaticOperation0<>("getVoid");

   StaticOperation0<C_Primitive> GET_BOOLEAN = new StaticOperation0<>("getBoolean");

   StaticOperation0<C_Primitive> GET_BYTE = new StaticOperation0<>("getByte");

   StaticOperation0<C_Primitive> GET_SHORT = new StaticOperation0<>("getShort");

   StaticOperation0<C_Primitive> GET_INT = new StaticOperation0<>("getInt");

   StaticOperation0<C_Primitive> GET_LONG = new StaticOperation0<>("getLong");

   StaticOperation0<C_Primitive> GET_CHAR = new StaticOperation0<>("getChar");

   StaticOperation0<C_Primitive> GET_FLOAT = new StaticOperation0<>("getFloat");

   StaticOperation0<C_Primitive> GET_DOUBLE = new StaticOperation0<>("getDouble");

   //instance
   InstanceOperation1<C_Type, C_Type, Boolean> TYPE_REPRESENTS_SAME_TYPE = new InstanceOperation1<>("type.representsSameType");

   InstanceOperation0<C_Nameable, String> NAMEABLE_GET_NAME = new InstanceOperation0<>("nameable.getName");

   InstanceOperation0<C_QualifiedNameable, String> QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME = new InstanceOperation0<>("qualifiedNameable.getQualifiedName");

   InstanceOperation0<C_Wildcard, C_Type> WILDCARD_GET_EXTENDS = new InstanceOperation0<>("wildcard.getExtends");

   InstanceOperation0<C_Wildcard, C_Type> WILDCARD_GET_SUPER = new InstanceOperation0<>("wildcard.getSuper");

   InstanceOperation0<C_Primitive, C_Type> PRIMITIVE_AS_BOXED = new InstanceOperation0<>("primitive.asBoxed");

   InstanceOperation1<C_Primitive, C_Type, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new InstanceOperation1<>("primitive.isSubtypeOf");

   InstanceOperation1<C_Primitive, C_Type, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("primitive.isAssignableFrom");

   InstanceOperation0<C_Primitive, C_Array> PRIMITIVE_AS_ARRAY = new InstanceOperation0<>("primitive.asArray");

   InstanceOperation0<C_Package, Boolean> PACKAGE_IS_UNNAMED = new InstanceOperation0<>("package.isUnnamed");

   InstanceOperation1<C_Package, String, C_Declared> PACKAGE_GET_DECLARED = new InstanceOperation1<>("package.getDeclared");

   InstanceOperation0<C_Package, List<? extends C_Declared>> PACKAGE_GET_DECLARED_LIST = new InstanceOperation0<>("package.getDeclaredList");

   InstanceOperation0<C_ModuleEnclosed, C_Module> MODULE_ENCLOSED_GET_MODULE = new InstanceOperation0<>("moduleEnclosed.getModule");

   InstanceOperation1<C_Declared, C_Type, Boolean> DECLARED_IS_SUBTYPE_OF = new InstanceOperation1<>("declared.isSubtypeOf");

   InstanceOperation0<C_Declared, C_NestingKind> DECLARED_GET_NESTING = new InstanceOperation0<>("declared.getNesting");

   InstanceOperation0<C_Declared, List<? extends C_Field>> DECLARED_GET_FIELDS = new InstanceOperation0<>("declared.getFields");

   InstanceOperation1<C_Declared, String, C_Field> DECLARED_GET_FIELD = new InstanceOperation1<>("declared.getField");

   InstanceOperation0<C_Declared, List<? extends C_Method>> DECLARED_GET_METHODS = new InstanceOperation0<>("declared.getMethods");

   InstanceOperation1<C_Declared, String, List<? extends C_Method>> DECLARED_GET_METHOD = new InstanceOperation1<>("declared.getMethod");

   InstanceOperation0<C_Declared, List<? extends C_Declared>> DECLARED_GET_DIRECT_SUPER_TYPES = new InstanceOperation0<>("declared.getDirectSuperTypes");

   InstanceOperation0<C_Declared, Set<? extends C_Declared>> DECLARED_GET_SUPER_TYPES = new InstanceOperation0<>("declared.getSuperTypes");

   InstanceOperation0<C_Declared, List<? extends C_Interface>> DECLARED_GET_INTERFACES = new InstanceOperation0<>("declared.getInterfaces");

   InstanceOperation1<C_Declared, String, C_Interface> DECLARED_GET_INTERFACE = new InstanceOperation1<>("declared.getInterface");

   InstanceOperation0<C_Declared, List<? extends C_Interface>> DECLARED_GET_DIRECT_INTERFACES = new InstanceOperation0<>("declared.getDirectInterfaces");

   InstanceOperation1<C_Declared, String, C_Interface> DECLARED_GET_DIRECT_INTERFACE = new InstanceOperation1<>("declared.getDirectInterface");

   InstanceOperation0<C_Declared, C_Package> DECLARED_GET_PACKAGE = new InstanceOperation0<>("declared.getPackage");

   InstanceOperation0<C_Declared, String> DECLARED_GET_BINARY_NAME = new InstanceOperation0<>("declared.getBinaryName");

   InstanceOperation0<C_Declared, C_Array> DECLARED_AS_ARRAY = new InstanceOperation0<>("declared.asArray");

   InstanceOperation0<C_Enum, List<? extends C_Constructor>> ENUM_GET_CONSTRUCTORS = new InstanceOperation0<>("enum.getConstructors");

   InstanceOperation0<C_Enum, List<? extends C_EnumConstant>> ENUM_GET_EUM_CONSTANTS = new InstanceOperation0<>("enum.getEumConstants");

   InstanceOperation1<C_Enum, String, C_EnumConstant> ENUM_GET_ENUM_CONSTANT = new InstanceOperation1<>("enum.getEnumConstant");

   InstanceOperation0<C_Interface, Boolean> INTERFACE_IS_FUNCTIONAL = new InstanceOperation0<>("Interface.isFunctional");

   InstanceOperation0<C_Interface, List<? extends C_Type>> INTERFACE_GET_GENERIC_TYPES = new InstanceOperation0<>("interface.getGenericTypes");

   InstanceOperation0<C_Interface, List<? extends C_Generic>> INTERFACE_GET_GENERICS = new InstanceOperation0<>("interface.getGenerics");

   InstanceOperation0<C_Record, List<? extends C_Constructor>> RECORD_GET_CONSTRUCTORS = new InstanceOperation0<>("record.getConstructors");

   InstanceOperation0<C_Record, List<? extends C_RecordComponent>> RECORD_GET_RECORD_COMPONENTS = new InstanceOperation0<>("record.getRecordComponents");

   InstanceOperation1<C_Record, String, C_RecordComponent> RECORD_GET_RECORD_COMPONENT = new InstanceOperation1<>("record.getRecordComponent");

   InstanceOperation0<C_Record, List<? extends C_Type>> RECORD_GET_GENERIC_TYPES = new InstanceOperation0<>("record.getGenericTypes");

   InstanceOperation0<C_Record, List<? extends C_Generic>> RECORD_GET_GENERICS = new InstanceOperation0<>("record.getGenerics");

   InstanceOperation0<C_Class, List<? extends C_Constructor>> CLASS_GET_CONSTRUCTORS = new InstanceOperation0<>("class.getConstructors");

   InstanceOperation0<C_Class, C_Class> CLASS_GET_SUPER_CLASS = new InstanceOperation0<>("class.getSuperClass");

   InstanceOperation0<C_Class, List<? extends C_Class>> CLASS_GET_PERMITTED_SUB_CLASSES = new InstanceOperation0<>("class.getPermittedSubClasses");

   InstanceOperation0<C_Class, List<? extends C_Property>> CLASS_GET_PROPERTIES = new InstanceOperation0<>("class.getProperties");

   InstanceOperation1<C_Class, C_Type, Boolean> CLASS_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("class.isAssignableFrom");

   InstanceOperation0<C_Class, C_Declared> CLASS_GET_OUTER_TYPE = new InstanceOperation0<>("class.getOuterType");

   InstanceOperation0<C_Class, List<? extends C_Type>> CLASS_GET_GENERIC_TYPES = new InstanceOperation0<>("class.getGenericTypes");

   InstanceOperation0<C_Class, List<? extends C_Generic>> CLASS_GET_GENERICS = new InstanceOperation0<>("class.getGenerics");

   InstanceOperation0<C_Class, C_Primitive> CLASS_AS_UNBOXED = new InstanceOperation0<>("class.asUnboxed");

   InstanceOperation1<C_Array, C_Type, Boolean> ARRAY_IS_SUBTYPE_OF = new InstanceOperation1<>("array.isSubtypeOf");

   InstanceOperation0<C_Array, C_Type> ARRAY_GET_COMPONENT_TYPE = new InstanceOperation0<>("array.getComponentType");

   InstanceOperation0<C_Array, List<? extends C_Type>> ARRAY_GET_DIRECT_SUPER_TYPES = new InstanceOperation0<>("array.getDirectSuperTypes");

   InstanceOperation0<C_Array, C_Array> ARRAY_AS_ARRAY = new InstanceOperation0<>("array.asArray");

   InstanceOperation0<C_EnumConstant, C_Enum> ENUM_CONSTANT_GET_SURROUNDING = new InstanceOperation0<>("enumConstant.getSurrounding");

   InstanceOperation0<C_Field, Boolean> FIELD_IS_CONSTANT = new InstanceOperation0<>("field.isConstant");

   InstanceOperation0<C_Field, Object> FIELD_GET_CONSTANT_VALUE = new InstanceOperation0<>("field.getConstantValue");

   InstanceOperation0<C_Field, C_Declared> FIELD_GET_SURROUNDING = new InstanceOperation0<>("field.getSurrounding");

   InstanceOperation0<C_Parameter, Boolean> PARAMETER_IS_VAR_ARGS = new InstanceOperation0<>("parameter.isVarArgs");

   InstanceOperation0<C_Parameter, C_Executable> PARAMETER_GET_SURROUNDING = new InstanceOperation0<>("parameter.getSurrounding");

   InstanceOperation1<C_Variable, C_Type, Boolean> VARIABLE_IS_SUBTYPE_OF = new InstanceOperation1<>("variable.isSubtypeOf");

   InstanceOperation1<C_Variable, C_Type, Boolean> VARIABLE_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("variable.isAssignableFrom");

   InstanceOperation0<C_Variable, C_Type> VARIABLE_GET_TYPE = new InstanceOperation0<>("variable.getType");

   InstanceOperation0<C_Variable, Object> VARIABLE_GET_SURROUNDING = new InstanceOperation0<>("variable.getSurrounding");

   InstanceOperation0<C_Executable, List<? extends C_Parameter>> EXECUTABLE_GET_PARAMETERS = new InstanceOperation0<>("executable.getParameters");

   InstanceOperation1<C_Executable, String, C_Parameter> EXECUTABLE_GET_PARAMETER = new InstanceOperation1<>("executable.getParameter");

   InstanceOperation0<C_Executable, List<? extends C_Type>> EXECUTABLE_GET_PARAMETER_TYPES = new InstanceOperation0<>("executable.getParameterTypes");

   InstanceOperation0<C_Executable, List<? extends C_Class>> EXECUTABLE_GET_THROWS = new InstanceOperation0<>("executable.getThrows");

   InstanceOperation0<C_Executable, Boolean> EXECUTABLE_IS_VAR_ARGS = new InstanceOperation0<>("executable.isVarArgs");

   InstanceOperation0<C_Executable, C_Declared> EXECUTABLE_GET_SURROUNDING = new InstanceOperation0<>("executable.getSurrounding");

   InstanceOperation0<C_Executable, List<? extends C_Generic>> EXECUTABLE_GET_GENERICS = new InstanceOperation0<>("executable.getGenerics");

   InstanceOperation0<C_Executable, C_Declared> EXECUTABLE_GET_RECEIVER_TYPE = new InstanceOperation0<>("executable.getReceiverType");

   InstanceOperation0<C_Executable, C_Receiver> EXECUTABLE_GET_RECEIVER = new InstanceOperation0<>("executable.getReceiver");

   InstanceOperation0<C_Method, C_Type> METHOD_GET_RETURN_TYPE = new InstanceOperation0<>("method.getReturnType");

   InstanceOperation1<C_Method, C_Method, Boolean> METHOD_OVERRIDES = new InstanceOperation1<>("method.overrides");

   InstanceOperation1<C_Method, C_Method, Boolean> METHOD_OVERWRITTEN_BY = new InstanceOperation1<>("method.overwrittenBy");

   InstanceOperation1<C_Method, C_Method, Boolean> METHOD_SAME_PARAMETER_TYPES = new InstanceOperation1<>("method.sameParameterTypes");

   InstanceOperation0<C_Method, Boolean> METHOD_IS_BRIDGE = new InstanceOperation0<>("method.isBridge");

   InstanceOperation0<C_Method, C_Result> METHOD_GET_RETURN = new InstanceOperation0<>("method.getReturn");

   InstanceOperation1<C_RecordComponent, C_Type, Boolean> RECORD_COMPONENT_IS_SUBTYPE_OF = new InstanceOperation1<>("recordComponent.isSubtypeOf");

   InstanceOperation1<C_RecordComponent, C_Type, Boolean> RECORD_COMPONENT_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("recordComponent.isAssignableFrom");

   InstanceOperation0<C_RecordComponent, C_Record> RECORD_COMPONENT_GET_RECORD = new InstanceOperation0<>("RecordComponent.getRecord");

   InstanceOperation0<C_RecordComponent, C_Type> RECORD_COMPONENT_GET_TYPE = new InstanceOperation0<>("recordComponent.getType");

   InstanceOperation0<C_RecordComponent, C_Method> RECORD_COMPONENT_GET_GETTER = new InstanceOperation0<>("recordComponent.getGetter");

   InstanceOperation0<C_Result, C_Type> RETURN_GET_TYPE = new InstanceOperation0<>("return.getType");

   InstanceOperation0<C_Receiver, C_Type> RECEIVER_GET_TYPE = new InstanceOperation0<>("receiver.getType");

   InstanceOperation0<C_Intersection, List<? extends C_Type>> INTERSECTION_GET_BOUNDS = new InstanceOperation0<>("intersection.getBounds");

   InstanceOperation0<C_Intersection, C_Array> INTERSECTION_AS_ARRAY = new InstanceOperation0<>("intersection.asArray");

   InstanceOperation0<C_Generic, C_Type> GENERIC_GET_EXTENDS = new InstanceOperation0<>("generic.getExtends");

   InstanceOperation0<C_Generic, C_Type> GENERIC_GET_SUPER = new InstanceOperation0<>("generic.getSuper");

   InstanceOperation0<C_Generic, Object> GENERIC_GET_ENCLOSING = new InstanceOperation0<>("generic.getEnclosing");

   InstanceOperation0<C_AnnotationUsage, Map<? extends C_Method, ? extends C_AnnotationValue>> ANNOTATION_USAGE_GET_VALUES = new InstanceOperation0<>("annotationUsage.getValues");

   InstanceOperation1<C_AnnotationUsage, String, C_AnnotationValue> ANNOTATION_USAGE_GET_VALUE = new InstanceOperation1<>("annotationUsage.getValue");

   InstanceOperation0<C_AnnotationUsage, C_Annotation> ANNOTATION_USAGE_GET_ANNOTATION = new InstanceOperation0<>("annotationUsage.getAnnotation");

   InstanceOperation0<C_Module, List<? extends C_Package>> MODULE_GET_PACKAGES = new InstanceOperation0<>("module.getPackages");

   InstanceOperation0<C_Module, Boolean> MODULE_IS_OPEN = new InstanceOperation0<>("module.isOpen");

   InstanceOperation0<C_Module, Boolean> MODULE_IS_UNNAMED = new InstanceOperation0<>("module.isUnnamed");

   InstanceOperation0<C_Module, Boolean> MODULE_IS_AUTOMATIC = new InstanceOperation0<>("module.isAutomatic");

   InstanceOperation0<C_Module, List<? extends C_Directive>> MODULE_GET_DIRECTIVES = new InstanceOperation0<>("module.getDirectives");

   InstanceOperation1<C_Module, String, C_Declared> MODULE_GET_DECLARED = new InstanceOperation1<>("module.getDeclared");

   InstanceOperation0<C_Module, List<? extends C_Declared>> MODULE_GET_DECLARED_LIST = new InstanceOperation0<>("module.getDeclaredList");

   InstanceOperation0<C_Modifiable, Set<C_Modifier>> MODIFIABLE_GET_MODIFIERS = new InstanceOperation0<>("modifiable.getModifiers");

   InstanceOperation1<C_Modifiable, C_Modifier, Boolean> MODIFIABLE_HAS_MODIFIER = new InstanceOperation1<>("modifiable.hasModifier");

   InstanceOperation0<C_Exports, C_Package> EXPORTS_GET_PACKAGE = new InstanceOperation0<>("exports.getPackage");

   InstanceOperation0<C_Exports, List<? extends C_Module>> EXPORTS_GET_TARGET_MODULES = new InstanceOperation0<>("exports.getTargetModules");

   InstanceOperation0<C_Exports, Boolean> EXPORTS_TO_ALL = new InstanceOperation0<>("exports.toAll");

   InstanceOperation0<C_Opens, C_Package> OPENS_GET_PACKAGE = new InstanceOperation0<>("opens.getPackage");

   InstanceOperation0<C_Opens, List<? extends C_Module>> OPENS_GET_TARGET_MODULES = new InstanceOperation0<>("opens.getTargetModules");

   InstanceOperation0<C_Opens, Boolean> OPENS_TO_ALL = new InstanceOperation0<>("opens.toAll");

   InstanceOperation0<C_Provides, C_Declared> PROVIDES_GET_SERVICE = new InstanceOperation0<>("provides.getService");

   InstanceOperation0<C_Provides, List<? extends C_Declared>> PROVIDES_GET_IMPLEMENTATIONS = new InstanceOperation0<>("provides.getImplementations");

   InstanceOperation0<C_Requires, Boolean> REQUIRES_IS_STATIC = new InstanceOperation0<>("requires.isStatic");

   InstanceOperation0<C_Requires, Boolean> REQUIRES_IS_TRANSITIVE = new InstanceOperation0<>("requires.isTransitive");

   InstanceOperation0<C_Requires, C_Module> REQUIRES_GET_DEPENDENCY = new InstanceOperation0<>("requires.getDependency");

   InstanceOperation0<C_Uses, C_Declared> USES_GET_SERVICE = new InstanceOperation0<>("uses.getService");

   InstanceOperation0<C_Annotationable, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_ANNOTATION_USAGES = new InstanceOperation0<>("annotationable.getAnnotationUsages");

   InstanceOperation1<C_Annotationable, C_Annotation, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_USAGES_OF = new InstanceOperation1<>("annotationUsage.getUsagesOf");

   InstanceOperation1<C_Annotationable, C_Annotation, C_AnnotationUsage> ANNOTATIONABLE_GET_USAGE_OF = new InstanceOperation1<>("annotationUsage.getUsageOf");

   InstanceOperation1<C_Annotationable, C_Annotation, Boolean> ANNOTATIONABLE_IS_ANNOTATED_WITH = new InstanceOperation1<>("annotationable.isAnnotatedWith");

   InstanceOperation0<C_Annotationable, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES = new InstanceOperation0<>("annotationable.getDirectAnnotationUsages");

   InstanceOperation1<C_Annotationable, C_Annotation, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_USAGES_OF = new InstanceOperation1<>("annotationUsage.getDirectUsagesOf");

   InstanceOperation1<C_Annotationable, C_Annotation, C_AnnotationUsage> ANNOTATIONABLE_GET_DIRECT_USAGE_OF = new InstanceOperation1<>("annotationUsage.getDirectUsageOf");

   InstanceOperation1<C_Annotationable, C_Annotation, Boolean>  ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH = new InstanceOperation1<>("annotationable.isDirectlyAnnotatedWith");

   InstanceOperation0<C_AnnotationValue, Boolean> ANNOTATION_VALUE_IS_DEFAULT = new InstanceOperation0<>("annotationValue.isDefault");

   InstanceOperation0<C_AnnotationValue, Object> ANNOTATION_VALUE_GET_VALUE = new InstanceOperation0<>("annotationValue.getValue");

   InstanceOperation0<C_Property, C_Type> PROPERTY_GET_TYPE = new InstanceOperation0<>("property.getType");

   InstanceOperation0<C_Property, C_Field> PROPERTY_GET_FIELD = new InstanceOperation0<>("property.getField");

   InstanceOperation0<C_Property, C_Method> PROPERTY_GET_GETTER = new InstanceOperation0<>("property.getGetter");

   InstanceOperation0<C_Property, C_Method> PROPERTY_GET_SETTER = new InstanceOperation0<>("property.getSetter");

   InstanceOperation0<C_Property, Boolean> PROPERTY_IS_MUTABLE = new InstanceOperation0<>("property.isMutable");

   InstanceOperation0<C_Erasable, C_Erasable> ERASABLE_ERASURE = new InstanceOperation0<>("erasable.erasure");
}
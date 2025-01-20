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
   public static StaticOperation2<String, String, C_Package> GET_PACKAGE_IN_MODULE = new StaticOperation2<>("getPackageInModule");

   public static StaticOperation1<String, List<? extends C_Package>> GET_PACKAGE = new StaticOperation1<>("getPackage");

   public static StaticOperation1<String, C_Declared> GET_DECLARED = new StaticOperation1<>("getDeclared");

   public static StaticOperation1<String, C_Annotation> GET_ANNOTATION = new StaticOperation1<>("getAnnotation");

   public static StaticOperation1<String, C_Class> GET_CLASS = new StaticOperation1<>("getClass");

   public static StaticOperation1<String, C_Enum> GET_ENUM = new StaticOperation1<>("getEnum");

   public static StaticOperation1<String, C_Interface> GET_INTERFACE = new StaticOperation1<>("getInterface");

   public static StaticOperation1<String, C_Record> GET_RECORD = new StaticOperation1<>("getRecord");

   public static StaticOperation1<String, C_Module> GET_MODULE = new StaticOperation1<>("getModule");

   public static StaticOperation0<C_Null> GET_NULL = new StaticOperation0<>("getNull");

   public static StaticOperation0<C_Void> GET_VOID = new StaticOperation0<>("getVoid");

   public static StaticOperation0<C_Primitive> GET_BOOLEAN = new StaticOperation0<>("getBoolean");

   public static StaticOperation0<C_Primitive> GET_BYTE = new StaticOperation0<>("getByte");

   public static StaticOperation0<C_Primitive> GET_SHORT = new StaticOperation0<>("getShort");

   public static StaticOperation0<C_Primitive> GET_INT = new StaticOperation0<>("getInt");

   public static StaticOperation0<C_Primitive> GET_LONG = new StaticOperation0<>("getLong");

   public static StaticOperation0<C_Primitive> GET_CHAR = new StaticOperation0<>("getChar");

   public static StaticOperation0<C_Primitive> GET_FLOAT = new StaticOperation0<>("getFloat");

   public static StaticOperation0<C_Primitive> GET_DOUBLE = new StaticOperation0<>("getDouble");

   //instance
   public static InstanceOperation1<C_Type, C_Type, Boolean> TYPE_REPRESENTS_SAME_TYPE = new InstanceOperation1<>("type.representsSameType");

   public static InstanceOperation0<C_Nameable, String> NAMEABLE_GET_NAME = new InstanceOperation0<>("nameable.getName");

   public static InstanceOperation0<C_QualifiedNameable, String> QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME = new InstanceOperation0<>("qualifiedNameable.getQualifiedName");

   public static InstanceOperation0<C_Wildcard, C_Type> WILDCARD_GET_EXTENDS = new InstanceOperation0<>("wildcard.getExtends");

   public static InstanceOperation0<C_Wildcard, C_Type> WILDCARD_GET_SUPER = new InstanceOperation0<>("wildcard.getSuper");

   public static InstanceOperation0<C_Primitive, C_Type> PRIMITIVE_AS_BOXED = new InstanceOperation0<>("primitive.asBoxed");

   public static InstanceOperation1<C_Primitive, C_Type, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new InstanceOperation1<>("primitive.isSubtypeOf");

   public static InstanceOperation1<C_Primitive, C_Type, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("primitive.isAssignableFrom");

   public static InstanceOperation0<C_Primitive, C_Array> PRIMITIVE_AS_ARRAY = new InstanceOperation0<>("primitive.asArray");

   public static InstanceOperation0<C_Package, Boolean> PACKAGE_IS_UNNAMED = new InstanceOperation0<>("package.isUnnamed");

   public static InstanceOperation1<C_Package, String, C_Declared> PACKAGE_GET_DECLARED = new InstanceOperation1<>("package.getDeclared");

   public static InstanceOperation0<C_Package, List<? extends C_Declared>> PACKAGE_GET_DECLARED_LIST = new InstanceOperation0<>("package.getDeclaredList");

   public static InstanceOperation0<C_ModuleEnclosed, C_Module> MODULE_ENCLOSED_GET_MODULE = new InstanceOperation0<>("moduleEnclosed.getModule");

   public static InstanceOperation1<C_Declared, C_Type, Boolean> DECLARED_IS_SUBTYPE_OF = new InstanceOperation1<>("declared.isSubtypeOf");

   public static InstanceOperation0<C_Declared, C_NestingKind> DECLARED_GET_NESTING = new InstanceOperation0<>("declared.getNesting");

   public static InstanceOperation0<C_Declared, List<? extends C_Field>> DECLARED_GET_FIELDS = new InstanceOperation0<>("declared.getFields");

   public static InstanceOperation1<C_Declared, String, C_Field> DECLARED_GET_FIELD = new InstanceOperation1<>("declared.getField");

   public static InstanceOperation0<C_Declared, List<? extends C_Method>> DECLARED_GET_METHODS = new InstanceOperation0<>("declared.getMethods");

   public static InstanceOperation1<C_Declared, String, List<? extends C_Method>> DECLARED_GET_METHOD = new InstanceOperation1<>("declared.getMethod");

   public static InstanceOperation0<C_Declared, List<? extends C_Declared>> DECLARED_GET_DIRECT_SUPER_TYPES = new InstanceOperation0<>("declared.getDirectSuperTypes");

   public static InstanceOperation0<C_Declared, Set<? extends C_Declared>> DECLARED_GET_SUPER_TYPES = new InstanceOperation0<>("declared.getSuperTypes");

   public static InstanceOperation0<C_Declared, List<? extends C_Interface>> DECLARED_GET_INTERFACES = new InstanceOperation0<>("declared.getInterfaces");

   public static InstanceOperation1<C_Declared, String, C_Interface> DECLARED_GET_INTERFACE = new InstanceOperation1<>("declared.getInterface");

   public static InstanceOperation0<C_Declared, List<? extends C_Interface>> DECLARED_GET_DIRECT_INTERFACES = new InstanceOperation0<>("declared.getDirectInterfaces");

   public static InstanceOperation1<C_Declared, String, C_Interface> DECLARED_GET_DIRECT_INTERFACE = new InstanceOperation1<>("declared.getDirectInterface");

   public static InstanceOperation0<C_Declared, C_Package> DECLARED_GET_PACKAGE = new InstanceOperation0<>("declared.getPackage");

   public static InstanceOperation0<C_Declared, String> DECLARED_GET_BINARY_NAME = new InstanceOperation0<>("declared.getBinaryName");

   public static InstanceOperation0<C_Declared, C_Array> DECLARED_AS_ARRAY = new InstanceOperation0<>("declared.asArray");

   public static InstanceOperation0<C_Enum, List<? extends C_Constructor>> ENUM_GET_CONSTRUCTORS = new InstanceOperation0<>("enum.getConstructors");

   public static InstanceOperation0<C_Enum, List<? extends C_EnumConstant>> ENUM_GET_EUM_CONSTANTS = new InstanceOperation0<>("enum.getEumConstants");

   public static InstanceOperation1<C_Enum, String, C_EnumConstant> ENUM_GET_ENUM_CONSTANT = new InstanceOperation1<>("enum.getEnumConstant");

   public static InstanceOperation0<C_Interface, Boolean> INTERFACE_IS_FUNCTIONAL = new InstanceOperation0<>("Interface.isFunctional");

   public static InstanceOperation0<C_Interface, List<? extends C_Type>> INTERFACE_GET_GENERIC_TYPES = new InstanceOperation0<>("interface.getGenericTypes");

   public static InstanceOperation0<C_Interface, List<? extends C_Generic>> INTERFACE_GET_GENERICS = new InstanceOperation0<>("interface.getGenerics");

   public static InstanceOperation0<C_Record, List<? extends C_Constructor>> RECORD_GET_CONSTRUCTORS = new InstanceOperation0<>("record.getConstructors");

   public static InstanceOperation0<C_Record, List<? extends C_RecordComponent>> RECORD_GET_RECORD_COMPONENTS = new InstanceOperation0<>("record.getRecordComponents");

   public static InstanceOperation1<C_Record, String, C_RecordComponent> RECORD_GET_RECORD_COMPONENT = new InstanceOperation1<>("record.getRecordComponent");

   public static InstanceOperation0<C_Record, List<? extends C_Type>> RECORD_GET_GENERIC_TYPES = new InstanceOperation0<>("record.getGenericTypes");

   public static InstanceOperation0<C_Record, List<? extends C_Generic>> RECORD_GET_GENERICS = new InstanceOperation0<>("record.getGenerics");

   public static InstanceOperation0<C_Class, List<? extends C_Constructor>> CLASS_GET_CONSTRUCTORS = new InstanceOperation0<>("class.getConstructors");

   public static InstanceOperation0<C_Class, C_Class> CLASS_GET_SUPER_CLASS = new InstanceOperation0<>("class.getSuperClass");

   public static InstanceOperation0<C_Class, List<? extends C_Class>> CLASS_GET_PERMITTED_SUB_CLASSES = new InstanceOperation0<>("class.getPermittedSubClasses");

   public static InstanceOperation0<C_Class, List<? extends C_Property>> CLASS_GET_PROPERTIES = new InstanceOperation0<>("class.getProperties");

   public static InstanceOperation1<C_Class, C_Type, Boolean> CLASS_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("class.isAssignableFrom");

   public static InstanceOperation0<C_Class, C_Declared> CLASS_GET_OUTER_TYPE = new InstanceOperation0<>("class.getOuterType");

   public static InstanceOperation0<C_Class, List<? extends C_Type>> CLASS_GET_GENERIC_TYPES = new InstanceOperation0<>("class.getGenericTypes");

   public static InstanceOperation0<C_Class, List<? extends C_Generic>> CLASS_GET_GENERICS = new InstanceOperation0<>("class.getGenerics");

   public static InstanceOperation0<C_Class, C_Primitive> CLASS_AS_UNBOXED = new InstanceOperation0<>("class.asUnboxed");

   public static InstanceOperation1<C_Array, C_Type, Boolean> ARRAY_IS_SUBTYPE_OF = new InstanceOperation1<>("array.isSubtypeOf");

   public static InstanceOperation0<C_Array, C_Type> ARRAY_GET_COMPONENT_TYPE = new InstanceOperation0<>("array.getComponentType");

   public static InstanceOperation0<C_Array, List<? extends C_Type>> ARRAY_GET_DIRECT_SUPER_TYPES = new InstanceOperation0<>("array.getDirectSuperTypes");

   public static InstanceOperation0<C_Array, C_Array> ARRAY_AS_ARRAY = new InstanceOperation0<>("array.asArray");

   public static InstanceOperation0<C_EnumConstant, C_Enum> ENUM_CONSTANT_GET_SURROUNDING = new InstanceOperation0<>("enumConstant.getSurrounding");

   public static InstanceOperation0<C_Field, Boolean> FIELD_IS_CONSTANT = new InstanceOperation0<>("field.isConstant");

   public static InstanceOperation0<C_Field, Object> FIELD_GET_CONSTANT_VALUE = new InstanceOperation0<>("field.getConstantValue");

   public static InstanceOperation0<C_Field, C_Declared> FIELD_GET_SURROUNDING = new InstanceOperation0<>("field.getSurrounding");

   public static InstanceOperation0<C_Parameter, Boolean> PARAMETER_IS_VAR_ARGS = new InstanceOperation0<>("parameter.isVarArgs");

   public static InstanceOperation0<C_Parameter, C_Executable> PARAMETER_GET_SURROUNDING = new InstanceOperation0<>("parameter.getSurrounding");

   public static InstanceOperation1<C_Variable, C_Type, Boolean> VARIABLE_IS_SUBTYPE_OF = new InstanceOperation1<>("variable.isSubtypeOf");

   public static InstanceOperation1<C_Variable, C_Type, Boolean> VARIABLE_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("variable.isAssignableFrom");

   public static InstanceOperation0<C_Variable, C_Type> VARIABLE_GET_TYPE = new InstanceOperation0<>("variable.getType");

   public static InstanceOperation0<C_Variable, Object> VARIABLE_GET_SURROUNDING = new InstanceOperation0<>("variable.getSurrounding");

   public static InstanceOperation0<C_Executable, List<? extends C_Parameter>> EXECUTABLE_GET_PARAMETERS = new InstanceOperation0<>("executable.getParameters");

   public static InstanceOperation1<C_Executable, String, C_Parameter> EXECUTABLE_GET_PARAMETER = new InstanceOperation1<>("executable.getParameter");

   public static InstanceOperation0<C_Executable, List<? extends C_Type>> EXECUTABLE_GET_PARAMETER_TYPES = new InstanceOperation0<>("executable.getParameterTypes");

   public static InstanceOperation0<C_Executable, List<? extends C_Class>> EXECUTABLE_GET_THROWS = new InstanceOperation0<>("executable.getThrows");

   public static InstanceOperation0<C_Executable, Boolean> EXECUTABLE_IS_VAR_ARGS = new InstanceOperation0<>("executable.isVarArgs");

   public static InstanceOperation0<C_Executable, C_Declared> EXECUTABLE_GET_SURROUNDING = new InstanceOperation0<>("executable.getSurrounding");

   public static InstanceOperation0<C_Executable, List<? extends C_Generic>> EXECUTABLE_GET_GENERICS = new InstanceOperation0<>("executable.getGenerics");

   public static InstanceOperation0<C_Executable, C_Declared> EXECUTABLE_GET_RECEIVER_TYPE = new InstanceOperation0<>("executable.getReceiverType");

   public static InstanceOperation0<C_Executable, C_Receiver> EXECUTABLE_GET_RECEIVER = new InstanceOperation0<>("executable.getReceiver");

   public static InstanceOperation0<C_Method, C_Type> METHOD_GET_RETURN_TYPE = new InstanceOperation0<>("method.getReturnType");

   public static InstanceOperation1<C_Method, C_Method, Boolean> METHOD_OVERRIDES = new InstanceOperation1<>("method.overrides");

   public static InstanceOperation1<C_Method, C_Method, Boolean> METHOD_OVERWRITTEN_BY = new InstanceOperation1<>("method.overwrittenBy");

   public static InstanceOperation1<C_Method, C_Method, Boolean> METHOD_SAME_PARAMETER_TYPES = new InstanceOperation1<>("method.sameParameterTypes");

   public static InstanceOperation0<C_Method, Boolean> METHOD_IS_BRIDGE = new InstanceOperation0<>("method.isBridge");

   public static InstanceOperation0<C_Method, C_Result> METHOD_GET_RETURN = new InstanceOperation0<>("method.getReturn");

   public static InstanceOperation1<C_RecordComponent, C_Type, Boolean> RECORD_COMPONENT_IS_SUBTYPE_OF = new InstanceOperation1<>("recordComponent.isSubtypeOf");

   public static InstanceOperation1<C_RecordComponent, C_Type, Boolean> RECORD_COMPONENT_IS_ASSIGNABLE_FROM = new InstanceOperation1<>("recordComponent.isAssignableFrom");

   public static InstanceOperation0<C_RecordComponent, C_Record> RECORD_COMPONENT_GET_RECORD = new InstanceOperation0<>("RecordComponent.getRecord");

   public static InstanceOperation0<C_RecordComponent, C_Type> RECORD_COMPONENT_GET_TYPE = new InstanceOperation0<>("recordComponent.getType");

   public static InstanceOperation0<C_RecordComponent, C_Method> RECORD_COMPONENT_GET_GETTER = new InstanceOperation0<>("recordComponent.getGetter");

   public static InstanceOperation0<C_Result, C_Type> RETURN_GET_TYPE = new InstanceOperation0<>("return.getType");

   public static InstanceOperation0<C_Receiver, C_Type> RECEIVER_GET_TYPE = new InstanceOperation0<>("receiver.getType");

   public static InstanceOperation0<C_Intersection, List<? extends C_Type>> INTERSECTION_GET_BOUNDS = new InstanceOperation0<>("intersection.getBounds");

   public static InstanceOperation0<C_Intersection, C_Array> INTERSECTION_AS_ARRAY = new InstanceOperation0<>("intersection.asArray");

   public static InstanceOperation0<C_Generic, C_Type> GENERIC_GET_EXTENDS = new InstanceOperation0<>("generic.getExtends");

   public static InstanceOperation0<C_Generic, C_Type> GENERIC_GET_SUPER = new InstanceOperation0<>("generic.getSuper");

   public static InstanceOperation0<C_Generic, Object> GENERIC_GET_ENCLOSING = new InstanceOperation0<>("generic.getEnclosing");

   public static InstanceOperation0<C_AnnotationUsage, Map<? extends C_Method, ? extends C_AnnotationValue>> ANNOTATION_USAGE_GET_VALUES = new InstanceOperation0<>("annotationUsage.getValues");

   public static InstanceOperation1<C_AnnotationUsage, String, C_AnnotationValue> ANNOTATION_USAGE_GET_VALUE = new InstanceOperation1<>("annotationUsage.getValue");

   public static InstanceOperation0<C_AnnotationUsage, C_Annotation> ANNOTATION_USAGE_GET_ANNOTATION = new InstanceOperation0<>("annotationUsage.getAnnotation");

   public static InstanceOperation0<C_Module, List<? extends C_Package>> MODULE_GET_PACKAGES = new InstanceOperation0<>("module.getPackages");

   public static InstanceOperation0<C_Module, Boolean> MODULE_IS_OPEN = new InstanceOperation0<>("module.isOpen");

   public static InstanceOperation0<C_Module, Boolean> MODULE_IS_UNNAMED = new InstanceOperation0<>("module.isUnnamed");

   public static InstanceOperation0<C_Module, Boolean> MODULE_IS_AUTOMATIC = new InstanceOperation0<>("module.isAutomatic");

   public static InstanceOperation0<C_Module, List<? extends C_Directive>> MODULE_GET_DIRECTIVES = new InstanceOperation0<>("module.getDirectives");

   public static InstanceOperation1<C_Module, String, C_Declared> MODULE_GET_DECLARED = new InstanceOperation1<>("module.getDeclared");

   public static InstanceOperation0<C_Module, List<? extends C_Declared>> MODULE_GET_DECLARED_LIST = new InstanceOperation0<>("module.getDeclaredList");

   public static InstanceOperation0<C_Modifiable, Set<C_Modifier>> MODIFIABLE_GET_MODIFIERS = new InstanceOperation0<>("modifiable.getModifiers");

   public static InstanceOperation1<C_Modifiable, C_Modifier, Boolean> MODIFIABLE_HAS_MODIFIER = new InstanceOperation1<>("modifiable.hasModifier");

   public static InstanceOperation0<C_Exports, C_Package> EXPORTS_GET_PACKAGE = new InstanceOperation0<>("exports.getPackage");

   public static InstanceOperation0<C_Exports, List<? extends C_Module>> EXPORTS_GET_TARGET_MODULES = new InstanceOperation0<>("exports.getTargetModules");

   public static InstanceOperation0<C_Exports, Boolean> EXPORTS_TO_ALL = new InstanceOperation0<>("exports.toAll");

   public static InstanceOperation0<C_Opens, C_Package> OPENS_GET_PACKAGE = new InstanceOperation0<>("opens.getPackage");

   public static InstanceOperation0<C_Opens, List<? extends C_Module>> OPENS_GET_TARGET_MODULES = new InstanceOperation0<>("opens.getTargetModules");

   public static InstanceOperation0<C_Opens, Boolean> OPENS_TO_ALL = new InstanceOperation0<>("opens.toAll");

   public static InstanceOperation0<C_Provides, C_Declared> PROVIDES_GET_SERVICE = new InstanceOperation0<>("provides.getService");

   public static InstanceOperation0<C_Provides, List<? extends C_Declared>> PROVIDES_GET_IMPLEMENTATIONS = new InstanceOperation0<>("provides.getImplementations");

   public static InstanceOperation0<C_Requires, Boolean> REQUIRES_IS_STATIC = new InstanceOperation0<>("requires.isStatic");

   public static InstanceOperation0<C_Requires, Boolean> REQUIRES_IS_TRANSITIVE = new InstanceOperation0<>("requires.isTransitive");

   public static InstanceOperation0<C_Requires, C_Module> REQUIRES_GET_DEPENDENCY = new InstanceOperation0<>("requires.getDependency");

   public static InstanceOperation0<C_Uses, C_Declared> USES_GET_SERVICE = new InstanceOperation0<>("uses.getService");

   public static InstanceOperation0<C_Annotationable, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_ANNOTATION_USAGES = new InstanceOperation0<>("annotationable.getAnnotationUsages");

   public static InstanceOperation1<C_Annotationable, C_Annotation, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_USAGES_OF = new InstanceOperation1<>("annotationUsage.getUsagesOf");

   public static InstanceOperation1<C_Annotationable, C_Annotation, C_AnnotationUsage> ANNOTATIONABLE_GET_USAGE_OF = new InstanceOperation1<>("annotationUsage.getUsageOf");

   public static InstanceOperation1<C_Annotationable, C_Annotation, Boolean> ANNOTATIONABLE_IS_ANNOTATED_WITH = new InstanceOperation1<>("annotationable.isAnnotatedWith");

   public static InstanceOperation0<C_Annotationable, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES = new InstanceOperation0<>("annotationable.getDirectAnnotationUsages");

   public static InstanceOperation1<C_Annotationable, C_Annotation, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_USAGES_OF = new InstanceOperation1<>("annotationUsage.getDirectUsagesOf");

   public static InstanceOperation1<C_Annotationable, C_Annotation, C_AnnotationUsage> ANNOTATIONABLE_GET_DIRECT_USAGE_OF = new InstanceOperation1<>("annotationUsage.getDirectUsageOf");

   public static InstanceOperation1<C_Annotationable, C_Annotation, Boolean>  ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH = new InstanceOperation1<>("annotationable.isDirectlyAnnotatedWith");

   public static InstanceOperation0<C_AnnotationValue, Boolean> ANNOTATION_VALUE_IS_DEFAULT = new InstanceOperation0<>("annotationValue.isDefault");

   public static InstanceOperation0<C_AnnotationValue, Object> ANNOTATION_VALUE_GET_VALUE = new InstanceOperation0<>("annotationValue.getValue");

   public static InstanceOperation0<C_Property, C_Type> PROPERTY_GET_TYPE = new InstanceOperation0<>("property.getType");

   public static InstanceOperation0<C_Property, C_Field> PROPERTY_GET_FIELD = new InstanceOperation0<>("property.getField");

   public static InstanceOperation0<C_Property, C_Method> PROPERTY_GET_GETTER = new InstanceOperation0<>("property.getGetter");

   public static InstanceOperation0<C_Property, C_Method> PROPERTY_GET_SETTER = new InstanceOperation0<>("property.getSetter");

   public static InstanceOperation0<C_Property, Boolean> PROPERTY_IS_MUTABLE = new InstanceOperation0<>("property.isMutable");

   public static InstanceOperation0<C_Erasable, C_Erasable> ERASABLE_ERASURE = new InstanceOperation0<>("erasable.erasure");
}
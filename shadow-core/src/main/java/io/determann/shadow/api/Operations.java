package io.determann.shadow.api;

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

public interface Operations
{
   public static Operation1<C_Type, C_Type, Boolean> TYPE_REPRESENTS_SAME_TYPE = new Operation1<>("type.representsSameType");

   public static Operation0<C_Nameable, String> NAMEABLE_GET_NAME = new Operation0<>("nameable.getName");

   public static Operation0<C_QualifiedNameable, String> QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME = new Operation0<>("qualifiedNameable.getQualifiedName");

   public static Operation0<C_Wildcard, C_Type> WILDCARD_GET_EXTENDS = new Operation0<>("wildcard.getExtends");

   public static Operation0<C_Wildcard, C_Type> WILDCARD_GET_SUPER = new Operation0<>("wildcard.getSuper");

   public static Operation0<C_Primitive, C_Type> PRIMITIVE_AS_BOXED = new Operation0<>("primitive.asBoxed");

   public static Operation1<C_Primitive, C_Type, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new Operation1<>("primitive.isSubtypeOf");

   public static Operation1<C_Primitive, C_Type, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new Operation1<>("primitive.isAssignableFrom");

   public static Operation0<C_Package, Boolean> PACKAGE_IS_UNNAMED = new Operation0<>("package.isUnnamed");

   public static Operation1<C_Package, String, C_Declared> PACKAGE_GET_DECLARED = new Operation1<>("package.getDeclared");

   public static Operation0<C_Package, List<? extends C_Declared>> PACKAGE_GET_DECLARED_LIST = new Operation0<>("package.getDeclaredList");

   public static Operation0<C_ModuleEnclosed, C_Module> MODULE_ENCLOSED_GET_MODULE = new Operation0<>("moduleEnclosed.getModule");

   public static Operation1<C_Declared, C_Type, Boolean> DECLARED_IS_SUBTYPE_OF = new Operation1<>("declared.isSubtypeOf");

   public static Operation0<C_Declared, C_NestingKind> DECLARED_GET_NESTING = new Operation0<>("declared.getNesting");

   public static Operation0<C_Declared, List<? extends C_Field>> DECLARED_GET_FIELDS = new Operation0<>("declared.getFields");

   public static Operation1<C_Declared, String, C_Field> DECLARED_GET_FIELD = new Operation1<>("declared.getField");

   public static Operation0<C_Declared, List<? extends C_Method>> DECLARED_GET_METHODS = new Operation0<>("declared.getMethods");

   public static Operation1<C_Declared, String, List<? extends C_Method>> DECLARED_GET_METHOD = new Operation1<>("declared.getMethod");

   public static Operation0<C_Declared, List<? extends C_Constructor>> DECLARED_GET_CONSTRUCTORS = new Operation0<>("declared.getConstructor");

   public static Operation0<C_Declared, List<? extends C_Declared>> DECLARED_GET_DIRECT_SUPER_TYPES = new Operation0<>("declared.getDirectSuperTypes");

   public static Operation0<C_Declared, List<? extends C_Declared>> DECLARED_GET_SUPER_TYPES = new Operation0<>("declared.getSuperTypes");

   public static Operation0<C_Declared, List<? extends C_Interface>> DECLARED_GET_INTERFACES = new Operation0<>("declared.getInterfaces");

   public static Operation1<C_Declared, String, C_Interface> DECLARED_GET_INTERFACE = new Operation1<>("declared.getInterface");

   public static Operation0<C_Declared, List<? extends C_Interface>> DECLARED_GET_DIRECT_INTERFACES = new Operation0<>("declared.getDirectInterfaces");

   public static Operation1<C_Declared, String, C_Interface> DECLARED_GET_DIRECT_INTERFACE = new Operation1<>("declared.getDirectInterface");

   public static Operation0<C_Declared, C_Package> DECLARED_GET_PACKAGE = new Operation0<>("declared.getPackage");

   public static Operation0<C_Enum, List<? extends C_EnumConstant>> ENUM_GET_EUM_CONSTANTS = new Operation0<>("enum.getEumConstants");

   public static Operation1<C_Enum, String, C_EnumConstant> ENUM_GET_ENUM_CONSTANT = new Operation1<>("enum.getEnumConstant");

   public static Operation0<C_Interface, Boolean> INTERFACE_IS_FUNCTIONAL = new Operation0<>("Interface.isFunctional");

   public static Operation0<C_Interface, List<? extends C_Type>> INTERFACE_GET_GENERIC_TYPES = new Operation0<>("interface.getGenericTypes");

   public static Operation0<C_Interface, List<? extends C_Generic>> INTERFACE_GET_GENERICS = new Operation0<>("interface.getGenerics");

   public static Operation0<C_Record, List<? extends C_RecordComponent>> RECORD_GET_RECORD_COMPONENTS = new Operation0<>("record.getRecordComponents");

   public static Operation1<C_Record, String, C_RecordComponent> RECORD_GET_RECORD_COMPONENT = new Operation1<>("record.getRecordComponent");

   public static Operation0<C_Record, List<? extends C_Type>> RECORD_GET_GENERIC_TYPES = new Operation0<>("record.getGenericTypes");

   public static Operation0<C_Record, List<? extends C_Generic>> RECORD_GET_GENERICS = new Operation0<>("record.getGenerics");

   public static Operation0<C_Class, C_Class> CLASS_GET_SUPER_CLASS = new Operation0<>("class.getSuperClass");

   public static Operation0<C_Class, List<? extends C_Class>> CLASS_GET_PERMITTED_SUB_CLASSES = new Operation0<>("class.getPermittedSubClasses");

   public static Operation0<C_Class, List<? extends C_Property>> CLASS_GET_PROPERTIES = new Operation0<>("class.getProperties");

   public static Operation1<C_Class, C_Type, Boolean> CLASS_IS_ASSIGNABLE_FROM = new Operation1<>("class.isAssignableFrom");

   public static Operation0<C_Class, C_Declared> CLASS_GET_OUTER_TYPE = new Operation0<>("class.getOuterType");

   public static Operation0<C_Class, List<? extends C_Type>> CLASS_GET_GENERIC_TYPES = new Operation0<>("class.getGenericTypes");

   public static Operation0<C_Class, List<? extends C_Generic>> CLASS_GET_GENERICS = new Operation0<>("class.getGenerics");

   public static Operation0<C_Class, C_Primitive> CLASS_AS_UNBOXED = new Operation0<>("class.asUnboxed");

   public static Operation1<C_Array, C_Type, Boolean> ARRAY_IS_SUBTYPE_OF = new Operation1<>("array.isSubtypeOf");

   public static Operation0<C_Array, C_Type> ARRAY_GET_COMPONENT_TYPE = new Operation0<>("array.getComponentType");

   public static Operation0<C_Array, List<? extends C_Type>> ARRAY_GET_DIRECT_SUPER_TYPES = new Operation0<>("array.getDirectSuperTypes");

   public static Operation0<C_EnumConstant, C_Enum> ENUM_CONSTANT_GET_SURROUNDING = new Operation0<>("enumConstant.getSurrounding");

   public static Operation0<C_Field, Boolean> FIELD_IS_CONSTANT = new Operation0<>("field.isConstant");

   public static Operation0<C_Field, Object> FIELD_GET_CONSTANT_VALUE = new Operation0<>("field.getConstantValue");

   public static Operation0<C_Field, C_Declared> FIELD_GET_SURROUNDING = new Operation0<>("field.getSurrounding");

   public static Operation0<C_Parameter, Boolean> PARAMETER_IS_VAR_ARGS = new Operation0<>("parameter.isVarArgs");

   public static Operation0<C_Parameter, C_Executable> PARAMETER_GET_SURROUNDING = new Operation0<>("parameter.getSurrounding");

   public static Operation1<C_Variable, C_Type, Boolean> VARIABLE_IS_SUBTYPE_OF = new Operation1<>("variable.isSubtypeOf");

   public static Operation1<C_Variable, C_Type, Boolean> VARIABLE_IS_ASSIGNABLE_FROM = new Operation1<>("variable.isAssignableFrom");

   public static Operation0<C_Variable, C_Type> VARIABLE_GET_TYPE = new Operation0<>("variable.getType");

   public static Operation0<C_Variable, C_Package> VARIABLE_GET_PACKAGE = new Operation0<>("variable.getPackage");

   public static Operation0<C_Variable, Object> VARIABLE_GET_SURROUNDING = new Operation0<>("variable.getSurrounding");

   public static Operation0<C_Executable, List<? extends C_Parameter>> EXECUTABLE_GET_PARAMETERS = new Operation0<>("executable.getParameters");

   public static Operation1<C_Executable, String, C_Parameter> EXECUTABLE_GET_PARAMETER = new Operation1<>("executable.getParameter");

   public static Operation0<C_Executable, C_Return> EXECUTABLE_GET_RETURN = new Operation0<>("executable.getReturn");

   public static Operation0<C_Executable, C_Type> EXECUTABLE_GET_RETURN_TYPE = new Operation0<>("executable.getReturnType");

   public static Operation0<C_Executable, List<? extends C_Type>> EXECUTABLE_GET_PARAMETER_TYPES = new Operation0<>("executable.getParameterTypes");

   public static Operation0<C_Executable, List<? extends C_Class>> EXECUTABLE_GET_THROWS = new Operation0<>("executable.getThrows");

   public static Operation0<C_Executable, Boolean> EXECUTABLE_IS_VAR_ARGS = new Operation0<>("executable.isVarArgs");

   public static Operation0<C_Executable, C_Declared> EXECUTABLE_GET_SURROUNDING = new Operation0<>("executable.getSurrounding");

   public static Operation0<C_Executable, C_Package> EXECUTABLE_GET_PACKAGE = new Operation0<>("executable.getPackage");

   public static Operation0<C_Executable, List<? extends C_Generic>> EXECUTABLE_GET_GENERICS = new Operation0<>("executable.getGenerics");

   public static Operation0<C_Executable, C_Declared> EXECUTABLE_GET_RECEIVER_TYPE = new Operation0<>("executable.getReceiverType");

   public static Operation0<C_Executable, C_Receiver> EXECUTABLE_GET_RECEIVER = new Operation0<>("executable.getReceiver");

   public static Operation1<C_Method, C_Method, Boolean> METHOD_OVERRIDES = new Operation1<>("method.overrides");

   public static Operation1<C_Method, C_Method, Boolean> METHOD_OVERWRITTEN_BY = new Operation1<>("method.overwrittenBy");

   public static Operation1<C_Method, C_Method, Boolean> METHOD_SAME_PARAMETER_TYPES = new Operation1<>("method.sameParameterTypes");

   public static Operation0<C_Method, Boolean> METHOD_IS_BRIDGE = new Operation0<>("method.isBridge");

   public static Operation1<C_RecordComponent, C_Type, Boolean> RECORD_COMPONENT_IS_SUBTYPE_OF = new Operation1<>("recordComponent.isSubtypeOf");

   public static Operation1<C_RecordComponent, C_Type, Boolean> RECORD_COMPONENT_IS_ASSIGNABLE_FROM = new Operation1<>("recordComponent.isAssignableFrom");

   public static Operation0<C_RecordComponent, C_Record> RECORD_COMPONENT_GET_RECORD = new Operation0<>("RecordComponent.getRecord");

   public static Operation0<C_RecordComponent, C_Type> RECORD_COMPONENT_GET_TYPE = new Operation0<>("recordComponent.getType");

   public static Operation0<C_RecordComponent, C_Method> RECORD_COMPONENT_GET_GETTER = new Operation0<>("recordComponent.getGetter");

   public static Operation0<C_RecordComponent, C_Package> RECORD_COMPONENT_GET_PACKAGE = new Operation0<>("recordComponent.getPackage");

   public static Operation0<C_Return, C_Type> RETURN_GET_TYPE = new Operation0<>("return.getType");

   public static Operation0<C_Receiver, C_Type> RECEIVER_GET_TYPE = new Operation0<>("receiver.getType");

   public static Operation0<C_Intersection, List<? extends C_Type>> INTERSECTION_GET_BOUNDS = new Operation0<>("intersection.getBounds");

   public static Operation0<C_Generic, C_Type> GENERIC_GET_EXTENDS = new Operation0<>("generic.getExtends");

   public static Operation0<C_Generic, C_Type> GENERIC_GET_SUPER = new Operation0<>("generic.getSuper");

   public static Operation0<C_Generic, Object> GENERIC_GET_ENCLOSING = new Operation0<>("generic.getEnclosing");

   public static Operation0<C_AnnotationUsage, Map<? extends C_Method, ? extends C_AnnotationValue>> ANNOTATION_USAGE_GET_VALUES = new Operation0<>("annotationUsage.getValues");

   public static Operation1<C_AnnotationUsage, String, C_AnnotationValue> ANNOTATION_USAGE_GET_VALUE = new Operation1<>("annotationUsage.getValue");

   public static Operation0<C_AnnotationUsage, C_Annotation> ANNOTATION_USAGE_GET_ANNOTATION = new Operation0<>("annotationUsage.getAnnotation");

   public static Operation0<C_Module, List<? extends C_Package>> MODULE_GET_PACKAGES = new Operation0<>("module.getPackages");

   public static Operation0<C_Module, Boolean> MODULE_IS_OPEN = new Operation0<>("module.isOpen");

   public static Operation0<C_Module, Boolean> MODULE_IS_UNNAMED = new Operation0<>("module.isUnnamed");

   public static Operation0<C_Module, Boolean> MODULE_IS_AUTOMATIC = new Operation0<>("module.isAutomatic");

   public static Operation0<C_Module, List<? extends C_Directive>> MODULE_GET_DIRECTIVES = new Operation0<>("module.getDirectives");

   public static Operation1<C_Module, String, C_Declared> MODULE_GET_DECLARED = new Operation1<>("module.getDeclared");

   public static Operation0<C_Module, List<? extends C_Declared>> MODULE_GET_DECLARED_LIST = new Operation0<>("module.getDeclaredList");

   public static Operation0<C_Modifiable, Set<C_Modifier>> MODIFIABLE_GET_MODIFIERS = new Operation0<>("modifiable.getModifiers");

   public static Operation1<C_Modifiable, C_Modifier, Boolean> MODIFIABLE_HAS_MODIFIER = new Operation1<>("modifiable.hasModifier");

   public static Operation0<C_Exports, C_Package> EXPORTS_GET_PACKAGE = new Operation0<>("exports.getPackage");

   public static Operation0<C_Exports, List<? extends C_Module>> EXPORTS_GET_TARGET_MODULES = new Operation0<>("exports.getTargetModules");

   public static Operation0<C_Exports, Boolean> EXPORTS_TO_ALL = new Operation0<>("exports.toAll");

   public static Operation0<C_Opens, C_Package> OPENS_GET_PACKAGE = new Operation0<>("opens.getPackage");

   public static Operation0<C_Opens, List<? extends C_Module>> OPENS_GET_TARGET_MODULES = new Operation0<>("opens.getTargetModules");

   public static Operation0<C_Opens, Boolean> OPENS_TO_ALL = new Operation0<>("opens.toAll");

   public static Operation0<C_Provides, C_Declared> PROVIDES_GET_SERVICE = new Operation0<>("provides.getService");

   public static Operation0<C_Provides, List<? extends C_Declared>> PROVIDES_GET_IMPLEMENTATIONS = new Operation0<>("provides.getImplementations");

   public static Operation0<C_Requires, Boolean> REQUIRES_IS_STATIC = new Operation0<>("requires.isStatic");

   public static Operation0<C_Requires, Boolean> REQUIRES_IS_TRANSITIVE = new Operation0<>("requires.isTransitive");

   public static Operation0<C_Requires, C_Module> REQUIRES_GET_DEPENDENCY = new Operation0<>("requires.getDependency");

   public static Operation0<C_Uses, C_Declared> USES_GET_SERVICE = new Operation0<>("uses.getService");

   public static Operation0<C_Annotationable, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_ANNOTATION_USAGES = new Operation0<>("annotationable.getAnnotationUsages");

   public static Operation1<C_Annotationable, C_Annotation, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_USAGES_OF = new Operation1<>("annotationUsage.getUsagesOf");

   public static Operation1<C_Annotationable, C_Annotation, C_AnnotationUsage> ANNOTATIONABLE_GET_USAGE_OF = new Operation1<>("annotationUsage.getUsageOf");

   public static Operation1<C_Annotationable, C_Annotation, Boolean> ANNOTATIONABLE_IS_ANNOTATED_WITH = new Operation1<>("annotationable.isAnnotatedWith");

   public static Operation0<C_Annotationable, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES = new Operation0<>("annotationable.getDirectAnnotationUsages");

   public static Operation1<C_Annotationable, C_Annotation, List<? extends C_AnnotationUsage>> ANNOTATIONABLE_GET_DIRECT_USAGES_OF = new Operation1<>("annotationUsage.getDirectUsagesOf");

   public static Operation1<C_Annotationable, C_Annotation, C_AnnotationUsage> ANNOTATIONABLE_GET_DIRECT_USAGE_OF = new Operation1<>("annotationUsage.getDirectUsageOf");

   public static Operation1<C_Annotationable, C_Annotation, Boolean> ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH = new Operation1<>("annotationable.isDirectlyAnnotatedWith");

   public static Operation0<C_AnnotationValue, Boolean> ANNOTATION_VALUE_IS_DEFAULT = new Operation0<>("annotationValue.isDefault");

   public static Operation0<C_AnnotationValue, Object> ANNOTATION_VALUE_GET_VALUE = new Operation0<>("annotationValue.getValue");

   public static Operation0<C_Property, String> PROPERTY_GET_NAME = new Operation0<>("property.getName");

   public static Operation0<C_Property, C_Type> PROPERTY_GET_TYPE = new Operation0<>("property.getType");

   public static Operation0<C_Property, C_Field> PROPERTY_GET_FIELD = new Operation0<>("property.getField");

   public static Operation0<C_Property, C_Method> PROPERTY_GET_GETTER = new Operation0<>("property.getGetter");

   public static Operation0<C_Property, C_Method> PROPERTY_GET_SETTER = new Operation0<>("property.getSetter");

   public static Operation0<C_Property, Boolean> PROPERTY_IS_MUTABLE = new Operation0<>("property.isMutable");
}
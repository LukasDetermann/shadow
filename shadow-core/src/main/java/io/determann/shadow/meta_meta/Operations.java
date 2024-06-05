package io.determann.shadow.meta_meta;

import io.determann.shadow.api.*;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import java.util.List;

public interface Operations
{
   public static Operation0<Shadow, TypeKind> SHADOW_GET_KIND = new Operation0<>("shadow.getKind");

   public static Operation1<Shadow, Shadow, Boolean> SHADOW_REPRESENTS_SAME_TYPE = new Operation1<>("shadow.representsSameType");

   public static Operation0<Nameable, String> NAMEABLE_NAME = new Operation0<>("nameable.name");

   public static Operation0<QualifiedNameable, String> QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME = new Operation0<>("qualifiedNameable.getQualifiedName");

   public static Operation0<Wildcard, Shadow> WILDCARD_EXTENDS = new Operation0<>("wildcard.extends");

   public static Operation0<Wildcard, Shadow> WILDCARD_SUPER = new Operation0<>("wildcard.super");

   public static Operation0<Primitive, Shadow> PRIMITIVE_AS_BOXED = new Operation0<>("primitive.asBoxed");

   public static Operation1<Primitive, Shadow, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new Operation1<>("primitive.isSubtypeOf");

   public static Operation1<Primitive, Shadow, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new Operation1<>("primitive.isAssignableFrom");

   public static Operation0<Package, List<Declared>> PACKAGE_GET_CONTENT = new Operation0<>("package.getContent");

   public static Operation0<Package, Boolean> PACKAGE_IS_UNNAMED = new Operation0<>("package.isUnnamed");

   public static Operation0<ModuleEnclosed, Module> MODULE_ENCLOSED_GET_MODULE = new Operation0<>("moduleEnclosed.getModule");

   public static Operation1<Declared, Shadow, Boolean> DECLARED_IS_SUBTYPE_OF = new Operation1<>("declared.isSubtypeOf");

   public static Operation0<Declared, NestingKind> DECLARED_GET_NESTING = new Operation0<>("declared.getNesting");

   public static Operation0<Declared, List<Field>> DECLARED_GET_FIELDS = new Operation0<>("declared.getFields");

   public static Operation1<Declared, String, Field> DECLARED_GET_FIELD = new Operation1<>("declared.getField");

   public static Operation0<Declared, List<Method>> DECLARED_GET_METHODS = new Operation0<>("declared.getMethods");

   public static Operation1<Declared, String, List<Method>> DECLARED_GET_METHOD = new Operation1<>("declared.getMethod");

   public static Operation0<Declared, List<Constructor>> DECLARED_GET_CONSTRUCTORS = new Operation0<>("declared.getConstructor");

   public static Operation0<Declared, List<Declared>> DECLARED_GET_DIRECT_SUPER_TYPES = new Operation0<>("declared.getDirectSuperTypes");

   public static Operation0<Declared, List<Declared>> DECLARED_GET_SUPER_TYPES = new Operation0<>("declared.getSuperTypes");

   public static Operation0<Declared, List<Interface>> DECLARED_GET_INTERFACES = new Operation0<>("declared.getInterfaces");

   public static Operation1<Declared, String, Interface> DECLARED_GET_INTERFACE = new Operation1<>("declared.getInterface");

   public static Operation0<Declared, List<Interface>> DECLARED_GET_DIRECT_INTERFACES = new Operation0<>("declared.getDirectInterfaces");

   public static Operation1<Declared, String, Interface> DECLARED_GET_DIRECT_INTERFACE = new Operation1<>("declared.getDirectInterfaceOrThrow");

   public static Operation0<Declared, Package> DECLARED_GET_PACKAGE = new Operation0<>("declared.getPackage");

   public static Operation0<Enum, List<EnumConstant>> ENUM_GET_EUM_CONSTANTS = new Operation0<>("enum.getEumConstants");

   public static Operation1<Enum, String, EnumConstant> ENUM_GET_ENUM_CONSTANT = new Operation1<>("enum.getEnumConstant");
}

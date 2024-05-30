package io.determann.shadow.meta_meta;

import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.TypeKind;
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
}

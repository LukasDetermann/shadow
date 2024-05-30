package io.determann.shadow.meta_meta;

import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

public interface Operations
{
   public static Operation0<Shadow, TypeKind> SHADOW_GET_KIND = new Operation0<>("shadow.getKind");

   public static Operation1<Shadow, Shadow, Boolean> SHADOW_REPRESENTS_SAME_TYPE = new Operation1<>("shadow.representsSameType");

   public static Operation0<Nameable, String> NAMEABLE_NAME = new Operation0<>("nameable.name");

   public static Operation0<Wildcard, Shadow> WILDCARD_EXTENDS = new Operation0<>("wildcard.extends");

   public static Operation0<Wildcard, Shadow> WILDCARD_SUPER = new Operation0<>("wildcard.super");

   public static Operation0<Primitive, Shadow> PRIMITIVE_AS_BOXED = new Operation0<>("primitive.asBoxed");

   public static Operation1<Primitive, Shadow, Boolean> PRIMITIVE_IS_SUBTYPE_OF = new Operation1<>("primitive.isSubtypeOf");

   public static Operation1<Primitive, Shadow, Boolean> PRIMITIVE_IS_ASSIGNABLE_FROM = new Operation1<>("primitive.isAssignableFrom");
}

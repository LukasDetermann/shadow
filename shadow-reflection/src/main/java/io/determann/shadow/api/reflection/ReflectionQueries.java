package io.determann.shadow.api.reflection;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.reflection.query.*;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface ReflectionQueries
{
   public static NameableReflection query(Nameable nameable)
   {
      return ((NameableReflection) validate(nameable));
   }

   public static QualifiedNameableReflection query(QualifiedNameable qualifiedNameable)
   {
      return ((QualifiedNameableReflection) validate(qualifiedNameable));
   }

   public static WildcardReflection query(Wildcard wildcard)
   {
      return ((WildcardReflection) validate(wildcard));
   }

   public static PrimitiveReflection query(Primitive primitive)
   {
      return ((PrimitiveReflection) validate(primitive));
   }

   public static ShadowReflection query(Shadow shadow)
   {
      return ((ShadowReflection) validate(shadow));
   }

   private static <T extends ImplementationDefined> T validate(T toValidate)
   {
      if (!Objects.equals(requireNonNull(toValidate.getImplementationName()), IMPLEMENTATION_NAME))
      {
         throw new IllegalArgumentException("Tried to use \"" + IMPLEMENTATION_NAME + " \" to query \"" + toValidate + "\" based on \"" + toValidate.getImplementationName() + "\"");
      }
      return toValidate;
   }
}

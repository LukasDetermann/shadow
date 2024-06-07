package io.determann.shadow.api.reflection;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.ModuleEnclosed;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.QualifiedNameable;
import io.determann.shadow.api.reflection.query.*;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

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

   public static PackageReflection query(Package aPackage)
   {
      return (PackageReflection) validate(aPackage);
   }

   public static ModuleEnclosedReflection query(ModuleEnclosed moduleEnclosed)
   {
      return (ModuleEnclosedReflection) validate(moduleEnclosed);
   }

   public static DeclaredReflection query(Declared declared)
   {
      return (DeclaredReflection) validate(declared);
   }

   public static EnumReflection query(Enum anEnum)
   {
      return (EnumReflection) validate(anEnum);
   }

   public static InterfaceReflection query(Interface anInterface)
   {
      return (InterfaceReflection) validate(anInterface);
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

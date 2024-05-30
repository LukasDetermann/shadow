package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.*;
import io.determann.shadow.api.lang_model.query.*;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface LangModelQueries
{
   public static NameableLangModel query(Nameable nameable)
   {
      return ((NameableLangModel) validate(nameable));
   }

   public static QualifiedNameableLamgModel query(QualifiedNameable qualifiedNameable)
   {
      return ((QualifiedNameableLamgModel) validate(qualifiedNameable));
   }

   public static WildcardLangModel query(Wildcard wildcard)
   {
      return ((WildcardLangModel) validate(wildcard));
   }

   public static PrimitiveLangModel query(Primitive primitive)
   {
      return ((PrimitiveLangModel) validate(primitive));
   }

   public static ShadowLangModel query(Shadow shadow)
   {
      return ((ShadowLangModel) validate(shadow));
   }

   public static PackageLangModel query(Package aPackage)
   {
      return (PackageLangModel) validate(aPackage);
   }

   public static ModuleEnclosedLangModel query(ModuleEnclosed moduleEnclosed)
   {
      return ((ModuleEnclosedLangModel) validate(moduleEnclosed));
   }

   public static DocumentedLangModel query(Documented documented)
   {
      return (DocumentedLangModel) validate(documented);
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

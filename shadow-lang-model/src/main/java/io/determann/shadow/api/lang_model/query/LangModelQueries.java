package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Wildcard;

public interface LangModelQueries
{
   public static NameableLangModel query(Nameable nameable)
   {
      return ((NameableLangModel) nameable);
   }

   public static WildcardLangModel query(Wildcard wildcard)
   {
      return ((WildcardLangModel) wildcard);
   }

   public static PrimitiveLangModel query(Primitive primitive)
   {
      return ((PrimitiveLangModel) primitive);
   }
}

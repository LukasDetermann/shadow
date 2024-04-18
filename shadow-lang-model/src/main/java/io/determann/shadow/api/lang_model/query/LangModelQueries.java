package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.Nameable;

public interface LangModelQueries
{
   public static NameableLangModel query(Nameable nameable)
   {
      return ((NameableLangModel) nameable);
   }
}

package org.determann.shadow.api;

public @interface Scope
{
   ScopeType value();

   enum ScopeType
   {
      /**
       * Annotation processing happens in rounds. In the first round everything being compiled is in this scope. In the following rounds only
       * generated stuff from the previous round.
       *
       * @see ShadowApi#writeSourceFile(String, String)
       * @see ShadowApi#writeClassFile(String, String)
       */
      CURRENT_COMPILATION,
      /**
       * Contains {@link #CURRENT_COMPILATION} and in addition all previously complied classes. so for example the jdk
       */
      ALL
   }
}

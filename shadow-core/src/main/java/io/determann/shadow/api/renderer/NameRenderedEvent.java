package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.type.C_Declared;

public class NameRenderedEvent
{
   private final C_Declared rendered;
   private final String name;
   private final boolean qualified;

   public NameRenderedEvent(C_Declared rendered, String name, boolean qualified)
   {
      this.rendered = rendered;
      this.name = name;
      this.qualified = qualified;
   }

   public C_Declared getRendered()
   {
      return rendered;
   }

   public String getName()
   {
      return name;
   }

   public boolean isQualified()
   {
      return qualified;
   }

   public boolean isSimple()
   {
      return !qualified;
   }
}

package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Declared;

public class NameRenderedEvent
{
   private final Declared rendered;
   private final String name;
   private final boolean qualified;

   public NameRenderedEvent(Declared rendered, String name, boolean qualified) {
      this.rendered = rendered;
      this.name = name;
      this.qualified = qualified;
   }

   public Declared getRendered()
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

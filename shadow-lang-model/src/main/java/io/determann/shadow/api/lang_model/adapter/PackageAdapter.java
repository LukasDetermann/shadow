package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.structure.PackageImpl;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;

public class PackageAdapter
{
   private final LM.Package aPackage;

   PackageAdapter(LM.Package aPackage)
   {
      this.aPackage = aPackage;
   }

   public NoType toNoType()
   {
      return ((PackageImpl) aPackage).getMirror();
   }

   public PackageElement toPackageElement()
   {
      return ((PackageImpl) aPackage).getElement();
   }
}

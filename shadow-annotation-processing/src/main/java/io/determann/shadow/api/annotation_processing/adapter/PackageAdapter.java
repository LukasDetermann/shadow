package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.structure.PackageImpl;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;

public class PackageAdapter
{
   private final AP.Package aPackage;

   PackageAdapter(AP.Package aPackage)
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

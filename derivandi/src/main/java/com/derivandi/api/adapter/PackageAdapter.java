package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.structure.PackageImpl;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.NoType;

public class PackageAdapter
{
   private final Ap.Package aPackage;

   PackageAdapter(Ap.Package aPackage)
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

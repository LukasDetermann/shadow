package io.determann.shadow.api;

public interface QualifiedNameable<ELEMENT extends javax.lang.model.element.QualifiedNameable> extends ElementBacked<ELEMENT>
{
   /**
    * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
    */
   default String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }
}

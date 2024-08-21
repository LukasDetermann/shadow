package io.determann.shadow.api.reflection.shadow;

import io.determann.shadow.api.shadow.C_QualifiedNameable;

public interface R_QualifiedNameable extends C_QualifiedNameable
{
   /**
    * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
    */
   String getQualifiedName();
}

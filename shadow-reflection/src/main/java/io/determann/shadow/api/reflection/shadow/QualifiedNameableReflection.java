package io.determann.shadow.api.reflection.shadow;

import io.determann.shadow.api.shadow.QualifiedNameable;

public interface QualifiedNameableReflection extends QualifiedNameable
{
   /**
    * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
    */
   String getQualifiedName();
}

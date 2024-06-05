package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.QualifiedNameable;

public interface QualifiedNameableReflection extends QualifiedNameable
{
   /**
    * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
    */
   String getQualifiedName();
}

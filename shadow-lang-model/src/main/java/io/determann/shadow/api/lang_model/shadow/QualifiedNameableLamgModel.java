package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.QualifiedNameable;

public interface QualifiedNameableLamgModel extends QualifiedNameable
{
   /**
    * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
    */
   String getQualifiedName();
}

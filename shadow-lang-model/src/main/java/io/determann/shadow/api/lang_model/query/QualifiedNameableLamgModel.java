package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.QualifiedNameable;

public interface QualifiedNameableLamgModel extends QualifiedNameable
{
   /**
    * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
    */
   String getQualifiedName();
}

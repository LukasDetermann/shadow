package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.Documented;

public interface DocumentedLangModel extends Documented
{
   /**
    * returns the javaDoc or null if none is present
    */
   String getJavaDoc();
}

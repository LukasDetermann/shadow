package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.Documented;

public interface DocumentedLangModel extends Documented
{
   /**
    * returns the javaDoc or null if none is present
    */
   String getJavaDoc();
}

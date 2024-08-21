package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.C_Documented;

public interface LM_Documented extends C_Documented
{
   /**
    * returns the javaDoc or null if none is present
    */
   String getJavaDoc();
}

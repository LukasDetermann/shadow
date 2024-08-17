package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Intersection;

import java.util.Optional;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface GenericLangModel extends Generic,
                                          AnnotationableLangModel,
                                          ShadowLangModel,
                                          NameableLangModel
{
   /**
    * @see Intersection
    */
   ShadowLangModel getExtends();

   Optional<ShadowLangModel> getSuper();

   /**
    * returns the class, method constructor etc. this is the generic for
    */
   Object getEnclosing();
}
